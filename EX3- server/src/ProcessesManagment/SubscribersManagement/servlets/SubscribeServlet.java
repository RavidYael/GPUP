package ProcessesManagment.SubscribersManagement.servlets;



import DTOs.MissionInfoDTO;
import DTOs.UserDTO;
import ProcessesManagment.SubscribersManagement.SubscribesManager;
import ProcessesManagment.ProcessesManager;

import ProcessesManagment.SubscribersManagement.constants.Constants;
import UserManagement.UserManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet("/subscribe")
public class SubscribeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        //TODO : THAT'S INTERESTING, WHEN DO WE USE THE SESSION UTIL AND WHEN DO WE REQUEST PARAM?
        String usernameFromSession = SessionUtils.getUsername(request);

        String subscribeType = request.getParameter(Constants.SUBSCRIBE_TYPE); //or in other way
        //register or unregister or pause or resume

        SubscribesManager subscribesManager = ServletUtils.getSubscribesManager(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        ProcessesManager processesManager = ServletUtils.getProcessesManager(getServletContext());
        UserDTO theUser = userManager.getUserDTO(usernameFromSession);

        synchronized (this) {

            String missionName = request.getParameter(Constants.MISSION_NAME);
            MissionInfoDTO theMission = processesManager.getMissionInfoDTO(missionName);

            if (subscribeType.equals("register")) {
                if(!theMission.getMissionStatus().equals(MissionInfoDTO.MissionStatus.finished) &&
                   !theMission.getMissionStatus().equals(MissionInfoDTO.MissionStatus.stopped) &&
                   !subscribesManager.getMissionWorkers(missionName).contains(theUser))
                {
                    theMission.increaseCurrentNumOfxExecutingWorkers();
                    subscribesManager.addSubscriber(theUser, theMission);
                }

            } else if (subscribeType.equals("unregister")) {
                if(!theMission.getMissionStatus().equals(MissionInfoDTO.MissionStatus.finished) &&
                   !theMission.getMissionStatus().equals(MissionInfoDTO.MissionStatus.stopped) &&
                   subscribesManager.getWorkerSubscribesMissionsMap().get(usernameFromSession).contains(theMission))
                {
                    theMission.decreaseCurrentNumOfxExecutingWorkers();
                    subscribesManager.removeSubscriber(theUser, theMission);
                }
            } else if (subscribeType.equals("pause")) {
                if(theMission.getMissionStatus().equals(MissionInfoDTO.MissionStatus.running) &&
                   subscribesManager.getWorkerSubscribesMissionsMap().get(usernameFromSession).contains(theMission))
                {
                    theMission.decreaseCurrentNumOfxExecutingWorkers();
                    subscribesManager.pauseSubscriber(theUser, theMission);
                }
            } else if (subscribeType.equals("resume")) {
                if(subscribesManager.getWorkerSubscribesMissionsMap().get(usernameFromSession).contains(theMission))
                {
                    theMission.increaseCurrentNumOfxExecutingWorkers();
                    subscribesManager.resumeSubscriber(theUser, theMission);
                }
            }

        }
    }

}