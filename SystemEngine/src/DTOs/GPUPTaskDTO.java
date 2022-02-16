package DTOs;

import DTOs.TasksPrefernces.CompilationParameters;
import DTOs.TasksPrefernces.SimulationParameters;

import java.util.Set;

public abstract class GPUPTaskDTO {

    private  String taskName;
    private  String taskCreator;
    private  String graphName;
    private  Set<String> targetsToExecute;
    private  Integer pricingForTarget;
    private  SimulationParameters simulationParameters;
    private  CompilationParameters compilationParameters;



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

    public SimulationParameters getSimulationParameters() { return this.simulationParameters;}

    public CompilationParameters getCompilationParameters() {
        return compilationParameters;
    }



}
