package ProcessesManagment.SubscribersManagement.servlets;

import DTOs.UserDTO;
import ProcessesManagment.SubscribersManagement.SubscribesManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.util.Set;

import static ProcessesManagment.SubscribersManagement.constants.Constants.MISSION_NAME;
//TODO ITS NOT SHOULD BE LIKE THAT! WE SHOULD CHANGE IT TO POST AND A GET METHODS IN TH SUBSCRIBE SERVLET..

@WebServlet("/subscribedUsers")
public class getSubscribedUsers extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        SubscribesManager subscribesManager = ServletUtils.getSubscribesManager(getServletContext());
        String taskName = req.getParameter(MISSION_NAME);
        Set<UserDTO> usersSubForTask = subscribesManager.getMissionWorkers(taskName);
        Gson usersGson = new Gson();
        resp.getWriter().println(usersGson.toJson(usersSubForTask));


    }


}
