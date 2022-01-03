package MainScreen.taskscreen;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class SimulationTaskController {

    @FXML
    private CheckBox simTimeRandCheckBox;

    @FXML
    private Spinner<Double> chanceSuceessSpinner;

    @FXML
    private Spinner<Double> chanceWarningSpinner;

    @FXML
    private Spinner<Integer> MaxSecSimulateSpinner;

    @FXML
    public void initialize() {
        SpinnerValueFactory successValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0,1,0,0.1);
        SpinnerValueFactory warningValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0,1,0,0.1);

        SpinnerValueFactory secValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,100);
        successValueFactory.setValue(0.5);
        warningValueFactory.setValue(0.5);
        secValueFactory.setValue(1);
        chanceSuceessSpinner.setValueFactory(successValueFactory);
        chanceWarningSpinner.setValueFactory(warningValueFactory);
        MaxSecSimulateSpinner.setValueFactory(secValueFactory);
    }
    public Double getChancesOfSuccess(){
        return chanceSuceessSpinner.getValue();
    }
    public Double getChancesOfWarning(){
        return chanceWarningSpinner.getValue();
    }
    public int getMaxSecToRun(){
        return MaxSecSimulateSpinner.getValue();
    }

    public boolean isTaskTimeRandom(){
        return simTimeRandCheckBox.isSelected();
    }

}




