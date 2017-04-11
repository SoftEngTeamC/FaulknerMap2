package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by jack on 3/30/17.
 */
public class HoursEditorController extends Controller{

    public void initialize(){}
    @FXML
    private Button logoutBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button submitbtn;
    @FXML
    private TextField morninghrs1;
    @FXML
    private TextField morninghrs2;
    @FXML
    private TextField eveninghrs1;
    @FXML
    private TextField eveninghrs2;
    @FXML
    private MenuButton ampm1;
    @FXML
    private MenuButton ampm2;
    @FXML
    private MenuButton ampm3;
    @FXML
    private MenuButton ampm4;

    @FXML
    public void back() throws Exception {
        switchScreen("view/AdminToolMenu.fxml", "Directory Editor", backBtn);
    }

    @FXML
    public void logout() throws Exception {
        switchScreen("view/Main.fxml", "Main", logoutBtn);
    }
    @FXML
    public void SubmitChanges(){

    }



}
