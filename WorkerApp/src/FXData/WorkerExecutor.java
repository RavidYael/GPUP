package FXData;

import DTOs.MissionInfoDTO;
import DTOs.TargetDTO;
import DTOs.WorkerDTO;
import dependency.graph.DependencyGraph;
import dependency.target.Target;

import execution.CompilationGPUPTask;
import execution.GPUPTask;
import execution.SimulationGPUPTask;

import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl.ThreadStateMap.Byte0.runnable;

public class WorkerExecutor extends Thread implements Runnable{

    private final TextAreaConsumer textAreaConsumer;
   // private WorkerDTO myWorker;
   // private Set<MissionInfoDTO> missionsMyWorkerSubscribe;
  //  private Set<TargetDTO> runnableTargetsForTheWorker;
    private int numOfThreads;
   // private ThreadPoolExecutor threadPoolExecutors = (ThreadPoolExecutor) Executors.newFixedThreadPool(numOfThreads);
    ServerDataManager serverDataManager;
    Boolean isPaused = false;


    public WorkerExecutor(int numOfThreads, ServerDataManager serverDataManager, TextAreaConsumer textAreaConsumer){
        this.serverDataManager = serverDataManager;
        this.numOfThreads = numOfThreads;
        this.textAreaConsumer = textAreaConsumer;
    }

    public void run(){
        System.out.println("in worker executer");
        GPUPTask gpupTask;
        TargetDTO targetDTOToRun = serverDataManager.getTargetToRunFromServer();
        //TODO check if there are no more targets available
        if (targetDTOToRun == null){
            try {
                System.out.println("there is no more targets to run right now");
                Thread.sleep(500);
                return;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (targetDTOToRun.getTaskType().equals(DependencyGraph.TaskType.SIMULATION)){
             gpupTask = new SimulationGPUPTask(targetDTOToRun.getSimulationParameters());
        }
        else
            gpupTask = new CompilationGPUPTask(targetDTOToRun.getCompilationParameters());

        gpupTask.runOnTarget(targetDTOToRun,textAreaConsumer); //TODO update message

        serverDataManager.updateServerWithTaskResult(targetDTOToRun);

    }

    public boolean isPaused() {
    return isPaused;
    }

    public void setPaused() {
        isPaused = true;
    }

    public void setResume(){
        isPaused = false;
    }
}




