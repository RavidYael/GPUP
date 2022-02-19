package ProcessesManagment.ProcessesConsumServer.servlets;


import DTOs.SimulationTaskDTO;
import DTOs.TargetDTO;
import ProcessesManagment.ProcessesManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;

import static ProcessesManagment.ProcessesConsumServer.constants.Constants.TASK_RESULT;
import static jakarta.servlet.http.HttpServletResponse.SC_ACCEPTED;
import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@WebServlet("/update-target-result")
public class processUpdateServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            TargetDTO updatedTargetDTO = gson.fromJson(req.getReader(), TargetDTO.class);

            ProcessesManager processesManager = ServletUtils.getProcessesManager(getServletContext());

            processesManager.updateTargetResult(updatedTargetDTO);

            resp.getWriter().println("Thanks for informing us!");
            resp.setStatus(SC_ACCEPTED);

    }
}
