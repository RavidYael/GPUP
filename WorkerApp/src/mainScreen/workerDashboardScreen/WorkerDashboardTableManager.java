package mainScreen.workerDashboardScreen;

import DTOs.MissionInfoDTO;
import DTOs.UserDTO;
import FXData.ServerDataManager;
import FXData.UserInTable;
import com.sun.jmx.snmp.tasks.TaskServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Set;

public class WorkerDashboardTableManager {

    private ServerDataManager serverDataManager;

    WorkerDashboardTableManager(ServerDataManager serverDataManager) {
        this.serverDataManager = serverDataManager;
    }

    public ObservableList<UserInTable> getUsersForTable() {
        ObservableList<UserInTable> usersInTable = FXCollections.observableArrayList();
        Set<UserDTO> usersFromServer = serverDataManager.getUsersWithRoles();
        for (UserDTO curUser : usersFromServer) {
            UserInTable tempUserInTable = new UserInTable();
            tempUserInTable.setName(curUser.getName());
            tempUserInTable.setRole(curUser.getDegree());
            usersInTable.add(tempUserInTable);
        }
        return usersInTable;
    }

    public ObservableList<TaskInTable> getTasksForTable() {
        ObservableList<TaskInTable> tasksinTable = FXCollections.observableArrayList();
        Set<MissionInfoDTO> missionsDTOSet = serverDataManager.getAllMissionsDTO();
        if (!missionsDTOSet.isEmpty()) {
            for (MissionInfoDTO curtask : missionsDTOSet) {
                TaskInTable tempTaskInTable = new TaskInTable(curtask.getMissionName()
                        , curtask.getMissionUploader()
                        , curtask.getMissionType()
                        , curtask.getTargetsCount()
                        , curtask.getRootsCount()
                        , curtask.getMiddlesCount()
                        , curtask.getLeavesCount()
                        , curtask.getIndependentsCount()
                        , curtask.getPriceByTaskType(curtask.getMissionType())
                        , curtask.getMissionStatus()
                        , curtask.getCurrentNumOfxExecutingWorkers()
                        , WorkerDashboardScreenController.YesOrNo.no); //TODO send request to know if user is listed for task

                tasksinTable.add(tempTaskInTable);
            }

        }
        return tasksinTable;
    }

}
