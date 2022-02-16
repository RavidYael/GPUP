import LoginScreen.LoginScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WorkerAppMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("worker login screen.fxml"));
        Parent root = fxmlLoader.load();
        LoginScreenController loginScreenController = fxmlLoader.getController();
        loginScreenController.setStage(primaryStage);
        Scene mainScene = new Scene(root, 620.0D, 450.0D);
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }
}
