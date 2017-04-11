package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Created by jack on 3/30/17.
 */
public class HoursEditorController extends Controller{

    public void initialize(){}
    @FXML
    private Button logoutBtn;
    @FXML
    private Button backBtn;

    @FXML
    public void back() throws Exception {
        switchScreen("view/AdminToolMenu.fxml", "Directory Editor", backBtn);
    }

    @FXML
    public void logout() throws Exception {
        switchScreen("view/Main.fxml", "Main", logoutBtn);
    }
    @FXML
    public void update(){}


    public void saveHours(){}
}
