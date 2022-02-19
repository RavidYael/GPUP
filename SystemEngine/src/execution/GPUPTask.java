package execution;

import dependency.target.Target;
import javafx.concurrent.Task;

public abstract class GPUPTask extends Task<Void> {

    private String taskName;
    private Target curTarget;
    private Long totalWork;
    private TaskExecution executionManager;


    public abstract void setCurTarget(Target target);

    public abstract void setTotalWork(Long totalWork);


    @Override
    public abstract Void call() throws Exception ;


    public abstract Void runOnTarget(Target target);
    public abstract String getTaskName();
    public abstract void finishWork();

    ;

    public TaskExecution getExecutionManager() {
        return executionManager;
    }

    public void setExecutionManager(TaskExecution Manager) {
        this.executionManager = Manager;
    }

    public abstract void startWork();
}
