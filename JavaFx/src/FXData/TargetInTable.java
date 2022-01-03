package FXData;

import dependency.target.Target;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.CheckBox;

public class TargetInTable {
    public static enum DependencyLevel {Root,Middle,Leaf, Independed}
    private CheckBox checked;
    private StringProperty dummyCheck = new SimpleStringProperty(this,"isChecked","notChecked");
    private String name;
    private Target.DependencyLevel location;
    private Target.TargetStatus targetStatus;
    private Target.TaskResult taskResult;
    private int totalDependsOn;
    private int totalRequiredFor;
    private String extraInfo;
    private int numOfSerialSets;

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(Target.DependencyLevel location) {
        this.location = location;
    }

    public void setTotalDependsOn(int totalDependsOn) {
        this.totalDependsOn = totalDependsOn;
    }

    public void setChecked(CheckBox checked) {
        this.checked = checked;
        dummyCheck.setValue("checked");
    }

    public CheckBox getChecked() {
        return checked;
    }

    public void setTotalRequiredFor(int totalRequiredFor) {
        this.totalRequiredFor = totalRequiredFor;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public void setNumOfSerialSets(int numOfSerialSets) {
        this.numOfSerialSets = numOfSerialSets;
    }

    public String getName() {
        return name;
    }

    public Target.TargetStatus getTargetStatus() {
        return targetStatus;
    }

    public Target.TaskResult getTaskResult() {
        return taskResult;
    }

    public void setTargetStatus(Target.TargetStatus targetStatus) {
        this.targetStatus = targetStatus;
    }

    public void setTaskResult(Target.TaskResult taskResult) {
        this.taskResult = taskResult;
    }

    public Target.DependencyLevel getLocation() {
        return location;
    }

    public int getTotalDependsOn() {
        return totalDependsOn;
    }

    public int getTotalRequiredFor() {
        return totalRequiredFor;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public int getNumOfSerialSets() {
        return numOfSerialSets;
    }
}
