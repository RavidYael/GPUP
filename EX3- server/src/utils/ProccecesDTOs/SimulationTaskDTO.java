package utils.ProccecesDTOs;

import utils.TasksPrefernces.SimulationParameters;

import java.util.HashSet;
import java.util.Set;

public class SimulationTaskDTO {

    private final String taskName;
    private final String taskCreator;
    private final String graphName;
    private final Set<String> targetsToExecute;
    private final Integer pricingForTarget;
    private final SimulationParameters simulationParameters;

    public SimulationTaskDTO(String taskName, String taskCreator, String graphName,
           Set<String> targetsToExecute, Integer pricingForTarget, SimulationParameters simulationParameters) {

        this.taskName = taskName;
        this.taskCreator = taskCreator;
        this.graphName = graphName;
        this.targetsToExecute = new HashSet<>();
        this.targetsToExecute.addAll(targetsToExecute);
        this.pricingForTarget = pricingForTarget;
        this.simulationParameters = simulationParameters;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public String getTaskCreator() {
        return this.taskCreator;
    }

    public String getGraphName() {
        return this.graphName;
    }

    public Set<String> getTargetsToExecute() {
        return this.targetsToExecute;
    }

    public Integer getPricingForTarget() {
        return this.pricingForTarget;
    }

    public SimulationParameters getSimulationParameters() {
        return this.simulationParameters;
    }
}
