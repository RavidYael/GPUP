package ProcessesManagment;

import DTOs.*;
import GraphManagment.GraphsManager;
import dependency.graph.DependencyGraph;
import dependency.target.Target;
import utils.GraphInExecution;

import java.util.*;
import java.util.stream.Collectors;

import static dependency.target.Target.TargetStatus.Waiting;

public class ProcessesManager {

        private Map<String, MissionInfoDTO> MissionDTOByName = new HashMap<>();
        private Map<String, GraphInExecution> graphInExecutionByName = new HashMap<>();
        private Map<String, GraphInfoDTO> graphInExecutionInfoByName = new HashMap<>();
        private static final Map<String, SimulationTaskDTO> simulationTasksMap = new HashMap<>();
        private static final Map<String, CompilationTaskDTO> compilationTasksMap = new HashMap<>();
        private static final Map<String, Set<String>> usersTasks = new HashMap<>();
        private Set<String> listOfAllMissions = new HashSet<>();
        private List<TargetDTO> allReadyTargetsInSystem = new LinkedList<>();


        public void updateTargetResult(TargetDTO theUpdatedTarget){
                graphInExecutionByName.get(theUpdatedTarget.getMissionName()).updateEffectOfTargetsExecution(theUpdatedTarget);
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
                simulationTasksMap.put(newTask.getTaskName().toLowerCase(), newTask);
                listOfAllMissions.add(newTask.getTaskName().toLowerCase());
                addUserTask(newTask.getTaskCreator(), newTask.getTaskName());
        }

        public synchronized void addCompilationTask(CompilationTaskDTO newTask) {
                compilationTasksMap.put(newTask.getTaskName().toLowerCase(), newTask);
                listOfAllMissions.add(newTask.getTaskName());

                addUserTask(newTask.getTaskCreator(), newTask.getTaskName());
        }

        public synchronized void addUserTask(String taskCreator, String taskName) {
                if (!usersTasks.containsKey(taskCreator))
                        usersTasks.put(taskCreator, new HashSet<>());

                usersTasks.get(taskCreator).add(taskName);
        }

        public synchronized Set<String> getAllTaskList() {
                return listOfAllMissions;
        }

        public synchronized Set<String> getUserTaskList(String userName) {
                return usersTasks.get(userName.toLowerCase());
        }


        public synchronized void addMissionsDTO(SimulationTaskDTO newTask, GraphsManager graphsManager) {
                GraphInExecution subGraphToWorkOn = new GraphInExecution(newTask.getTaskName(), newTask.getTargetsToExecute(), graphsManager.getGraphByName(newTask.getGraphName()));
                MissionDTOByName.put(newTask.getTaskName(), new MissionInfoDTO(subGraphToWorkOn.getGraphInExecution(), newTask.getTargetsToExecute(), newTask.getTaskName(), newTask.getTaskCreator(), DependencyGraph.TaskType.SIMULATION, newTask.getPricingForTarget(), 0, MissionInfoDTO.MissionStatus.frozen));
                graphInExecutionByName.put(newTask.getGraphName(), subGraphToWorkOn);
                GraphInfoDTO graphInfoDTO = new GraphInfoDTO(subGraphToWorkOn.getGraphInExecution());
                graphInExecutionInfoByName.put(graphInfoDTO.getGraphName(), graphInfoDTO);
        }

        public synchronized void addMissionsDTO(CompilationTaskDTO newTask, GraphsManager graphsManager) {
                GraphInExecution subGraphToWorkOn = new GraphInExecution(newTask.getTaskName(), newTask.getTargetsToExecute(), graphsManager.getGraphByName(newTask.getGraphName()));
                MissionDTOByName.put(newTask.getTaskName(), new MissionInfoDTO(subGraphToWorkOn.getGraphInExecution(), newTask.getTargetsToExecute(), newTask.getTaskName(), newTask.getTaskCreator(), DependencyGraph.TaskType.COMPILATION, newTask.getPricingForTarget(), 0, MissionInfoDTO.MissionStatus.frozen));
                graphInExecutionByName.put(newTask.getGraphName(), subGraphToWorkOn);
                GraphInfoDTO graphInfoDTO = new GraphInfoDTO(subGraphToWorkOn.getGraphInExecution());
                graphInExecutionInfoByName.put(graphInfoDTO.getGraphName(), graphInfoDTO);
        }

        public synchronized MissionInfoDTO getMissionInfoDTO(String missionName) {
                return MissionDTOByName.get(missionName);
        }

        public synchronized Set<MissionInfoDTO> getAllMissionInfoDTO() {
                return MissionDTOByName.values().stream().collect(Collectors.toSet());
        }

        private void refresh() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                        public void run() {
                                refreshReadyTargets();
                        }

                }, 0, 2000);
        }
//TODO: THERE IS A BUG! WE DONT REMOVE TARGETS THAT ARE NO LONGER WAITING
        private void refreshReadyTargets() {
                for (String mission : listOfAllMissions) {
                        for (Target target : graphInExecutionByName.get(mission).getGraphInExecution().getAllTargets().values().stream().filter(t -> t.getTargetStatus().equals(Waiting)).collect(Collectors.toSet())) {
                                if (!allReadyTargetsInSystem.stream().anyMatch(targetDTO -> targetDTO.getName().equals(target.getName()) && targetDTO.getTaskType().equals(mission))) {
                                        allReadyTargetsInSystem.add(new TargetDTO(target.getName(), target.getData(), getMissionInfoDTO(mission).getMissionType(), mission));
                                }
                        }
                }
        }

        public synchronized TargetDTO pollTaskReadyForWorker(Set<String> tasksThatWorkerIsSignedTo) {

                List<TargetDTO> filteredList = allReadyTargetsInSystem.stream().filter
                        (targetDTO -> tasksThatWorkerIsSignedTo.contains(targetDTO.getMissionName()))
                        .collect(Collectors.toList());

                if(!filteredList.isEmpty()) {
                        TargetDTO ret = filteredList.get(0);
                        filteredList.remove(0);
                        return ret;
                }

                else //filtered list is empty
                        return null;

        }


}

