package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 * Created by Guillermo on 4/17/17.
 */
public class MeetTheGroupController extends Controller {
    @FXML
    private Button backbtn;

    @FXML
    private AnchorPane Parent;

    @FXML
    public void back() {
        switchScreen("view/AboutUs.fxml", "About Us", backbtn);
    }

    @FXML
    public void initialize(){
        startIdleListener(Parent, backbtn);
    }
}
