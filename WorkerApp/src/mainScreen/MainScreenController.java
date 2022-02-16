package mainScreen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import mainScreen.workerDashboardScreen.WorkerDashboardScreenController;
import okhttp3.OkHttpClient;

import java.io.IOException;

public class MainScreenController {

    @FXML
    private BorderPane centerPane;

    @FXML
    private Button dashboardButton;

    private OkHttpClient client;
    private Scene mainScene;
    private Parent dashboardScreen;


    public void myInitializer() {
        FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("worker dashboard screen.fxml"));
        try {
            dashboardScreen = fxmlLoader2.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        centerPane.setCenter(dashboardScreen);
        WorkerDashboardScreenController dashboardScreenController = fxmlLoader2.getController();
        dashboardScreenController.setClient(client);
        dashboardScreenController.myInitializer();

    }


    @FXML
    void dashBoardButtonAction(ActionEvent event) {
        centerPane.setCenter(dashboardScreen);

    }

    public void setScene(Scene mainScene) {
        this.mainScene = mainScene;
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }


}
