package MainScreen.graphInfoScreen;

import FXData.*;
import dependency.target.Target;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import sun.nio.ch.ThreadPool;

import java.io.File;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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
    private TableView<SerialSetInTable> serialSetTable;

    @FXML
    private TableColumn<SerialSetInTable, String> serialSetName;

    @FXML
    private TableColumn<SerialSetInTable, String> targetsInSet;

    @FXML
    private Label cycleTargetSelected;

    @FXML
    private Button loacteCycleButton;

    @FXML
    private TextArea loacteCycleTA;

    @FXML
    private Label pathTargetSelected1;

    @FXML
    private Label pathTargetSelected2;

    @FXML
    private Button findPathButton;

    @FXML
    private TextArea findPathTA;

    @FXML
    private ComboBox<String> dependencyComboBox;

    @FXML
    private Label whatIfSelectedTarget;

    @FXML
    private TableView<TargetInTable> whatIfTableView;

    @FXML
    private TableColumn<TargetInTable, String> whatIfTargetNameColumn;

    @FXML
    private TableColumn<TargetInTable, Target.DependencyLevel> whatIfLocationColumn;

    @FXML
    private Button displayInfoButton;

    @FXML
    private ComboBox<String> dependencyComboBox1;

    @FXML
    private Button chooseImageDirectory;
    @FXML
    private ImageView visualGraphImageView;

    @FXML
    private Button reverseButton;

    @FXML
    private TreeView<String> treeView;
    @FXML
    private ComboBox<Target.Dependency> treeDirectionComboBox;

    private String srcTarget;
    private String destTarget;
    private boolean isReveresed = false;

    private BackEndMediator backEndMediator;
    private TableManager mainTableManager;

    public void setBackEndMediator(BackEndMediator backEndMediator) {
        this.backEndMediator = backEndMediator;
    }

    @FXML
    public void initialize(){
        ObservableList depList = FXCollections.observableArrayList("Depends On", "Required For");
        dependencyComboBox.setItems(depList);
        dependencyComboBox.setPromptText("Dependency Type");
        dependencyComboBox1.setItems(depList);
        dependencyComboBox1.setPromptText("Dependency Type");
        treeDirectionComboBox.setItems(FXCollections.observableArrayList(Target.Dependency.DependsOn, Target.Dependency.RequiredFor));


    }
    public void myInitialize(){

        targetNameColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,String>("name"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,Target.DependencyLevel>("location"));
        totalDependsOnColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,Integer>("totalDependsOn"));
        totalRequiredForColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,Integer>("totalRequiredFor"));
        extraInfoColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,String>("extraInfo"));
        checkedCulumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,CheckBox>("checked"));
        numOfSerialColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,Integer>("numOfSerialSets"));
        ObservableList<TargetInTable> targetInTables = backEndMediator.getAllTargetsForTable();
        mainTableManager = new TableManager(targetInTables);
        mainTableManager.bindTable2Lables(pathTargetSelected1,pathTargetSelected2, cycleTargetSelected, whatIfSelectedTarget);
        targetsTable.setItems(targetInTables);


        serialSetTable.setItems(backEndMediator.getAllSerialSetsForTable());
        serialSetName.setCellValueFactory(new PropertyValueFactory<SerialSetInTable,String>("setName"));
        targetsInSet.setCellValueFactory(new PropertyValueFactory<SerialSetInTable,String>("targetsInSet"));



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
        String message = "";
        Alert alert = new Alert(Alert.AlertType.ERROR);
        boolean userError = false;
        List<TargetInTable> selectedTargets = mainTableManager.getSelectedTargets();
        if (selectedTargets.size() != 1){
            message = "Please select 1 targets";
            userError = true;
        }
        else if(dependencyComboBox1.getValue() == null){
            message = "Please select Dependency Type";
            userError = true;
        }
        if (userError) {
            alert.setContentText(message);
            alert.showAndWait();
            return;
        }

        else{
            String targetName = selectedTargets.get(0).getName();
            Target.Dependency dependency = stringToDependency(dependencyComboBox1.getValue());
            ObservableList<TargetInTable> tableTargets = backEndMediator.getTransitiveTargetData(targetName,dependency);
            whatIfTableView.setItems(tableTargets);
            whatIfTargetNameColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,String>("name"));
            whatIfLocationColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable, Target.DependencyLevel>("location"));
        }

    }

    @FXML
    void findPathButtonAction(ActionEvent event) {
        String message = "";
        Alert alert = new Alert(Alert.AlertType.ERROR);
        boolean userError = false;
        List<TargetInTable> selectedTargets = mainTableManager.getSelectedTargets();
        if (selectedTargets.size() != 2){
            message = "Please select 2 targets";
            userError = true;
        }
        else if(dependencyComboBox.getValue() == null){
            message = "Please select Dependency Type";
            userError = true;
        }
        if (userError) {
            alert.setContentText(message);
            alert.showAndWait();
            return;
        }


        String dep = dependencyComboBox.getValue();
        if (!isReveresed) {
            srcTarget = selectedTargets.get(0).getName();
            destTarget = selectedTargets.get(1).getName();
        }
        else{
            srcTarget = selectedTargets.get(1).getName();
            destTarget = selectedTargets.get(0).getName();

        }
        isReveresed = false;
        String initMessage = "Path from " +srcTarget +" To " +destTarget+ " in direction:' "+ dep+ "' :";
        TextAreaConsumer findPathTAConsumer = new TextAreaConsumer(findPathTA,initMessage);
//        findPathTAConsumer.clear();
        Target.Dependency dependency = stringToDependency(dep);
        boolean isPath = backEndMediator.getDependencyGraph().displayAllPathsBetweenTwoTargets(srcTarget,destTarget,dependency,findPathTAConsumer);
        if (!isPath){
            findPathTA.setText("No Existing Path from " + srcTarget+ " To "+ destTarget + " in Direction: " + dep);
        }
    }
    @FXML
    void reverseButtonAction(ActionEvent event) {
        isReveresed = true;
        String temp = pathTargetSelected1.getText();
        pathTargetSelected1.setText(pathTargetSelected2.getText());
        pathTargetSelected2.setText(temp);

    }

    @FXML
    void locateCycleButtonAction(ActionEvent event) {
        List<TargetInTable> selectedTargets = mainTableManager.getSelectedTargets();
        if (selectedTargets.size() !=1){
            String message = "Please select 1 target";
            Alert alert = new Alert(Alert.AlertType.ERROR,message);
            alert.showAndWait();
            return;
        }

        Boolean[] cycleCheck = new Boolean[1];
        cycleCheck[0] = false;
        String selectedTargetName = selectedTargets.get(0).getName();
        List<String> cycleStr =  backEndMediator.getDependencyGraph().isTargetInCycle(selectedTargets.get(0).getName(),cycleCheck);

        if(cycleCheck[0]) {
            loacteCycleTA.setText("Target " + selectedTargetName+ " is in the following cycle:\n");
            loacteCycleTA.appendText(String.join("-->", cycleStr));

        }
        else
            loacteCycleTA.setText("target is not in cycle");

    }
    private Target.Dependency stringToDependency(String dep) {
        if (dep.equals("Depends On")) return Target.Dependency.DependsOn;
        else if (dep.equals("Required For")) return Target.Dependency.RequiredFor;

        return null;
    }
    @FXML
    void chooseImageDirectoryAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File graphImageFile = directoryChooser.showDialog(null);


               // Image image = new Image(graphImageFile.getPath()+ "\\happy_dog.jpeg");
                visualGraphImageView.setImage(backEndMediator.createVisualGraph(graphImageFile.getPath()));
                visualGraphImageView.setFitHeight(700);
                visualGraphImageView.setFitWidth(700);

    }
    @FXML
    void treeDirectionComboBoxAction(ActionEvent event) {
        Target.Dependency dependency = treeDirectionComboBox.getValue();
        treeView.setRoot(backEndMediator.createTreeView(treeDirectionComboBox.getValue()));
        treeView.setShowRoot(false);

    }

}
