package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Navigable;
import model.Node;
import textDirections.Step;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private SplitPane Home_MainSplit;

    @FXML
    private VBox Main_VBox;

    @FXML
    private ImageView Logo_ImageView;

    @FXML
    private ScrollPane Search_ScrollPane;

    @FXML
    private VBox Searching_VBox;

    @FXML
    private ButtonBar Options_ButtonBar;

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

    private ObservableList<Navigable> searchResults = FXCollections.observableArrayList();
    private ListView<Navigable> directoryView = new ListView<>(searchResults);

    private ObservableList<Step> steps = FXCollections.observableArrayList();
    private ListView<Step> stepsView = new ListView<>(steps);

    private ObservableList<Node> destinations = FXCollections.observableArrayList();

    private TextField searchBox = new TextField();

    @FXML
    void initialize() {
        // Only allow one destination to be selected at a time
        directoryView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        directoryView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                destinations.add(newValue.getNode());
                // TODO: change the view to show the steps and list of destinations
            }
        });

        Searching_VBox.getChildren().add(searchBox);
        Searching_VBox.getChildren().add(directoryView);
        //TODO: Populate the searchBox with hot spots
        searchBox.textProperty().addListener((observable, oldValue, query) -> {
            // TODO: Populate searchResults with the
        });
    }

}
