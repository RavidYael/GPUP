package DataManagment.FileUploadServer.servlets;

import DataManagment.GraphsManager;
import dependency.graph.DependencyGraph;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.ServletUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import static DataManagment.FileUploadServer.constants.Constants.WORKING_DIRECTORY_PATH;
import static dependency.graph.GraphFactory.newGraphWithData;


@WebServlet("/upload-xml")
    @MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)

    public class FileUploadServlet extends HttpServlet {

    @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().println("welcome to uploading servlet");
        DependencyGraph graph = new DependencyGraph();
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            response.setContentType("text/plain");

            PrintWriter out = response.getWriter();

            out.println("we are trying to upload");

            Part theFile = request.getPart("fileToUpload");

            Path filePath = Paths.get(WORKING_DIRECTORY_PATH + "\\" + theFile.getName() + ".xml");

            if (Files.exists(filePath))
                Files.delete(filePath);

            Files.createFile(filePath);

            InputStream fileInputStream = theFile.getInputStream();

            Files.copy(fileInputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

            GraphsManager graphsManager = ServletUtils.getGraphsManager(getServletContext());

            out.println("in GraphFactory servlet, about to validate " + theFile.getName() + " you just uploaded");

            //TODO UPDATE THE JAXB TO HAVE A GRAPH NAME. BY NOW, THE GRAPH HAS NONAME THEREFORE YOU WONT BE ABLE TO FIND IT IN THE MAP OF THE GRAPH MANAGER

            DependencyGraph uploadedGraph;

            try {
                uploadedGraph = newGraphWithData(filePath.toString());
            } catch (Exception e) {
                e.printStackTrace();
                out.println(e.getMessage());
                return;
            }

            if (graphsManager.isGraphExist(uploadedGraph.getGraphName())){
                response.addHeader("message"," The graph: " + uploadedGraph.getGraphName() + " is already exists");
                out.println("It seems like the graph that you tried to upload is already exists in our system");
                out.println("Please try again");
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            }

            else {
                graphsManager.addGraph(uploadedGraph.getGraphName(),uploadedGraph);
                uploadedGraph.setUploaderName(request.getHeader("username"));
                graphsManager.addGraphXml(uploadedGraph.getGraphName(),filePath);
                response.addHeader("message"," The graph: " + uploadedGraph.getGraphName() + " uploaded successfully");
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
            }

    }



        private void printPart(Part part, PrintWriter out) {
            StringBuilder sb = new StringBuilder();
            sb
                    .append("Parameter Name: ").append(part.getName()).append("\n")
                    .append("Content Type (of the file): ").append(part.getContentType()).append("\n")
                    .append("Size (of the file): ").append(part.getSize()).append("\n")
                    .append("Part Headers:").append("\n");

            for (String header : part.getHeaderNames()) {
                sb.append(header).append(" : ").append(part.getHeader(header)).append("\n");
            }

            out.println(sb.toString() + "was saved to server successfully");
        }

        private String readFromInputStream(InputStream inputStream) {
            return new Scanner(inputStream).useDelimiter("\\Z").next();
        }

        private void printFileContent(String content, PrintWriter out) {
            out.println("File content:");
            out.println(content);
        }

    }


