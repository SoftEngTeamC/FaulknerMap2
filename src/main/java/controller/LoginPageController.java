package controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;

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
    private Text displayerror;
    @FXML
    private Text displayerror1;
    @FXML
    private Text displayerror2;
    @FXML
    private Text displaysuccess;
    @FXML
    private Text displaySuccess;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Text successText;

    String UserName= "Admin";
    String Password= "Admin";
    int attempts =0;

    @FXML
    public void back() throws Exception {
        switchToMainScreen(backBtn);
    }
    @FXML
    public void initialize(){
        displayerror.setVisible(false);
        displayerror1.setVisible(false);
        displayerror2.setVisible(false);
        successText.setVisible(false);
    }

    @FXML
    public void login() throws Exception {
        attempts++;

        if(username.getText().equals(UserName) && passwordField.getText().equals(Password)){
            displayerror.setVisible(false);
            displayerror1.setVisible(false);
            displayerror2.setVisible(false);
            switchScreen("view/AdminToolMenu.fxml", "AdminToolMenu", loginBtn);
        }

        else if (attempts>=3){
            displayerror2.setText(Integer.toString(attempts));
            flashErrorMessage();
            switchToMainScreen(backBtn);
        }

        else if(username.getText().trim().isEmpty() && passwordField.getText().trim().isEmpty()){
            displayerror.setVisible(true);
            displayerror1.setVisible(true);
            displayerror2.setText(Integer.toString(attempts));
            flashErrorMessage();
        }

        else {
            displayerror.setVisible(true);
            displayerror1.setVisible(true);
            displayerror2.setText(Integer.toString(attempts));
            flashErrorMessage();

        }
    }
    private void flashSuccessMessage(){
        //THESE LINES ARE THE WINNERS! COPY PASTE EVERYWHERE! THEY WORK!
        //This is how you make a message flash on for only two and a half seconds.
        //Change the "displaySuccess" in "displaySuccess.visibleProperty()" to
        //the name of the message that you want to flash.
        displaySuccess.setVisible(true);
        final Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2500),
                new KeyValue(displaySuccess.visibleProperty(), false)));
        timeline.play();

    }

    private void flashErrorMessage(){
        displayerror.setVisible(true);
        final Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2500),
                new KeyValue(displayerror.visibleProperty(), false)));
        timeline.play();
    }
}

