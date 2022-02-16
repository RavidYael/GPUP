package mainScreen.workerDashboardScreen;

import DTOs.MissionInfoDTO;
import dependency.graph.DependencyGraph;

public class TaskInTable {
    String name;
    String uploadedBy;
    DependencyGraph.TaskType taskType;
    Integer totalTargets;
    Integer roots;
    Integer middles;
    Integer leafs;
    Integer independent;
    Integer priceForTarget;
    MissionInfoDTO.MissionStatus missionStatus;
    Integer numOfWorkers;
    WorkerDashboardScreenController.YesOrNo amIListed;

    public TaskInTable(String name, String uploadedBy, DependencyGraph.TaskType taskType, Integer totalTargets, Integer roots, Integer middles, Integer leafs, Integer independent, Integer priceForTarget, MissionInfoDTO.MissionStatus missionStatus, Integer numOfWorkers, WorkerDashboardScreenController.YesOrNo amIListed) {
        this.name = name;
        this.uploadedBy = uploadedBy;
        this.taskType = taskType;
        this.totalTargets = totalTargets;
        this.roots = roots;
        this.middles = middles;
        this.leafs = leafs;
        this.independent = independent;
        this.priceForTarget = priceForTarget;
        this.missionStatus = missionStatus;
        this.numOfWorkers = numOfWorkers;
        this.amIListed = amIListed;
    }

    public String getName() {
        return name;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public DependencyGraph.TaskType getTaskType() {
        return taskType;
    }

    public Integer getTotalTargets() {
        return totalTargets;
    }

    public Integer getRoots() {
        return roots;
    }

    public Integer getMiddles() {
        return middles;
    }

    public Integer getLeafs() {
        return leafs;
    }

    public Integer getIndependent() {
        return independent;
    }

    public Integer getPriceForTarget() {
        return priceForTarget;
    }

    public MissionInfoDTO.MissionStatus getMissionStatus() {
        return missionStatus;
    }

    public Integer getNumOfWorkers() {
        return numOfWorkers;
    }

    public WorkerDashboardScreenController.YesOrNo isAmIListed() {
        return amIListed;
    }
}


