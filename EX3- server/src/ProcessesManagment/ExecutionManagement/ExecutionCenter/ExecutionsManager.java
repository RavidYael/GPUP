package ProcessesManagment.ExecutionManagement.ExecutionCenter;

import DTOs.*;
import ProcessesManagment.ExecutionManagement.SubscribersManagement.SubscribesManager;
import ProcessesManagment.ProcessesManager;
import execution.WorkerExecutor;
import jakarta.servlet.ServletContext;
import utils.GraphInExecution;
import utils.ServletUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.TimeUnit.MINUTES;

public class ExecutionsManager {

    private Set<String> listOfAllTasks = new HashSet<>();
    private Map<String, GraphInExecution> graphInExecutionByName = new HashMap<>();
    private Map<String, GraphInfoDTO> graphInExecutionInfoByName = new HashMap<>();
    private ThreadPoolExecutor calculators = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
    private Map<WorkerExecutor,Set<MissionInfoDTO>> workerExecutorMissionsMap = new HashMap<>();

    void generateWorkForWorker(TargetDTO DoneTarget){

            GraphInExecution theGraph = graphInExecutionByName.get(DoneTarget.getMissionName);
            calculators.submit(theGraph.generateAvailableTargets()); //TODO: THIS METHOD HAS TO BE RUNNABLE, AND SHOULD TAKE TO DUMMY CALCULATOR LOCK

        }
    }









}
