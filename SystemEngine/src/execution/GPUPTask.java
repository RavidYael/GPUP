package execution;

import dependency.target.Target;
import javafx.concurrent.Task;

public abstract class GPUPTask extends Task<Void> {
    private String taskName;
    private Target curTarget;
    private Long totalWork;

    public abstract void setCurTarget(Target target);

    public void setTotalWork(Long totalWork){
    this.totalWork = totalWork;
    }

    @Override
    public abstract Void call() throws Exception ;


    public abstract Void runOnTarget(Target target);
    public abstract String getTaskName();
    public void finishWork(){};
}
