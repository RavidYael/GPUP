package execution;

import dependency.target.Target;

public class CompilationGPUPTask extends GPUPTask {
    private String taskName = "Compilation";

    @Override
    public Void call() throws Exception { // TOBEFILLED
        return null;
    }

    @Override
    public Void runOnTarget(Target target){
        return null;
    }

    @Override
    public String getTaskName() {
        return taskName;
    }
}
