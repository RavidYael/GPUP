package ProcessesManagment.ProcessesConsumServer.servlets;

import GraphManagment.GraphsManager;
import ProcessesManagment.ProcessesManager;
import com.google.gson.Gson;
import dependency.target.Target;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.util.stream.Collectors;

import static ProcessesManagment.ProcessesConsumServer.constants.Constants.MISSION_NAME;

@WebServlet("/task-progress")
public class TaskProgressServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        double progress;
        String taskName = req.getParameter(MISSION_NAME);
        ProcessesManager processesManager = ServletUtils.getProcessesManager(getServletContext());
        double finishedTargets = processesManager.getGraphInExecutionByName().get(taskName)
                .getGraphInExecution().getAllTargets().values().stream()
                    .filter(t-> t.getTargetStatus().equals(Target.TargetStatus.Finished)).collect(Collectors.toSet()).size();
        double allTargets = processesManager.getGraphInExecutionByName().get(taskName).getGraphInExecution().getAllTargets().values().size();

            progress = (finishedTargets / allTargets) * 100.0;
      //  resp.getWriter().println(new Gson().toJson(progress));
        resp.getWriter().println(progress);


    }
}
