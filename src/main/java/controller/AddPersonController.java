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
    private Button logoutBtn;
    @FXML
    private Button addPersonBtn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField idField;
    @FXML
    private Text warningText;
    @FXML
    private TextField titleField;
    @FXML
    private Text successField;


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

    /**
     * Handler for the logout button. Switches back to the main screen.
     */
    public void logoutBtnPressed() throws java.io.IOException{

        // switch screens to main
        switchScreen("view/Main.fxml", "Main screen", logoutBtn);

    }

    /**
     * @author Paul
     * handler for the add person button. Adds to the hospital professional database if possible.
     */
    public void setAddPersonBtnPressed() {


    }


}
