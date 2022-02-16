package mainScreen.workerDashboardScreen;

import DTOs.MissionInfoDTO;
import FXData.ServerDataManager;
import FXData.UserInTable;
import dependency.graph.DependencyGraph;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.OkHttpClient;

import javax.sql.rowset.serial.SerialArray;
import java.util.Timer;
import java.util.TimerTask;

public class WorkerDashboardScreenController {

    public static enum YesOrNo {yes, no};

    @FXML
    private TableView<UserInTable> usersTable;

    @FXML
    private TableColumn<UserInTable,String> userNameColumn;

    @FXML
    private TableColumn<UserInTable,String> userRoleColumn;

    @FXML
    private TableView<TaskInTable> tasksTable;

    @FXML
    private TableColumn<TaskInTable, String> taskNameColumn;

    @FXML
    private TableColumn<TaskInTable, String> taskUploadedByColumn;

    @FXML
    private TableColumn<TaskInTable, DependencyGraph.TaskType> taskTypeColumn;

    @FXML
    private TableColumn<TaskInTable, Integer> totalTargetsColumn;

    @FXML
    private TableColumn<TaskInTable, Integer>  rootsColumn;

    @FXML
    private TableColumn<TaskInTable, Integer>  middlesColumn;

    @FXML
    private TableColumn<TaskInTable, Integer>  leafsColumn;

    @FXML
    private TableColumn<TaskInTable, Integer>  independentColumn;

    @FXML
    private TableColumn<TaskInTable, MissionInfoDTO.MissionStatus>  statusColumn;

    @FXML
    private TableColumn<TaskInTable, Integer>  workesrColumn;

    @FXML
    private TableColumn<TaskInTable, YesOrNo>  amIListedColumn;

    @FXML
    private TableColumn<TaskInTable, Integer> pricePerTargetColumn;

    private ServerDataManager serverDataManager;
    private OkHttpClient client;
    private WorkerDashboardTableManager tableManager;



    public void myInitializer(){
        serverDataManager = new ServerDataManager();
        serverDataManager.setClient(client);
        tableManager = new WorkerDashboardTableManager(serverDataManager);
        refresh();
        tasksTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            submitToTaskDialog(tasksTable.getSelectionModel().getSelectedItem().name);
        });


    }

    private void submitToTaskDialog(String taskName){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("submission to task");
        alert.setContentText("Would you like to list as worker for this task?");
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("no", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type -> {
            if (type == okButton) {
                moveToTaskSubmissionScreen(taskName);
            } else if (type == noButton) {
                alert.close();
            } else {
            }
        });

    }

    private void moveToTaskSubmissionScreen(String taskName){


    }

    private void initializeUsersTable(){

        ObservableList<UserInTable> usersInTable = tableManager.getUsersForTable();
        usersTable.setItems(usersInTable);
        userNameColumn.setCellValueFactory(new PropertyValueFactory<UserInTable,String>("name"));
        userRoleColumn.setCellValueFactory(new PropertyValueFactory<UserInTable,String>("role"));

    }

    private void initializeTasksTable() {
        ObservableList<TaskInTable> tasksInTable = tableManager.getTasksForTable();
        taskNameColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable,String>("name"));
        taskUploadedByColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable,String>("uploadedBy"));
        taskTypeColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable,DependencyGraph.TaskType>("taskType"));
        totalTargetsColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable,Integer>("totalTargets"));
        rootsColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable,Integer>("roots"));
        middlesColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable,Integer>("middles"));
        leafsColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable,Integer>("leafs"));
        independentColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable,Integer>("independent"));
        pricePerTargetColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable,Integer>("priceForTarget"));
        workesrColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable,Integer>("numOfWorkers"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable, MissionInfoDTO.MissionStatus>("missionStatus"));
        amIListedColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable, YesOrNo>("amIListed"));
        tasksTable.setItems(tasksInTable);

    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }
    private void initializeAllTables(){
        initializeUsersTable();
        initializeTasksTable();
    }


    private void refresh(){
        System.out.println("refreshing");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        initializeAllTables();
                    }
                });

            }
        },0, 2000);
    }
}
