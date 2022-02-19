package mainScreen;

import FXData.ServerDataManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import mainScreen.submittedTasksScreen.SubmittedTasksScreenController;
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
    private Parent submittedTasksScreen;
    private SubmittedTasksScreenController submittedTasksScreenController;
    private WorkerDashboardScreenController dashboardScreenController;
    ServerDataManager serverDataManager;


    public void myInitializer() {
        serverDataManager = new ServerDataManager();
        serverDataManager.setClient(client);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("worker dashboard screen.fxml"));
        try {
            dashboardScreen = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        centerPane.setCenter(dashboardScreen);
         dashboardScreenController = fxmlLoader.getController();
        dashboardScreenController.setClient(client);
        dashboardScreenController.setCenterPane(centerPane);
        dashboardScreenController.setServerDataManager(serverDataManager);
        dashboardScreenController.myInitializer();

        FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("submitted tasks.fxml"));
        try {
            submittedTasksScreen = fxmlLoader1.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        submittedTasksScreenController = fxmlLoader1.getController();
        submittedTasksScreenController.setServerDataManager(serverDataManager);
        submittedTasksScreenController.myInitializer(client);
        dashboardScreenController.setSubmittedTasksScreen(submittedTasksScreen, submittedTasksScreenController);

    }


    @FXML
    void dashBoardButtonAction(ActionEvent event) {
        centerPane.setCenter(dashboardScreen);
        dashboardScreenController.setNotAgain(false);

    }
    @FXML
    void submittedTasksButtonAction(ActionEvent event) {
        centerPane.setCenter(submittedTasksScreen);


    }

    public void setScene(Scene mainScene) {
        this.mainScene = mainScene;
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }


}
