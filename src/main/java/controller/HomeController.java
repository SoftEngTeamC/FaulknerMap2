package controller;


import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Navigable;
import pathfinding.MapNode;
import pathfinding.Path;
import textDirections.Step;
import util.ImageViewPane;
import util.MappedList;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

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

    @FXML
    private Pane mapContainer;

    private ImageViewPane mapView;

    //------------------------
    private ObservableList<Navigable> searchResults = FXCollections.observableArrayList();
    private ListView<Navigable> searchResultsView = new ListView<>(searchResults);

    private ObservableList<Step> steps = FXCollections.observableArrayList();
    private ListView<Step> stepsView = new ListView<>(steps);

    private ObservableList<Navigable> destinations = FXCollections.observableArrayList();
    private MappedList<javafx.scene.Node, Navigable> destinationNodes = new MappedList<>(destinations, this::makeDestinationView);
    private Map<Navigable, HBox> destinationNodeCache = new HashMap<>();

    private TextField searchBox = new TextField();

    private Button addDestinationButton = new Button();

    private TextField currentSearchField;
    private int currentDestinationIndex = -1;

    private pathfinding.Map map = new pathfinding.Map(nodeService.getAllNodes());
    private ObservableList<Path> paths = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        initializeMap();
        initializeDirectory();
    }

    //------------------------------------MAP FUNCTIONS----------------------------------------

    private void initializeMap() {
        ImageViewPane mapView = new ImageViewPane(ImageProvider.getImage(images.get(0)), nodeService);
        mapView.prefHeightProperty().bind(mapContainer.heightProperty());
        mapView.prefWidthProperty().bind(mapContainer.widthProperty());
        mapContainer.getChildren().add(mapView);
        mapView.toBack();
        this.mapView = mapView;
    }

    private List<String> images = new LinkedList<>(Arrays.asList(
            "images/1_thefirstfloor.png",
            "images/2_thesecondfloor.png",
            "images/3_thethirdfloor.png",
            "images/4_thefourthfloor.png",
            "images/5_thefifthfloor.png",
            "images/6_thesixthfloor.png",
            "images/7_theseventhfloor.png"));

    //--------------------------------------------------------------------------------------------------
    private void initializeDirectory() {
        searchResultsView.setPlaceholder(new Label("No matches :("));
        // Only allow one destination to be selected at a time
        searchResultsView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        searchResultsView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedDestination) -> {
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
            search("");
            showAddDestination();
        });

        search(""); // TODO: Populate the searchBox with hot spots

        // Recalculate the path
        destinations.addListener((ListChangeListener<Navigable>) c -> {
            while (c.next()) {} // Apply all the changes
            ObservableList<? extends Navigable> destinations = c.getList();
            paths.clear();
            if (c.getList().size() >= 2) {
                Navigable start = destinations.get(0);
                for (Navigable dest : destinations.subList(1, destinations.size())) {
                    paths.add(map.shortestPath(start.getNode(), dest.getNode()));
                    start = dest;
                }
            }
            for (Navigable dest : destinations) {
                mapView.drawNode(new MapNode(dest.getNode()));
            }
        });

        Searching_VBox = makeVBox();
        showSearch();
    }

    private void search(String query) {
        List<? extends Navigable> results = professionalService.search(query);
        searchResults.clear();
        searchResults.addAll(results);
        searchResults.removeIf(result -> destinations.stream().map(Object::toString).collect(Collectors.toList()).contains(result.toString()));
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
        Searching_VBox.getChildren().add(searchResultsView);
        searchResultsView.getSelectionModel().clearSelection();
        setCurrentSearchField(searchBox);
    }

    private void showEditDestination(TextField field) {
        Searching_VBox = makeVBox();
        Searching_VBox.getChildren().addAll(destinationNodes);
        Searching_VBox.getChildren().add(searchResultsView);
        setCurrentSearchField(field);
    }

    private void showAddDestination() {
        Searching_VBox = makeVBox();
        Searching_VBox.getChildren().addAll(destinationNodes);
        Searching_VBox.getChildren().add(searchBox);
        Searching_VBox.getChildren().add(searchResultsView);
        setCurrentSearchField(searchBox);
    }


    private void setCurrentSearchField(TextField field) {
        currentSearchField = field;
        currentSearchField.textProperty()
                .addListener((observable, oldValue, query) -> search(query.toLowerCase()));
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
        field.setEditable(false);
        field.setText(location.toString() + " - " + location.getNode().getName());
        field.setOnMouseClicked(e -> {
            if (currentSearchField == null) {
                field.setEditable(true);
                field.setText("");
                search("");
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
            destinationNodeCache.remove(location);
            currentDestinationIndex = -1;
            if (destinations.isEmpty()) showSearch();
            else showDirections();
        });
        deleteButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        deleteButton.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.REMOVE));
        return deleteButton;
    }
}
