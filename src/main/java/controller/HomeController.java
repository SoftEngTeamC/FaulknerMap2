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
import javafx.scene.paint.Color;
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
    private HBox Map_HBox;


    //Content inside ScrollPane
    @FXML
    private ImageView SnapToHome_ImageView;
    @FXML
    private Slider Map_Slider;
    @FXML
    private VBox FloorButtons_VBox;
    @FXML
    private Button FirstFloor_Button;
    @FXML
    private Button SecondFloor_Button;
    @FXML
    private Button ThirdFloor_Button;
    @FXML
    private Button FourthFloor_Button;
    @FXML
    private Button FifthFloor_Button;
    @FXML
    private Button SixthFloor_Button;
    @FXML
    private Button SeventhFloor_Button;

    ImageView MapImageView = new ImageView();
    Group MapGroup = new Group();
    //---------

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
        InitializeFloorButtons();
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
    }

    //------------------------------------MAP FUNCTIONS----------------------------------------
    private void InitializeMap(){
        Map_ScrollPane.prefWidthProperty().bind(Map_AnchorPane.widthProperty());
        Map_ScrollPane.prefHeightProperty().bind(Map_AnchorPane.heightProperty());
        MapImageView.setPreserveRatio(true);

        Image MapPic = ImageProvider.getImage("images/1_thefirstfloor.png");
        MapImageView.setImage(MapPic);
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

    private void MakeCircleInGroup (model.Node N){
        //This function trusts that it is only being called to build circles on the displayed floor
        double x = N.getLocation().getX();
        double y = N.getLocation().getY();

        ImageView Map1 = (ImageView) MapGroup.getChildren().get(0);
        // initial size of image and the image ratio
        double ImgW = Map1.getImage().getWidth();
        double ImgH = Map1.getImage().getHeight();
        double ImgR = ImgH / ImgW;

        Circle circle = new Circle();
        //These bind the center positions relative to the width property of the image
        //the new center is calculated using the initial ratios
        circle.centerXProperty().bind(Map1.fitWidthProperty().multiply(x / ImgW));
        circle.centerYProperty().bind(Map1.fitWidthProperty().multiply(ImgR).multiply(y / ImgH));
        circle.radiusProperty().bind(Map1.fitWidthProperty().multiply(10 / ImgW));
        circle.fillProperty().setValue(Color.RED);

        circle.setId(N.getId().toString());
        MapGroup.getChildren().addAll(circle);
    }

    private void InitializeFloorButtons(){
        FirstFloor_Button.setOnMouseClicked(e -> {
            MapImageView.setImage(ImageProvider.getImage("images/1_thefirstfloor.png"));ClearMapGroup();});
        SecondFloor_Button.setOnMouseClicked(e -> {
            MapImageView.setImage(ImageProvider.getImage("images/2_thesecondfloor.png"));ClearMapGroup();});
        ThirdFloor_Button.setOnMouseClicked(e -> {
            MapImageView.setImage(ImageProvider.getImage("images/3_thethirdfloor.png"));ClearMapGroup();});
        FourthFloor_Button.setOnMouseClicked(e -> {
            MapImageView.setImage(ImageProvider.getImage("images/4_thefourthfloor.png"));ClearMapGroup();});
        FifthFloor_Button.setOnMouseClicked(e -> {
            MapImageView.setImage(ImageProvider.getImage("images/5_thefifthfloor.png"));ClearMapGroup();});
        SixthFloor_Button.setOnMouseClicked(e -> {
            MapImageView.setImage(ImageProvider.getImage("images/6_thesixthfloor.png"));ClearMapGroup();});
        SeventhFloor_Button.setOnMouseClicked(e -> {
            MapImageView.setImage(ImageProvider.getImage("images/2_theseventhfloor.png"));ClearMapGroup();});
    }

    //--------------------------------------------------------------------------------------------------



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