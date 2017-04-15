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
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Text displayerror;
    String UserName= "Admin";
    String Password= "Admin";
    int attempts =0;

    @FXML
    public void back() throws Exception {
        switchScreen("view/Main.fxml", "Faulkner Kiosk", backBtn);
    }
    public void login() throws Exception {
    System.out.print(attempts);
        if(username.getText().equals(UserName) && password.getText().equals(Password)){
            displayerror.setVisible(false);
            switchScreen("view/AdminToolMenu.fxml", "AdminToolMenu", loginBtn);
            System.out.println("Login Succesful");
        }

        else if (attempts>=2){
            System.out.println("switch screen");
            switchScreen("view/Main.fxml", "Faulkner Kiosk", backBtn);
        }

        else if(username.getText().trim().isEmpty() && password.getText().trim().isEmpty()){
            displayerror.setVisible(true);
            attempts++;
            System.out.println("No username or password");
        }

        else {
            System.out.println("else");
            displayerror.setVisible(true);
            attempts++;
        }
    }
}
