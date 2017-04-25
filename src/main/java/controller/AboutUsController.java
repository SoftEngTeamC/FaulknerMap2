package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class AboutUsController extends Controller{

    @FXML
    private Button MeetTheTeamButton;

    @FXML
    private Button backbtn;

    @FXML
    public void OpenMeetTheGroup() {
        switchScreen("view/MeetTheGroup.fxml", "Meet the Group", MeetTheTeamButton);
    }

    @FXML
    public void back() {
        switchToMainScreen(MeetTheTeamButton);
    }
}