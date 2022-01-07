package MainScreen.taskscreen;

import FXData.BackEndMediator;
import FXData.TableManager;
import FXData.TargetInTable;
import dependency.graph.DependencyGraph;
import dependency.target.Target;
import execution.SimulationTask;
import execution.Task;
import execution.TaskExecution;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;

public class TaskScreenController {

    @FXML
    private TableView<TargetInTable> targetsTable;

    @FXML
    private TableColumn<TargetInTable, CheckBox> checkedCulumn;

    @FXML
    private TableColumn<TargetInTable,String> targetNameColumn;

    @FXML
    private TableColumn<TargetInTable, Target.DependencyLevel> locationColumn;

    @FXML
    private TableColumn<TargetInTable, Target.TargetStatus> executionStatusColumn;

    @FXML
    private TableColumn<TargetInTable, Target.TaskResult> ProccessingResultColumn;

    @FXML
    private BorderPane taskSettingsPane;

    @FXML
    private ComboBox<String> taskComboBox;

    @FXML
    private Spinner<Integer> numOfThreads;

    @FXML
    private RadioButton fromScratchRbutton;

    @FXML
    private RadioButton incrementalRbutton;

    @FXML
    private Button runButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label totalCounter;

    @FXML
    private Label inProccessCounter;

    @FXML
    private Label totalCounter1;

    @FXML
    private Label failedCounter;

    @FXML
    private Label skippedCounter;

    @FXML
    private DialogPane taskDialogPane;


    private Parent simulationTaskScreen;
    private BackEndMediator backEndMediator;
    private SimulationTaskController simulationTaskController;
    private TableManager tableManager;

    public void setBackEndMediator(BackEndMediator backEndMediator) {
        this.backEndMediator = backEndMediator;
    }

    @FXML
    public void initialize(){
        taskComboBox.setPromptText("Task Type");
        taskComboBox.setItems(FXCollections.observableArrayList("Simulation Task", "Compilation Task"));
        ToggleGroup incrOrScratch = new ToggleGroup();
        incrementalRbutton.setToggleGroup(incrOrScratch);
        fromScratchRbutton.setToggleGroup(incrOrScratch);

    }
    public void myInitialize(){

        numOfThreads.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,backEndMediator.getParallelism()));
        checkedCulumn.setCellValueFactory(new PropertyValueFactory<TargetInTable, CheckBox>("checked"));
        targetNameColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable, String>("name"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,Target.DependencyLevel>("location"));
        executionStatusColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable, Target.TargetStatus>("targetStatus"));
        ProccessingResultColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,Target.TaskResult>("taskResult"));

        ObservableList<TargetInTable>  targetInTables = backEndMediator.getAllTargetsForTable();
        targetsTable.setItems(targetInTables);
        tableManager = new TableManager(targetInTables);

    }


    @FXML
    void runButtonAction(ActionEvent event) {
        DependencyGraph graphInExecution = backEndMediator.getSubGraphFromTable(tableManager.getSelectedTargets());
        if (taskComboBox.getValue() == "Simulation Task") {
            TaskExecution taskExecution = new TaskExecution(graphInExecution,
                    backEndMediator.getDependencyGraph(),
                    new SimulationTask(
                    simulationTaskController.getMaxSecToRun()*1000,
                    simulationTaskController.isTaskTimeRandom(),
                    simulationTaskController.getChancesOfSuccess(),
                    simulationTaskController.getChancesOfWarning()));

            if (fromScratchRbutton.isSelected()) {
                taskExecution.runTaskFromScratch(numOfThreads.getValue());
            }

        }
      //  backEndMediator.updateGraphFromSubGraph(graphInExecution);
    }


        @FXML
        void taskComboBoxAction (ActionEvent event) throws IOException {
            String screenName = taskComboBox.getValue() + " screen.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(screenName));
            taskSettingsPane.setCenter(fxmlLoader.load());
            simulationTaskController = fxmlLoader.getController();

        }


    }


