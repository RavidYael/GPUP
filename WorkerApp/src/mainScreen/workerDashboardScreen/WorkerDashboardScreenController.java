package mainScreen.workerDashboardScreen;

import DTOs.MissionInfoDTO;
import FXData.ServerDataManager;
import FXData.TableManager;
import FXData.UserInTable;
import dependency.graph.DependencyGraph;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import mainScreen.submittedTasksScreen.SubmittedTasksScreenController;
import okhttp3.*;
import okio.BufferedSink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static FXData.Constants.BASE_URL;
import static FXData.Constants.MISSION_NAME;

public class WorkerDashboardScreenController {




    public static enum YesOrNo {yes, no;};
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
    private TableManager tableManager;
    private Parent submittedTasksScreen;
    private SubmittedTasksScreenController submittedTaskScreenController;
    private BorderPane centerPane;
    private boolean notAgain;

    public void myInitializer(){

        tableManager = new TableManager(serverDataManager);
        refresh();
        tasksTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
            submitToTaskDialog(tasksTable.getSelectionModel().getSelectedItem());
        });

    }

    private void submitToTaskDialog(TaskInTable task) {

        if (!notAgain) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to list as worker for this task?", ButtonType.YES, ButtonType.NO);

            // clicking X also means no
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);

            if (ButtonType.NO.equals(result)) {
                alert.close();
            } else if (result.equals(ButtonType.YES)) {
                subscribeToTaskAction(task);
                alert.close();
            }
            this.notAgain = true;

        }
    }

    private void subscribeToTaskAction(TaskInTable newTask){
        centerPane.setCenter(submittedTasksScreen);
        submittedTaskScreenController.addNewTask(newTask);
        subscribeToTask(newTask);



    }

    private void subscribeToTask(TaskInTable newTask) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL +"/subscribe").newBuilder();
        urlBuilder.addQueryParameter(MISSION_NAME,newTask.name);

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.code());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setCenterPane(BorderPane centerPane) {
        this.centerPane = centerPane;
    }

    public void setServerDataManager(ServerDataManager serverDataManager) {
        this.serverDataManager = serverDataManager;
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

    public void setSubmittedTasksScreen(Parent submittedTasksScreen, SubmittedTasksScreenController submittedTasksScreenController) {
        this.submittedTasksScreen = submittedTasksScreen;
        this.submittedTaskScreenController = submittedTasksScreenController;
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }
    private void initializeAllTables(){
        initializeUsersTable();
        initializeTasksTable();
    }

    public void setNotAgain(boolean b) {
        this.notAgain = b;
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
                        notAgain = false;
                    }
                });

            }
        },0, 2000);
    }
}
