package MainScreen.taskscreen;

import FXData.BackEndMediator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class TaskScreenController {

    @FXML
    private ChoiceBox<String> taskChoiceBox;

    @FXML
    private BorderPane taskSettingsPane;

    @FXML
    private Button OKButton;

    @FXML
    private Spinner<Integer> numOfThreads;

    @FXML
    private RadioButton fromScratchRbutton;

    @FXML
    private RadioButton incrementalRbutton;

    @FXML
    private Button runButton;

    private Parent simulationTaskScreen;
    private BackEndMediator backEndMediator;
    private SimulationTaskController simulationTaskController;

    public void setBackEndMediator(BackEndMediator backEndMediator) {
        this.backEndMediator = backEndMediator;
    }

    @FXML
    void OKButtonAction(ActionEvent event) throws IOException {
        if (taskChoiceBox.getValue().equals("Simulation Task")){
            taskSettingsPane.setCenter(simulationTaskScreen);
        }


    }
    @FXML
    void runButtonAction(ActionEvent event) {
        if (taskChoiceBox.getValue().equals("Simulation Task")){
        }

    }


}
