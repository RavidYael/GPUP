package dependency.target;

import dependency.graph.DependencyGraph;
import execution.GPUPTask;
import execution.TaskExecution;

import java.time.Duration;
import java.time.Instant;

public class TargetRunner implements Runnable {
    Target target;
    GPUPTask GPUPTask;
    Target.TaskResult result;
    Target.TargetStatus status;
    Duration duration;
    DependencyGraph dependencyGraph;
    TaskExecution taskManager;


    public TargetRunner(Target target, GPUPTask GPUPTask, DependencyGraph dependencyGraph, TaskExecution manager) {
        this.target = target;
        this.GPUPTask = GPUPTask;
        this.dependencyGraph = dependencyGraph;
        this.taskManager = manager;
    }

    public void setTask(GPUPTask GPUPTask) {
        this.GPUPTask = GPUPTask;
    }

    public GPUPTask getTask() {
        return GPUPTask;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public Target getTarget() {
        return target;
    }

    @Override
    public void run() {

    synchronized (this) {
        GPUPTask.setCurTarget(target);
    }
            try {
            GPUPTask.call();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
