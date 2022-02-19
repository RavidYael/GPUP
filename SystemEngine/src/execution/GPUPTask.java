package execution;

import DTOs.TargetDTO;
import dependency.target.Target;

import java.util.function.Consumer;

public abstract class GPUPTask /*extends Task<Void>*/ {

    private String taskName;
    private Target curTarget;
    private Long totalWork;
    //private TaskExecution executionManager;


    public abstract void setCurTarget(Target target);

    public abstract void setTotalWork(Long totalWork);


//    @Override
  //  public abstract Void call() throws Exception ;


   // public abstract String runOnTarget(TargetDTO target);
    public abstract Void runOnTarget(TargetDTO target, Consumer consumer);
    public abstract String getTaskName();
  //  public abstract void finishWork();

    ;

//    public TaskExecution getExecutionManager() {
//        return executionManager;
//    }
//
//    public void setExecutionManager(TaskExecution Manager) {
//        this.executionManager = Manager;
//    }

   // public abstract void startWork();
}
