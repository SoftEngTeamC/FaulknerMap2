package controller;

import Memento.LoginStatusMemento;
import Singleton.LoginStatusSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class AdminToolController extends Controller{

    @FXML
    private Button logoutBtn;
    @FXML
    private Button editDirectoryBtn;
    @FXML
    private Button editHoursBtn;
    @FXML
    private Button mapEditorBtn;
    @FXML
    private VBox AdminTool_VBox;
    @FXML
    private AnchorPane AdminTool_AnchorPane;
    @FXML
    private Button algorithmSelectorBtn;

    private Stage stage;

    public void initialize(){
        startIdleListener(AdminTool_AnchorPane, algorithmSelectorBtn);
    }
    private void setStage(){
        stage = (Stage)logoutBtn.getScene().getWindow();
    }

    @FXML
    public void editMap() {
        setStage();
        switchScreen("view/MapEditor.fxml", "Map Editor", stage);
    }
    @FXML
    public void editHours() {
        setStage();
        switchScreen("view/HoursEditorScreen.fxml", "Directory Editor", stage);
    }
    @FXML
    public void editDirectory() {
        setStage();
        switchScreen("view/DirectoryEditor.fxml", "Directory Editor", stage);
    }

    @FXML
    public void logout() {
        setStage();
        switchToMainScreen(logoutBtn);
        LoginStatusSingleton.getInstance().addMemento(new LoginStatusMemento(false));
    }

    @FXML
    public void algorithmSelectorBtnAction() {
        setStage();
        switchScreen("view/PathfindingChooseScreen.fxml", "Algorithm Selector", stage);
    }

    /**
     * Goes back to main screen while still logged in.
     */
    public void backBtnAction(){
        switchToMainScreen(editDirectoryBtn);
    }

    public void pathFinding(){
        setStage();
        switchScreen("view/EmployeePathfinding.fxml", "Employee Pathfinding", stage);
    }

}
