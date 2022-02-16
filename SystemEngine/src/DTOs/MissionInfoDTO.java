package DTOs;

import dependency.graph.DependencyGraph;
import dependency.target.Target;

import java.util.Map;
import java.util.Set;

public class MissionInfoDTO {
    public static enum MissionStatus {frozen,running,paused,stopped,finished}

        private  String missionName;
        private  String MissionUploader;
        private  DependencyGraph.TaskType missionType;
        private  String graphName;
        private  Integer targetsCount;
        private  Integer rootsCount;
        private  Integer middlesCount;
        private  Integer leavesCount;
        private  Integer independentsCount;
        private  Set<String> targetsToExecute;
        private Integer taskTotalPayment;
        private Integer currentNumOfxExecutingWorkers;
        private MissionStatus missionStatus;
        private Integer simulationPrice;
        private Integer compilationPrice;

    public MissionInfoDTO(DependencyGraph theGraph, Set<String> TargetsToExecute , String MissionName , String MissionUploader, DependencyGraph.TaskType MissionType, Integer taskTotalPayment, Integer curWorkers, MissionStatus status) {

        this.graphName = theGraph.getGraphName();
        this.MissionUploader = MissionUploader;
        this.missionName = MissionName;
        this.missionType = MissionType;
        this.targetsToExecute = TargetsToExecute;
        this.rootsCount = theGraph.getTargetsCountByLevel(Target.DependencyLevel.Root);
        this.middlesCount = theGraph.getTargetsCountByLevel(Target.DependencyLevel.Middle);
        this.leavesCount = theGraph.getTargetsCountByLevel(Target.DependencyLevel.Leaf);
        this.independentsCount = theGraph.getTargetsCountByLevel(Target.DependencyLevel.Independed);
        this.targetsCount = theGraph.getAllTargets().size();
        this.taskTotalPayment = taskTotalPayment;
        this.currentNumOfxExecutingWorkers = curWorkers;
        this.missionStatus = status;

        Map<DependencyGraph.TaskType, Integer> taskPricing = theGraph.getTaskPricing();

        this.simulationPrice = taskPricing.get(DependencyGraph.TaskType.SIMULATION) != null ? taskPricing.get(DependencyGraph.TaskType.SIMULATION) : 0;
        this.compilationPrice = taskPricing.get(DependencyGraph.TaskType.COMPILATION) != null ? taskPricing.get(DependencyGraph.TaskType.COMPILATION) : 0;

    }

    public String getMissionName() {
        return missionName;
    }

    public String getMissionUploader() {
        return MissionUploader;
    }

    public DependencyGraph.TaskType getMissionType() {
        return missionType;
    }

    public String getGraphName() {
        return graphName;
    }

    public Integer getTargetsCount() {
        return targetsCount;
    }

    public Integer getRootsCount() {
        return rootsCount;
    }

    public Integer getMiddlesCount() {
        return middlesCount;
    }

    public Integer getLeavesCount() {
        return leavesCount;
    }

    public Integer getIndependentsCount() {
        return independentsCount;
    }

    public Integer getTaskTotalPayment() {
        return taskTotalPayment;
    }

    public Integer getCurrentNumOfxExecutingWorkers() {
        return currentNumOfxExecutingWorkers;
    }

    public MissionStatus getMissionStatus() {
        return missionStatus;
    }

    public Integer getSimulationPrice() {
        return simulationPrice;
    }

    public Integer getCompilationPrice() {
        return compilationPrice;
    }

    public Integer getPriceByTaskType(DependencyGraph.TaskType taskType){
        if (taskType.equals(DependencyGraph.TaskType.SIMULATION))
            return simulationPrice;
        else
            return compilationPrice;
    }


    public Set<String> getTargetsToExecute() {
        return targetsToExecute;
    }

}
