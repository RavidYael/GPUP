//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package loginScreen;

import MainScreen.MainController;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import okhttp3.*;
import okhttp3.HttpUrl.Builder;

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
       client =  new OkHttpClient();
    }

    @FXML
    void submitButtonAction(ActionEvent event) throws IOException {
        this.adminName = this.adminNameTextField.getText();
        Builder urlBuilder = HttpUrl.parse("http://localhost:8080/EX3/login").newBuilder();
        urlBuilder.addQueryParameter("username", this.adminName);
        String url = urlBuilder.build().toString();
        Request loginRequest = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();

        //Todo handle response
       Call call = client.newCall(loginRequest);

        Response response = call.execute();
        System.out.println(response.code());
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("MainScreen.fxml"));
        Parent root = fxmlLoader.load();
        Scene mainScene = new Scene(root, 1000, 700);
        this.Mstage.setScene(mainScene);
        MainController mainController = fxmlLoader.getController();
        mainController.setScene(mainScene);
        Mstage.show();
    }

    public String getAdminName() {
        return adminName;
    }
}
