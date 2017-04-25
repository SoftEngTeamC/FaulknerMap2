package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import pathfinding.MapNode;
import model.Node;
import pathfinding.Path;
import model.Navigable;
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
    private AnchorPane Map_AnchorPane;

    @FXML
    private ScrollPane Map_ScrollPane;

    @FXML
    private Slider Map_Slider;

    @FXML
    private ImageView SnapToHome_ImageView;

    @FXML
    private HBox Map_HBox;

    ImageView MapImageView = new ImageView();

    private ObservableList<Navigable> searchResults = FXCollections.observableArrayList();
    private ListView<Navigable> directoryView = new ListView<>(searchResults);

    private ObservableList<Step> steps = FXCollections.observableArrayList();
    private ListView<Step> stepsView = new ListView<>(steps);

    private ObservableList<Node> destinations = FXCollections.observableArrayList();
    private MappedList<javafx.scene.Node, Node> destinationNodes = new MappedList<>(destinations, HomeController::destinationNode);

    private TextField searchBox = new TextField();

    @FXML
    void initialize() {
        InitializeMap();

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
    }

    private void InitializeMap(){
        Map_ScrollPane.prefWidthProperty().bind(Map_AnchorPane.widthProperty());
        Map_ScrollPane.prefHeightProperty().bind(Map_AnchorPane.heightProperty());
        MapImageView.setPreserveRatio(true);

        Image MapPic = ImageProvider.getImage("images/1_thefirstfloor.png");
        MapImageView.setImage(MapPic);
        Group MapGroup = new Group();
        MapGroup.getChildren().add(MapImageView);
        Map_ScrollPane.setContent(MapGroup);
        Map_ScrollPane.setPannable(true);
        Map_ScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Map_ScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Map_Slider.minProperty().bind(Map_Split.widthProperty());
        Map_Slider.setMax(MapPic.getWidth());
        MapImageView.fitWidthProperty().bind(Map_Slider.valueProperty());
    }

    private void BuildMapGroup(Image map, Path path){
        MapImageView.setImage(map);
        Map_Slider.setMax(map.getWidth());

        if (path.numNodes() < 1) {
            System.err.println("Can't display map because there is no path.");
            return;
        }

        for (MapNode node : path) ShowNodesEdgesHelper.MakeCircle(node.getModelNode());
        path.edges().stream().map(ShowNodesEdgesHelper::MakeLine);
        //HideTabs(path);
    }

    private void ClearMapGroup(){
        Group group1 = (Group) Map_ScrollPane.getContent();
        group1.getChildren().remove(1, group1.getChildren().size());
    }

    private Circle MakeCircle (model.Node N){
        Circle circle = new Circle();
        return circle;
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
