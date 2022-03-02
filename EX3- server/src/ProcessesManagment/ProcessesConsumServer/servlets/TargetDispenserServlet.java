package ProcessesManagment.ProcessesConsumServer.servlets;

import DTOs.CompilationTaskDTO;
import DTOs.MissionInfoDTO;
import DTOs.SimulationTaskDTO;
import DTOs.TargetDTO;
import ProcessesManagment.ExecutionManagement.SubscribersManagement.SubscribesManager;
import ProcessesManagment.ProcessesManager;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.omg.PortableInterceptor.SUCCESSFUL;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.Set;


import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet("/runnable-target")
public class TargetDispenserServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ProcessesManager processesManager = ServletUtils.getProcessesManager(getServletContext());

        SubscribesManager subscribesManager = ServletUtils.getSubscribesManager(getServletContext());

        String workerName = SessionUtils.getUsername(req);

        if (workerName != null) {

            Set<String> missionsWorkerWorksOn = subscribesManager.getWorkerWorkingMissionsNames(workerName);

            TargetDTO targetDTO = processesManager.pullTaskReadyForWorker(missionsWorkerWorksOn,workerName);

            if (targetDTO == null) {

                resp.getWriter().println("there is no runnable targets for this worker");
                resp.setStatus(SC_CONFLICT);

            }
            else {
                resp.getWriter().write(gson.toJson(targetDTO));
                resp.setStatus(SC_ACCEPTED);
            }
        }
    }
}


