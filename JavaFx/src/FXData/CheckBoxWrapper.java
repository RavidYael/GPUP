package FXData;

import javafx.scene.control.CheckBox;

public class CheckBoxWrapper {
    private CheckBox checkBox;
    private String targetName;


    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public String getTargetName() {
        return targetName;
    }
}
