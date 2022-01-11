package execution;

import dependency.target.Target;
import javafx.concurrent.Task;

public abstract class GPUPTask extends Task<Void> {
    private String taskName;
    private Target curTarget;

    public abstract void setCurTarget(Target target);


    @Override
    public abstract Void call() throws Exception ;


    public abstract Void runOnTarget(Target target);
    public abstract String getTaskName();
    public void finishWork(){};
}
