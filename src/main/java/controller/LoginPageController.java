package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    @FXML
    private Text displayerror1;
    @FXML
    private Text displayerror2;
    @FXML
    private Text displaysuccess;

    String UserName= "Admin";
    String Password= "Admin";
    int attempts =0;

    @FXML
    public void back() throws Exception {
        switchScreen("view/Main.fxml", "Faulkner Kiosk", backBtn);
    }

    @FXML
    public void login() throws Exception {
    System.out.print(attempts);
        attempts++;

        if(username.getText().equals(UserName) && password.getText().equals(Password)){
            displayerror.setVisible(false);
            displayerror1.setVisible(false);
            displayerror2.setVisible(false);
            displaysuccess.setVisible(true);
            switchScreen("view/AdminToolMenu.fxml", "AdminToolMenu", loginBtn);
        }

        else if (attempts>=3){
            displayerror2.setText(Integer.toString(attempts));
            switchScreen("view/Main.fxml", "Faulkner Kiosk", backBtn);
        }

        else if(username.getText().trim().isEmpty() && password.getText().trim().isEmpty()){
            displayerror.setVisible(true);
            displayerror1.setVisible(true);
            displayerror2.setText(Integer.toString(attempts));
        }

        else {
            displayerror.setVisible(true);
            displayerror1.setVisible(true);
            displayerror2.setText(Integer.toString(attempts));

        }
    }
}
