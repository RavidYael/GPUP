package FXData;

import dependency.graph.DependencyGraph;
import dependency.graph.GraphFactory;
import dependency.target.Target;
import execution.TaskExecution;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


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
            tempTargetInTable.setNumOfSerialSets(dependencyGraph.getNumOfSerialSets(target));
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
        subGraph.setMaxParallelism(dependencyGraph.getMaxParallelism());
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

    public Image createVisualGraph(String imagePath){
        GraphvizFactory factory = new GraphvizFactory(imagePath,"red", "green","blue","yellow");
        factory.openGraph();
        for (Target curTarget : dependencyGraph.getAllTargets().values()){
            factory.addNode(curTarget);
            factory.addConnections(curTarget,"black");
        }

        factory.closeGraph();
        factory.saveImage("testGraph");
        return factory.generateImage();

    }

    public TreeItem<String> createTreeView(Target.Dependency dependency) {
        Map<Target, TreeItem<String>> target2TreeItem = new HashMap<>();
        TreeItem<String> root = new TreeItem<>();
        for (Target curTarget : dependencyGraph.getAllTargets().values()) {
            target2TreeItem.put(curTarget, new TreeItem<>(curTarget.getName() + "(" + curTarget.getDependencyLevel().toString() + ")"));

        }
        if (dependency.equals(Target.Dependency.DependsOn)) {
            for (Map.Entry<Target, TreeItem<String>> entry : target2TreeItem.entrySet()) {
                for (String targetName : entry.getKey().getDependsOn()) {
                    entry.getValue().getChildren().add(target2TreeItem.get(dependencyGraph.getTargetByName(targetName)));
                }
            }

            for (Map.Entry<Target, TreeItem<String>> entry : target2TreeItem.entrySet()) {
                if (entry.getKey().getDependencyLevel().equals(Target.DependencyLevel.Root) || entry.getKey().getDependencyLevel().equals(Target.DependencyLevel.Independed)) {
                    root.getChildren().add(entry.getValue());
                }
            }
        }
        else{
            for (Map.Entry<Target, TreeItem<String>> entry : target2TreeItem.entrySet()) {
                for (String targetName : entry.getKey().getRequiredFor()) {
                    entry.getValue().getChildren().add(target2TreeItem.get(dependencyGraph.getTargetByName(targetName)));
                }
            }
            for (Map.Entry<Target, TreeItem<String>> entry : target2TreeItem.entrySet()) {
                if (entry.getKey().getDependencyLevel().equals(Target.DependencyLevel.Leaf)|| entry.getKey().getDependencyLevel().equals(Target.DependencyLevel.Independed)) {
                    root.getChildren().add(entry.getValue());
                }
            }

        }

        return root;

    }





    private void makeBranch(String s, TreeItem<String> parent) {
        TreeItem<String> newItem = new TreeItem<>();
        newItem.setValue(s);
        parent.getChildren().add(newItem);
    }


}
