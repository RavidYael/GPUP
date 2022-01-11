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
   private long totalWork;
   private long workDone = 0l;

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
        String additionalInfo = "";

        Instant start = Instant.now();

        Target.TaskResult taskResult;

        if (Thread.interrupted()) {
            return null;
        }

        target.setTargetStatus(Target.TargetStatus.InProcess);
        double rand = new Random().nextDouble();
        Platform.runLater(() -> updateMessage("Target " + target.getName() + " is now processing "));
        Platform.runLater(() -> updateMessage("Target information: " + target.getData()));

        Instant startSleeping = Instant.now();
        try {
            Thread.sleep(processTime);
        } catch (InterruptedException e) {
            Instant interrupted = Instant.now();
            long sleptFor = Duration.between(startSleeping, interrupted).toMillis();
            try {
                Thread.sleep(processTime - sleptFor);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } finally {
            if (rand < successProb) {
                double newRand = new Random().nextDouble();
                if (newRand < successWithWarningProb) {
                    taskResult = Target.TaskResult.Warning;
                    status = "Warning.";

                } else {
                    taskResult = Target.TaskResult.Success;
                    status = "Success.";

                }

            } else {
                taskResult = Target.TaskResult.Failure;
                status = "Failure.";
                additionalInfo = "\nThe following targets cannot be procceseed:\n" + target.getRequiredFor().toString();

            }

            String finalStatus = status;
            target.setTaskResult(taskResult);
            target.setTargetStatus(Target.TargetStatus.Finished);
            Platform.runLater(() -> updateProgress(workDone++, totalWork));
            Instant finish = Instant.now();
            Duration duration = Duration.between(start, finish);
            String finalAdditionalInfo = additionalInfo;
            Platform.runLater(() -> updateMessage("Target " + target.getName() + " completed with status: " + finalStatus + "\n" +
                    "Process time: " + duration.getSeconds() + " sec" + finalAdditionalInfo));
            target.setExecutionTime(duration.getSeconds()); // if needed can be added to a (new) Duration member in GPUPTask
            Platform.runLater(() -> updateMessage(""));
            return null;
        }
    }
    @Override
    public String getTaskName() {
        return taskName;
    }

    @Override
    public void finishWork(){
        Platform.runLater(()-> updateProgress(totalWork, totalWork));
    }


}
