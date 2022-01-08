package MainScreen.taskscreen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import java.io.File;

public class CompilationTaskController {

    private File toCompilePath;
    private File neededResourcesPath;
    private File outputPath;

    @FXML
    private Button toCompileFileChooser;

    @FXML
    private Button neededResourcesFileChooser;

    @FXML
    private Button outputPathFileChooser;


    @FXML
    void neededResourcesFileChooserAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        neededResourcesPath = fileChooser.showOpenDialog(null);


    }

    @FXML
    void outputPathFileChooserAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        outputPath = fileChooser.showOpenDialog(null);

    }

    @FXML
    void toCompileFileChooserAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        toCompilePath = fileChooser.showOpenDialog(null);

    }

    public File getToCompilePath() {
        return toCompilePath;
    }

    public File getNeededResourcesPath() {
        return neededResourcesPath;
    }

    public File getOutputPath() {
        return outputPath;
    }
}
