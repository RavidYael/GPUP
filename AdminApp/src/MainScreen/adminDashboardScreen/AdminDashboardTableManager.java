package MainScreen.adminDashboardScreen;

import DTOs.GraphInfoDTO;
import DTOs.MissionInfoDTO;
import DTOs.UserDTO;
import FXData.ServerDataManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.Set;

import static javafx.collections.FXCollections.observableArrayList;

public class AdminDashboardTableManager {

    private ServerDataManager serverDataManager;

    AdminDashboardTableManager(ServerDataManager serverDataManager){
        this.serverDataManager = serverDataManager;
    }

    public ObservableList<UserInTable> getUsersForTable(){
        ObservableList<UserInTable> usersInTable =  FXCollections.observableArrayList();
        Set<UserDTO> usersFromServer = serverDataManager.getUsersWithRoles();
        if (usersFromServer != null) {
            for (UserDTO curUser : usersFromServer) {
                UserInTable tempUserInTable = new UserInTable();
                tempUserInTable.setName(curUser.getName());
                tempUserInTable.setRole(curUser.getDegree());
                usersInTable.add(tempUserInTable);
            }
        }
        return usersInTable;
    }

    public ObservableList<GraphInTable> getGraphsForTable(){
        ObservableList<GraphInTable> graphsInTable =  FXCollections.observableArrayList();
        Set<GraphInfoDTO> graphsFromServer = serverDataManager.getGraphsDTO();
        if (!graphsFromServer.isEmpty()) {
            for (GraphInfoDTO curGraph : graphsFromServer) {
                GraphInTable tempGraphInTable = new GraphInTable(curGraph.getGraphName()
                        , curGraph.getUploader()
                        , curGraph.getTargetsCount()
                        , curGraph.getRootsCount()
                        , curGraph.getMiddlesCount()
                        , curGraph.getLeavesCount()
                        , curGraph.getIndependentsCount()
                        , curGraph.getSimulationPrice()
                        , curGraph.getCompilationPrice());
                graphsInTable.add(tempGraphInTable);

            }
        }


        return graphsInTable;

    }

    public ObservableList<TaskInTable> getTasksForTable(){
        ObservableList<TaskInTable> tasksInTable =  FXCollections.observableArrayList();
        Set<MissionInfoDTO> tasksFromServer = serverDataManager.getAllMissionsDTO();
        if (!tasksFromServer.isEmpty()){
            for (MissionInfoDTO curtask : tasksFromServer){
                TaskInTable tempTaskInTable = new TaskInTable(curtask.getMissionName()
                ,curtask.getMissionUploader()
                ,curtask.getGraphName()
                ,curtask.getTargetsCount()
                ,curtask.getRootsCount()
                ,curtask.getMiddlesCount()
                ,curtask.getLeavesCount()
                ,curtask.getIndependentsCount()
                ,curtask.getTaskTotalPayment()
                ,curtask.getCurrentNumOfxExecutingWorkers());
                tasksInTable.add(tempTaskInTable);
            }

        }
        return tasksInTable;

    }
}
