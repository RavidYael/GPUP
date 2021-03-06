//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package loginScreen;

import FXData.SimpleCookieManager;
import MainScreen.MainScreenController;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import okhttp3.*;
import okhttp3.HttpUrl.Builder;

import static FXData.Constants.BASE_URL;
import static jakarta.servlet.http.HttpServletResponse.*;

public class LoginScreenController {
    @FXML
    private TextField adminNameTextField;
    @FXML
    private Button submitButton;
    private String adminName;
    private Stage Mstage;
    private OkHttpClient client; //Todo thread check

    public LoginScreenController() {
    }

    public void setStage(Stage stage) {
        this.Mstage = stage;
    }

    @FXML
    public void initialize() {
       client =  new OkHttpClient.Builder()
               .cookieJar(new SimpleCookieManager())
               .build();
    }

    @FXML
    void submitButtonAction(ActionEvent event) throws IOException {
        boolean valid = false;
        this.adminName = this.adminNameTextField.getText();
        Builder urlBuilder = HttpUrl.parse(BASE_URL +"/login").newBuilder();
        urlBuilder.addQueryParameter("username", this.adminName);
        urlBuilder.addQueryParameter("user-degree","admin");
        Request loginRequest = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();

       Call call = client.newCall(loginRequest);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        String message = "";
        Response response = call.execute();
        int statusCode = response.code();
        switch(statusCode){
            case SC_OK :
            case SC_ACCEPTED:
            message = "User registered successfully";
            valid = true;
            break;

            case SC_UNAUTHORIZED:
            message = "A user name with identical name already exists!";
            break;

            case SC_CONFLICT:
                message = "username invalid";
                break;

            default:
                message = "Unhandled server error";
                break;

        }
        if (!valid)
            alert.setAlertType(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();

        System.out.println(response.code());
        response.body().close();
        if (valid) {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("MainScreen.fxml"));
            Parent root = fxmlLoader.load();
            Scene mainScene = new Scene(root, 1000, 700);
            this.Mstage.setScene(mainScene);
            MainScreenController mainController = fxmlLoader.getController();
            mainController.setScene(mainScene);
            mainController.setClient(client);
            mainController.myInitializer();
            Mstage.show();
        }
    }

    public String getAdminName() {
        return adminName;
    }
}
