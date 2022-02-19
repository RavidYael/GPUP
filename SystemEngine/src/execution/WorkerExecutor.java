package execution;

import DTOs.MissionInfoDTO;
import DTOs.TargetDTO;
import DTOs.WorkerDTO;
import dependency.target.Target;

import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class WorkerExecutor extends Thread{

    private WorkerDTO myWorker;
    private Set<MissionInfoDTO> missionsMyWorkerSubscribe;
    private Set<TargetDTO> runnableTargetsForTheWorker;
    private int numOfThreads;
    private ThreadPoolExecutor threadPoolExecutors = (ThreadPoolExecutor) Executors.newFixedThreadPool(numOfThreads);

    public void run(){
    while(threadPoolExecutors.getActiveCount() != numOfThreads){
    //TODO WE NEED TO GET NEW TARGET TO WORK ON
    }
    }



}
