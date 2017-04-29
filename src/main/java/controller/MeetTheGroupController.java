package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Created by Guillermo on 4/17/17.
 */
public class MeetTheGroupController extends Controller {
    @FXML
    private Button backbtn;

    @FXML
    private AnchorPane Parent;

    private Stage stage;

    @FXML
    public void back() {
        stage = (Stage) backbtn.getScene().getWindow();
        switchScreen("view/AboutUs.fxml", "About Us", stage);
    }

    @FXML
    public void initialize(){
        startIdleListener(Parent, backbtn);
    }
}
