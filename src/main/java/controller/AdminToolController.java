package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;



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

    public void initialize(){
        AdminTool_VBox.prefWidthProperty().bind(AdminTool_AnchorPane.widthProperty());
        editDirectoryBtn.prefWidthProperty().bind(AdminTool_AnchorPane.widthProperty().multiply(0.3));
        mapEditorBtn.prefWidthProperty().bind(editDirectoryBtn.widthProperty());
        editHoursBtn.prefWidthProperty().bind(editDirectoryBtn.widthProperty());
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
        switchScreen("view/Main.fxml", "Directory Editor", logoutBtn);
    }

}
