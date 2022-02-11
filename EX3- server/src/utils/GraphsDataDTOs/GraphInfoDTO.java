package utils.GraphsDataDTOs;

import dependency.graph.DependencyGraph;
import dependency.target.Target;

import java.util.Map;

public class GraphInfoDTO {

        private final String graphName;
        private final String uploader;
        private final Integer targetsCount;
        private final Integer rootsCount;
        private final Integer middlesCount;
        private final Integer leavesCount;
        private final Integer independentsCount;
        private Integer simulationPrice;
        private Integer compilationPrice;
        Map<DependencyGraph.TaskType, Integer> taskPricing;


        public GraphInfoDTO (DependencyGraph theGraph) {

            this.graphName = theGraph.getGraphName();
            this.uploader = theGraph.getUploaderName();

            this.rootsCount = theGraph.getTargetsCountByLevel(Target.DependencyLevel.Root);
            this.middlesCount = theGraph.getTargetsCountByLevel(Target.DependencyLevel.Middle);
            this.leavesCount = theGraph.getTargetsCountByLevel(Target.DependencyLevel.Root);
            this.independentsCount = theGraph.getTargetsCountByLevel(Target.DependencyLevel.Middle);
            this.targetsCount = theGraph.getAllTargets().size();

            taskPricing = theGraph.getTaskPricing();

            this.simulationPrice = taskPricing.get(DependencyGraph.TaskType.SIMULATION) != null ? taskPricing.get(DependencyGraph.TaskType.SIMULATION) : 0;
            this.compilationPrice = taskPricing.get(DependencyGraph.TaskType.COMPILATION) != null ? taskPricing.get(DependencyGraph.TaskType.COMPILATION) : 0;

        }

        public String getGraphName() {
            return this.graphName;
        }

        public String getUploader() {
            return this.uploader;
        }

        public Integer getRootsCount() {
            return this.rootsCount;
        }

        public Integer getMiddlesCount() {
            return this.middlesCount;
        }

        public Integer getLeavesCount() {
            return this.leavesCount;
        }

        public Integer getIndependentsCount() {
            return this.independentsCount;
        }

        public Integer getSimulationPrice() {
            return this.simulationPrice;
        }

        public void setSimulationPrice(Integer simulationPrice) {
            this.simulationPrice = simulationPrice;
        }

        public Integer getCompilationPrice() {
            return this.compilationPrice;
        }

        public void setCompilationPrice(Integer compilationPrice) {
            this.compilationPrice = compilationPrice;
        }

        public Integer getTargetsCount() {
            return targetsCount;
        }

        public Map<DependencyGraph.TaskType, Integer> getTaskPricing() {return taskPricing;}

}

