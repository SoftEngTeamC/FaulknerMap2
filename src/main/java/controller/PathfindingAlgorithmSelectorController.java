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
import pathfinding.Map;
import service.AlgorithmSingleton;

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

    ToggleGroup group;

    private int selected; // 0 is A*, 1 is bfs, 2 is dfs


    public void initialize(){

        setButton(backBtn);

        //ensure only one dude is selected
        group = new ToggleGroup();

        // <TODO> enable the current algorithm

        // initialize the success text to be invisible
        successText.setVisible(false);

    }

    @FXML
    /**
     * @author paul
     *
     */
    public void submitBtnAction(){

        // init selected algorithm
        if(aRadioButton.selectedProperty().getValue()){
            selected = 0;
        } else if (bfsRadioButton.selectedProperty().getValue()){
            selected = 1;
        } else if (dfsRadioButton.selectedProperty().getValue()){
            selected = 2;
        } else selected = -1;

        // push
        switch (selected){
            case 0: // A*
                AlgorithmSingleton.getInstance().setCurrentAlgorithm(Map.algorithm.ASTAR);
                break;
            case 1: // BFS
                AlgorithmSingleton.getInstance().setCurrentAlgorithm(Map.algorithm.BFS);
                break;
            case 2: // DFS
                AlgorithmSingleton.getInstance().setCurrentAlgorithm(Map.algorithm.DFS);
                break;
        }

        //indicate success
        successText.setVisible(true);
    }


    @FXML
    void backBtnAction(ActionEvent event) {
        switchScreen("view/AdminToolMenu.fxml", "Admin tool menu", backBtn);
    }

    @FXML
    void logoutBtnAction(ActionEvent event) {
        switchToMainScreen(logoutBtn);
    }

}
