package ProcessesManagment;

import DTOs.*;
import GraphManagment.GraphsManager;
import ProcessesManagment.SubscribersManagement.SubscribesManager;
import dependency.graph.DependencyGraph;
import dependency.target.Target;
import utils.GraphInExecution;

import java.util.*;
import java.util.stream.Collectors;

import static dependency.target.Target.TargetStatus.InProcess;
import static dependency.target.Target.TargetStatus.Waiting;

public class ProcessesManager {

        private Map<String, MissionInfoDTO> MissionDTOByName = new HashMap<>();
        private Map<String, GraphInExecution> graphInExecutionByName = new HashMap<>();
        private Map<String, GraphInfoDTO> graphInExecutionInfoByName = new HashMap<>();
        private static final Map<String, SimulationTaskDTO> simulationTasksMap = new HashMap<>();
        private static final Map<String, CompilationTaskDTO> compilationTasksMap = new HashMap<>();
        private static final Map<String, Set<String>> usersTasks = new HashMap<>();
        private Set<String> listOfAllExecutingMissions = new HashSet<>();
        private List<TargetDTO> allReadyTargetsInSystem = new LinkedList<>();
        private Set<String> listOfAllPausedMissions = new HashSet<>();
        private Set<String> listOfAllFinishedMissions = new HashSet<>();
        private Set<String> listOfAllStoppedMissions = new HashSet<>();
        private List<TargetDTO> allExecutedTargets = new LinkedList<>();

        public void controlMission(String MissionName,String controlType){

                if(controlType.equals("resume")){
                        resumeMission(MissionName);
                }
                else if(controlType.equals("pause")){
                        pauseMission(MissionName);
                }
                else if(controlType.equals("stop")){
                        stopMission(MissionName);
                }
        }

        public void createBasedOnMission(String originalMissionName,SubscribesManager subscribesManager,String incrementalType,GraphsManager graphsManager,String uploaderName){

                MissionInfoDTO theNewMission = getMissionInfoDTO(originalMissionName);

                if(incrementalType.equals("fromScratch")) {

                        if (theNewMission.getMissionType().equals(DependencyGraph.TaskType.SIMULATION)) {
                                SimulationTaskDTO originalTask = simulationTasksMap.get(originalMissionName);
                                SimulationTaskDTO theNewTask = new SimulationTaskDTO(originalTask.getTaskName() + "1", uploaderName, originalTask.getTaskName() + "1", originalTask.getTargetsToExecute(), originalTask.getPricingForTarget(), originalTask.getSimulationParameters());
                                addMissionsDTO(theNewTask, graphsManager, subscribesManager, uploaderName);
                                addSimulationTask(theNewTask);
                        } else if (theNewMission.getMissionType().equals(DependencyGraph.TaskType.COMPILATION)) {
                                CompilationTaskDTO originalTask = compilationTasksMap.get(originalMissionName);
                                CompilationTaskDTO theNewTask = new CompilationTaskDTO(originalTask.getTaskName() + "1", uploaderName, originalTask.getTaskName() + "1", originalTask.getTargetsToExecute(), originalTask.getPricingForTarget(), originalTask.getCompilationParameters());
                                addMissionsDTO(theNewTask, graphsManager, subscribesManager, uploaderName);
                                addCompilationTask(theNewTask);
                        }
                }
                else if(incrementalType.equals("incrementally")){
                        if (theNewMission.getMissionType().equals(DependencyGraph.TaskType.SIMULATION)) {
                                SimulationTaskDTO originalTask = simulationTasksMap.get(originalMissionName);
                                DependencyGraph incrementGraph = getGraphInExecutionByName().get(originalMissionName).getGraphInExecution().createDeepCopy();
                                incrementGraph.updateTargetStatusForIncrementalExecution();
                                incrementGraph.setGraphName(originalMissionName + "1");
                                GraphInExecution IncrementGraphInExec = new GraphInExecution(originalMissionName,originalTask.getTargetsToExecute(),incrementGraph,uploaderName);
                                SimulationTaskDTO theNewTask = new SimulationTaskDTO(originalTask.getTaskName() + "1", uploaderName , originalTask.getTaskName() + "1", originalTask.getTargetsToExecute(), originalTask.getPricingForTarget(), originalTask.getSimulationParameters());
                                addMissionsDTOIncrement(theNewTask, IncrementGraphInExec, subscribesManager, originalTask.getTaskCreator());
                                addSimulationTask(theNewTask);

                        }else if((theNewMission.getMissionType().equals(DependencyGraph.TaskType.COMPILATION))){
                                CompilationTaskDTO originalTask = compilationTasksMap.get(originalMissionName);
                                DependencyGraph incrementGraph = getGraphInExecutionByName().get(originalMissionName).getGraphInExecution().createDeepCopy();
                                incrementGraph.updateTargetStatusForIncrementalExecution();
                                incrementGraph.setGraphName(originalMissionName + "1");
                                GraphInExecution IncrementGraphInExec = new GraphInExecution(originalMissionName,originalTask.getTargetsToExecute(),incrementGraph,uploaderName);
                                CompilationTaskDTO theNewTask = new CompilationTaskDTO(originalTask.getTaskName() + "1", uploaderName, originalTask.getTaskName() + "1", originalTask.getTargetsToExecute(), originalTask.getPricingForTarget(), originalTask.getCompilationParameters());
                                addMissionsDTOIncrement(theNewTask, IncrementGraphInExec, subscribesManager, uploaderName);
                                addCompilationTask(theNewTask);
                        }


                }


        }



        public void resumeMission(String missionName) {
                if(listOfAllPausedMissions.contains(missionName)) {
                        listOfAllPausedMissions.remove(missionName);
                        listOfAllExecutingMissions.add(missionName);
                        refreshReadyTargets();
                        getMissionInfoDTO(missionName).setMissionStatus(MissionInfoDTO.MissionStatus.running);
                }
        }


        public void pauseMission(String missionName){
                if(listOfAllExecutingMissions.contains(missionName)) {
                        listOfAllExecutingMissions.remove(missionName);
                        listOfAllPausedMissions.add(missionName);
                        refreshReadyTargets();
                        getMissionInfoDTO(missionName).setMissionStatus(MissionInfoDTO.MissionStatus.paused);
                }
        }


        public void stopMission(String missionName){
                listOfAllExecutingMissions.remove(missionName);
                listOfAllStoppedMissions.add(missionName);
                refreshReadyTargets();
                getMissionInfoDTO(missionName).setMissionStatus(MissionInfoDTO.MissionStatus.stopped);
        }


        public void updateTargetResult(TargetDTO theUpdatedTarget){

                MissionInfoDTO.MissionStatus missionStatus = graphInExecutionByName.get(theUpdatedTarget.getMissionName()).updateEffectOfTargetsExecution(theUpdatedTarget);
                graphInExecutionByName.get(theUpdatedTarget.getMissionName()).incrementTargetExecutedForUser(theUpdatedTarget.getRunBy());

                if(missionStatus == MissionInfoDTO.MissionStatus.finished){
                missionFinished(theUpdatedTarget.getMissionName());
                }
        }

        public void missionFinished(String missionName){
                getMissionInfoDTO(missionName).setMissionStatus(MissionInfoDTO.MissionStatus.finished);
                graphInExecutionByName.get(missionName).setMissionStatus(MissionInfoDTO.MissionStatus.finished);
                listOfAllExecutingMissions.remove(missionName);
                listOfAllFinishedMissions.add(missionName);
        }


        public synchronized boolean isMissionExists(String missionName) {
                return MissionDTOByName.containsKey(missionName);
        }

        public synchronized boolean isTaskExists(String taskName) {
                return simulationTasksMap.containsKey(taskName.toLowerCase()) || compilationTasksMap.containsKey(taskName.toLowerCase());
        }

        public synchronized boolean isSimulationTask(String taskName) {
                return simulationTasksMap.containsKey(taskName.toLowerCase());
        }

        public synchronized CompilationTaskDTO getCompilationTaskInformation(String taskName) {
                return compilationTasksMap.get(taskName.toLowerCase());
        }

        public synchronized SimulationTaskDTO getSimulationTaskInformation(String taskName) {
                return simulationTasksMap.get(taskName.toLowerCase());
        }

        public synchronized void addSimulationTask(SimulationTaskDTO newTask) {
                simulationTasksMap.put(newTask.getTaskName(), newTask);
                listOfAllExecutingMissions.add(newTask.getTaskName());
                addUserTask(newTask.getTaskCreator(), newTask.getTaskName());
        }

        public Map<String, GraphInExecution> getGraphInExecutionByName() {
                return graphInExecutionByName;
        }

        public synchronized void addCompilationTask(CompilationTaskDTO newTask) {
                compilationTasksMap.put(newTask.getTaskName(), newTask);
                listOfAllExecutingMissions.add(newTask.getTaskName());

                addUserTask(newTask.getTaskCreator(), newTask.getTaskName());
        }

        public synchronized void addUserTask(String taskCreator, String taskName) {
                if (!usersTasks.containsKey(taskCreator))
                        usersTasks.put(taskCreator, new HashSet<>());

                usersTasks.get(taskCreator).add(taskName);
        }

        public synchronized Set<String> getAllTaskList() {
                return listOfAllExecutingMissions;
        }

        public synchronized Set<String> getUserTaskList(String userName) {
                return usersTasks.get(userName);
        }


        public synchronized void addMissionsDTOIncrement(SimulationTaskDTO newTask, GraphInExecution theNewGraphInExe, SubscribesManager subscribesManager,String uploader) {
                MissionDTOByName.put(newTask.getTaskName(), new MissionInfoDTO(theNewGraphInExe.getGraphInExecution(), newTask.getTargetsToExecute(), newTask.getTaskName(), newTask.getTaskCreator(), DependencyGraph.TaskType.SIMULATION, newTask.getPricingForTarget(), 0, MissionInfoDTO.MissionStatus.frozen,newTask));
                graphInExecutionByName.put(newTask.getTaskName(), theNewGraphInExe);
                GraphInfoDTO graphInfoDTO = new GraphInfoDTO(theNewGraphInExe.getGraphInExecution());
                graphInExecutionInfoByName.put(graphInfoDTO.getGraphName(), graphInfoDTO);
                subscribesManager.addMission(MissionDTOByName.get(newTask.getTaskName()));
                refreshReadyTargets();
        }

        public synchronized void addMissionsDTOIncrement(CompilationTaskDTO newTask, GraphInExecution theNewGraphInExe, SubscribesManager subscribesManager,String uploader) {
                MissionDTOByName.put(newTask.getTaskName(), new MissionInfoDTO(theNewGraphInExe.getGraphInExecution(), newTask.getTargetsToExecute(), newTask.getTaskName(), newTask.getTaskCreator(), DependencyGraph.TaskType.COMPILATION, newTask.getPricingForTarget(), 0, MissionInfoDTO.MissionStatus.frozen,newTask));
                graphInExecutionByName.put(newTask.getTaskName(), theNewGraphInExe);
                GraphInfoDTO graphInfoDTO = new GraphInfoDTO(theNewGraphInExe.getGraphInExecution());
                graphInExecutionInfoByName.put(graphInfoDTO.getGraphName(), graphInfoDTO);
                subscribesManager.addMission(MissionDTOByName.get(newTask.getTaskName()));
                refreshReadyTargets();
        }


        public synchronized void addMissionsDTO(SimulationTaskDTO newTask, GraphsManager graphsManager, SubscribesManager subscribesManager,String uploader) {
                GraphInExecution subGraphToWorkOn = new GraphInExecution(newTask.getTaskName(), newTask.getTargetsToExecute(), graphsManager.getGraphByName(newTask.getGraphName()),uploader);
                MissionDTOByName.put(newTask.getTaskName(), new MissionInfoDTO(subGraphToWorkOn.getGraphInExecution(), newTask.getTargetsToExecute(), newTask.getTaskName(), newTask.getTaskCreator(), DependencyGraph.TaskType.SIMULATION, newTask.getPricingForTarget(), 0, MissionInfoDTO.MissionStatus.frozen,newTask));
                graphInExecutionByName.put(newTask.getTaskName(), subGraphToWorkOn);
                GraphInfoDTO graphInfoDTO = new GraphInfoDTO(subGraphToWorkOn.getGraphInExecution());
                graphInExecutionInfoByName.put(graphInfoDTO.getGraphName(), graphInfoDTO);
                subscribesManager.addMission(MissionDTOByName.get(newTask.getTaskName()));
                refreshReadyTargets();
        }

        public synchronized void addMissionsDTO(CompilationTaskDTO newTask, GraphsManager graphsManager, SubscribesManager subscribesManager,String uploader) {
                GraphInExecution subGraphToWorkOn = new GraphInExecution(newTask.getTaskName(), newTask.getTargetsToExecute(), graphsManager.getGraphByName(newTask.getGraphName()),uploader);
                MissionDTOByName.put(newTask.getTaskName(), new MissionInfoDTO(subGraphToWorkOn.getGraphInExecution(), newTask.getTargetsToExecute(), newTask.getTaskName(), newTask.getTaskCreator(), DependencyGraph.TaskType.COMPILATION, newTask.getPricingForTarget(), 0, MissionInfoDTO.MissionStatus.frozen,newTask));
                graphInExecutionByName.put(newTask.getTaskName(), subGraphToWorkOn);
                GraphInfoDTO graphInfoDTO = new GraphInfoDTO(subGraphToWorkOn.getGraphInExecution());
                graphInExecutionInfoByName.put(graphInfoDTO.getGraphName(), graphInfoDTO);
                subscribesManager.addMission(MissionDTOByName.get(newTask.getTaskName()));
                refreshReadyTargets();
        }

        public MissionInfoDTO getMissionInfoDTO(String missionName) {
                return MissionDTOByName.get(missionName);
        }

        public synchronized Set<MissionInfoDTO> getAllMissionInfoDTO() {
                return MissionDTOByName.values().stream().collect(Collectors.toSet());
        }


        private synchronized void refreshReadyTargets() {

                allReadyTargetsInSystem.clear();
                for (String mission : listOfAllExecutingMissions) {

                        for (Target target : graphInExecutionByName.get(mission)
                                .getGraphInExecution().getAllTargets().values().stream()
                                .filter(t -> t.getTargetStatus().equals(Waiting)).collect(Collectors.toSet()))
                        {
                                        allReadyTargetsInSystem.add
                                        (new TargetDTO(target.getName(), target.getData(), getMissionInfoDTO(mission)));
                        }
                }

        }



        public TargetDTO pullTaskReadyForWorker(Set<String> tasksThatWorkerIsSignedTo,String workerName) {

                List<TargetDTO> filteredList = allReadyTargetsInSystem.stream().filter
                        (targetDTO -> tasksThatWorkerIsSignedTo.contains(targetDTO.getMissionName()))
                        .collect(Collectors.toList());

                if(!filteredList.isEmpty()) {
                        synchronized (this) {
                                TargetDTO ret = filteredList.get(0);
                                if(ret.getRunBy() != null){
                                        return null;
                                }

                                graphInExecutionByName.get(ret.getMissionName())
                                        .getGraphInExecution().getTargetByName(ret.getName())
                                        .setTargetStatus(InProcess);

                                ret.setRunBy(workerName);
                                allReadyTargetsInSystem.remove(ret);
                                return ret;
                        }
                }
                else //filtered list is empty
                {
                        refreshReadyTargets();
                        return null;
                }

        }


}

