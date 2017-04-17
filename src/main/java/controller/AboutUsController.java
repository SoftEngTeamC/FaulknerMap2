package controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class AboutUsController extends Controller{

    @FXML
    private Button MeetTheTeamButton;

    @FXML
    private Button back;

    @FXML
    public void OpenMeetTheGroup() throws Exception {
        // goto genres screen
        System.out.println("HERE WE ARE");
        Stage stage = (Stage) MeetTheTeamButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/MeetTheGroup.fxml"));
        stage.setTitle("Meet The Group");
        stage.setScene(new Scene(root, 300, 300));
        stage.show();
    }


      @FXML
      public void back() throws Exception {
          switchScreen("view/Main.fxml", "About Us", back);
      }
}