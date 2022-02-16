package DTOs;

import DTOs.TasksPrefernces.SimulationParameters;

import java.util.HashSet;
import java.util.Set;

public class SimulationTaskDTO  {

    private  String taskName;
    private  String taskCreator;
    private  String graphName;
    private  Set<String> targetsToExecute;
    private  Integer pricingForTarget;
    private  SimulationParameters simulationParameters;

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

    public String getTaskName() {return this.taskName;}

    //@Override
    public String getTaskCreator() {
        return this.taskCreator;
    }
    //@Override
    public String getGraphName() {
        return this.graphName;
    }
    //@Override
    public Set<String> getTargetsToExecute() {
        return this.targetsToExecute;
    }
    //@Override
    public Integer getPricingForTarget() {
        return this.pricingForTarget;
    }
    //@Override
    public SimulationParameters getSimulationParameters() {
        return this.simulationParameters;
    }

    public void setTaskCreator(String taskCreator) {
        this.taskCreator = taskCreator;
    }
}
