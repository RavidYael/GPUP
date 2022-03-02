package MainScreen.tasksIfonScreen;

public class TaskInfoInTable {
    private String name;
    private String graphName;
    private Integer totalTargets;
    private Integer numOfWorkers;
    private Double progress;


    public void setName(String name) {
        this.name = name;
    }

    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }

    public void setTotalTargets(Integer totalTargets) {
        this.totalTargets = totalTargets;
    }

    public void setNumOfWorkers(Integer numOfWorkers) {
        this.numOfWorkers = numOfWorkers;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public String getName() {
        return name;
    }

    public String getGraphName() {
        return graphName;
    }

    public Integer getTotalTargets() {
        return totalTargets;
    }

    public Integer getNumOfWorkers() {
        return numOfWorkers;
    }

    public Double getProgress() {
        return progress;
    }
}
