package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HomeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private SplitPane Home_MainSplit;

    @FXML
    private VBox Home_VBox;

    @FXML
    private SplitPane Map_Split;

    @FXML
    private ScrollPane Map_ScrollPane;

    @FXML
    private Slider Map_Slider;

    @FXML
    private ImageView SnapToHome_ImageView;

    @FXML
    private HBox Map_HBox;

    @FXML
    void initialize() {
        
    }
}
