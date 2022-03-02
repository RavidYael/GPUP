package mainScreen.submittedTasksScreen;

import DTOs.MissionInfoDTO;
import DTOs.TasksPrefernces.CompilationParameters;
import DTOs.TasksPrefernces.SimulationParameters;
import FXData.*;
import dependency.graph.DependencyGraph;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import mainScreen.workerDashboardScreen.TaskInTable;
import mainScreen.workerDashboardScreen.WorkerDashboardScreenController;
import okhttp3.OkHttpClient;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import static FXData.Constants.NUM_OF_THREADS;
import static java.util.concurrent.TimeUnit.MINUTES;

public class SubmittedTasksScreenController {

    @FXML
    private TableView<SubmittedTaskInTable> tasksTable;

    @FXML
    private TableColumn<SubmittedTaskInTable, String> taskNameColumn;

    @FXML
    private TableColumn <SubmittedTaskInTable, Integer> workesrColumn;

    @FXML
    private TableColumn <SubmittedTaskInTable, Double> taskProgressColumn;

    @FXML
    private TableColumn <SubmittedTaskInTable, Integer> targetsRunColumn;

    @FXML
    private TableColumn <SubmittedTaskInTable, Integer> creditsColumn;

    @FXML
    private TextArea runningTargetsTA;

    @FXML
    private TabPane submittedTaskTabPane;

    @FXML
    private TextArea taskPropertiesTA;

    @FXML
    private Button pauseButton;

    @FXML
    private Button playButton;

    @FXML
    private Button stopButton;

    @FXML
    private Label numOfCredits;

    private OkHttpClient client;
    private TableManager tableManager;
    private WorkerExecutor workerExecutor;
    private ServerDataManager serverDataManager;
    private Integer numOfThreads = (Integer)SimpleCookieManager.getSimpleCookie(NUM_OF_THREADS);
    private TextAreaConsumer textAreaConsumer;
    private ThreadPoolExecutor threadPoolExecutor;
    private Boolean inExecution = false;


    public void setInExecution(Boolean inExecution) {
        this.inExecution = inExecution;
    }



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
       // this.tableManager = tableManager;
        //initializeTasksTable();
       workerExecutor = new WorkerExecutor(numOfThreads,numOfCredits,serverDataManager,textAreaConsumer);
       threadPoolExecutor = new ThreadPoolExecutor(numOfThreads,numOfThreads,1000, MINUTES,new LinkedBlockingQueue<Runnable>());
       refresh();
    }

    private void initializeTasksTable(){

        ObservableList<SubmittedTaskInTable> tasksInTable = tableManager.getSubmittedTasksForTable();
        taskNameColumn.setCellValueFactory(new PropertyValueFactory<SubmittedTaskInTable,String>("name"));
        workesrColumn.setCellValueFactory(new PropertyValueFactory<SubmittedTaskInTable,Integer>("numOfWorkers"));
        taskProgressColumn.setCellValueFactory(new PropertyValueFactory<SubmittedTaskInTable,Double>("progress"));
        targetsRunColumn.setCellValueFactory(new PropertyValueFactory<SubmittedTaskInTable,Integer>("numOfTargets"));
      //  creditsColumn.setCellValueFactory(new PropertyValueFactory<SubmittedTaskInTable,Integer>("credits"));
        tasksTable.setItems(tasksInTable);
    }

    public void addNewTask(TaskInTable newTask) {
      //  tasksInTable.add(newTask);

        if(inExecution == false) {
            inExecution = true;
            Thread thread = new Thread(() -> beginExecution());
            thread.start();
        }
    }

    private void beginExecution() {

        while (true) {

            //  threadPoolExecutor.submit(workerExecutor);
            if (threadPoolExecutor.getActiveCount() == numOfThreads){
                try {
                    System.out.println( " im going to sleep and wait that a a sub - worker will finish his job");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            threadPoolExecutor.execute(workerExecutor);
            if (threadPoolExecutor.getActiveCount() == numOfThreads) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }

    }




    @FXML
    void pauseButtonAction(ActionEvent event) {

        if (tasksTable.getSelectionModel().getSelectedItem() != null) {
            new Thread(() -> {
               serverDataManager.pauseResumeOrStopRequest(tasksTable.getSelectionModel().getSelectedItem().getName(),"pause");
            }).start();

            //workerExecutor.setPaused();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR,"please select task to pause");
            alert.showAndWait();
        }

    }

    @FXML
    void playButtonAction(ActionEvent event) {
        //TODO check if been paused

        if (tasksTable.getSelectionModel().getSelectedItem() != null) {
            new Thread(() -> {
            serverDataManager.pauseResumeOrStopRequest(tasksTable.getSelectionModel().getSelectedItem().getName(),"resume");
        }).start();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR,"please select task to resume");
            alert.showAndWait();
        }


    }



    @FXML
    void stopButtonAction(ActionEvent event) {
        if (tasksTable.getSelectionModel().getSelectedItem() != null) {  new Thread(() -> {
            serverDataManager.pauseResumeOrStopRequest(tasksTable.getSelectionModel().getSelectedItem().getName(),"unregister");
        }).start();

        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR,"please select task to stop");
            alert.showAndWait();
        }

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
                        initializeTasksTable();
                    }
                });

            }
        },0, 2000);
    }


    }


