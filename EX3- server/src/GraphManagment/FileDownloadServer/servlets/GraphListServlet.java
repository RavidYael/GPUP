package GraphManagment.FileDownloadServer.servlets;


import DTOs.GraphInfoDTO;
import GraphManagment.GraphsManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.util.Set;

@WebServlet("/graphsList")
public class GraphListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson graphsGson = new Gson();
        GraphsManager graphsManager = ServletUtils.getGraphsManager(getServletContext());
        Set<GraphInfoDTO> graphsSet = graphsManager.getAllGraphsDTO();
        if (graphsSet == null){
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().println("No graph has been uploaded yet");
        }
       String graphsAsJson =  graphsGson.toJson(graphsSet);
        resp.getWriter().println(graphsAsJson);
        resp.setStatus(HttpServletResponse.SC_OK);

    }
}
