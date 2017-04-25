package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Navigable;
import model.Node;
import textDirections.Step;
import util.MappedList;

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
    private MappedList<javafx.scene.Node, Node> destinationNodes = new MappedList<>(destinations, HomeController::destinationNode);

    private TextField searchBox = new TextField();

    @FXML
    void initialize() {
        directoryView.setPlaceholder(new Label("No matches :("));
        // Only allow one destination to be selected at a time
        directoryView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        directoryView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                destinations.add(newValue.getNode());
                showDirections();
            }
        });

        showSearch();
        //TODO: Populate the searchBox with hot spots

        searchBox.textProperty().addListener((observable, oldValue, query) -> {
            // TODO: Populate searchResults from the query
        });

        ImageView Map = new ImageView();
//        Image MapPic =

        Group MapGroup = new Group();

    }

    private void showSearch() {
        Searching_VBox.getChildren().clear();
        Searching_VBox.getChildren().add(searchBox);
        Searching_VBox.getChildren().add(directoryView);
    }

    private void showDirections() {
        Searching_VBox.getChildren().clear();
        Searching_VBox.getChildren().addAll(destinationNodes);
        Searching_VBox.getChildren().add(stepsView);
    }

    private static javafx.scene.Node destinationNode(Node node) {
        HBox hbox = new HBox();
        TextField name = new TextField();
        name.setText(node.getName());
        Button deleteButton = new Button();
        deleteButton.setText("X");
        return hbox;
    }

}
