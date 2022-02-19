package DTOs;

import DTOs.TasksPrefernces.TaskPreferences;
import dependency.target.Target;

public class TargetDTO {

    private String name;
    private Target.TaskResult result;
    private Target.TargetStatus status;
    private String runLog;
    private String extraData;
    private String taskType;
    private String missionName;
    private TaskPreferences runningPreferences;

    public String getMissionName() {
        return missionName;
    }

    public TargetDTO(String name, String extraData, String taskName, String missionName,TaskPreferences taskPreferences) {
        this.name = name;
        this.extraData = extraData;
        this.runLog = "";
        this.taskType = taskName;
        this.missionName = missionName;
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

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }





}
