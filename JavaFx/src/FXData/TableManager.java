package FXData;

import javafx.collections.ObservableList;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TableManager {

    private ObservableList<TargetInTable> targetsTable;
    private Set<TargetInTable> checkedTargets;
    private String srcTarget;
    private String destTarget;
    private String cycleTarget;

    public TableManager(ObservableList<TargetInTable> targetsTable) {
        this.targetsTable = targetsTable;
    }


    public void setTargetsTable(ObservableList<TargetInTable> targetsTable) {
        this.targetsTable = targetsTable;
    }

    public List<TargetInTable> getSelectedTargets(){
        return targetsTable.stream().filter(t -> t.getChecked().isSelected()).collect(Collectors.toList());

    }

    public void setCheckedTargets(Set<TargetInTable> checkedTargets) {
        this.checkedTargets = checkedTargets;
    }

    public void setSrcTarget(String srcTarget) {
        this.srcTarget = srcTarget;
    }

    public void setDestTarget(String destTarget) {
        this.destTarget = destTarget;
    }

    public void setCycleTarget(String cycleTarget) {
        this.cycleTarget = cycleTarget;
    }
}