package MainScreen;


import com.sun.tracing.dtrace.DependencyClass;
import dependency.graph.DependencyGraph;
import dependency.graph.GraphFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.soap.Text;
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
    private Pane centerPane;

    DependencyGraph dependencyGraph;

    @FXML
    void graphInfoButtonAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("secondaryScene.fxml"));
        centerPane.getChildren().add(root);
    }

    @FXML
    void loadXmlButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File xml = fileChooser.showOpenDialog(null);
        String message = "File loaded successfully";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        try {
            dependencyGraph = GraphFactory.newGraphWithData(xml.getPath().toString());
        } catch (Exception e) {
            e.printStackTrace();
            message = "file loading failed!\n" + e.getMessage();
            alert.setAlertType(Alert.AlertType.ERROR);
        }

        alert.setContentText(message);
        alert.showAndWait();


    }

    @FXML
    void taskButtonAction(ActionEvent event) {

    }
    @FXML
    void switchSkinToggleAction(ActionEvent event) {

    }

}
