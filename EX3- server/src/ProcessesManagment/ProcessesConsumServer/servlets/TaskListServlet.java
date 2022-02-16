package ProcessesManagment.ProcessesConsumServer.servlets;

import DTOs.MissionInfoDTO;
import ProcessesManagment.ProcessesManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.util.Set;

@WebServlet("/tasksList")
public class TaskListServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProcessesManager processesManager = ServletUtils.getMissionsManager(getServletContext());
        Set<MissionInfoDTO> missionDTOs = processesManager.getAllMissionInfoDTO();
        if (missionDTOs.isEmpty()){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("not tasks have been uploaded yet!");
        }
        Gson missionGson = new Gson();
        String missionsAsJson = missionGson.toJson(missionDTOs);
        resp.getWriter().println(missionsAsJson);

    }
}
