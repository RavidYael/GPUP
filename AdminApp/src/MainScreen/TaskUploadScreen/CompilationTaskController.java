package MainScreen.TaskUploadScreen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

public class CompilationTaskController {

    private File toCompilePath;
    private File outputPath;

    @FXML
    private Button toCompileFileChooser;



    @FXML
    private Button outputPathFileChooser;

    @FXML
    private Spinner<Integer> processTImeSpinner;



    @FXML
    public void initialize(){
        processTImeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10,0,1));
    }

    @FXML
    void outputPathFileChooserAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        outputPath = directoryChooser.showDialog(null);

    }

    @FXML
    void toCompileFileChooserAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        toCompilePath = directoryChooser.showDialog(null);

    }

    public File getToCompilePath() {
        return toCompilePath;
    }

    public File getOutputPath() {
        return outputPath;
    }

    public Integer getProcessTime(){
        return processTImeSpinner.getValue();
    }
}
