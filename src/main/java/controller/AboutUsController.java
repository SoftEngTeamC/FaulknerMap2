package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class AboutUsController extends Controller{
    @FXML
    public AnchorPane Parent;
    @FXML
    private Button MeetTheTeamButton;

    @FXML
    private Button backbtn;

    private Stage stage;

    @FXML
    public void OpenMeetTheGroup() {
        stage = (Stage) MeetTheTeamButton.getScene().getWindow();
        switchScreen("view/MeetTheGroup.fxml", "Meet the Group", stage);
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