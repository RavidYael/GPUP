package FXData;

import dependency.target.Target;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

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


    public Set<TargetInTable> getSelectedTargets(){
        return targetsTable.stream().filter(t -> t.getChecked().isSelected()).collect(Collectors.toSet());

    }


}