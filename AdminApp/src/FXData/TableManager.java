package FXData;

import dependency.target.Target;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

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
        selectAllcheckBoxAction();
        selectWithDepCheckBoxAction();
    }

    private void selectWithDepCheckBoxAction() {
        selectWithDep.setOnAction(event -> {
            if (selectWithDep.isSelected()){
                for (TargetInTable curTargetInTable : targetsTable){
                    curTargetInTable.getChecked().setOnAction(e -> {
                        selectWithDependency(curTargetInTable,dependencyTypeForSelection.getValue());
                    });
                }
            }
        });
    }

    public ObservableList<TargetInTable> getTargetsTable() {
        return targetsTable;
    }

    private void selectAllcheckBoxAction() {
        selectAll.setOnAction(e->{
            if (selectAll.isSelected()){
                for (TargetInTable curTargetInTable : targetsTable){
                    curTargetInTable.getChecked().setSelected(true);
                }
            }
            else {
                for (TargetInTable curTargetInTable : targetsTable){
                    curTargetInTable.getChecked().setSelected(false);
                }

            }
        });
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

    public void checkBoxesAction(){

        for (TargetInTable curTargetInTable : targetsTable){
            curTargetInTable.getChecked().selectedProperty().bind(selectAll.selectedProperty());

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


    public void selectByDependecyLevel(Target.DependencyLevel location) {
        targetsTable.stream().filter(t -> t.getLocation().equals(location)).forEach(t-> t.getChecked().setSelected(true));

    }

    public void deselectAll() {
        targetsTable.stream().forEach(t-> t.getChecked().setSelected(false));
    }

//    public void bindColumns(TableColumn<TargetInTable, ObjectProperty<Target.TaskResult>> proccessingResultColumn, TableColumn<TargetInTable, ObjectProperty<Target.TargetStatus>> executionStatusColumn) {
//        for (TargetInTable curTargetInTable : targetsTable){
//            curTargetInTable.
//
//        }
//
//    }

    public void bindTable2Lables(Label src, Label dest){
        targetsTable.stream().forEach(t-> t.getChecked().selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (getSelectedTargets().size() == 1)
                src.setText(t.getName());
            else if(getSelectedTargets().size()>1)
                dest.setText(t.getName());
        }));
    }


}