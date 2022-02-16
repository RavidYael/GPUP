//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import loginScreen.LoginScreenController;

public class AdminAppMain extends Application {
    public AdminAppMain() {
    }

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("admin login screen.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        LoginScreenController loginScreenController = (LoginScreenController)fxmlLoader.getController();
        loginScreenController.setStage(primaryStage);
        Scene mainScene = new Scene(root, 620.0D, 450.0D);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
}
