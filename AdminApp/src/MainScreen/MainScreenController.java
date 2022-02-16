package MainScreen;


import FXData.BackEndMediator;
import MainScreen.TaskUploadScreen.UploadTaskScreenController;
import MainScreen.adminDashboardScreen.AdminDashboardScreenController;
import MainScreen.graphInfoScreen.GraphInfoScreenController;
import jakarta.servlet.http.HttpServletResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import okhttp3.*;

import java.io.File;
import java.io.IOException;

import static FXData.Constants.BASE_URL;

public class MainScreenController {

    @FXML
    private Button loadXmlButton;

    @FXML
    private Button graphInfoButton;

    @FXML
    private Button uploadTaskButton;

    @FXML
    private Button taskInfoButton;

    @FXML
    private Label fileLoadLabel;

    @FXML
    private ToggleButton switchSkinToggle;

    @FXML
    private BorderPane centerPane;

    @FXML
    private Button dashboardButton; // for commit


    private Parent uploadTaskScreen;
    private Parent graphInfoScreen;
    private Parent dashboardScreen;
    private UploadTaskScreenController uploadTaskScreenController;
    private GraphInfoScreenController graphInfoScreenController;
    private BackEndMediator backEndMediator =  new BackEndMediator();
    private Scene mainScene;
    private OkHttpClient client;
    private FXMLLoader graphScreenLoader;


    @FXML
    public void initialize() throws IOException {
      //  graphInfoButton.setDisable(true);
      //  uploadTaskButton.setDisable(true);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("tasks upload screen.fxml"));
        uploadTaskScreen = fxmlLoader.load();
        uploadTaskScreenController = fxmlLoader.getController();
        uploadTaskScreenController.setBackEndMediator(backEndMediator);

        graphScreenLoader = new FXMLLoader(getClass().getResource("graph info screen.fxml"));
        graphInfoScreen = graphScreenLoader.load();
        graphInfoScreenController = graphScreenLoader.getController();
        graphInfoScreenController.setBackEndMediator(backEndMediator);


    }

    public void myInitializer(){
        FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("dashboard screen.fxml"));
        try {
            dashboardScreen = fxmlLoader2.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        centerPane.setCenter(dashboardScreen);
        AdminDashboardScreenController dashboardScreenController = fxmlLoader2.getController();
        dashboardScreenController.setClient(client);
        dashboardScreenController.setCenterPane(centerPane);
        dashboardScreenController.setGraphInfoScreen(graphInfoScreen);
        dashboardScreenController.setBackEndMediator(backEndMediator);
        dashboardScreenController.myInitializer();
        uploadTaskScreenController.setClient(client);

    }

    @FXML
    void graphInfoButtonAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("graph info screen.fxml"));
        graphInfoScreenController.myInitialize();
        centerPane.setCenter(graphInfoScreen);
    }

    @FXML
    void loadXmlButtonAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        // String xmlFormatted = "/"+xmlFile.getPath().replace("\\","/");
        String message = "init";
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("fileToUpload",file.getPath(),
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                file))
                .addFormDataPart("","")
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL+"/graphs-data")
                .method("POST", body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println("file upload response: " + response.code());
            message = response.header("message");

            if (response.code() != HttpServletResponse.SC_ACCEPTED){
                alert.setAlertType(Alert.AlertType.ERROR);

            }

            response.body().close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        alert.setContentText(message);
        alert.showAndWait();



    }

    @FXML
    void uploadTaskButtonAction(ActionEvent event) throws IOException {

        centerPane.setCenter(uploadTaskScreen);
        uploadTaskScreenController.myInitialize();
    }

    @FXML
    void dashBoardButtonAction(ActionEvent event) {
        centerPane.setCenter(dashboardScreen);

    }
    @FXML
    void taskInfoButtonAction(ActionEvent event) {

    }


    public void setScene(Scene mainScene) {
        this.mainScene = mainScene;
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }
}
