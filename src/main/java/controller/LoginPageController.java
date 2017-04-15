package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
/**
 * Created by Guillermo on 4/15/17.
 */
public class LoginPageController extends Controller{
    @FXML
    private Button backBtn;
    @FXML
    private Button loginBtn;
    @FXML
    public void back() throws Exception {
        switchScreen("view/Main.fxml", "Faulkner Kiosk", backBtn);
    }
    public void login() throws Exception {
        switchScreen("view/AdminToolMenu.fxml", "AdminToolMenu", loginBtn);
    }
}
