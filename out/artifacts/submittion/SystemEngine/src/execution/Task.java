package execution;

import dependency.target.Target;

public abstract class Task {
    private String taskName;


    public abstract Target.TaskResult runOnTarget(Target target);
    public abstract String getTaskName();
}
