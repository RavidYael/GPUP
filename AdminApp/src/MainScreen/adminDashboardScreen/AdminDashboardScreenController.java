package MainScreen.adminDashboardScreen;

import FXData.BackEndMediator;
import FXData.ServerDataManager;
import MainScreen.graphInfoScreen.GraphInfoScreenController;
import MainScreen.tasksIfonScreen.TasksInfoScreenController;
import dependency.graph.DependencyGraph;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class AdminDashboardScreenController {

    private OkHttpClient client;


    @FXML
    private TableView<GraphInTable> graphsTable;

    @FXML
    private TableColumn<GraphInTable, String> graphNameColumn;

    @FXML
    private TableColumn<GraphInTable,String> graphUploadedByColumn;

    @FXML
    private TableColumn<GraphInTable,Integer> totalTargetsColumn;

    @FXML
    private TableColumn<GraphInTable,Integer> rootsColumn;

    @FXML
    private TableColumn<GraphInTable,Integer> leafsColumn;

    @FXML
    private TableColumn<GraphInTable, Integer> middlesColumn;

    @FXML
    private TableColumn<GraphInTable, Integer> independentColumn;

    @FXML
    private TableColumn<GraphInTable, Integer> simulationPriceColumn;

    @FXML
    private TableColumn<GraphInTable, Integer> compilationPriceColumn;

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
    private TableColumn<TaskInTable, String> taskGraphNameColumn;

    @FXML
    private TableColumn<TaskInTable, Integer> taskTotalTargetsColumn;

    @FXML
    private TableColumn<TaskInTable, Integer> taskRootsColumn;

    @FXML
    private TableColumn<TaskInTable, Integer> taskMiddlesColumn;

    @FXML
    private TableColumn<TaskInTable, Integer> taskLeafsColumn;

    @FXML
    private TableColumn<TaskInTable, Integer> taskIndependentColumn;

    @FXML
    private TableColumn<TaskInTable, Integer> taskTotalPaymentColumn;

    @FXML
    private TableColumn<TaskInTable, Integer> workesrColumn;

    private AdminDashboardTableManager dashboardTableManager;
    private BorderPane centerPane;
    private Parent graphInfoScreen;
    private BackEndMediator backEndMediator;
    private FXMLLoader graphScreenLoader;
    private GraphInfoScreenController graphInfoScreenController;
    private ServerDataManager serverDataManager;
    private Parent tasksInfoScreen;
    private TasksInfoScreenController tasksInfoScreenController;


    @FXML
    public void initialize(){

    }
    public void myInitializer(){

        componentsInitializer();
        serverDataManager = new ServerDataManager();
        serverDataManager.setClient(client);
        dashboardTableManager = new AdminDashboardTableManager(serverDataManager);
        initializeTaskInfoScreen();
        refresh();



    }

    private void initializeTaskInfoScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("tasks info screen.fxml"));
        try {
            tasksInfoScreen = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tasksInfoScreenController = fxmlLoader.getController();
        tasksInfoScreenController.setServerDataManager(serverDataManager);
        tasksInfoScreenController.myInitializer();
    }

    private void componentsInitializer() {
        graphsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            try {
                moveToGraphInfoScreen(graphsTable.getSelectionModel().getSelectedItem().getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        tasksTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
            try {
                moveToTaskInfoScreen(tasksTable.getSelectionModel().getSelectedItem().name);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void moveToTaskInfoScreen(String name) throws IOException {

        tasksInfoScreenController.populateTableWithNewTask(name);
        centerPane.setCenter(tasksInfoScreen);


    }

    private void initializeUsersTable(){

        ObservableList<UserInTable> usersInTable = dashboardTableManager.getUsersForTable();
        usersTable.setItems(usersInTable);
        userNameColumn.setCellValueFactory(new PropertyValueFactory<UserInTable,String>("name"));
        userRoleColumn.setCellValueFactory(new PropertyValueFactory<UserInTable,String>("role"));

    }

    private void initializeGraphsTable(){
        System.out.println("updating graphs table");
        ObservableList<GraphInTable> graphsInTable = dashboardTableManager.getGraphsForTable();
        graphsTable.setItems(graphsInTable);
        graphNameColumn.setCellValueFactory(new PropertyValueFactory<GraphInTable,String>("name"));
        graphUploadedByColumn.setCellValueFactory(new PropertyValueFactory<GraphInTable,String>("uploadedBy"));
        totalTargetsColumn.setCellValueFactory(new PropertyValueFactory<GraphInTable,Integer>("totalTargets"));
        rootsColumn.setCellValueFactory(new PropertyValueFactory<GraphInTable,Integer>("roots"));
        middlesColumn.setCellValueFactory(new PropertyValueFactory<GraphInTable,Integer>("middles"));
        independentColumn.setCellValueFactory(new PropertyValueFactory<GraphInTable,Integer>("independent"));
        leafsColumn.setCellValueFactory(new PropertyValueFactory<GraphInTable,Integer>("leafs"));
        simulationPriceColumn.setCellValueFactory(new PropertyValueFactory<GraphInTable,Integer>("simulationPrice"));
        compilationPriceColumn.setCellValueFactory(new PropertyValueFactory<GraphInTable,Integer>("compilationPrice"));
    }

    private void initializeTasksTable() {
        ObservableList<TaskInTable> tasksInTable = dashboardTableManager.getTasksForTable();
        tasksTable.setItems(tasksInTable);
        taskNameColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable,String>("name"));
        taskUploadedByColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable,String>("uploadedBy"));
        taskGraphNameColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable,String>("graphName"));
        taskTotalTargetsColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable,Integer>("totalTargets"));
        taskRootsColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable,Integer>("roots"));
        taskMiddlesColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable,Integer>("middles"));
        taskLeafsColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable,Integer>("leafs"));
        taskIndependentColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable,Integer>("independent"));
        taskTotalPaymentColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable,Integer>("totalPrice"));
        workesrColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable,Integer>("workers"));



    }
    private void initializeAllTables(){
        initializeUsersTable();
        initializeGraphsTable();
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

    public void setGraphInfoScreen(Parent graphInfoScreen) {
        this.graphInfoScreen = graphInfoScreen;
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }


    public void setCenterPane(BorderPane centerPane) {
        this.centerPane = centerPane;
    }

    public void setBackEndMediator(BackEndMediator backEndMediator){
        this.backEndMediator = backEndMediator;
    }

    private void moveToGraphInfoScreen(String graphName) throws IOException {
        backEndMediator.setDependencyGraph(serverDataManager.getDependencyGraphByName(graphName));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("graph info screen.fxml"));
        graphInfoScreen = fxmlLoader.load();
        graphInfoScreenController = fxmlLoader.getController();
        graphInfoScreenController.setBackEndMediator(backEndMediator);
        graphInfoScreenController.myInitialize();
        centerPane.setCenter(graphInfoScreen);

    }


}


