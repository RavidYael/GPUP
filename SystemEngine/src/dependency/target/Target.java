

package dependency.target;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;



public class Target implements Serializable {


    public static enum DependencyLevel {Root,Middle,Leaf, Independed}
    public static enum Dependency {DependsOn , RequiredFor}
    public static enum TargetStatus { Frozen, Skipped, Waiting ,InProcess, Finished, Done}
    public static enum TaskResult {Success, Warning, Failure,Skipped}

    private String name;
    private Set<String> requiredFor;
    private Set<String> dependsOn;
    private String data;
    private DependencyLevel dependencyLevel;
    private Long beginProcessTime;
    private Long startWaitingTime;

    // private TargetStatus targetStatus = TargetStatus.Frozen;
    // private TaskResult taskResult = TaskResult.Skipped;

    private ObjectProperty<TaskResult> taskResult =  new SimpleObjectProperty(TaskResult.Skipped); // what is the initial value???
    private ObjectProperty<TargetStatus> targetStatus = new SimpleObjectProperty<>(TargetStatus.Frozen); // what is the initial value??

//    public Long getExecutionTime(){
//    return executionTime;
//    }


    public void setBeginProcessTime(Long beginProcessTime) {
        this.beginProcessTime = beginProcessTime;
    }

    public void setStartWaitingTime(Long startWaitingTime) {
        this.startWaitingTime = startWaitingTime;
    }

    public Long getBeginProcessTime() {
        return beginProcessTime;
    }

    public Long getStartWaitingTime() {
        return startWaitingTime;
    }

//    public void setExecutionTime(Long Time){
//        executionTime = Time;
//    }

    public Target(String name,String data) {

        this.name = name;
        this.data = data;
        requiredFor = new HashSet<>();
        dependsOn = new HashSet<>();

        targetStatusProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(TargetStatus.Waiting)) {
                this.startWaitingTime = System.currentTimeMillis();
            }
        });


    }

    public Target (Target other){

        this.setName(other.getName());
        this.setData(other.data);
        this.setTaskResult(other.getTaskResult());
        this.targetStatus = other.targetStatus;
        this.requiredFor = new HashSet<>();
        for (String req : other.requiredFor){
            requiredFor.add(req);
        }
        this.dependsOn = new HashSet<>();
        for (String dep : other.dependsOn){
            dependsOn.add(dep);
        }




        this.setDependencyLevel(other.dependencyLevel);

    }



    public String getName() {
        return name;
    }
    public DependencyLevel getDependencyLevel() {
        return dependencyLevel;
    }
    public Set<String> getRequiredFor() {
        return requiredFor;
    }
    public Set<String> getDependsOn() {return dependsOn;}
    public String getData() {return data;}
    public Set<String> getDependsOnOrNeededFor(Dependency depEnum) {
        if (depEnum == Dependency.DependsOn)
            return getDependsOn();

        else
            return getRequiredFor();
    }
    public void addToRequiredFor(String name) {requiredFor.add(name);}
    public void addToDependsOn(String name) {dependsOn.add(name);}
    public void setDependencyLevel(DependencyLevel dependencyLevel) {this.dependencyLevel = dependencyLevel;}

    public TargetStatus getTargetStatus() {
        return targetStatus.get();
    }


    public ObjectProperty<TargetStatus> targetStatusProperty() {
        return targetStatus;
    }


    public TaskResult getTaskResult() {
        return taskResult.get();
    }


    public ObjectProperty<TaskResult> taskResultProperty() {
        return taskResult;
    }


//    public TaskResult getTaskResult() {
//        return taskResult;
//    }
//
//    public TargetStatus getTargetStatus() {
//        return targetStatus;
//    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRequiredFor(Set<String> requiredFor) {
        this.requiredFor = requiredFor;
    }

    public void setDependsOn(Set<String> dependsOn) {
        this.dependsOn = dependsOn;
    }

    public void setData(String data) {
        this.data = data;
    }
//
//    public void setTargetStatus(TargetStatus targetStatus) {
//        this.targetStatus = targetStatus;
//    }


    public void setTargetStatus(TargetStatus targetStatus) {
        this.targetStatus.set(targetStatus);
    }

    public void setTaskResult(TaskResult taskResult) {
        this.taskResult.set(taskResult);
    }



//    public void setTaskResult(TaskResult taskResult) {
//        this.taskResult = taskResult;
//    }

    public void addTargetToDependsOrNeededFor(String targetName, Dependency dependency){
        if (dependency == Dependency.DependsOn)
            dependsOn.add(targetName);
        else
            requiredFor.add(targetName);

    }

    public Target getDeepCopy(){
        Target copyTarget = new Target(name,data);
        for (String curTargetName : dependsOn){
            copyTarget.addTargetToDependsOrNeededFor(curTargetName,Dependency.DependsOn);
        }
        for (String curTargetName : requiredFor){
            copyTarget.addTargetToDependsOrNeededFor(curTargetName,Dependency.RequiredFor);
        }
        return copyTarget;
    }

    public void removeTargetFromDependsOrNeeded(String targetName, Dependency dependency){
        if (dependency == Dependency.DependsOn){
            dependsOn.remove(targetName);
        }
        else{
            requiredFor.remove(targetName);
        }
    }





    @Override
    public String toString() {
        return "Target{" +
                "name='" + name + '\'' +
                '}';
    }

}
