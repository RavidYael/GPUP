package ProcessesManagment.ExecutionManagement.SubscribersManagement;

import DTOs.MissionInfoDTO;
import DTOs.UserDTO;
import UserManagement.UsersManagementServer.servlets.UsersListServlet;
import utils.GraphInExecution;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SubscribesManager {

    Map<MissionInfoDTO, Set<UserDTO>> missionInfoDTOWorkersMap;
    Map<String,MissionInfoDTO> missionInfoDTOMap;

    public SubscribesManager() {
        missionInfoDTOWorkersMap= new HashMap<>();
        missionInfoDTOMap= new HashMap<>();
    }

    public void addSubscriber(UserDTO user ,MissionInfoDTO missionInfoDTO){
    missionInfoDTOWorkersMap.put(missionInfoDTO,new HashSet<>());
    missionInfoDTOWorkersMap.get(missionInfoDTO).add(user);
    missionInfoDTOMap.put(missionInfoDTO.getMissionName(),missionInfoDTO);
    }

    public Set<UserDTO> getMissionWorkers(String missionName){
        return missionInfoDTOWorkersMap.get(missionInfoDTOMap.get(missionName));
    }


}
