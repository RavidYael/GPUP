package ProcessesManagment.ProcessesConsumServer.servlets;


import ProcessesManagment.ProcessesManager;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import DTOs.MissionInfoDTO;
import utils.ServletUtils;
import DTOs.CompilationTaskDTO;
import DTOs.SimulationTaskDTO;

import java.io.IOException;

@WebServlet("/task-data")
public class ProcessAccessServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ProcessesManager processesManager = ServletUtils.getProcessesManager(getServletContext());

        if(req.getParameter("selectedMissionName") != null)
        {
            String missionInfoName = req.getParameter("selectedMissionName");
            String infoAsString;

            if(processesManager.isMissionExists(missionInfoName))
            {
                MissionInfoDTO missionInfo = processesManager.getMissionInfoDTO(missionInfoName);
                infoAsString = gson.toJson(missionInfo, MissionInfoDTO.class);

                resp.getWriter().write(infoAsString);
                resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            }
            else //Task not exists in the system
            {
                resp.getWriter().println("The task " + missionInfoName + " doesn't exist in the system!");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        else if(req.getParameter("task") != null) //Requesting for task-info
        {
            String taskName = req.getParameter("task");

            if(processesManager.isTaskExists(taskName)) //The task exists in the system
            {
                String infoAsString = null;

                if(processesManager.isSimulationTask(taskName)) //Requesting for simulation task
                {
                    SimulationTaskDTO simulationInfo = processesManager.getSimulationTaskInformation(taskName);
                    infoAsString = this.gson.toJson(simulationInfo, SimulationTaskDTO.class);

                    resp.addHeader("taskType", "simulation");
                }
                else  //Requesting for compilation task
                {
                    CompilationTaskDTO compilationInfo = processesManager.getCompilationTaskInformation(taskName);
                    infoAsString = this.gson.toJson(compilationInfo, CompilationTaskDTO.class);

                    resp.addHeader("taskType", "compilation");
                }

                resp.getWriter().write(infoAsString);
                resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            }
            else //Task not exists in the system
            {
                resp.getWriter().println("Task not exists!");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        else //Invalid request
        {
            resp.getWriter().println("Invalid parameter!");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


}

