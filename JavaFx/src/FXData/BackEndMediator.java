package FXData;

import dependency.graph.DependencyGraph;
import dependency.target.Target;
import execution.TaskExecution;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;

import java.util.*;
import java.util.stream.Collectors;

/// FirstCOnnectionWork


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

    public void setLastTaskExecution(TaskExecution taskExecution) {
        this.lastTaskExecution = taskExecution;
    }

    public ObservableList<TargetInTable> getAllTargetsForTable(){
        ObservableList<TargetInTable> targetInTables = FXCollections.observableArrayList();
        for(Target target : dependencyGraph.getAllTargets().values()){
            TargetInTable tempTargetInTable = new TargetInTable();
            tempTargetInTable.setChecked(new CheckBox());
            tempTargetInTable.setName(target.getName());
            tempTargetInTable.setLocation(target.getDependencyLevel());
            tempTargetInTable.setExtraInfo(target.getData());
            tempTargetInTable.setTotalDependsOn(dependencyGraph.getTotalDependencies(target.getName(), Target.Dependency.DependsOn).size());
            tempTargetInTable.setTotalRequiredFor(dependencyGraph.getTotalDependencies(target.getName(), Target.Dependency.RequiredFor).size());

            tempTargetInTable.setTargetStatus(target.getTargetStatus());
            tempTargetInTable.targetStatusProperty().bind(target.targetStatusProperty());

            tempTargetInTable.setTaskResult(target.getTaskResult());
            tempTargetInTable.taskResultProperty().bind(target.taskResultProperty());

            //TODO get number of serial sets per target
            targetInTables.add(tempTargetInTable);
        }
        return targetInTables;
    }

    public int getTotalNumOfTargets(){
        return dependencyGraph.getAllTargets().size();

    }

    public ObservableList<TargetInTable> getTransitiveTargetData(String targetName, Target.Dependency dependency){
        Set<Target> transitiveTargets = dependencyGraph.getTotalDependencies(targetName,dependency);
        ObservableList<TargetInTable>  targetInTables = FXCollections.observableArrayList();
        for (Target target: transitiveTargets){
            TargetInTable tempTargetInTable = new TargetInTable();
            tempTargetInTable.setName(target.getName());
            tempTargetInTable.setLocation(target.getDependencyLevel());
            targetInTables.add(tempTargetInTable);

        }
        return targetInTables;


    }
    public ObservableList<SerialSetInTable> getAllSerialSetsForTable(){
        Map<String,Set<String>> allSets = dependencyGraph.getAllSerialSets();
        ObservableList<SerialSetInTable> serialSetInTables = FXCollections.observableArrayList();
        for (Map.Entry<String,Set<String>> entry :allSets.entrySet()){
            SerialSetInTable tempSetInTabel = new SerialSetInTable();
            tempSetInTabel.setSetName(entry.getKey());
            String targetsInSetStr = entry.getValue().stream().collect(Collectors.joining(","));
            tempSetInTabel.setTargetsInSet(targetsInSetStr);
            serialSetInTables.add(tempSetInTabel);

        }
        return serialSetInTables;

    }

    public int getNumOfTargetsByDependencyLevel(Target.DependencyLevel dependencyLevel){
        return dependencyGraph.getTargetsByLevel(dependencyLevel).size();
    }

    public int getParallelism(){
        return dependencyGraph.getMaxParallelism();
    }



    public DependencyGraph getSubGraphFromTable(List<TargetInTable> selectedTargets,Map<Target,Set<String>> MapOFTargetNDependOnTargets,Map<Target,Set<String>> MapOFTargetNRequiredForTargets){
        DependencyGraph subGraph = new DependencyGraph();

        for (TargetInTable curTargetInTabel : selectedTargets){
            Target curTarget = dependencyGraph.getTargetByName(curTargetInTabel.getName());

            MapOFTargetNDependOnTargets.put(curTarget,new HashSet<>(curTarget.getDependsOn()));
            MapOFTargetNDependOnTargets.put(curTarget,new HashSet<>(curTarget.getRequiredFor()));

            //Target curTargetCopy = curTarget.getDeepCopy(); may not be needed

            subGraph.addTargetToGraph(curTargetInTabel.getName(),curTarget);

        }
        subGraph.filterTargetDependendies();
        subGraph.updateAllTargetDependencyLevel();
        subGraph.setName2SerialSet(dependencyGraph.getName2SerialSet()); // not logically true
        return subGraph;
    }

}
