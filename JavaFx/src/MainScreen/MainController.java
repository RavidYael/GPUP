package MainScreen;


import FXData.BackEndMediator;
import MainScreen.graphInfoScreen.GraphInfoScreenController;
import MainScreen.taskscreen.TaskScreenController;
import dependency.graph.GraphFactory;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainController {

    @FXML
    private Button loadXmlButton;

    @FXML
    private Button graphInfoButton;

    @FXML
    private Button taskButton;

    @FXML
    private Label fileLoadLabel;

    @FXML
    private ToggleButton switchSkinToggle;

    @FXML
    private BorderPane centerPane;

    @FXML
    private ComboBox<String> skinChooser;

    private Parent tasksScreen;
    private Parent graphInfoScreen;
    private TaskScreenController taskScreenController;
    private GraphInfoScreenController graphInfoScreenController;
    private BackEndMediator backEndMediator =  new BackEndMediator();
    private Scene mainScene;


    @FXML
    public void initialize() throws IOException {
        graphInfoButton.setDisable(true);
        taskButton.setDisable(true);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("tasks screen.fxml"));
        tasksScreen = fxmlLoader.load();
        taskScreenController = fxmlLoader.getController();
        taskScreenController.setBackEndMediator(backEndMediator);

        FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("graph info screen.fxml"));
        graphInfoScreen = fxmlLoader1.load();
        graphInfoScreenController = fxmlLoader1.getController();
        graphInfoScreenController.setBackEndMediator(backEndMediator);




    }

    @FXML
    void graphInfoButtonAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("graph info screen.fxml"));
        graphInfoScreenController.myInitialize();
        centerPane.setCenter(graphInfoScreen);
    }

    @FXML
    void loadXmlButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File xml = fileChooser.showOpenDialog(null);
        backEndMediator.setCurXML(xml);
        String message = "File loaded successfully";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        boolean loadSuccess = true;

        try {
            backEndMediator.setDependencyGraph(GraphFactory.newGraphWithData(xml.getPath().toString()));
        } catch (Exception e) {
            e.printStackTrace();
            message = "file loading failed!\n" + e.getMessage();
            alert.setAlertType(Alert.AlertType.ERROR);
            loadSuccess = false;
        }
        alert.setContentText(message);
        alert.showAndWait();
        if (loadSuccess)
            taskScreenController.initializeTaskExecution();

        taskButton.setDisable(false);
        graphInfoButton.setDisable(false);
        graphInfoButton.fire();

    }

    @FXML
    void taskButtonAction(ActionEvent event) throws IOException {

        centerPane.setCenter(tasksScreen);
        taskScreenController.myInitialize();
    }


    public void setScene(Scene mainScene) {
        this.mainScene = mainScene;
    }
}
