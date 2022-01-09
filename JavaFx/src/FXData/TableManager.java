package FXData;

import dependency.target.Target;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

import javax.swing.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TableManager {

    private ObservableList<TargetInTable> targetsTable;
    private ComboBox<Target.Dependency> dependencyTypeForSelection;
    BackEndMediator backEndMediator;
    CheckBox selectAll;
    CheckBox selectWithDep;




    public TableManager(ObservableList<TargetInTable> targetsTable, CheckBox selectWithDep,CheckBox selectAll, ComboBox dependencyTypeForSelection,BackEndMediator backEndMediator) {
        this.targetsTable = targetsTable;
        this.dependencyTypeForSelection = dependencyTypeForSelection;
        this.backEndMediator = backEndMediator;
        this.selectAll = selectAll;
        this.selectWithDep = selectWithDep;
        //bindCheckBoxes();
    }

    public TableManager(ObservableList<TargetInTable> targetsTable) {
        this.targetsTable = targetsTable;
    }

    public void setTargetsTable(ObservableList<TargetInTable> targetsTable) {
        this.targetsTable = targetsTable;
    }

    public List<TargetInTable> getSelectedTargets(){
        System.out.println( targetsTable.stream().filter(t -> t.getChecked().isSelected()).collect(Collectors.toList()));;
        return targetsTable.stream().filter(t -> t.getChecked().isSelected()).collect(Collectors.toList());


    }

    public void bindCheckBoxes(){

        for (TargetInTable curTargetInTable : targetsTable){
            if (selectAll.isDisable())
                curTargetInTable.getChecked().selectedProperty().unbind();
                curTargetInTable.getChecked().setOnAction(event -> {
            if (selectWithDep.isSelected()){
                selectWithDependency(curTargetInTable,dependencyTypeForSelection.getValue());
            }
            });
        }
    }

    private void selectWithDependency(TargetInTable targetInTable ,Target.Dependency dependency){
       ObservableList<TargetInTable> transitiveTargets =  backEndMediator.getTransitiveTargetData(targetInTable.getName(),dependency);
       for (TargetInTable transitiveTarget :transitiveTargets){
           for (TargetInTable curTargetInTable : targetsTable){
               if (curTargetInTable.getName().equals(transitiveTarget.getName()))
                   curTargetInTable.getChecked().setSelected(true);
           }

       }
    }


}