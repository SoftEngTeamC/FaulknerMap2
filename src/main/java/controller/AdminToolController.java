package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;



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

    public void initialize(){
        AdminTool_VBox.prefWidthProperty().bind(AdminTool_AnchorPane.widthProperty());
        editDirectoryBtn.prefWidthProperty().bind(AdminTool_AnchorPane.widthProperty().multiply(0.3));
        mapEditorBtn.prefWidthProperty().bind(editDirectoryBtn.widthProperty());
        editHoursBtn.prefWidthProperty().bind(editDirectoryBtn.widthProperty());
        algorithmSelectorBtn.prefWidthProperty().bind(editDirectoryBtn.widthProperty());
    }

    @FXML
    public void editMap() throws Exception{
        switchScreen("view/MapEditor.fxml", "Map Editor", mapEditorBtn);
    }
    @FXML
    public void editHours()throws Exception{
        switchScreen("view/HoursEditorScreen.fxml", "Directory Editor", editHoursBtn);
    }
    @FXML
    public void editDirectory() throws Exception{
        switchScreen("view/DirectoryEditor.fxml", "Directory Editor", editDirectoryBtn);
    }

    @FXML
    public void logout() throws Exception{
        switchToMainScreen(logoutBtn);
    }

    @FXML
    public void algorithmSelectorBtnAction() throws Exception{
        switchScreen("view/PathfindingChooseScreen.fxml", "Algorithm Selector", algorithmSelectorBtn);
    }

}
