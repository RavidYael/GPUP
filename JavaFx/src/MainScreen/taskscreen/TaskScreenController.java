package MainScreen.taskscreen;

import FXData.BackEndMediator;
import FXData.TableManager;
import FXData.TargetInTable;
import dependency.graph.DependencyGraph;
import dependency.target.Target;
import execution.*;
//
//import execution.SimulationGPUPTask;
import javafx.beans.property.ObjectProperty;
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
import java.util.*;

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
    private TableColumn<TargetInTable, ObjectProperty<Target.TargetStatus>> executionStatusColumn;

    @FXML
    private TableColumn<TargetInTable, ObjectProperty<Target.TaskResult>> ProccessingResultColumn;

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

    @FXML
    private CheckBox selectAll;

    @FXML
    private CheckBox selectWithDependency;

    @FXML
    private TextArea taskProccessTA;

    @FXML
    private ComboBox<Target.Dependency> dependencyForSelection;

    @FXML
    private ComboBox<Target.DependencyLevel> selectSpecificComboBox;

    @FXML
    private CheckBox selectSpecificCheckBox;

    @FXML
    private Button pauseButton;

    @FXML
    private Button stopButton;


    private Parent simulationTaskScreen;
    private BackEndMediator backEndMediator;
    private SimulationTaskController simulationTaskController;
    private CompilationTaskController compilationTaskController;
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
        dependencyForSelection.setDisable(true);
        selectWithDependency.selectedProperty().addListener((observable, oldValue, newValue) -> dependencyForSelection.setDisable(!newValue));
        dependencyForSelection.setItems(FXCollections.observableArrayList(Target.Dependency.DependsOn, Target.Dependency.RequiredFor));
        selectSpecificComboBox.setItems(FXCollections.observableArrayList(Target.DependencyLevel.Root, Target.DependencyLevel.Middle, Target.DependencyLevel.Leaf, Target.DependencyLevel.Independed));
        selectSpecificComboBox.setDisable(true);
        selectSpecificCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> selectSpecificComboBox.setDisable(!newValue));

    }
    public void myInitialize(){

        numOfThreads.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,backEndMediator.getParallelism()));
        checkedCulumn.setCellValueFactory(new PropertyValueFactory<TargetInTable, CheckBox>("checked"));
        targetNameColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable, String>("name"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,Target.DependencyLevel>("location"));
        //executionStatusColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable, Target.TargetStatus>("targetStatus"));
       // ProccessingResultColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,Target.TaskResult>("taskResult"));

        ProccessingResultColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,ObjectProperty<Target.TaskResult>>("taskResult"));
        executionStatusColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,ObjectProperty<Target.TargetStatus>>("targetStatus"));

        ObservableList<TargetInTable>  targetInTables = backEndMediator.getAllTargetsForTable();
        targetsTable.setItems(targetInTables);
        tableManager = new TableManager(targetInTables,selectWithDependency,selectAll,dependencyForSelection,backEndMediator);

    }


    @FXML
    void runButtonAction(ActionEvent event) {

        Map<Target, Set<String>> Target2ItsOriginalDependOnTargets = new HashMap<>();
        Map<Target, Set<String>> Target2ItsOriginalRequiredForTargets = new HashMap<>();
        GPUPTask task =null;
        DependencyGraph graphInExecution = backEndMediator.getSubGraphFromTable(tableManager.getSelectedTargets(), Target2ItsOriginalDependOnTargets, Target2ItsOriginalRequiredForTargets);

        // seems classic to create a map of target to its original dependencies, send it to getSubGraph method
        // before changing the traversing data of one target insert it to this map- by that you can prevent from
        // create a deep copy, you can work directly on the target from the original graph and just in the end of this method(run)
        // reset the traversing data to the origin using the map we had created first

        if (taskComboBox.getValue() == "Simulation Task") {
            task = new SimulationGPUPTask(simulationTaskController.getMaxSecToRun()*1000,
                    simulationTaskController.isTaskTimeRandom(),
                    simulationTaskController.getChancesOfSuccess(),
                    simulationTaskController.getChancesOfWarning());
        }
        else if (taskComboBox.getValue().equals("Compilation Task")) {
            task = new CompilationGPUPTask(compilationTaskController.getToCompilePath(),
                    compilationTaskController.getOutputPath());

        }

        TaskExecution taskExecution = new TaskExecution(graphInExecution, numOfThreads.getValue(), task);
        bindUIComponents(task);
        if (fromScratchRbutton.isSelected()) {
            new Thread(taskExecution).start();
        }
       // backEndMediator.getDependencyGraph().updateAllTargetDependencyLevelAfterExecution();
        //backEndMediator.getDependencyGraph().resetTraverseDataAfterChangedInSubGraph(Target2ItsOriginalDependOnTargets,Target2ItsOriginalRequiredForTargets);
    }

    @FXML
    void pauseButtonAction(ActionEvent event) {

    }


    @FXML
    void stopButtonAction(ActionEvent event) {

    }


        @FXML
        void taskComboBoxAction (ActionEvent event) throws IOException {
            String screenName = taskComboBox.getValue() + " screen.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(screenName));
            taskSettingsPane.setCenter(fxmlLoader.load());
            if (taskComboBox.getValue().equals("Simulation Task"))
            simulationTaskController = fxmlLoader.getController();
            else
                compilationTaskController = fxmlLoader.getController();


        }

    @FXML
    void selectSpecificOnAction(ActionEvent event) {
        tableManager.deselectAll();
        tableManager.selectByDependecyLevel(selectSpecificComboBox.getValue());

    }
    private void bindUIComponents(GPUPTask task){
        taskProccessTA.clear();
        task.messageProperty().addListener((observable, oldValue, newValue) -> taskProccessTA.appendText(newValue+ "\n"));
        progressBar.progressProperty().bind(task.progressProperty());





    }


    }


