package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import service.HoursService;

import java.net.URL;
import java.util.*;

public class EmployeePathfindingController extends HomeController implements Initializable {
    @FXML
    private VBox Main_VBox;

    @FXML
    private ImageView Logo_ImageView;

    @FXML
    private Button backButton;

    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.setEmployee(true);
        super.clearFloorArray();
        pathfinding.Map map = new pathfinding.Map(nodeService.getAllNodes(), false);

        super.initializeMap();
        super.initializeDirectory(map);
        super.MakeGetDirectionsButton();
        Logo_ImageView.setImage(ImageProvider.getImage("images/logo.png"));
        Logo_ImageView.setPreserveRatio(true);
        Logo_ImageView.fitHeightProperty().bind(Main_VBox.heightProperty().multiply(0.1));
        HoursService hs = new HoursService();
        super.timeofdaywarning(hs.find(1L).getVisitingHoursMorningStart(), hs.find(1L).getVisitingHoursEveningEnd(), date);
    }

    private void setStage() {
        stage = (Stage) backButton.getScene().getWindow();
    }

    public void backScreen() {
        setStage();
        switchScreen("view/AdminToolMenu.fxml", "Admin Tool Menu", stage);
    }

}
