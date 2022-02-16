package GraphManagment.FileDownloadServer.servlets;

import GraphManagment.GraphsManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.nio.file.Path;

@WebServlet("/graphs-xml")
public class GraphXMLServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        GraphsManager graphsManager = ServletUtils.getGraphsManager(getServletContext());
        String graphName = req.getParameter("graphName");
        Path path = graphsManager.getGraphsXmlsByName(graphName);
        resp.getWriter().println(path.toString());

    }
}
