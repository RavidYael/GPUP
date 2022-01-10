package execution;

import dependency.target.Target;
import javafx.application.Platform;

import java.io.Serializable;
import java.lang.management.PlatformLoggingMXBean;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;

public class SimulationGPUPTask extends GPUPTask implements Serializable {
    private String taskName = "Simulation";
    private int processTime;
    private boolean isRandomTime;
    private double successProb;
    private double successWithWarningProb;
    private Target curTarget;

    public void setCurTarget(Target target){
        curTarget = target;
    }

    public SimulationGPUPTask(int processTime, boolean isRandomTime, Double successProb, Double successWithWarningProb) {

        this.isRandomTime = isRandomTime;
        if (isRandomTime)
        {
            this.processTime = new Random().nextInt(processTime);
        }
        else
            this.processTime = processTime;
        this.successProb = successProb;
        this.successWithWarningProb = successWithWarningProb;
    }

    @Override
    public Void call() throws Exception {
        Target toRun;
        synchronized (this){
            toRun = curTarget;
        }
        return runOnTarget(toRun);
    }

    @Override
    public Void runOnTarget(Target target) {
        String status = "";


        Instant start = Instant.now();

        Target.TaskResult taskResult;
        Platform.runLater(()->target.setTargetStatus(Target.TargetStatus.InProcess));
        double rand = new Random().nextDouble();
         Platform.runLater(()->updateMessage("Target " + target.getName() + " is now processing "));
         Platform.runLater(()->updateMessage("Target information: " + target.getData()));

        try {
            Thread.sleep(processTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (rand < successProb)
        {
            double newRand = new Random().nextDouble();
            if (newRand < successWithWarningProb) {
                taskResult = Target.TaskResult.Warning;
                status = "Warning.";

            }
            else {
                taskResult = Target.TaskResult.Success;
                status = "Success.";

            }

        }
        else {
            taskResult = Target.TaskResult.Failure;
            status = "Failure.";

        }

        String finalStatus = status;
        Platform.runLater(()->updateMessage("Target " +target.getName()+ " completed with status: "+ finalStatus));
        Platform.runLater(()->updateMessage(""));
        Platform.runLater(()->target.setTaskResult(taskResult));
        Platform.runLater(()->target.setTargetStatus(Target.TargetStatus.Finished));


        Instant finish = Instant.now();
        Duration duration = Duration.between(start,finish); // חרא גדול
        target.setExecutionTime(duration.getSeconds()); // if needed can be added to a (new) Duration member in GPUPTask
        return null;
    }

    @Override
    public String getTaskName() {
        return taskName;
    }

}
