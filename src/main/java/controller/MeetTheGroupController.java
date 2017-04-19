package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
/**
 * Created by Guillermo on 4/17/17.
 */
public class MeetTheGroupController extends Controller {
    @FXML
    private Button backbtn;
    @FXML
    public void back() throws Exception {
        switchScreen("view/AboutUs.fxml", "About Us", backbtn);
    }
}
