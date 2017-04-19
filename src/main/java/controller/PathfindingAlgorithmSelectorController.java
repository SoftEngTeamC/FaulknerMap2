/**
 * @author paul
 * controller for pathfinding algorithm selector
 */
package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

public class PathfindingAlgorithmSelectorController  extends Controller{

    @FXML
    private Button logoutBtn;

    @FXML
    private Button backBtn;

    @FXML
    private RadioButton bfsRadioButton;

    @FXML
    private RadioButton dfsRadioButton;

    @FXML
    private RadioButton aRadioButton;

    @FXML
    private Button submitBtn;

    @FXML
    private Text successText;


    public void initialize(){

        // initialize the right checkbox to be selected


        // initialize the success text to be invisible
        successText.setVisible(false);

        //ensure only one dude is selected
        ToggleGroup group = new ToggleGroup();
    }

    @FXML
    /**
     * @author paul
     *
     */
    public void submitBtnAction(){
        // select from the backend what happens

        //indicate success
        successText.setVisible(true);

    }


    @FXML
    void backBtnAction(ActionEvent event) throws Exception{
        switchScreen("view/AdminToolMenu.fxml", "Admin tool menu", backBtn);
    }

    @FXML
    void logoutBtnAction(ActionEvent event) throws Exception{
        switchScreen("view/Main.fxml", "Main menu", logoutBtn);
    }

}
