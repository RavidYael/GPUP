package ProcessesManagment.ExecutionManagement.SubscribersManagement;

import DTOs.MissionInfoDTO;
import DTOs.UserDTO;
import UserManagement.UsersManagementServer.servlets.UsersListServlet;
import utils.GraphInExecution;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SubscribesManager {

    Map<MissionInfoDTO, Set<UserDTO>> missionInfoDTOWorkersMap;

    public Set<String> getWorkerMissionsNames(String workerName) {
        return workerMissionsMap.get(workerName).stream().map(M->M.getMissionName()).collect(Collectors.toSet());
    }

    Map<String,Set<MissionInfoDTO>>workerMissionsMap;
    Map<String,MissionInfoDTO> missionInfoDTOMap;

    public SubscribesManager() {
        missionInfoDTOWorkersMap= new HashMap<>();
        missionInfoDTOMap= new HashMap<>();
        workerMissionsMap = new HashMap<>();
    }

    public void addSubscriber(UserDTO user ,MissionInfoDTO missionInfoDTO){
    //missionInfoDTOWorkersMap.put(missionInfoDTO,new HashSet<>()); // you don't want to create a new one each time, only the first time. so replaced with:
    missionInfoDTOWorkersMap.computeIfAbsent(missionInfoDTO, s-> new HashSet<>()).add(user);
    missionInfoDTOMap.put(missionInfoDTO.getMissionName(),missionInfoDTO);
    //TODO OMER TRICK WITH INSERT INTO NEW SET
    }

    public Set<UserDTO> getMissionWorkers(String missionName){
        return missionInfoDTOWorkersMap.get(missionInfoDTOMap.get(missionName));
    }

}
