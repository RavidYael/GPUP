package FXData;

import DTOs.MissionInfoDTO;
import DTOs.UserDTO;
import FXData.ServerDataManager;
import FXData.UserInTable;
import com.sun.jmx.snmp.tasks.TaskServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mainScreen.workerDashboardScreen.TaskInTable;
import mainScreen.workerDashboardScreen.WorkerDashboardScreenController;

import java.util.Set;

public class TableManager {

    private ServerDataManager serverDataManager;

    public TableManager(ServerDataManager serverDataManager) {
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
                        , (serverDataManager.amIListed(curtask.getMissionName())? WorkerDashboardScreenController.YesOrNo.yes : WorkerDashboardScreenController.YesOrNo.no));

                tasksinTable.add(tempTaskInTable);
            }

        }
        return tasksinTable;
    }

    public ObservableList<SubmittedTaskInTable> getSubmittedTasksForTable() {
        ObservableList<SubmittedTaskInTable> tasksInTable = FXCollections.observableArrayList();
        Set<MissionInfoDTO> missionsDTOSet = serverDataManager.getAllMissionsForUser();
        if (!missionsDTOSet.isEmpty()) {
            for (MissionInfoDTO curtask : missionsDTOSet) {
                SubmittedTaskInTable tempTaskInTable = new SubmittedTaskInTable();
                tempTaskInTable.setNumOfWorkers(curtask.getCurrentNumOfxExecutingWorkers());
                tempTaskInTable.setName(curtask.getMissionName());
                tempTaskInTable.setProgress(serverDataManager.getTaskProcess(curtask.getMissionName()));
                tempTaskInTable.numOfTargets = serverDataManager.getExecutedTargetsForUser(curtask.getMissionName());
                tasksInTable.add(tempTaskInTable);
            }

        }

        return tasksInTable;
    }

}
