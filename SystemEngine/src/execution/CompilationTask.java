package execution;

import dependency.target.Target;

public class CompilationTask extends Task {
    private String taskName = "Compilation";


    @Override
    public Target.TaskResult runOnTarget(Target target)
    {
        return null;
    }

    @Override
    public String getTaskName() {
        return taskName;
    }
}
