package execution;

import DTOs.TargetDTO;
import DTOs.TasksPrefernces.SimulationParameters;
import dependency.target.Target;
import javafx.application.Platform;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.function.Consumer;

public class SimulationGPUPTask extends GPUPTask implements Serializable {
    private String taskName = "Simulation";
    private int processTime;
    private boolean isRandomTime;
    private double successProb;
    private double successWithWarningProb;
    private Target curTarget;

    @Override
    public void setTotalWork(Long totalWork) {
        this.totalWork = totalWork;
    }

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

    public SimulationGPUPTask(SimulationParameters simulationParameters){
        this.successProb = simulationParameters.getSuccessRate();
        this.successWithWarningProb = simulationParameters.getSuccessWithWarnings();
        this.isRandomTime = simulationParameters.isRandom();
        if (isRandomTime)
            this.processTime = new Random().nextInt(processTime);
        else
        this.processTime = simulationParameters.getProcessingTime();


    }

//    @Override
//    public Void call() throws Exception {
//
//        Target toRun;
//        synchronized (this){
//            toRun = curTarget;
//        }
//
//        synchronized (getExecutionManager().getStopWorkSyncer()){
//
//            while(getExecutionManager().isPaused()){
//                try {
//                    getExecutionManager().getStopWorkSyncer().wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return runOnTarget(toRun);
//    }
//backup for runOnTarget:
//    @Override
//    public Void runOnTarget(TargetDTO target) {
//
//        if(target.getTargetStatus() == Target.TargetStatus.Finished)
//            return null;
//
//        String status = "";
//        String additionalInfo = "";
//
//        Instant start = Instant.now();
//        curTarget.setBeginProcessTime(System.currentTimeMillis());
//
//        Target.TaskResult taskResult;
//
//
//        target.setTargetStatus(Target.TargetStatus.InProcess);
//        double rand = new Random().nextDouble();
//        Platform.runLater(() -> updateMessage("Target " + target.getName() + " is now processing " +
//                "\nTarget information: " + target.getData()));
//
//        Instant startSleeping = Instant.now();
//        try {
//            Thread.sleep(processTime);
//        } catch (InterruptedException e) {
//            Instant interrupted = Instant.now();
//            long sleptFor = Duration.between(startSleeping, interrupted).toMillis();
//            try {
//                Thread.sleep(processTime - sleptFor);
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            }
//        } finally {
//            if (rand < successProb) {
//                double newRand = new Random().nextDouble();
//                if (newRand < successWithWarningProb) {
//                    taskResult = Target.TaskResult.Warning;
//                    status = "Warning.";
//
//                } else {
//                    taskResult = Target.TaskResult.Success;
//                    status = "Success.";
//
//                }
//
//            } else {
//                taskResult = Target.TaskResult.Failure;
//                status = "Failure.";
//                additionalInfo = "\nThe following targets cannot be procceseed:\n" + target.getRequiredFor().toString();
//
//            }
//
//            String finalStatus = status;
//            target.setTaskResult(taskResult);
//            target.setTargetStatus(Target.TargetStatus.Finished);
//            Platform.runLater(() -> updateProgress(workDone++, totalWork));
//            Instant finish = Instant.now();
//            Duration duration = Duration.between(start, finish);
//            String finalAdditionalInfo = additionalInfo;
//            Platform.runLater(() -> updateMessage("Target " + target.getName() + " completed with status: " + finalStatus + "\n" +
//                    "Process time: " + duration.getSeconds() + " sec" + finalAdditionalInfo));
//        //    target.setExecutionTime(duration.getSeconds()); // if needed can be added to a (new) Duration member in GPUPTask
//            Platform.runLater(() -> updateMessage(""));
//            return null;
//        }
//    }


    @Override
    public Void runOnTarget(TargetDTO target, Consumer consumer) {

        if(target.getTargetStatus() == Target.TargetStatus.Finished)
            return null;

        String status = "";
        String additionalInfo = "";

        Instant start = Instant.now();
        curTarget.setBeginProcessTime(System.currentTimeMillis());

        Target.TaskResult taskResult;


        target.setTargetStatus(Target.TargetStatus.InProcess);
        double rand = new Random().nextDouble();
        consumer.accept("Target " + target.getName() + " is now processing " +
                "\nTarget information: " + target.getExtraData());

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
              //  additionalInfo = "\nThe following targets cannot be procceseed:\n" + target.getRequiredFor().toString();

            }

            String finalStatus = status;
            target.setResult(taskResult);
            target.setTargetStatus(Target.TargetStatus.Finished);
           // Platform.runLater(() -> updateProgress(workDone++, totalWork));
            Instant finish = Instant.now();
            Duration duration = Duration.between(start, finish);
            String finalAdditionalInfo = additionalInfo;
            consumer.accept("Target " + target.getName() + " completed with status: " + finalStatus + "\n" +
                    "Process time: " + duration.getSeconds() + " sec" + finalAdditionalInfo);
            //    target.setExecutionTime(duration.getSeconds()); // if needed can be added to a (new) Duration member in GPUPTask
        //    Platform.runLater(() -> updateMessage(""));

            return null;
        }
    }



    @Override
    public String getTaskName() {
        return taskName;
    }

//    @Override
//    public void finishWork(){
//        Platform.runLater(()-> updateProgress(totalWork, totalWork));
//    }
//
//    @Override
//    public void startWork(){
//        Platform.runLater(()->updateProgress(0,totalWork));
//    }


}
