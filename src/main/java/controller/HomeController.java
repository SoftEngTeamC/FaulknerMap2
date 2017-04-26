package controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Navigable;
import pathfinding.MapNode;
import textDirections.Step;
import util.MappedList;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class HomeController extends Controller {

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

    private ImageView MapImageView = new ImageView();
    private Group MapGroup = new Group();
    //---------

    private ObservableList<Navigable> searchResults = FXCollections.observableArrayList();
    private ListView<Navigable> directoryView = new ListView<>(searchResults);

    private ObservableList<Step> steps = FXCollections.observableArrayList();
    private ListView<Step> stepsView = new ListView<>(steps);

    private ObservableList<Navigable> destinations = FXCollections.observableArrayList();
    private MappedList<javafx.scene.Node, Navigable> destinationNodes = new MappedList<>(destinations, this::makeDestinationView);
    private Map<Navigable, HBox> destinationNodeCache = new HashMap<>();

    private TextField searchBox = new TextField();

    private Button addDestinationButton = new Button();

    private TextField currentSearchField;
    private int currentDestinationIndex = -1;

    @FXML
    void initialize() {
        InitializeMap();
        InitializeFloorButtons();
        initializeDirectory();
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
        //Set the scroll min and max values to the pixel size of the Image View / scrollPane
        Map_ScrollPane.hmaxProperty().bind(MapImageView.fitWidthProperty().subtract(Map_ScrollPane.widthProperty()));
        Map_ScrollPane.setHmin(0);
        Map_ScrollPane.vmaxProperty().bind(MapImageView.fitHeightProperty().subtract(Map_ScrollPane.heightProperty()));
        Map_ScrollPane.setVmin(0);
        Map_ScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Map_ScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Map_Slider.minProperty().bind(Map_Split.widthProperty());
        Map_Slider.setMax(MapPic.getWidth());
        MapImageView.fitWidthProperty().bind(Map_Slider.valueProperty());
    }

    private void PanToPoint(double X, Double Y){
        double Width = MapImageView.getImage().getWidth();
        double Height = MapImageView.getImage().getHeight();
        double currWidth = MapImageView.getFitWidth();
        double currHeight = MapImageView.getFitHeight();
        double Xprime = X*(currWidth/Width);
        double Yprime = Y*(currHeight/Height);
        Map_ScrollPane.setHvalue(Xprime - Map_ScrollPane.getWidth());
        Map_ScrollPane.setVvalue(Yprime - Map_ScrollPane.getHeight());
    }

//    private void BuildMapGroup(Image map, Path path){
//        MapImageView.setImage(map);
//        Map_Slider.setMax(map.getWidth());
//
//        if (path.numNodes() < 1) {
//            System.err.println("Can't display map because there is no path.");
//            return;
//        }
//        for (MapNode node : path) MakeCircleInGroup(node);
//        path.edges().stream().map(ShowNodesEdgesHelper::MakeLine);
//    }

    private void ClearMapGroup(){
        Group group1 = (Group) Map_ScrollPane.getContent();
        group1.getChildren().remove(1, group1.getChildren().size());
    }

    private void MakeCircleInGroup (MapNode N){
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
    private void initializeDirectory() {
        directoryView.setPlaceholder(new Label("No matches :("));
        // Only allow one destination to be selected at a time
        directoryView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        directoryView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedDestination) -> {
            if (selectedDestination != null) {
                if (currentDestinationIndex >= 0) {
                    destinations.set(currentDestinationIndex, selectedDestination);
                    currentDestinationIndex = -1;
                } else {
                    destinations.add(selectedDestination);
                }
                showDirections();
            }
        });

        addDestinationButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        addDestinationButton.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.PLUS));
        addDestinationButton.setOnAction(e -> {
            searchBox.setText("");
            showAddDestination();
        });

        setSearchResults(professionalService.getAllProfessionals()); // TODO: Populate the searchBox with hot spots

        Searching_VBox = makeVBox();
        showSearch();
    }

    private void setSearchResults(List<? extends Navigable> results) {
        searchResults.clear();
        searchResults.addAll(results);
        searchResults.removeAll(destinations); // Don't allow loops
    }

    private VBox makeVBox() {
        VBox newVBox = new VBox();
        newVBox.prefWidthProperty().bind(Search_ScrollPane.widthProperty());
        Search_ScrollPane.setContent(newVBox);
        return newVBox;
    }

    // --------------- View State Changers --------------- //
    private void showDirections() {
        Searching_VBox = makeVBox();
        Searching_VBox.getChildren().addAll(destinationNodes);
        Searching_VBox.getChildren().add(addDestinationButton);
        Searching_VBox.getChildren().add(stepsView);
        currentSearchField = null;
    }

    private void showSearch() {
        Searching_VBox = makeVBox();
        Searching_VBox.getChildren().add(searchBox);
        Searching_VBox.getChildren().add(directoryView);
        setCurrentSearchField(searchBox);
    }

    private void showEditDestination(TextField field) {
        Searching_VBox = makeVBox();
        Searching_VBox.getChildren().addAll(destinationNodes);
        Searching_VBox.getChildren().add(directoryView);
        setCurrentSearchField(field);
    }

    private void showAddDestination() {
        Searching_VBox = makeVBox();
        Searching_VBox.getChildren().addAll(destinationNodes);
        Searching_VBox.getChildren().add(searchBox);
        Searching_VBox.getChildren().add(directoryView);
        setCurrentSearchField(searchBox);
    }

    private void setCurrentSearchField(TextField field) {
        currentSearchField = field;
        currentSearchField.textProperty().addListener((observable, oldValue, query) -> {
            setSearchResults(professionalService.search(query.toLowerCase()));
        });
        currentSearchField.requestFocus();
    }

    // ----------- Destination View Factories ------------ //
    private HBox makeDestinationView(Navigable location) {
        if (destinationNodeCache.containsKey(location)) return destinationNodeCache.get(location);
        HBox destinationView = makeDestinationNodeElement(location);
        destinationNodeCache.put(location, destinationView);
        return destinationView;
    }

    private HBox makeDestinationNodeElement(Navigable location) {
        HBox container = new HBox();
        container.getChildren().add(makeDestinationField(location));
        container.getChildren().add(makeDeleteButton(location));
        return container;
    }

    private TextField makeDestinationField(Navigable location) {
        TextField field = new TextField();
        field.setText(location.toString() + " - " + location.getNode().getName());
        field.setOnMouseClicked(e -> {
            if (currentSearchField == null) {
                field.setText("");
                showEditDestination(field);
                currentDestinationIndex = destinations.indexOf(location);
            }
        });
        return field;
    }

    private Button makeDeleteButton(Navigable location) {
        Button deleteButton = new Button();
        deleteButton.setOnAction(e -> {
            destinations.remove(location);
            showDirections();
            if (destinations.isEmpty()) { // Check if we are the only destination
                currentDestinationIndex = -1;
                showSearch();
            }
        });
        deleteButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        deleteButton.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.REMOVE));
        return deleteButton;
    }
}
