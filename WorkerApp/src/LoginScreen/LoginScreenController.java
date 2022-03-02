package LoginScreen;

import FXData.SimpleCookieManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mainScreen.MainScreenController;
import okhttp3.*;

import java.io.IOException;

import static FXData.Constants.*;
import static jakarta.servlet.http.HttpServletResponse.*;

public class LoginScreenController {


    @FXML
    private TextField adminNameTextField;

    @FXML
    private Button submitButton;

    @FXML
    private Spinner<Integer> numOfThreadsSpinner;

     private OkHttpClient client;
    private Stage mStage;


    @FXML
    public void initialize() {
        client =  new OkHttpClient.Builder()
                .cookieJar(new SimpleCookieManager())
                .build();

        numOfThreadsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,100,1,1));
    }

    @FXML
    void submitButtonAction(ActionEvent event) throws IOException {
        boolean valid = false;
        String workerName = this.adminNameTextField.getText();
        SimpleCookieManager.addSimpleCookie(USER_NAME,workerName);
        SimpleCookieManager.addSimpleCookie(NUM_OF_THREADS, numOfThreadsSpinner.getValue());
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL +"/login").newBuilder();
        urlBuilder.addQueryParameter(USER_NAME, workerName);
        urlBuilder.addQueryParameter(DEGREE,"worker");
        urlBuilder.addQueryParameter(NUM_OF_THREADS, numOfThreadsSpinner.getValue().toString());
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
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("worker main screen.fxml"));
            Parent root = fxmlLoader.load();
            Scene mainScene = new Scene(root, 1000, 700);
            this.mStage.setScene(mainScene);
            MainScreenController mainController = fxmlLoader.getController();
            mainController.setScene(mainScene);
            mainController.setClient(client);
            mainController.myInitializer();
            mStage.show();
        }
    }

    public void setStage(Stage primaryStage) {
        this.mStage = primaryStage;
    }
}


