package controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class AddPersonController extends Controller{

    @FXML
    private Button backBtn;
    @FXML
    private TextField nameField;


    @FXML
    public void initialize() {

    }

    /**
     * handler for the back button being pressed. Brings it back to the directory editor screen.
     */
    public void backBtnPressed() throws java.io.IOException{

        // switch screens to directory editor
        switchScreen("view/DirectoryEditor.fxml", "Directory editor", backBtn);
    }
}
