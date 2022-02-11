package ProcessesManagment;

import DataManagment.GraphsManager;
import dependency.graph.DependencyGraph;
import utils.GraphInExecution;
import utils.GraphsDataDTOs.GraphInfoDTO;
import utils.ProccecesDTOs.MissionInfoDTO;
import utils.ProccecesDTOs.CompilationTaskDTO;
import utils.ProccecesDTOs.SimulationTaskDTO;

import java.util.*;

public class ProcessesManager {

        private Map<String , MissionInfoDTO> MissionDTOByName = new HashMap<>();
        private Map<String, GraphInExecution> graphInExecutionByName = new HashMap<>();
        private Map<String, GraphInfoDTO> graphInExecutionInfoByName = new HashMap<>();
        private static final Map<String, SimulationTaskDTO> simulationTasksMap = new HashMap<>();
        private static final Map<String, CompilationTaskDTO> compilationTasksMap = new HashMap<>();
        private static final Map<String, Set<String>> usersTasks = new HashMap<>();
        private Set<String> listOfAllTasks = new HashSet<>();


        public synchronized boolean isMissionExists(String missionName) {
                return MissionDTOByName.containsKey(missionName);
        }

        public synchronized boolean isTaskExists(String taskName) {
                return simulationTasksMap.containsKey(taskName.toLowerCase()) || compilationTasksMap.containsKey(taskName.toLowerCase());
        }

        public synchronized boolean isSimulationTask(String taskName) { return simulationTasksMap.containsKey(taskName.toLowerCase()); }

        public synchronized CompilationTaskDTO getCompilationTaskInformation(String taskName) {
                return compilationTasksMap.get(taskName.toLowerCase());
        }

        public synchronized SimulationTaskDTO getSimulationTaskInformation(String taskName) {
                return simulationTasksMap.get(taskName.toLowerCase());
        }

        public synchronized void addSimulationTask(SimulationTaskDTO newTask) {
                simulationTasksMap.put(newTask.getTaskName().toLowerCase(), newTask);
                listOfAllTasks.add(newTask.getTaskName().toLowerCase());
                addUserTask(newTask.getTaskCreator().toLowerCase(), newTask.getTaskName());
        }

        public synchronized void addCompilationTask(CompilationTaskDTO newTask) {
                compilationTasksMap.put(newTask.getTaskName().toLowerCase(), newTask);
                listOfAllTasks.add(newTask.getTaskName());

                addUserTask(newTask.getTaskCreator().toLowerCase(), newTask.getTaskName());
        }

        public synchronized void addUserTask(String taskCreator, String taskName) {
                if(!usersTasks.containsKey(taskCreator))
                        usersTasks.put(taskCreator, new HashSet<>());

                usersTasks.get(taskCreator).add(taskName);
        }

        public synchronized Set<String> getAllTaskList()
        {
                return listOfAllTasks;
        }

        public synchronized Set<String> getUserTaskList(String userName)
        {
                return usersTasks.get(userName.toLowerCase());
        }


        public synchronized void addMissionsDTO(SimulationTaskDTO newTask,GraphsManager graphsManager)
        {
                MissionDTOByName.put(newTask.getTaskName().toLowerCase(), new MissionInfoDTO(graphsManager.getGraphInfoDTO(newTask.getGraphName()),newTask.getTargetsToExecute(),newTask.getTaskName(), newTask.getTaskCreator(), DependencyGraph.TaskType.SIMULATION));
                GraphInExecution subGraphToWorkOn = new GraphInExecution(getMissionInfoDTO(newTask.getTaskName()), graphsManager.getGraphByName(newTask.getGraphName()));
                graphInExecutionByName.put(newTask.getGraphName(),subGraphToWorkOn);
                GraphInfoDTO graphInfoDTO = new GraphInfoDTO(subGraphToWorkOn.getGraphInExecution());
                graphInExecutionInfoByName.put(graphInfoDTO.getGraphName(),graphInfoDTO);
        }

        public synchronized void addMissionsDTO(CompilationTaskDTO newTask, GraphsManager graphsManager)
        {
                MissionDTOByName.put(newTask.getTaskName().toLowerCase(), new MissionInfoDTO(graphsManager.getGraphInfoDTO(newTask.getGraphName()),newTask.getTargetsToExecute(),newTask.getTaskName(), newTask.getTaskCreator(), DependencyGraph.TaskType.COMPILATION));
                GraphInExecution subGraphToWorkOn = new GraphInExecution(getMissionInfoDTO(newTask.getTaskName()), graphsManager.getGraphByName(newTask.getGraphName()));
                graphInExecutionByName.put(newTask.getGraphName(),subGraphToWorkOn);
                GraphInfoDTO graphInfoDTO = new GraphInfoDTO(subGraphToWorkOn.getGraphInExecution());
                graphInExecutionInfoByName.put(graphInfoDTO.getGraphName(),graphInfoDTO);
        }


        public synchronized MissionInfoDTO getMissionInfoDTO(String missionName)
        {
                return MissionDTOByName.get(missionName.toLowerCase());
        }



}
