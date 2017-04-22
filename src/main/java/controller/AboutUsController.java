package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;


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
          switchScreen("view/Main.fxml", "Main", backbtn);
      }
}