package MainScreen.tasksIfonScreen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TasksInfoScreenController {

    @FXML
    private TableView<?> tasksTable;

    @FXML
    private TableColumn<?, ?> taskNameColumn;

    @FXML
    private TableColumn<?, ?> graphNameColumn;

    @FXML
    private TableColumn<?, ?> totalTargetsColumn;

    @FXML
    private TableColumn<?, ?> workesrColumn;

    @FXML
    private TableColumn<?, ?> taskProgressColumn;

    @FXML
    private TabPane submittedTaskTabPane;

    @FXML
    private Tab taskPropertiesTab;

    @FXML
    private RadioButton fromScratch;

    @FXML
    private RadioButton incrementally;

    @FXML
    private Button reUploadTaskButton;

    @FXML
    private Label reUploadTaskName;

    @FXML
    private Button pauseButton;

    @FXML
    private Button resumeButton;

    @FXML
    private Button stopButton;

    @FXML
    void pauseButtonAction(ActionEvent event) {

    }

    @FXML
    void reUploadTaskButtonAction(ActionEvent event) {

    }

    @FXML
    void resumeButton(ActionEvent event) {

    }

    @FXML
    void stopButtonAction(ActionEvent event) {

    }

}
