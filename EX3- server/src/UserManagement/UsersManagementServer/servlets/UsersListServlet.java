package UserManagement.UsersManagementServer.servlets;

import DTOs.UserDTO;
import utils.ServletUtils;
import UserManagement.UserManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(name = "usersList Servlet", urlPatterns = "/userList")
public class UsersListServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {

            Gson gson = new Gson();

            UserManager userManager = ServletUtils.getUserManager(getServletContext());


            Collection<UserDTO> usersList = userManager.getUsers();

            String json = gson.toJson(usersList);

            out.println(json);

            out.flush();

        }
    }

}
