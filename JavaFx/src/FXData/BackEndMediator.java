package FXData;

import dependency.graph.DependencyGraph;
import dependency.target.Target;
import execution.Task;
import execution.TaskExecution;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;

public class BackEndMediator {
    private DependencyGraph dependencyGraph;
    private TaskExecution lastTaskExecution;

    public DependencyGraph getDependencyGraph() {
        return dependencyGraph;
    }

    public TaskExecution getLastTaskExecution() {
        return lastTaskExecution;
    }

    public void setDependencyGraph(DependencyGraph dependencyGraph) {
        this.dependencyGraph = dependencyGraph;
    }

    public void setLastTaskExecution(Task task) {
        this.lastTaskExecution = new TaskExecution(dependencyGraph,task);
    }

    public ObservableList<TargetInTable> getTargets(){
        ObservableList<TargetInTable> targetInTables = FXCollections.observableArrayList();
        for(Target target : dependencyGraph.getAllTargets().values()){
            TargetInTable tempTargetInTable = new TargetInTable();
           // tempTargetInTable.setChecked(new CheckBox());
            tempTargetInTable.setChecked(new CheckBox());
            tempTargetInTable.setName(target.getName());
            tempTargetInTable.setLocation(target.getDependencyLevel());
            tempTargetInTable.setExtraInfo(target.getData());
            tempTargetInTable.setTotalDependsOn(dependencyGraph.getTotalDependencies(target.getName(), Target.Dependency.DependsOn));
            tempTargetInTable.setTotalRequiredFor(dependencyGraph.getTotalDependencies(target.getName(), Target.Dependency.RequiredFor));
            tempTargetInTable.setTargetStatus(target.getTargeStatus());
            tempTargetInTable.setTaskResult(target.getTaskResult());
            //TODO get number of serial sets per target
            targetInTables.add(tempTargetInTable);
        }
        return targetInTables;
    }

    public int getTotalNumOfTargets(){
        return dependencyGraph.getAllTargets().size();

    }

    public int getNumOfTargetsByDependencyLevel(Target.DependencyLevel dependencyLevel){
        return dependencyGraph.getTargetsByLevel(dependencyLevel).size();
    }

    public int getParallelism(){
        return dependencyGraph.getMaxParallelism();
    }

    public void runTask(int proccessTime, boolean taskTimeRandom, Double chancesOfSuccess, Double chancesOfWarning, boolean isIncremental){

    }

}
