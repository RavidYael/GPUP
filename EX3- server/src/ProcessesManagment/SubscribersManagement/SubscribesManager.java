package ProcessesManagment.SubscribersManagement;

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

    public void removeMission(MissionInfoDTO theMission){
        missionInfoDTOWorkersMap.remove(theMission);
        missionInfoDTOMap.remove(theMission.getMissionName(),theMission);

        for (String worker: workerSubscribesMissionsMap.keySet()) {
            if(workerSubscribesMissionsMap.get(worker).contains(theMission)){
                workerSubscribesMissionsMap.get(worker).remove(theMission);
            }
        }
        for (String worker: workerWorkOnMissionMap.keySet()) {
            if(workerWorkOnMissionMap.get(worker).contains(theMission)){
                workerWorkOnMissionMap.get(worker).remove(theMission);
            }
        }

    }

    public Set<String> getWorkerWorkingMissionsNames(String workerName) {
        return workerWorkOnMissionMap.get(workerName).stream().map(M->M.getMissionName()).collect(Collectors.toSet());
    }

//    public Set<String> getWorkerWorkingMissionsNamesWhichNotDone(String workerName) {
//        return workerWorkOnMissionMap.get(workerName).stream().filter(M->m.getMis).map(M->M.getMissionName()).collect(Collectors.toSet());
//    }


    public SubscribesManager() {
        missionInfoDTOWorkersMap= new HashMap<>();
        missionInfoDTOMap= new HashMap<>();
        workerSubscribesMissionsMap = new HashMap<>();
        workerWorkOnMissionMap = new HashMap<>();
    }

    public Map<String, Set<MissionInfoDTO>> getWorkerSubscribesMissionsMap() {
        return workerSubscribesMissionsMap;
    }

    public void addMission(MissionInfoDTO theMission){
        missionInfoDTOMap.put(theMission.getMissionName(),theMission);
        missionInfoDTOWorkersMap.put(theMission,new HashSet<>());

    }


    public void addSubscriber(UserDTO user ,MissionInfoDTO missionInfoDTO){
    //missionInfoDTOWorkersMap.put(missionInfoDTO,new HashSet<>()); // you don't want to create a new one each time, only the first time. so replaced with:
        missionInfoDTO.setMissionStatus(MissionInfoDTO.MissionStatus.running);
        missionInfoDTOWorkersMap.get(missionInfoDTO).add(user);
        workerSubscribesMissionsMap.computeIfAbsent(user.getName(),s-> new HashSet<>()).add(missionInfoDTO);
        workerWorkOnMissionMap.computeIfAbsent(user.getName(),s-> new HashSet<>()).add(missionInfoDTO);
    }

    public Set<UserDTO> getMissionWorkers(String missionName){
        return missionInfoDTOWorkersMap.get(missionInfoDTOMap.get(missionName));
    }

    public void removeSubscriber(UserDTO theUser, MissionInfoDTO theMission) {

        missionInfoDTOWorkersMap.get(theMission).remove(theUser);
        workerWorkOnMissionMap.get(theUser.getName()).remove(theMission);
        workerSubscribesMissionsMap.get(theUser).remove(theMission);

    }

    public void pauseSubscriber(UserDTO theUser, MissionInfoDTO theMission) {
        workerWorkOnMissionMap.get(theUser.getName()).remove(theMission);
    }

    public void resumeSubscriber(UserDTO theUser, MissionInfoDTO theMission) {
        workerWorkOnMissionMap.computeIfAbsent(theUser.getName(),s-> new HashSet<>()).add(theMission);
    }


}
