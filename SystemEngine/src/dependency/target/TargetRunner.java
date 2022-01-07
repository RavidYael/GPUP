package dependency.target;

import dependency.graph.DependencyGraph;
import execution.Task;

import java.time.Duration;
import java.time.Instant;

public class TargetRunner implements Runnable {
    Target target;
    Task task;
    Target.TaskResult result;
    Target.TargetStatus status;
    Duration duration;
    DependencyGraph dependencyGraph;

    public TargetRunner(Target target, Task task,DependencyGraph dependencyGraph) {
        this.target = target;
        this.task = task;
        this.dependencyGraph = dependencyGraph;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public Target getTarget() {
        return target;
    }

    @Override
    public void run() {
        Instant start = Instant.now();
        Target.TaskResult result = ( task.runOnTarget(target));
        target.setTaskResult(result);
        Instant finish = Instant.now();
        duration = Duration.between(start,finish); // חרא גדול
        Long s = duration.getSeconds();
        dependencyGraph.getTargetByName(target.getName()).setTaskResult(result);
       // dependencyGraph.getTargetByName(target.getName()).setTasks(result);
    }
}
