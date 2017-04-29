package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;


public class AboutUsController extends Controller{
    @FXML
    public AnchorPane Parent;
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

    @FXML
    public void initialize(){
        startIdleListener(Parent, MeetTheTeamButton);
    }
}