package MainScreen.TaskUploadScreen;

import FXData.BackEndMediator;
import FXData.TableManager;
import FXData.TargetInTable;
import FXData.TextAreaConsumer;
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

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TaskScreenController {

    @FXML
    private TableView<TargetInTable> targetsTable;

    @FXML
    private TableColumn<TargetInTable, CheckBox> checkedCulumn;

    @FXML
    private TableColumn<TargetInTable, String> targetNameColumn;

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
    private TextArea taskProcessTA;

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

    @FXML
    private Button resumeButton;

    @FXML
    private Tab specificTargetTab;

    @FXML
    private TextArea specificTargetTA;

    @FXML
    private TabPane TaskScreenTabPane;

    @FXML
    private Tab taskProcessInfoTab;


    private Parent simulationTaskScreen;
    private BackEndMediator backEndMediator;
    private SimulationTaskController simulationTaskController;
    private CompilationTaskController compilationTaskController;
    private TableManager tableManager;
    private TextAreaConsumer textAreaConsumer;
    private TaskExecution curTaskExecution;
    private GPUPTask task;
    private boolean fromScratch = true;

    public void setBackEndMediator(BackEndMediator backEndMediator) {
        this.backEndMediator = backEndMediator;
    }

    @FXML
    public void initialize() {
        taskComboBox.setPromptText("Task Type");
        taskComboBox.setItems(FXCollections.observableArrayList("Simulation Task", "Compilation Task"));
        ToggleGroup incrOrScratch = new ToggleGroup();
        incrementalRbutton.setToggleGroup(incrOrScratch);
        fromScratchRbutton.setToggleGroup(incrOrScratch);
        dependencyForSelection.setDisable(true);
        textAreaConsumer = new TextAreaConsumer(taskProcessTA);
        selectWithDependency.selectedProperty().addListener((observable, oldValue, newValue) -> dependencyForSelection.setDisable(!newValue));
        dependencyForSelection.setItems(FXCollections.observableArrayList(Target.Dependency.DependsOn, Target.Dependency.RequiredFor));
        selectSpecificComboBox.setItems(FXCollections.observableArrayList(Target.DependencyLevel.Root, Target.DependencyLevel.Middle, Target.DependencyLevel.Leaf, Target.DependencyLevel.Independed));
        selectSpecificComboBox.setDisable(true);
        selectSpecificCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> selectSpecificComboBox.setDisable(!newValue));
        initLiveDataAction();
        initPauseResumeStopButtons();


    }

    private void initPauseResumeStopButtons() {
        resumeButton.setDisable(true);
        stopButton.setDisable(true);
        pauseButton.setDisable(true);
    }

    private void activatePauseStop() {
        pauseButton.setDisable(false);
        stopButton.setDisable(false);
    }

    public void myInitialize() {

        numOfThreads.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, backEndMediator.getParallelism()));
        checkedCulumn.setCellValueFactory(new PropertyValueFactory<TargetInTable, CheckBox>("checked"));
        targetNameColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable, String>("name"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable, Target.DependencyLevel>("location"));
        //executionStatusColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable, Target.TargetStatus>("targetStatus"));
        // ProccessingResultColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable,Target.TaskResult>("taskResult"));

        ProccessingResultColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable, ObjectProperty<Target.TaskResult>>("taskResult"));
        executionStatusColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable, ObjectProperty<Target.TargetStatus>>("targetStatus"));
        initTable();

    }

    public void initializeTaskExecution() {
        curTaskExecution = null;
    }

    private void initTable() {
        ObservableList<TargetInTable> targetInTables = backEndMediator.getAllTargetsForTable();
        targetsTable.setItems(targetInTables);
        tableManager = new TableManager(targetInTables, selectWithDependency, selectAll, dependencyForSelection, backEndMediator);
    }

    private void refreshTable() {
        ObservableList<TargetInTable> currentTable = tableManager.getTargetsTable();
        ObservableList<TargetInTable> refreshedTargetsInTable = FXCollections.observableArrayList();
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        for (TargetInTable curTargetInTable : currentTable) {
            checkBoxes.add(curTargetInTable.getChecked());
        }
        refreshedTargetsInTable = backEndMediator.getAllTargetsForTable();
        Iterator<CheckBox> iter = checkBoxes.iterator();
        for (TargetInTable curTargetInTable : refreshedTargetsInTable) {
            curTargetInTable.setChecked(iter.next());
        }
        targetsTable.setItems(refreshedTargetsInTable);
        tableManager.setTargetsTable(refreshedTargetsInTable);

    }


    @FXML
    void runButtonAction(ActionEvent event) {
        if (incrementalRbutton.isSelected())
            fromScratch = false;
        else
            fromScratch = true;

        String message;
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (notAllParametersSelected()) {
            message = "not all required parameters have been selected!";
            alert.setContentText(message);
            alert.showAndWait();
            return;
        }
        if (tableManager.getSelectedTargets().isEmpty()) {
            message = "no targets selected";
            alert.setContentText(message);
            alert.showAndWait();
        }


        if (fromScratchRbutton.isSelected()) {
            backEndMediator.restoreGraphToDefault();
            refreshTable();
        }

        DependencyGraph graphInExecution = backEndMediator.getSubGraphFromTable(tableManager.getSelectedTargets());


        if (taskComboBox.getValue() == "Simulation Task") {
                task = new SimulationGPUPTask(simulationTaskController.getMaxSecToRun() * 1000,
                        simulationTaskController.isTaskTimeRandom(),
                        simulationTaskController.getChancesOfSuccess(),
                        simulationTaskController.getChancesOfWarning());

        }
        else if (taskComboBox.getValue().equals("Compilation Task")) {
            task = new CompilationGPUPTask(compilationTaskController.getToCompilePath(),
                    compilationTaskController.getOutputPath(),
                    compilationTaskController.getProcessTime());

        }


        task.setExecutionManager(curTaskExecution);
        bindUIComponents(task);
        activatePauseStop();


        if (fromScratchRbutton.isSelected() || curTaskExecution == null) {
            curTaskExecution = new TaskExecution(graphInExecution, numOfThreads.getValue(), task, textAreaConsumer);
            new Thread(()->curTaskExecution.runTaskFromScratch()).start();
        }
        else if (incrementalRbutton.isSelected()){
            curTaskExecution.setCurNumOfThreads(numOfThreads.getValue());
            curTaskExecution.setGPUPTask(task);
            curTaskExecution.getThreadPoolExecutor().getQueue().clear();
            new Thread(()->curTaskExecution.runTaskIncrementally()).start();
        }
    }

    private boolean notAllParametersSelected() {
        boolean scratchOrInc = !fromScratchRbutton.isSelected() && !incrementalRbutton.isSelected();
        boolean taskNotSelected = taskComboBox.getValue() == null;
        boolean directoryNotSelected = false;

        if (taskComboBox.getValue() == "Compilation Task")
            directoryNotSelected =  compilationTaskController.getToCompilePath() == null || compilationTaskController.getOutputPath() == null;

        return scratchOrInc || taskNotSelected || directoryNotSelected;
    }

    private void resetCheckBoxes(){
        selectAll.setSelected(false);
        selectSpecificCheckBox.setSelected(false);
        selectWithDependency.setSelected(false);
    }

    @FXML
    void pauseButtonAction(ActionEvent event) {
        curTaskExecution.setPauseStatus(true);
        pauseButton.setDisable(true);
        resumeButton.setDisable(false);
    }


    @FXML
    void stopButtonAction(ActionEvent event) {

    }

    @FXML
    void resumeButtonAction(ActionEvent event) {
        curTaskExecution.setCurNumOfThreads(numOfThreads.getValue());
        curTaskExecution.setPauseStatus(false);
        pauseButton.setDisable(false);
        stopButton.setDisable(false);
        resumeButton.setDisable(true);
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
        taskProcessTA.clear();
        task.messageProperty().addListener((observable, oldValue, newValue) -> taskProcessTA.appendText(newValue+ "\n"));
        progressBar.progressProperty().bind(task.progressProperty());
    }

    private void clearDirectory(File directoryToBeDeleted) {
        final File[] files = directoryToBeDeleted.listFiles();
        Arrays.stream(files).forEach(f-> f.delete());
    }
    public void initLiveDataAction(){
        targetsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            TaskScreenTabPane.getSelectionModel().select(1);
            specificTargetTA.setText(String.join("\n",backEndMediator.getTargetLiveData(targetsTable.getSelectionModel().getSelectedItem())));

        });
    }


    }


