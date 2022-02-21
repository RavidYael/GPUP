package mainScreen.submittedTasksScreen;

import DTOs.MissionInfoDTO;
import DTOs.TasksPrefernces.CompilationParameters;
import DTOs.TasksPrefernces.SimulationParameters;
import FXData.*;
import dependency.graph.DependencyGraph;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import mainScreen.workerDashboardScreen.TaskInTable;
import mainScreen.workerDashboardScreen.WorkerDashboardScreenController;
import okhttp3.OkHttpClient;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import static FXData.Constants.NUM_OF_THREADS;
import static java.util.concurrent.TimeUnit.MINUTES;

public class SubmittedTasksScreenController {

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
    private TableColumn<TaskInTable, WorkerDashboardScreenController.YesOrNo>  amIListedColumn;

    @FXML
    private TableColumn<TaskInTable, Integer> pricePerTargetColumn;

    @FXML
    private TextArea runningTargetsTA;

    @FXML
    private TabPane submittedTaskTabPane;

    @FXML
    private TextArea taskPropertiesTA;

    private OkHttpClient client;
    private TableManager tableManager;
    ObservableList<TaskInTable> tasksInTable = FXCollections.observableArrayList();
    WorkerExecutor workerExecutor;
    private ServerDataManager serverDataManager;
    private Integer numOfThreads = (Integer)SimpleCookieManager.getSimpleCookie(NUM_OF_THREADS);
    private TextAreaConsumer textAreaConsumer;


    public void setServerDataManager(ServerDataManager serverDataManager) {
        this.serverDataManager = serverDataManager;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public void setTableManager(TableManager tableManager) {
        this.tableManager = tableManager;
    }


    @FXML
    public void initialize(){
        bindSpecificTask();
        textAreaConsumer = new TextAreaConsumer(runningTargetsTA);


    }

    private void bindSpecificTask() {
        tasksTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
            specificTaskAction(tasksTable.getSelectionModel().getSelectedItem().getName());

        });
    }

    private void specificTaskAction(String taskName) {
        taskPropertiesTA.clear();
        submittedTaskTabPane.getSelectionModel().select(1);
        // display specific task information
        MissionInfoDTO missionInfoDTO = serverDataManager.getTaskFromServer(taskName);
        if (missionInfoDTO != null){
            taskPropertiesTA.appendText("Task parameters: \n"+
                    "Task Type = ");
            if (missionInfoDTO.getMissionType().equals(DependencyGraph.TaskType.SIMULATION)) {
                SimulationParameters simulationParameters = missionInfoDTO.getSimulationParameters();
                taskPropertiesTA.appendText("Simulation\n");
                taskPropertiesTA.appendText(simulationParameters.toString());
            }
            else{
                CompilationParameters compilationParameters = missionInfoDTO.getCompilationParameters();
                taskPropertiesTA.appendText("Compilation\n");
                taskPropertiesTA.appendText(compilationParameters.toString());
            }
        }

    }

    public void myInitializer(OkHttpClient client){
        this.client = client;
        this.tableManager = tableManager;
        initializeTasksTable();
        workerExecutor = new WorkerExecutor(numOfThreads,serverDataManager,textAreaConsumer);

    }

    private void initializeTasksTable(){
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
        amIListedColumn.setCellValueFactory(new PropertyValueFactory<TaskInTable, WorkerDashboardScreenController.YesOrNo>("amIListed"));
        tasksTable.setItems(tasksInTable);
    }

    public void addNewTask(TaskInTable newTask) {
        tasksInTable.add(newTask);
        initializeTasksTable();
        Thread thread = new Thread(()->beginExecution());
        thread.start();
        //TODO check if already executing
    }

    private void beginExecution() {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(numOfThreads,numOfThreads,1000, MINUTES,new LinkedBlockingQueue<Runnable>());
        while (true) {
            //  threadPoolExecutor.submit(workerExecutor);
            if (threadPoolExecutor.getActiveCount() == numOfThreads){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else{
                while(workerExecutor.isPaused()){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }

            threadPoolExecutor.execute(workerExecutor);
            }

        }


        }


    }


