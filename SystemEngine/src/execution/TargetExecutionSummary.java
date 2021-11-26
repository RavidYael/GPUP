package execution;

import dependency.target.Target;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class TargetExecutionSummary {
    private String targetName;
    private Target.TaskResult taskResult;
    private Long duration;


    public TargetExecutionSummary(String targetName, Target.TaskResult taskResult, Long duration) {
        this.targetName = targetName;
        this.taskResult = taskResult;
        this.duration = duration;
    }

    public String getTargetName() {
        return targetName;
    }

    public Target.TaskResult getTaskResult() {
        return taskResult;
    }

    public Long getDuration() {
        return duration;
    }



    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public void setTaskResult(Target.TaskResult taskResult) {
        this.taskResult = taskResult;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        String res =
                "\nTargetName: " + targetName +
                "\nTask ended with: " + taskResult;
        if (duration != null){
            String durationStr = "\nTask ran for: " + duration.toString() + " ms";
            res+=durationStr;
        }
        return res;
    }
}

