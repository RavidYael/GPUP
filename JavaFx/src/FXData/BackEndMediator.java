package FXData;

import dependency.graph.DependencyGraph;
import dependency.target.Target;
import execution.TaskExecution;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BackEndMediator {
    private DependencyGraph dependencyGraph;
    private TaskExecution taskExecution;

    public DependencyGraph getDependencyGraph() {
        return dependencyGraph;
    }

    public TaskExecution getTaskExecution() {
        return taskExecution;
    }

    public void setDependencyGraph(DependencyGraph dependencyGraph) {
        this.dependencyGraph = dependencyGraph;
    }

    public void setTaskExecution(TaskExecution taskExecution) {
        this.taskExecution = taskExecution;
    }

    public ObservableList<TargetInTable> getTargets(){
        ObservableList<TargetInTable> targetInTables = FXCollections.observableArrayList();
        for(Target target : dependencyGraph.getAllTargets().values()){
            TargetInTable tempTargetInTable = new TargetInTable();
            tempTargetInTable.setName(target.getName());
            tempTargetInTable.setLocation(target.getDependencyLevel());
            tempTargetInTable.setExtraInfo(target.getData());
            tempTargetInTable.setTotalDependsOn(dependencyGraph.getTotalDependencies(target.getName(), Target.Dependency.DependsOn));
            tempTargetInTable.setTotalRequiredFor(dependencyGraph.getTotalDependencies(target.getName(), Target.Dependency.RequiredFor));
            targetInTables.add(tempTargetInTable);
        }
        return targetInTables;
    }

}
