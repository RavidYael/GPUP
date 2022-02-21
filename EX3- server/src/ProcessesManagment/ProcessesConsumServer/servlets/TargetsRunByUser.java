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
import utils.GraphInExecution;
import utils.ServletUtils;

import java.io.IOException;

import static ProcessesManagment.ProcessesConsumServer.constants.Constants.MISSION_NAME;
import static ProcessesManagment.ProcessesConsumServer.constants.Constants.WORKER_NAME;


import GraphManagment.GraphsManager;
import com.google.gson.Gson;
import dependency.target.Target;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;

    @WebServlet("/target-run-by-user")
    public class TargetsRunByUser extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            String userName = req.getParameter(WORKER_NAME);
            String missionName = req.getParameter(MISSION_NAME);
            ProcessesManager processesManager = ServletUtils.getProcessesManager(getServletContext());

            GraphInExecution graphInExecution = processesManager.getGraphInExecutionByName().get(missionName);
            Integer numOfTargets = graphInExecution.getNumOfExecutedTargetsForUser(userName);

            resp.getWriter().println(new Gson().toJson(numOfTargets,Integer.class));
            resp.setStatus(HttpServletResponse.SC_OK);

        }
    }

