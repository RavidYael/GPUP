package utils.ProccecesDTOs;

import dependency.graph.DependencyGraph;
import dependency.target.Target;
import utils.GraphsDataDTOs.GraphInfoDTO;

import java.util.Map;
import java.util.Set;

public class MissionInfoDTO {

        private final String missionName;
        private final String MissionUploader;
        private final DependencyGraph.TaskType missionType;
        private final String graphName;
        private final Integer targetsCount;
        private final Integer rootsCount;
        private final Integer middlesCount;
        private final Integer leavesCount;
        private final Integer independentsCount;
        private Set<String> targetsToExecute;
        private Integer taskTotalPayment;
        private Integer currentNumOfxExecutingWorkers;
        private String missionStatus;
        private Integer simulationPrice;
        private Integer compilationPrice;

    public MissionInfoDTO(GraphInfoDTO theGraph, Set<String> TargetsToExecute , String MissionName , String MissionUploader, DependencyGraph.TaskType MissionType) {

        this.graphName = theGraph.getGraphName();
        this.MissionUploader = MissionUploader;
        this.missionName = MissionName;
        this.missionType = MissionType;
        this.targetsToExecute = TargetsToExecute;
        this.rootsCount = theGraph.getRootsCount();
        this.middlesCount = theGraph.getMiddlesCount();
        this.leavesCount = theGraph.getLeavesCount();
        this.independentsCount = theGraph.getIndependentsCount();
        this.targetsCount = theGraph.getTargetsCount();

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

    public String getMissionStatus() {
        return missionStatus;
    }

    public Integer getSimulationPrice() {
        return simulationPrice;
    }

    public Integer getCompilationPrice() {
        return compilationPrice;
    }


    public Set<String> getTargetsToExecute() {
        return targetsToExecute;
    }

}
