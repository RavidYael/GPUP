package MainScreen.TaskUploadScreen;

import DTOs.CompilationTaskDTO;
import DTOs.SimulationTaskDTO;
import DTOs.TasksPrefernces.CompilationParameters;
import DTOs.TasksPrefernces.SimulationParameters;
import FXData.BackEndMediator;
import FXData.TableManager;
import FXData.TargetInTable;
import com.google.gson.Gson;
import dependency.graph.DependencyGraph;
import dependency.target.Target;
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
import okhttp3.*;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import static FXData.Constants.BASE_URL;
import static jakarta.servlet.http.HttpServletResponse.SC_ACCEPTED;

public class UploadTaskScreenController {

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
    private CheckBox selectAll;

    @FXML
    private CheckBox deselectAll;

    @FXML
    private CheckBox selectWithDependency;

    @FXML
    private ComboBox<Target.Dependency> dependencyForSelection;

    @FXML
    private ComboBox<Target.DependencyLevel> selectSpecificComboBox;

    @FXML
    private CheckBox selectSpecificCheckBox;

    @FXML
    private BorderPane taskSettingsPane;

    @FXML
    private ComboBox<String> taskComboBox;

    @FXML
    private TextField taskNameTF;

    @FXML
    private Button uploadTaskButton;

    private Parent simulationTaskScreen;
    private BackEndMediator backEndMediator;
    private SimulationTaskController simulationTaskController;
    private CompilationTaskController compilationTaskController;
    private TableManager tableManager;
    private OkHttpClient client;

    private final String SIMULATION = "Simulation Task";
    private final String COMPILATION = "Compilation Task";


    @FXML
    public void initialize() {
        taskComboBox.setPromptText("Task Type");
        taskComboBox.setItems(FXCollections.observableArrayList(SIMULATION, COMPILATION));        dependencyForSelection.setDisable(true);
        selectWithDependency.selectedProperty().addListener((observable, oldValue, newValue) -> dependencyForSelection.setDisable(!newValue));
        dependencyForSelection.setItems(FXCollections.observableArrayList(Target.Dependency.DependsOn, Target.Dependency.RequiredFor));
        selectSpecificComboBox.setItems(FXCollections.observableArrayList(Target.DependencyLevel.Root, Target.DependencyLevel.Middle, Target.DependencyLevel.Leaf, Target.DependencyLevel.Independed));
        selectSpecificComboBox.setDisable(true);
        selectSpecificCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> selectSpecificComboBox.setDisable(!newValue));


    }
    public void myInitialize() {

        checkedCulumn.setCellValueFactory(new PropertyValueFactory<TargetInTable, CheckBox>("checked"));
        targetNameColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable, String>("name"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable, Target.DependencyLevel>("location"));
        ProccessingResultColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable, ObjectProperty<Target.TaskResult>>("taskResult"));
        executionStatusColumn.setCellValueFactory(new PropertyValueFactory<TargetInTable, ObjectProperty<Target.TargetStatus>>("targetStatus"));
        initTable();

    }

    private void initTable() {
        ObservableList<TargetInTable> targetInTables = backEndMediator.getAllTargetsForTable();
        targetsTable.setItems(targetInTables);
        tableManager = new TableManager(targetInTables, selectWithDependency, selectAll, dependencyForSelection, backEndMediator);
    }

    @FXML
    void taskComboBoxAction (ActionEvent event) throws IOException {
        String screenName = taskComboBox.getValue() + " screen.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(screenName));
        taskSettingsPane.setCenter(fxmlLoader.load());
        if (taskComboBox.getValue().equals(SIMULATION))
            simulationTaskController = fxmlLoader.getController();
        else
            compilationTaskController = fxmlLoader.getController();
    }

    private boolean notAllParametersSelected() {
        boolean taskNotSelected = taskComboBox.getValue() == null;
        boolean directoryNotSelected = false;
        boolean taskNameNotSelected = taskNameTF.getText().equals("");
        if (!taskNotSelected) {
            if (taskComboBox.getValue().equals("Compilation Task"))
                directoryNotSelected = compilationTaskController.getToCompilePath() == null || compilationTaskController.getOutputPath() == null;
        }

        return taskNameNotSelected || taskNotSelected || directoryNotSelected;
    }

    @FXML
    void uploadTaskAction(ActionEvent event) {
        boolean notAllParametersSelected = notAllParametersSelected();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        String message = "";
        if (!notAllParametersSelected) {
            Gson taskGson = new Gson();
            String taskHeader = "";
            String taskAsJson = "";
            String taskName = taskNameTF.getText();
            String graphName = backEndMediator.getDependencyGraph().getGraphName();
            Set<String> targetsToExecute = tableManager.getSelectedTargets()
                    .stream()
                    .map(targetInTable -> targetInTable.getName())
                    .collect(Collectors.toSet());
            if (taskComboBox.getValue().equals(SIMULATION)) {
                int proccessTime = simulationTaskController.getMaxSecToRun() *1000;
                boolean isTimeRandom = simulationTaskController.isTaskTimeRandom();
                Double suceessProb = simulationTaskController.getChancesOfSuccess();
                Double warningProb = simulationTaskController.getChancesOfWarning();
                SimulationParameters simulationParameters = new SimulationParameters(proccessTime, isTimeRandom, suceessProb, warningProb);
                int price = backEndMediator.getDependencyGraph().getTaskPricing().get(DependencyGraph.TaskType.SIMULATION) * targetsToExecute.size();
                SimulationTaskDTO simulationTaskDTO = new SimulationTaskDTO(taskName, new String(), graphName, targetsToExecute, price, simulationParameters);
                taskAsJson = taskGson.toJson(simulationTaskDTO);
                taskHeader = "simulation";


            } else {

                String sourcePath = compilationTaskController.getToCompilePath().getPath();
                String desPath = compilationTaskController.getOutputPath().getPath();
                CompilationParameters compilationParameters = new CompilationParameters(sourcePath, desPath);
                int price = backEndMediator.getDependencyGraph().getTaskPricing().get(DependencyGraph.TaskType.COMPILATION) * targetsToExecute.size();
                // taskDTO = new CompilationTaskDTO(taskName,new String(),graphName,targetsToExecute,price,compilationParameters);
                CompilationTaskDTO compilationTaskDTO = new CompilationTaskDTO(taskName, new String(), graphName, targetsToExecute, price, compilationParameters);
                taskAsJson = taskGson.toJson(compilationTaskDTO);
                taskHeader = "compilation";
            }


            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"), taskAsJson);

            Request request = new Request.Builder()
                    .url(BASE_URL + "/upload-task")
                    .post(body)
                    .addHeader("taskType", taskHeader)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.code() != SC_ACCEPTED)
                    alert.setAlertType(Alert.AlertType.ERROR);
                message = response.header("message");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            message = "not all required task parameters are selected!";
            alert.setAlertType(Alert.AlertType.ERROR);
        }

        alert.setContentText(message);
        alert.showAndWait();

    }

    public void setBackEndMediator(BackEndMediator backEndMediator) {
        this.backEndMediator = backEndMediator;
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }
}

