package ProcessesManagment.TasksUploadServer.servlets;

//@WebServlet(name = "IncrementalTasksUploadServlet", urlPatterns = "/upload-incremental-task")
//public class IncrementalTaskUploadServlet extends HttpServlet {
//
//    private Gson gson = new Gson();
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//
//
//        ProcessesManager missionsManager = ServletUtils.getProcessesManager(getServletContext());
//        SubscribesManager subscribesManager = ServletUtils.getSubscribesManager(getServletContext());
//        String originalMissionName = req.getHeader(MISSION_NAME);
//
//        if(req.getHeader("incrementalType").equals("fromScratch"))
//        {
//            missionsManager.createIncrementalMission(originalMissionName,subscribesManager,fromScratch);
//
//            if(!missionsManager.isMissionExists(newTaskInfo.getTaskName())) //No task with the same name was found
//            {
//                missionsManager.addSimulationTask(newTaskInfo);
//
//                resp.addHeader("message", "The task " + newTaskInfo.getTaskName() + " uploaded successfully!");
//                resp.setStatus(HttpServletResponse.SC_ACCEPTED);
//                GraphsManager graphsManager = ServletUtils.getGraphsManager(getServletContext());
//                missionsManager.addMissionsDTO(newTaskInfo,graphsManager,subscribesManager,(String)req.getSession().getAttribute(Constants.USERNAME));
//
//            }
//            else //A task with the same name already exists in the system
//            {
//                resp.addHeader("message", "The task " + newTaskInfo.getTaskName() + " already exists in the system!");
//                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            }
//        }
//        else if(req.getHeader("taskType").equals("compilation")) //Uploaded compilation task
//        {
//            CompilationTaskDTO newTaskInfo = gson.fromJson(req.getReader(), CompilationTaskDTO.class);
//            newTaskInfo.setTaskCreator((String)req.getSession(false).getAttribute(Constants.USERNAME));
//
//            if(!missionsManager.isMissionExists(newTaskInfo.getTaskName())) //No task with the same name was found
//            {
//                missionsManager.addCompilationTask(newTaskInfo);
//
//                resp.addHeader("message", "The task " + newTaskInfo.getTaskName() + " uploaded successfully!");
//                resp.setStatus(HttpServletResponse.SC_ACCEPTED);
//                GraphsManager graphsManager = ServletUtils.getGraphsManager(getServletContext());
//                missionsManager.addMissionsDTO(newTaskInfo,graphsManager,subscribesManager,(String)req.getSession().getAttribute(Constants.USERNAME));
//            }
//            else //A task with the same name already exists in the system
//            {
//                resp.addHeader("message", "The task " + newTaskInfo.getTaskName() + " already exists in the system!");
//                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            }
//        }
//        else //invalid header for uploading new task to system
//        {
//            resp.addHeader("message", "Error in uploading task to server!");
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        }
//    }
//}
//
//}
