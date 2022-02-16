package DTOs;

import dependency.target.Target;

import java.util.Set;

public class TargetDTO {

    private String name;
    private Target.TaskResult result;
    private Target.TargetStatus status;
    private String runLog;
    private String extraData;
    private String taskName;


    public TargetDTO(String name, String extraData, String taskName) {
        this.name = name;
        this.extraData = extraData;
        this.runLog = "";
        this.taskName = taskName;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Target.TaskResult getResult() {
        return result;
    }

    public void setResult(Target.TaskResult result) {
        this.result = result;
    }

    public Target.TargetStatus getStatus() {
        return status;
    }

    public void setStatus(Target.TargetStatus status) {
        this.status = status;
    }

    public String getRunLog() {
        return runLog;
    }

    public void setRunLog(String runLog) {
        this.runLog = runLog;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }





}
