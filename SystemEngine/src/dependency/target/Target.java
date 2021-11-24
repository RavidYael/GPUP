

package dependency.target;

import java.util.HashSet;
import java.util.Set;



public class Target {

  public static enum DependencyLevel {Root,Middle,Leaf, Independed}
  public static enum Dependency {DependsOn , RequiredFor}
    public static enum TargetStatus { Frozen, Skipped, Waiting ,InProcess, Finished, Done}
    public static enum TaskResult {Success, Warning, Failure}


    private String name;
    private Set<String> requiredFor;
    private Set<String> dependsOn;
    private String data;
    private DependencyLevel dependencyLevel;
    private TargetStatus targetStatus = TargetStatus.Frozen;
    private TaskResult taskResult;


    public Target(String name,String data) {

        this.name = name;
        this.data = data;
        requiredFor = new HashSet<>();
        dependsOn = new HashSet<>();
    }

    public Target (Target other){

        this.setName(other.getName());
        this.setData(other.data);
        this.setTaskResult(other.getTaskResult());
        this.requiredFor = new HashSet<>();
        for (String req : other.requiredFor){
            requiredFor.add(req);
        }
        this.dependsOn = new HashSet<>();
        for (String dep : other.dependsOn){
            dependsOn.add(dep);
        }

        this.setTargetStatus(other.targetStatus);
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

    public TargetStatus getTargeStatus() {
        return targetStatus;
    }

    public TaskResult getTaskResult() {
        return taskResult;
    }



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
    public void setTargetStatus(TargetStatus targetStatus) {
        this.targetStatus = targetStatus;
    }

    public void setTaskResult(TaskResult taskResult) {
        this.taskResult = taskResult;
    }

    @Override
    public String toString() {
        return "Target{" +
                "name='" + name + '\'' +
                '}';
    }

}
