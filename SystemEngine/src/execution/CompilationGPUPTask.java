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
    private Long totalWork;

    public CompilationGPUPTask(File toCompilePath, File outPutPath) {
        this.toCompilePath = toCompilePath;
        this.outPutPath = outPutPath;

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
        return runOnTarget(toRun);
    }


    @Override
    public Void runOnTarget(Target target)
    {
        String toComplileJavaFile = calculateJavaFileLoaction(target.getData());
        ProcessBuilder processBuilder = new ProcessBuilder("javac","-d",outPutPath.getPath(),"-cp", outPutPath.getPath(),toComplileJavaFile);
        Process process;
        target.setTargetStatus(Target.TargetStatus.InProcess);

        try {
            process = processBuilder.start();
            int code = process.waitFor();
            if (code == 0){
                target.setTaskResult(Target.TaskResult.Success);
                //prints out sucess message
                Platform.runLater(()->updateMessage("Target: " + target.getName() +" Processed successfully"));
            }
            else{
                target.setTaskResult(Target.TaskResult.Failure);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                Platform.runLater(()->updateMessage("Target: " + target.getName() +" Failed:"));
                Platform.runLater(()-> bufferedReader.lines().forEach(l -> updateMessage(l)));
            }
            target.setTargetStatus(Target.TargetStatus.Finished);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String getTaskName() {
        return taskName;
    }

    private String calculateJavaFileLoaction(String fqn){
        String[] seperatedFqn = fqn.split("\\.");
        String res = String.join("\\", seperatedFqn);
        return toCompilePath+ "\\" +res + ".java";
    }
}

