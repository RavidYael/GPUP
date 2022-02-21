package FXData;

public class SubmittedTaskInTable {
    String name;
    Integer numOfWorkers;
    Double progress;
    Integer numOfTargets;
    Integer credits;

    public String getName() {
        return name;
    }

    public Integer getNumOfWorkers() {
        return numOfWorkers;
    }

    public Double getProgress() {
        return progress;
    }

    public Integer getNumOfTargets() {
        return numOfTargets;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumOfWorkers(Integer numOfWorkers) {
        this.numOfWorkers = numOfWorkers;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public void setNumOfTargets(Integer numOfTargets) {
        this.numOfTargets = numOfTargets;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }
}
