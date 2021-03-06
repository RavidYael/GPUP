package MainScreen.tasksIfonScreen;

import FXData.ServerDataManager;
import MainScreen.adminDashboardScreen.UserInTable;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class TasksInfoScreenController {

    @FXML
    private TableView<TaskInfoInTable> tasksTable;

    @FXML
    private TableColumn<TaskInfoInTable, String> taskNameColumn;

    @FXML
    private TableColumn<TaskInfoInTable, String> graphNameColumn;

    @FXML
    private TableColumn<TaskInfoInTable, Integer> totalTargetsColumn;

    @FXML
    private TableColumn<TaskInfoInTable, Integer> workesrColumn;

    @FXML
    private TableColumn<TaskInfoInTable, Double> taskProgressColumn;

    @FXML
    private TabPane submittedTaskTabPane;

    @FXML
    private Tab taskPropertiesTab;

    @FXML
    private RadioButton fromScratch;

    @FXML
    private RadioButton incrementally;

    @FXML
    private Button reUploadTaskButton;

    @FXML
    private Label reUploadTaskName;

    @FXML
    private Button pauseButton;

    @FXML
    private Button resumeButton;

    @FXML
    private Button stopButton;
    private ServerDataManager serverDataManager;
    private TasksInfoTableManager tableManager;
    private String taskName;


    public void myInitializer(){
        this.tableManager = new TasksInfoTableManager(serverDataManager);
        initializeTaskInfoTable();
        ToggleGroup scratchOrIncremental = new ToggleGroup();
        scratchOrIncremental.getToggles().add(fromScratch);
        scratchOrIncremental.getToggles().add(incrementally);
    }

    private void initializeTaskInfoTable() {
        taskNameColumn.setCellValueFactory(new PropertyValueFactory<TaskInfoInTable,String>("name"));
        graphNameColumn.setCellValueFactory(new PropertyValueFactory<TaskInfoInTable,String>("graphName"));
        totalTargetsColumn.setCellValueFactory(new PropertyValueFactory<TaskInfoInTable,Integer>("totalTargets"));
        workesrColumn.setCellValueFactory(new PropertyValueFactory<TaskInfoInTable,Integer>("numOfWorkers"));
        taskProgressColumn.setCellValueFactory(new PropertyValueFactory<TaskInfoInTable,Double>("progress"));
    }


    public void refreshTaskInfoScreenWithNewTask(String taskName){
        this.taskName = taskName;
        reUploadTaskName.setText(taskName);
        populateTableWithNewTask(taskName);
    }

    public void populateTableWithNewTask(String name) {
        TaskInfoInTable taskInfoInTable = tableManager.getTaskInfoForTable(name);
        tasksTable.setItems(FXCollections.observableArrayList(taskInfoInTable));

    }

    @FXML
    void reUploadTaskButtonAction(ActionEvent event) {
        if (!fromScratch.isSelected() && !incrementally.isSelected()){
            Alert alert = new Alert(Alert.AlertType.ERROR,"please choose from scratch or incrementally");
            alert.showAndWait();
        }
        String type = fromScratch.isSelected() ? fromScratch.getText() : incrementally.getText();
        serverDataManager.reUploadTask(taskName,type);



    }

    @FXML
    void pauseButtonAction(ActionEvent event) {
        serverDataManager.taskExecutionControl(taskName,"pause");


    }

    @FXML
    void resumeButton(ActionEvent event) {
        serverDataManager.taskExecutionControl(taskName,"resume");

    }

    @FXML
    void stopButtonAction(ActionEvent event) {
        serverDataManager.taskExecutionControl(taskName,"stop");
    }

    public void setServerDataManager(ServerDataManager serverDataManager) {
        this.serverDataManager = serverDataManager;
    }

}
