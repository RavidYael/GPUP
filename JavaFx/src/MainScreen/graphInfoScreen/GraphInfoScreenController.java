package MainScreen.graphInfoScreen;

import FXData.BackEndMediator;
import FXData.TableManager;
import FXData.TargetInTable;
import dependency.target.Target;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Set;

public class GraphInfoScreenController {

    @FXML
    private Label totalTargetsCounter;

    @FXML
    private Label rootsCounter;

    @FXML
    private Label independentCounter;

    @FXML
    private Label MiddlesCounter;

    @FXML
    private Label leafsCounter;

    @FXML
    private TableView<TargetInTable> targetsTable;

   @FXML
   private TableColumn<TargetInTable, CheckBox> checkedCulumn;

    @FXML
    private TableColumn<TargetInTable,String> targetNameColumn;

    @FXML
    private TableColumn<TargetInTable, Target.DependencyLevel> locationColumn;

    @FXML
    private TableColumn<TargetInTable,Integer> totalDependsOnColumn;

    @FXML
    private TableColumn<TargetInTable,Integer> totalRequiredForColumn;

    @FXML
    private TableColumn<TargetInTable, String> extraInfoColumn;

    @FXML
    private TableColumn<TargetInTable, Integer> numOfSerialColumn;

    @FXML
    private TableView<?> serialSetTable;

    @FXML
    private TableColumn<?, ?> serialSetName;

    @FXML
    private TableColumn<?, ?> targetsInSet;

    @FXML
    private Label cycleTargetSelected;

    @FXML
    private Button loacteCycleButton;

    @FXML
    private DialogPane cycleDialog;

    @FXML
    private Label pathTargetSelected1;

    @FXML
    private Label pathTargetSelected2;

    @FXML
    private Button findPathButton;

    @FXML
    private ListView<?> findPathList;

    @FXML
    private ComboBox<String> dependencyComboBox;

    @FXML
    private Label whatIfSelectedTarget;

    @FXML
    private Button displayInfoButton;

    @FXML
    private ComboBox<?> dependencyComboBox1;

    private BackEndMediator backEndMediator;
    private TableManager tableManager;

    public void setBackEndMediator(BackEndMediator backEndMediator) {
        this.backEndMediator = backEndMediator;
    }

    @FXML
    public void initialize(){
        ObservableList depList = FXCollections.observableArrayList("DependsOn", "RequiredFor");
        dependencyComboBox.setItems(depList);
        dependencyComboBox.setPromptText("Dependency Type");
        dependencyComboBox1.setItems(depList);
        dependencyComboBox1.setPromptText("Dependency Type");

    }
    public void myInitialize(){

        targetNameColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,String>("name"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,Target.DependencyLevel>("location"));
        totalDependsOnColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,Integer>("totalDependsOn"));
        totalRequiredForColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,Integer>("totalRequiredFor"));
        extraInfoColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,String>("extraInfo"));
        checkedCulumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,CheckBox>("checked"));

        ObservableList<TargetInTable> targetInTables = backEndMediator.getTargets();
        tableManager = new TableManager(targetInTables);
        targetsTable.setItems(targetInTables);


        System.out.println("tabel created");
        totalTargetsCounter.setText(String.valueOf(backEndMediator.getTotalNumOfTargets()));
        rootsCounter.setText(String.valueOf(backEndMediator.getNumOfTargetsByDependencyLevel(Target.DependencyLevel.Root)));
        independentCounter.setText(String.valueOf(backEndMediator.getNumOfTargetsByDependencyLevel(Target.DependencyLevel.Independed)));
        MiddlesCounter.setText(String.valueOf(backEndMediator.getNumOfTargetsByDependencyLevel(Target.DependencyLevel.Middle)));
        leafsCounter.setText(String.valueOf(backEndMediator.getNumOfTargetsByDependencyLevel(Target.DependencyLevel.Leaf)));

    }

    @FXML
    void dependencyComboBoxAction(ActionEvent event) {

    }

    @FXML
    void displayInfoButtonAction(ActionEvent event) {

    }

    @FXML
    void findPathButtonAction(ActionEvent event) {

    }

    @FXML
    void locateCycleButtonAction(ActionEvent event) {
        Set<TargetInTable> selectedTargets = tableManager.getSelectedTargets();
        if (selectedTargets.size() >1){
            String message = "Please select 1 target only";
            Alert alert = new Alert(Alert.AlertType.ERROR,message);
            alert.showAndWait();
            return;
        }
            backEndMediator.getDependencyGraph().isTargetInCycle(selectedTargets.iterator().next().getName());

    }

}
