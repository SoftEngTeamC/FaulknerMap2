package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;


public class AboutUsController extends Controller{

    @FXML
    private Button MeetTheTeamButton;

    @FXML
    private Button backbtn;

    @FXML
    public void OpenMeetTheGroup() throws Exception {

        // goto meet the group screen
        switchScreen("view/MeetTheGroup.fxml", "Meet the Group", MeetTheTeamButton);

    }


      @FXML
      public void back() throws Exception {
        switchToMainScreen(MeetTheTeamButton);
      }
}