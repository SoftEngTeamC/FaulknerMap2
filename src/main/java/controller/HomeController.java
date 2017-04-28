package controller;


import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import model.Navigable;
import pathfinding.Path;
import textDirections.Step;
import util.ImageViewPane;
import util.MappedList;

import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.List;
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

    @FXML
    private StackPane imageContainer;


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

    private ObjectProperty<Rectangle2D> currentViewport = new SimpleObjectProperty<>();
    private ObjectProperty<Integer> currentFloor = new SimpleObjectProperty<>();

    private void initializeMap() {
        imageContainer.getChildren().add(makeFloorImageView(1, imageContainer));

        // Setup listeners for floor change
        FirstFloor_Button.setOnAction(event -> makeFloorImageView(1, imageContainer));
        SecondFloor_Button.setOnAction(event -> makeFloorImageView(2, imageContainer));
        ThirdFloor_Button.setOnAction(event -> makeFloorImageView(3, imageContainer));
        FourthFloor_Button.setOnAction(event -> makeFloorImageView(4, imageContainer));
        FifthFloor_Button.setOnAction(event -> makeFloorImageView(5, imageContainer));
        SixthFloor_Button.setOnAction(event -> makeFloorImageView(6, imageContainer));
        SeventhFloor_Button.setOnAction(event -> makeFloorImageView(7, imageContainer));
    }

    private Canvas makeDrawPane(int floor, StackPane container) {
        Image floorImage = ImageProvider.getImage(images.get(floor - 1));
        Canvas canvas = new Canvas(floorImage.getWidth(), floorImage.getHeight());

    }

    private Affine canvasTransformFromViewport(Rectangle2D viewport, double width, double height) {
        return new Affine(1, 0, 0, viewport.getMinX(),
                0, 1, 0, viewport.getMinY(),
                0, 0, 1, 0);
    }

    static void clipChildren(Region region, double arc) {

        final Rectangle outputClip = new Rectangle();
        outputClip.setArcWidth(arc);
        outputClip.setArcHeight(arc);
        region.setClip(outputClip);

        region.layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
            outputClip.setWidth(newValue.getWidth());
            outputClip.setHeight(newValue.getHeight());
        });
    }

    private List<String> images = new LinkedList<>(Arrays.asList(
            "images/1_thefirstfloor.png",
            "images/2_thesecondfloor.png",
            "images/3_thethirdfloor.png",
            "images/4_thefourthfloor.png",
            "images/5_thefifthfloor.png",
            "images/6_thesixthfloor.png",
            "images/7_theseventhfloor.png"));

    private static final int MIN_PIXELS = 400;
    private ImageViewPane makeFloorImageView(int floor, StackPane container) {
        currentFloor.set(floor);

        Image floorImage = ImageProvider.getImage(images.get(floor - 1));
        ImageView floorView = new ImageView(floorImage);
        floorView.setPreserveRatio(true);
        resetFloorView(floorView);

        ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

        floorView.setOnMousePressed(e -> {
            // Record the location we first click for drag
            Point2D mousePress = imageViewToImageCoordinate(floorView, new Point2D(e.getX(), e.getY()));
            mouseDown.set(mousePress);
        });

        floorView.setOnMouseDragged(e -> {
            Point2D dragPoint = imageViewToImageCoordinate(floorView, new Point2D(e.getX(), e.getY()));
            shift(floorView, dragPoint.subtract(mouseDown.get()));
            mouseDown.set(imageViewToImageCoordinate(floorView, new Point2D(e.getX(), e.getY())));
        });

        floorView.setOnScroll(e -> {
            Rectangle2D viewport = floorView.getViewport();
            double width = floorImage.getWidth();
            double height = floorImage.getHeight();
            double delta = -e.getDeltaY();
            double scale = clamp(Math.pow(1.01, delta),
                    // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
                    Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),
                    // don't scale so that we're bigger than image dimensions
                    Math.max(width / viewport.getWidth(), height / viewport.getHeight()));

            Point2D mouse = imageViewToImageCoordinate(floorView, new Point2D(e.getX(), e.getY()));
            double newWidth = viewport.getWidth() * scale;
            double newHeight = viewport.getHeight() * scale;
            // To keep the visual point under the mouse from moving, we need
            // (x - newViewportMinX) / (x - currentViewportMinX) = scale
            // where x is the mouse X coordinate in the image

            // solving this for newViewportMinX gives

            // newViewportMinX = x - (x - currentViewportMinX) * scale

            // we then clamp this value so the image never scrolls out
            // of the floorView
            double newMinX = clamp(
                    mouse.getX() - scale*(mouse.getX() - viewport.getMinX()),
                    0,
                    width - newWidth);
            double newMinY = clamp(
                    mouse.getY() - scale*(mouse.getY() - viewport.getMinY()),
                    0,
                    height - newHeight
            );

            setViewport(floorView, new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
        });

        floorView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) resetFloorView(floorView);
        });

        // Wrap it in a ImageViewPane so it scales correctly
        ImageViewPane floorViewPane = new ImageViewPane(floorView);

        floorViewPane.prefWidthProperty().bind(container.widthProperty());
        floorViewPane.prefHeightProperty().bind(container.heightProperty());

        return floorViewPane;
    }

    private void setViewport(ImageView floorView, Rectangle2D viewport) {
        currentViewport.set(viewport);
        floorView.setViewport(viewport);
    }

    private void resetFloorView(ImageView floorView) {
        double width = floorView.getImage().getWidth();
        double height = floorView.getImage().getHeight();
        if (currentViewport.get() == null) {
            setViewport(floorView, new Rectangle2D(0, 0, width, height));
        } else {
            setViewport(floorView, currentViewport.get());
        }
    }

    private void shift(ImageView floorView, Point2D delta) {
        Rectangle2D viewport = floorView.getViewport();

        double width = floorView.getImage().getWidth();
        double height = floorView.getImage().getHeight();

        double maxX = width - viewport.getWidth();
        double maxY = height - viewport.getHeight();

        double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
        double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);

        setViewport(floorView, new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
    }

    private double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    private Point2D imageViewToImageCoordinate(ImageView floorView, Point2D floorViewCoordinates) {
        // Transform mouse coordinates on the floorView to the pixel coordinates of the image.
        double xProp = floorViewCoordinates.getX() / floorView.getBoundsInLocal().getWidth();
        double yProp = floorViewCoordinates.getY() / floorView.getBoundsInLocal().getHeight();

        Rectangle2D viewport = floorView.getViewport();
        return new Point2D(
                viewport.getMinX() + xProp*viewport.getWidth(),
                viewport.getMinY() + yProp*viewport.getHeight()
        );
    }


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
