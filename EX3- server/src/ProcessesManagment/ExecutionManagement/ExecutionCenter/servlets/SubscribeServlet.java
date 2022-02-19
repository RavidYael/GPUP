package ProcessesManagment.ExecutionManagement.ExecutionCenter.servlets;


import DTOs.MissionInfoDTO;
import DTOs.UserDTO;
import ProcessesManagment.ExecutionManagement.ExecutionCenter.ExecutionsManager;
import ProcessesManagment.ProcessesManager;
import UserManagement.UserManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static ProcessesManagment.ExecutionManagement.SubscribersManagement.constants.Constants.MISSION_NAME;

@WebServlet("/ExecutionsCenter")
public class SubscribeServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setContentType("text/plain;charset=UTF-8");

        }
    }