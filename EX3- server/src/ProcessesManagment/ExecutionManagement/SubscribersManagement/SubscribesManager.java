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
    Map<String,Set<MissionInfoDTO>>workerSubscribesMissionsMap;
    Map<String,Set<MissionInfoDTO>>workerWorkOnMissionMap;
    Map<String,MissionInfoDTO> missionInfoDTOMap;


    public Set<String> getWorkerWorkingMissionsNames(String workerName) {
        return workerWorkOnMissionMap.get(workerName).stream().map(M->M.getMissionName()).collect(Collectors.toSet());
    }

    public SubscribesManager() {
        missionInfoDTOWorkersMap= new HashMap<>();
        missionInfoDTOMap= new HashMap<>();
        workerSubscribesMissionsMap = new HashMap<>();
        workerWorkOnMissionMap = new HashMap<>();
    }

    public void addMission(MissionInfoDTO theMission){
        missionInfoDTOMap.put(theMission.getMissionName(),theMission);
    }

    public void removeMission(MissionInfoDTO theMission){
        missionInfoDTOMap.remove(theMission.getMissionName(),theMission);
    }

    public void addSubscriber(UserDTO user ,MissionInfoDTO missionInfoDTO){
    //missionInfoDTOWorkersMap.put(missionInfoDTO,new HashSet<>()); // you don't want to create a new one each time, only the first time. so replaced with:
        missionInfoDTOWorkersMap.computeIfAbsent(missionInfoDTO, s-> new HashSet<>()).add(user);
        missionInfoDTOMap.put(missionInfoDTO.getMissionName(),missionInfoDTO); //TODO: it doesnt ha to be here! whats between the subscriber and the mission?
    //I HAD WRITTEN SOME METHODS FOR DOING THAT AND JUST NEED TO THINK WHERE THEY SHOULD BE LOCATED
        workerSubscribesMissionsMap.computeIfAbsent(user.getName(),s-> new HashSet<>()).add(missionInfoDTO);
        workerWorkOnMissionMap.computeIfAbsent(user.getName(),s-> new HashSet<>()).add(missionInfoDTO);
    }

    public Set<UserDTO> getMissionWorkers(String missionName){
        return missionInfoDTOWorkersMap.get(missionInfoDTOMap.get(missionName));
    }

    public void removeSubscriber(UserDTO theUser, MissionInfoDTO theMission) {

        missionInfoDTOWorkersMap.get(theMission).remove(theUser);
        workerWorkOnMissionMap.get(theUser).remove(theMission);
        workerSubscribesMissionsMap.get(theUser).remove(theMission);

    }

    public void pauseSubscriber(UserDTO theUser, MissionInfoDTO theMission) {
        workerWorkOnMissionMap.get(theMission).remove(theUser);
    }

    public void resumeSubscriber(UserDTO theUser, MissionInfoDTO theMission) {
        workerWorkOnMissionMap.computeIfAbsent(theUser.getName(),s-> new HashSet<>()).add(theMission);
    }
}
