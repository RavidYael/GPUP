package ProcessesManagment.TasksUploadServer.servlets;

import GraphManagment.GraphsManager;
import ProcessesManagment.ProcessesManager;
import ProcessesManagment.SubscribersManagement.SubscribesManager;
import UserManagement.UsersManagementServer.constants.Constants;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;

import static ProcessesManagment.ExecutionManagement.Constants.CONTROL_TYPE;
import static ProcessesManagment.SubscribersManagement.constants.Constants.MISSION_NAME;

@WebServlet(name = "IncrementalTasksUploadServlet", urlPatterns = "/re-upload-task")
public class IncrementalTaskUploadServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {


        ProcessesManager missionsManager = ServletUtils.getProcessesManager(getServletContext());
        SubscribesManager subscribesManager = ServletUtils.getSubscribesManager(getServletContext());
        GraphsManager graphsManager = ServletUtils.getGraphsManager(getServletContext());
        String originalMissionName = req.getHeader(MISSION_NAME);

        if(req.getHeader(CONTROL_TYPE).equals("From Scratch")) {
            missionsManager.createBasedOnMission(originalMissionName, subscribesManager, "fromScratch", graphsManager, (String) req.getSession().getAttribute(Constants.USERNAME));
        }
         else if(req.getHeader(CONTROL_TYPE).equals("Incrementally")){
            missionsManager.createBasedOnMission(originalMissionName, subscribesManager, "incrementally", graphsManager, (String) req.getSession().getAttribute(Constants.USERNAME));
        }
    }
}
