package ProcessesManagment.ProcessesConsumServer.servlets;

import DTOs.MissionInfoDTO;
import ProcessesManagment.ExecutionManagement.SubscribersManagement.SubscribesManager;
import ProcessesManagment.ProcessesManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.Set;

@WebServlet("/tasksListForUser")
public class TaskListForUser extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = SessionUtils.getUsername(req);
        SubscribesManager subscribesManager = ServletUtils.getSubscribesManager(getServletContext());
        Set<MissionInfoDTO> missionDTOs = subscribesManager.getWorkerSubscribesMissionsMap().get(userName);
        if (missionDTOs == null ){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Gson missionGson = new Gson();
        String missionsAsJson = missionGson.toJson(missionDTOs);
        resp.getWriter().println(missionsAsJson);

    }


}
