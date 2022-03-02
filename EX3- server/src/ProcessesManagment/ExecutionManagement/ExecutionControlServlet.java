package ProcessesManagment.ExecutionManagement;

import ProcessesManagment.ProcessesManager;
import dependency.target.Target;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.util.stream.Collectors;

import static ProcessesManagment.ExecutionManagement.Constants.CONTROL_TYPE;
import static ProcessesManagment.ProcessesConsumServer.constants.Constants.MISSION_NAME;


    @WebServlet("/task-control")
    public class ExecutionControlServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            String taskName = req.getParameter(MISSION_NAME);
            String controlChoice = req.getParameter(CONTROL_TYPE);
            ProcessesManager processesManager = ServletUtils.getProcessesManager(getServletContext());

            processesManager.controlMission(taskName,controlChoice);

            resp.setStatus(HttpServletResponse.SC_ACCEPTED);

        }
    }

