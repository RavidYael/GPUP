package MainScreen.dashboardScreen;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DashboardScreenController {

    @FXML
    private TableView<?> graphsTable;

    @FXML
    private TableColumn<?, ?> graphNameColumn;

    @FXML
    private TableColumn<?, ?> graphUploadedByColumn;

    @FXML
    private TableColumn<?, ?> totalTargetsColumn;

    @FXML
    private TableColumn<?, ?> rootsColumn;

    @FXML
    private TableColumn<?, ?> leafsColumn;

    @FXML
    private TableColumn<?, ?> middlesColumn;

    @FXML
    private TableColumn<?, ?> independentColumn;

    @FXML
    private TableView<?> usersTable;

    @FXML
    private TableColumn<?, ?> userNameColumn;

    @FXML
    private TableColumn<?, ?> userRoleColumn;

    @FXML
    private TableView<?> tasksTable;

    @FXML
    private TableColumn<?, ?> taskNameColumn;

    @FXML
    private TableColumn<?, ?> taskUploadedByColumn;

}
