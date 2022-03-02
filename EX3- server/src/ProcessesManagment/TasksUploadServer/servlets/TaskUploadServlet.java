package ProcessesManagment.TasksUploadServer.servlets;


import GraphManagment.GraphsManager;
import ProcessesManagment.ExecutionManagement.SubscribersManagement.SubscribesManager;
import ProcessesManagment.ProcessesManager;
import UserManagement.UsersManagementServer.constants.Constants;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import DTOs.CompilationTaskDTO;
import DTOs.SimulationTaskDTO;

import java.io.IOException;

@WebServlet(name = "TasksUploadServlet", urlPatterns = "/upload-task")
public class TaskUploadServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // TODO I HOPE GSON CREATE NEW OBJECT, OTHERWISE THE TASKS ARE LOCALLY TO THIS SCOPE AND WE LOSE THEM WHEN INSERTING TO THE MAPS
        // ITS NOT THAT COMPLICATED IT JUST THAT WE NEED TO CREATE A A CONSTRUCTOR WHICH I DONT HAVE ZAIN TO CREATE RIGHT NOW

        ProcessesManager missionsManager = ServletUtils.getProcessesManager(getServletContext());
        SubscribesManager subscribesManager = ServletUtils.getSubscribesManager(getServletContext());

        if(req.getHeader("taskType").equals("simulation")) //Uploaded simulation task
        {
            SimulationTaskDTO newTaskInfo = gson.fromJson(req.getReader(), SimulationTaskDTO.class);
            newTaskInfo.setTaskCreator((String)req.getSession(false).getAttribute(Constants.USERNAME));

            if(!missionsManager.isMissionExists(newTaskInfo.getTaskName())) //No task with the same name was found
            {
                missionsManager.addSimulationTask(newTaskInfo);


                resp.addHeader("message", "The task " + newTaskInfo.getTaskName() + " uploaded successfully!");
                resp.setStatus(HttpServletResponse.SC_ACCEPTED);
                GraphsManager graphsManager = ServletUtils.getGraphsManager(getServletContext());
                missionsManager.addMissionsDTO(newTaskInfo,graphsManager,subscribesManager,(String)req.getSession().getAttribute(Constants.USERNAME));

            }
            else //A task with the same name already exists in the system
            {
                resp.addHeader("message", "The task " + newTaskInfo.getTaskName() + " already exists in the system!");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        else if(req.getHeader("taskType").equals("compilation")) //Uploaded compilation task
        {
            CompilationTaskDTO newTaskInfo = gson.fromJson(req.getReader(), CompilationTaskDTO.class);
            newTaskInfo.setTaskCreator((String)req.getSession(false).getAttribute(Constants.USERNAME));

            if(!missionsManager.isMissionExists(newTaskInfo.getTaskName())) //No task with the same name was found
            {
                missionsManager.addCompilationTask(newTaskInfo);

                resp.addHeader("message", "The task " + newTaskInfo.getTaskName() + " uploaded successfully!");
                resp.setStatus(HttpServletResponse.SC_ACCEPTED);
                GraphsManager graphsManager = ServletUtils.getGraphsManager(getServletContext());
                missionsManager.addMissionsDTO(newTaskInfo,graphsManager,subscribesManager,(String)req.getSession().getAttribute(Constants.USERNAME));
            }
            else //A task with the same name already exists in the system
            {
                resp.addHeader("message", "The task " + newTaskInfo.getTaskName() + " already exists in the system!");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        else //invalid header for uploading new task to system
        {
            resp.addHeader("message", "Error in uploading task to server!");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}

