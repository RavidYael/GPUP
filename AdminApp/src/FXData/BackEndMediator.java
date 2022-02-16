package FXData;

import dependency.graph.DependencyGraph;
import dependency.graph.GraphFactory;
import dependency.target.Target;
import execution.TaskExecution;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/// FirstCOnnectionWork


public class BackEndMediator {
    private DependencyGraph dependencyGraph;
    private File curXML;

    public DependencyGraph getDependencyGraph() {
        return dependencyGraph;
    }

    public void setCurXML(File curXML) {
        this.curXML = curXML;
    }

    public void setDependencyGraph(DependencyGraph dependencyGraph) {
        this.dependencyGraph = dependencyGraph;
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

            targetInTables.add(tempTargetInTable);
        }
        return targetInTables;
    }

    public int getTotalNumOfTargets(){
        return dependencyGraph.getAllTargets().size();

    }

    public void restoreGraphToDefault(){
        try {
            dependencyGraph = GraphFactory.newGraphWithData(curXML.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
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



    public DependencyGraph getSubGraphFromTable(List<TargetInTable> selectedTargets){
        DependencyGraph subGraph = new DependencyGraph();

        for (TargetInTable curTargetInTabel : selectedTargets){
            Target curTarget = dependencyGraph.getTargetByName(curTargetInTabel.getName());

            subGraph.addTargetToGraph(curTargetInTabel.getName(),curTarget);

        }
        subGraph.filterTargetDependendies();
        subGraph.updateAllTargetDependencyLevel();
        subGraph.setName2SerialSet(dependencyGraph.getName2SerialSet()); // not logically true
        return subGraph;
    }
    private boolean isTargetNeededForExecution(Target target){
        return target.getTargetStatus().equals(Target.TargetStatus.Frozen) || target.getTargetStatus().equals(Target.TargetStatus.Waiting) || target.getTargetStatus().equals(Target.TargetStatus.InProcess);
    }



    public ArrayList<String> getTargetLiveData(TargetInTable selectedItem) {
        ArrayList<String> res = new ArrayList<>();

        res.add("Target name: " + selectedItem.getName());
        res.add("Target location: "+selectedItem.getLocation());
        Set<String> serialsetsForTarget = dependencyGraph.getSerialSetsForTarget(selectedItem.getName());
        if (!serialsetsForTarget.isEmpty()){
            res.add("Targets is in following sets: " +String.join(",",serialsetsForTarget));
        }
        if (selectedItem.getTargetStatus().equals(Target.TargetStatus.Frozen)){
            res.add("Target waiting for following targets to finish: "+ dependencyGraph.getTotalDependencies(selectedItem.getName(), Target.Dependency.DependsOn)
                    .stream().filter(t-> isTargetNeededForExecution(t))
            .map(t-> t.getName()).collect(Collectors.joining(",")));
        }
        else if(selectedItem.getTargetStatus().equals(Target.TargetStatus.Skipped)){
            res.add("Target is skipped due to the following failed Targets: " + dependencyGraph.getTotalDependencies(selectedItem.getName(),Target.Dependency.DependsOn)
                    .stream().filter(t-> t.getTaskResult().equals(Target.TaskResult.Failure)).map(t-> t.getName()).collect(Collectors.joining(",")));
        }
        else if (selectedItem.getTargetStatus().equals(Target.TargetStatus.InProcess)){
            Target selectedTarget = dependencyGraph.getTargetByName(selectedItem.getName());
            res.add("Target has been processing for: " + (System.currentTimeMillis() - selectedTarget.getBeginProcessTime())/1000.0 + "seconds");
        }
        else if (selectedItem.getTargetStatus().equals(Target.TargetStatus.Waiting)){
            Target selectedTarget = dependencyGraph.getTargetByName(selectedItem.getName());
            res.add("Target has been waiting for: " + (System.currentTimeMillis() - selectedTarget.getStartWaitingTime())/1000.0 + "seconds" );
        }
        else if (selectedItem.getTargetStatus().equals(Target.TargetStatus.Finished)){
            res.add("Target finished with with result: " + selectedItem.getTaskResult());
        }


return res;
    }
}
