package ProcessesManagment.ExecutionManagement.SubscribersManagement.servlets;



import DTOs.MissionInfoDTO;
import DTOs.UserDTO;
import ProcessesManagment.ExecutionManagement.SubscribersManagement.SubscribesManager;
import ProcessesManagment.ProcessesManager;

import UserManagement.UserManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static ProcessesManagment.ExecutionManagement.SubscribersManagement.constants.Constants.MISSION_NAME;
import static ProcessesManagment.ExecutionManagement.SubscribersManagement.constants.Constants.SUBSCRIBE_TYPE;
import static UserManagement.UsersManagementServer.constants.Constants.DEGREE;
import static UserManagement.UsersManagementServer.constants.Constants.USERNAME;

@WebServlet("/subscribe")
public class SubscribeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        //TODO : THAT'S INTERESTING, WHEN DO WE USE THE SESSION UTIL AND WHEN DO WE REQUEST PARAM?
        String usernameFromSession = SessionUtils.getUsername(request);

        String subscribeType = request.getParameter(SUBSCRIBE_TYPE); //or in other way
        //register or unregister or pause or resume

        SubscribesManager subscribesManager = ServletUtils.getSubscribesManager(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        ProcessesManager processesManager = ServletUtils.getProcessesManager(getServletContext());

        synchronized (this) {
            String missionName = request.getParameter(MISSION_NAME);
            //String userName = request.getParameter(USERNAME);

            MissionInfoDTO theMission = processesManager.getMissionInfoDTO(missionName);

            if (subscribeType.equals("register")) {

                theMission.increaseCurrentNumOfxExecutingWorkers();
                UserDTO theUser = userManager.getUserDTO(usernameFromSession);
                subscribesManager.addSubscriber(theUser, theMission);

            } else if (subscribeType.equals("unregister")) {

                theMission.decreaseCurrentNumOfxExecutingWorkers();
                UserDTO theUser = userManager.getUserDTO(usernameFromSession);
                subscribesManager.removeSubscriber(theUser, theMission);

            } else if (subscribeType.equals("pause")) {

                theMission.decreaseCurrentNumOfxExecutingWorkers();
                UserDTO theUser = userManager.getUserDTO(usernameFromSession);
                subscribesManager.pauseSubscriber(theUser, theMission);

            } else if (subscribeType.equals("resume")) {

                theMission.increaseCurrentNumOfxExecutingWorkers();
                UserDTO theUser = userManager.getUserDTO(usernameFromSession);
                subscribesManager.resumeSubscriber(theUser, theMission);

            }
            // TODO : ADD RESPONSE STATUS IN RETURN
        }
    }

}