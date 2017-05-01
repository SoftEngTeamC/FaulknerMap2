package controller;

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
        AdminTool_VBox.prefWidthProperty().bind(AdminTool_AnchorPane.widthProperty());
        editDirectoryBtn.prefWidthProperty().bind(AdminTool_AnchorPane.widthProperty().multiply(0.3));
        mapEditorBtn.prefWidthProperty().bind(editDirectoryBtn.widthProperty());
        editHoursBtn.prefWidthProperty().bind(editDirectoryBtn.widthProperty());
        algorithmSelectorBtn.prefWidthProperty().bind(editDirectoryBtn.widthProperty());
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
    }

    @FXML
    public void algorithmSelectorBtnAction() {
        setStage();
        switchScreen("view/PathfindingChooseScreen.fxml", "Algorithm Selector", stage);
    }

}
