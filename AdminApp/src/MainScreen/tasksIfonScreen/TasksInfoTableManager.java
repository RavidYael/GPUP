package MainScreen.tasksIfonScreen;

import DTOs.MissionInfoDTO;
import FXData.ServerDataManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Set;

public class TasksInfoTableManager {

    private ServerDataManager serverDataManager;

    TasksInfoTableManager(ServerDataManager serverDataManager){
        this.serverDataManager = serverDataManager;
    }


    public TaskInfoInTable getTaskInfoForTable(String taskName) {
        TaskInfoInTable tempTaskInfoInTable  = new TaskInfoInTable();
        tempTaskInfoInTable.setName(taskName);
        List<MissionInfoDTO> missionInfoDTOList = serverDataManager.getMissionDTOByName(taskName);
        MissionInfoDTO missionInfoDTO = missionInfoDTOList.get(0);
        tempTaskInfoInTable.setGraphName(missionInfoDTO.getGraphName());
        tempTaskInfoInTable.setTotalTargets(missionInfoDTO.getTargetsCount());
        tempTaskInfoInTable.setNumOfWorkers(missionInfoDTO.getCurrentNumOfxExecutingWorkers());
        tempTaskInfoInTable.setProgress(serverDataManager.getTaskProcess(taskName));

        return tempTaskInfoInTable;
    }


}
