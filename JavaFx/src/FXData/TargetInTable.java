package FXData;

import dependency.target.Target;

public class TargetInTable {
    //public static enum DependencyLevel {Root,Middle,Leaf, Independed}

    private String name;
    private Target.DependencyLevel location;
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
