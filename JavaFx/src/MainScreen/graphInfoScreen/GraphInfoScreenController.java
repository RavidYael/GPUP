package MainScreen.graphInfoScreen;

import FXData.BackEndMediator;
import FXData.TargetInTable;
import dependency.target.Target;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class GraphInfoScreenController {

    @FXML
    private Label totalTargetsCounter;

    @FXML
    private Label LeafsCounter;

    @FXML
    private Label independentCounter;

    @FXML
    private Label MiddlesCounter;

    @FXML
    private Label rootsCounter;

    @FXML
    private TableView<TargetInTable> targetsTable;

//    @FXML
//    private TableColumn<?, ?> checkedCulumn;

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
    private ComboBox<?> dependencyComboBox;

    @FXML
    private Label whatIfSelectedTarget;

    @FXML
    private Button displayInfoButton;

    @FXML
    private ComboBox<?> dependencyComboBox1;

    private BackEndMediator backEndMediator;

    public void setBackEndMediator(BackEndMediator backEndMediator) {
        this.backEndMediator = backEndMediator;
    }

    @FXML
    public void initialize(){


    }
    public void myInitializer(){

        targetNameColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,String>("name"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,Target.DependencyLevel>("location"));
        totalDependsOnColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,Integer>("totalDependsOn"));
        totalRequiredForColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,Integer>("totalRequiredFor"));
        extraInfoColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,String>("extraInfo"));
        targetsTable.setItems(backEndMediator.getTargets());
        //targetsTable.getColumns().add(targetNameColumn);
        System.out.println("tabel created");

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

    }

}
