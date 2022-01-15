package execution;

import dependency.target.Target;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CompilationGPUPTask extends GPUPTask {
    private String taskName = "Compilation";
    private File toCompilePath;
    private File outPutPath;
    private File needeResourcesPath;
    private Target curTarget;
    private Long workDone =0L;
    private Long totalWork;
    private Integer processingTime;

    public CompilationGPUPTask(File toCompilePath, File outPutPath, Integer processTime) {
        this.toCompilePath = toCompilePath;
        this.outPutPath = outPutPath;
        this.processingTime = processTime;


    }

    @Override
    public void setTotalWork(Long totalWork) {
        this.totalWork = totalWork;
    }

    @Override
    public void setCurTarget(Target target) {
        this.curTarget = target;
    }

    @Override
    public Void call() throws Exception {
        Target toRun;
        synchronized (this){
            toRun = curTarget;
        }

        synchronized (getExecutionManager().getStopWorkSyncer()){

            while(getExecutionManager().isPaused()){
                try {
                    getExecutionManager().getStopWorkSyncer().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return runOnTarget(toRun);
    }


    @Override
    public Void runOnTarget(Target target)
    {
        if(target.getTargetStatus() == Target.TargetStatus.Finished)
            return null;

        String toComplileJavaFile = calculateJavaFileLoaction(target.getData());
        ProcessBuilder processBuilder = new ProcessBuilder("javac","-d",outPutPath.getPath(),"-cp", outPutPath.getPath(),toComplileJavaFile);
        Process process;
        Boolean done = false;

        target.setTargetStatus(Target.TargetStatus.InProcess);
        target.setBeginProcessTime(System.currentTimeMillis());
        Platform.runLater(()->updateMessage("Target: "+ target.getName()+ " is now processing \n File: " + toComplileJavaFile));
        try {
            process = processBuilder.start();
            if (this.processingTime !=0)
            Thread.sleep(Long.valueOf(processingTime)*1000);
            int code = process.waitFor();
            if (code == 0){
                target.setTaskResult(Target.TaskResult.Success);
                Platform.runLater(()->updateMessage("Target: " + target.getName() +" Processed successfully"));
                done = true;
            }
            else{
                target.setTaskResult(Target.TaskResult.Failure);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                Platform.runLater(()->updateMessage("Target: " + target.getName() +" Failed:"));
                Platform.runLater(()-> bufferedReader.lines().forEach(l -> updateMessage(l)));
                done = true;
            }

            target.setTargetStatus(Target.TargetStatus.Finished);
            Platform.runLater(()->updateProgress(workDone++, totalWork));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            if(!done){
                target.setTargetStatus(Target.TargetStatus.Waiting);
                Platform.runLater(()->updateMessage("Target: " + target.getName() +" was interrupted"));
            }
            e.printStackTrace();


        }
        return null;
    }


    @Override
    public String getTaskName() {
        return taskName;
    }

    @Override
    public void finishWork() {
        Platform.runLater(()-> updateProgress(totalWork,totalWork));
    }

    @Override
    public void startWork() {
        Platform.runLater(()->updateProgress(0,totalWork));
    }

    private String calculateJavaFileLoaction(String fqn){
        String[] seperatedFqn = fqn.split("\\.");
        String res = String.join("\\", seperatedFqn);
        return toCompilePath+ "\\" +res + ".java";
    }
}

