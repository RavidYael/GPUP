package FXData;


import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TextAreaConsumer  implements Consumer<String> {
    private TextArea textArea;
    String init;
    boolean first = false;

    public TextAreaConsumer(TextArea textArea,String init) {
        this.textArea = textArea;
        this.init = init;
        this.first = true;
    }

    public TextAreaConsumer(TextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void accept(String s) {
        if (first){
            textArea.setText(init + "\n");
            first = false;
        }
        textArea.appendText(s + "\n");

    }


//    public void clear(){
//        textArea.clear();
//        first = true;
//
//    }
}
