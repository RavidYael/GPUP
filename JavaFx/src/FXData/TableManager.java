package FXData;

import javafx.collections.ObservableList;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TableManager {

    private ObservableList<TargetInTable> targetsTable;

    private Set<TargetInTable> checkedTargets;

    public TableManager(ObservableList<TargetInTable> targetsTable) {
        this.targetsTable = targetsTable;
    }

////public ConnectCheckedWithCheckBox(){
////    for (TargetInTable curTarget:targetsTable) {
////curTarget.
////
////
////    }
//}


    public List<TargetInTable> getSelectedTargets(){
        return targetsTable.stream().filter(t -> t.getChecked().isSelected()).collect(Collectors.toList());

    }


}