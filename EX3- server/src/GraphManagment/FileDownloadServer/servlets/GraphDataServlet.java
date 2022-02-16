package GraphManagment.FileDownloadServer.servlets;

import DTOs.GraphInfoDTO;
import GraphManagment.GraphsManager;
import UserManagement.UsersManagementServer.constants.Constants;
import com.google.gson.Gson;
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

import static GraphManagment.FileUploadServer.constants.Constants.WORKING_DIRECTORY_PATH;
import static dependency.graph.GraphFactory.newGraphWithData;


@WebServlet("/graphs-data")
    @MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)

    public class GraphDataServlet extends HttpServlet {


    private static final Object creatingDirectory = new Object();



    @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        System.out.println("in Data servlet - get");
        GraphsManager graphsManager = ServletUtils.getGraphsManager(getServletContext());

        Gson gson = new Gson();

        if (req.getParameter("graphName") != null) {
            String graphName = req.getParameter("graphName");

            if (graphsManager.isGraphExist(graphName)) {
                GraphInfoDTO currDTO = graphsManager.getGraphInfoDTO(graphName);
                String dtoAsString = gson.toJson(currDTO, GraphInfoDTO.class);
                resp.getWriter().write(dtoAsString);
                resp.setStatus(HttpServletResponse.SC_ACCEPTED);

            } else {
                resp.getWriter().println("Graph doesn't exist!");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            //try {
//            response.setContentType("text/plain");
//
//            PrintWriter out = response.getWriter();
//            out.println("we are trying to upload");
//            response.addHeader("message","");
//            Part theFile = request.getPart("fileToUpload");
//            Path fileDir = Paths.get(WORKING_DIRECTORY_PATH);
//            Path filePath = Paths.get(WORKING_DIRECTORY_PATH + "\\" + theFile.getSubmittedFileName() +".xml");
//            if (!Files.exists(fileDir))
//                Files.createDirectory(fileDir);
//
//            if (Files.exists(filePath))
//                Files.delete(filePath);
//
//            Files.createFile(filePath);
//
//            InputStream fileInputStream = theFile.getInputStream();
//
//            Files.copy(fileInputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

                try {
                    synchronized (creatingDirectory) {
                        if (!Files.exists(Paths.get(WORKING_DIRECTORY_PATH)))
                            Files.createDirectory(Paths.get(WORKING_DIRECTORY_PATH));
                    }

                    Path filePath = Paths.get(WORKING_DIRECTORY_PATH + "\\CurrentGraph.xml");

                    if (Files.exists(filePath))
                        Files.delete(filePath);

                    Files.createFile(filePath);

                    Part filePart = request.getPart("fileToUpload");
                    InputStream fileInputStream = filePart.getInputStream();
                    Files.copy(fileInputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                    PrintWriter out = response.getWriter();
                    GraphsManager graphsManager = ServletUtils.getGraphsManager(getServletContext());

                    out.println("in GraphFactory servlet, about to validate " + filePart.getName() + " you just uploaded");

                    DependencyGraph uploadedGraph;

                    try {
                        uploadedGraph = newGraphWithData(filePath.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        // response.addHeader("message",e.getMessage());
                        response.setHeader("message", e.getMessage());
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }

                    if (graphsManager.isGraphExist(uploadedGraph.getGraphName())) {
                        //response.addHeader("message", " The graph: " + uploadedGraph.getGraphName() + " already exists");
                        response.setHeader("message", " The graph: " + uploadedGraph.getGraphName() + " already exists");
                        response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);

                    } else {
                        uploadedGraph.setUploaderName((String) request.getSession(true).getAttribute(Constants.USERNAME));
                        graphsManager.addGraph(uploadedGraph.getGraphName(), uploadedGraph);
                        graphsManager.addGraphXml(uploadedGraph.getGraphName(), filePath);
                        // response.addHeader("message"," The graph: " + uploadedGraph.getGraphName() + " uploaded successfully");
                        response.setHeader("message", " The graph: " + uploadedGraph.getGraphName() + " uploaded successfully");
                        response.setStatus(HttpServletResponse.SC_ACCEPTED);
                    }

                } catch (Exception e) {
                    response.getWriter().println("an error occurred, graph might have not been uploaded correctly ");
                    response.getWriter().println(e.getStackTrace());
                }

            }




//        private void printPart(Part part, PrintWriter out) {
//            StringBuilder sb = new StringBuilder();
//            sb
//                    .append("Parameter Name: ").append(part.getName()).append("\n")
//                    .append("Content Type (of the file): ").append(part.getContentType()).append("\n")
//                    .append("Size (of the file): ").append(part.getSize()).append("\n")
//                    .append("Part Headers:").append("\n");
//
//            for (String header : part.getHeaderNames()) {
//                sb.append(header).append(" : ").append(part.getHeader(header)).append("\n");
//            }
//
//            out.println(sb.toString() + "was saved to server successfully");
//        }
//
//        private String readFromInputStream(InputStream inputStream) {
//            return new Scanner(inputStream).useDelimiter("\\Z").next();
//        }
//
//        private void printFileContent(String content, PrintWriter out) {
//            out.println("File content:");
//            out.println(content);
//        }

    }


