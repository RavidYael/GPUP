package DTOs;

import DTOs.TasksPrefernces.CompilationParameters;

import java.util.Set;

public class CompilationTaskDTO {
    private  String taskName;
    private  String taskCreator;
    private  String graphName;
    private  Set<String> targetsToExecute;
    private  Integer pricingForTarget;
    private  CompilationParameters compilationParameters;

    public CompilationTaskDTO(String taskName, String taskCreator, String graphName,
           Set<String> targetsToExecute, Integer pricingForTarget, CompilationParameters compilationParameters) {

        this.taskName = taskName;
        this.taskCreator = taskCreator;
        this.graphName = graphName;
        this.targetsToExecute = targetsToExecute;
        this.pricingForTarget = pricingForTarget;
        this.compilationParameters = compilationParameters;
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

    public CompilationParameters getCompilationParameters() {
        return compilationParameters;
    }
    public Set<String> getTargetsToExecute() {
        return this.targetsToExecute;
    }
    public Integer getPricingForTarget() {
        return this.pricingForTarget;
    }

    public void setTaskCreator(String taskCreator) {
        this.taskCreator = taskCreator;
    }
}
