
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sun.nio.ch.ThreadPool;

import java.util.concurrent.ThreadPoolExecutor;

public class GpupApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        Scene mainScene = new Scene(root,1200,950);
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        GpupApp.launch();
    }
}
