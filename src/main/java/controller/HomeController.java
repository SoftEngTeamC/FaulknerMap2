package controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import model.Edge;
import model.Hours;
import model.Navigable;
import pathfinding.MapNode;
import pathfinding.Path;
import textDirections.Step;
import util.MappedList;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import textDirections.TextualDirections.*;

import static textDirections.TextualDirections.pathSteps;

public class HomeController extends Controller implements Initializable {

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
    private AnchorPane Map_AnchorPane;

    @FXML
    private ScrollPane Map_ScrollPane;

    //Content inside ScrollPane
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
    private Button AdminToolButton;

    @FXML
    private MenuItem english_button;
    @FXML
    private MenuItem spanish_button;
    @FXML
    private MenuItem french_button;
    @FXML
    private MenuItem japanese_button;
    @FXML
    private MenuItem chinese_button;
    @FXML
    private MenuItem portuguese_button;
    @FXML
    private MenuItem italian_button;

    private ImageView MapImageView = new ImageView();
    private static Group MapGroup = new Group();
    //Center ScrollPane relative to Image Coordinates
    private double CenterX;
    private double CenterY;
    private boolean CenterLocked = false;

    //------------------------
    private ObservableList<Navigable> searchResults = FXCollections.observableArrayList();
    private ListView<Navigable> searchResultsView = new ListView<>(searchResults);

    private ObservableList<Step> steps = FXCollections.observableArrayList();
    private ListView<Step> stepsView = new ListView<>(steps);

    private ObservableList<Navigable> destinations = FXCollections.observableArrayList();
    private MappedList<javafx.scene.Node, Navigable> destinationNodes = new MappedList<>(destinations, this::makeDestinationView);
    private Map<Navigable, HBox> destinationNodeCache = new HashMap<>();

    private ObjectProperty<Navigable> selectedDestination = new SimpleObjectProperty<>();
    private TextField searchBox = new TextField();

    private Button addDestinationButton = new Button();
    private Button DirectionButton = new Button();
    private HBox addDestandDirectionButtons = new HBox();

    private TextField currentSearchField;
    private int currentDestinationIndex = -1;

    private pathfinding.Map map = new pathfinding.Map(nodeService.getAllNodes());
    private ObservableList<Path> paths = FXCollections.observableArrayList();

    private static ResourceBundle bundle;

    private IntegerProperty currFloor = new SimpleIntegerProperty(1);
    private ListProperty<Integer> FloorSpan = new SimpleListProperty<>();

    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;

        english_button.setOnAction(event -> loadView(new Locale("en", "US")));
        spanish_button.setOnAction(event -> loadView(new Locale("es", "PR")));
        french_button.setOnAction(event -> loadView(new Locale("fr", "FR")));
        japanese_button.setOnAction(event -> loadView(new Locale("jp", "JP")));
        chinese_button.setOnAction(event -> loadView(new Locale("zh", "CN")));
        portuguese_button.setOnAction(event -> loadView(new Locale("pt", "BR")));
        italian_button.setOnAction(event -> loadView(new Locale("it", "IT")));


        InitializeMap();
        InitializeFloorButtons();
        InitializeZoomListener();
        initializeDirectory();
        Logo_ImageView.setImage(ImageProvider.getImage("images/logo.png"));
        Logo_ImageView.setPreserveRatio(true);
        Logo_ImageView.fitHeightProperty().bind(Main_VBox.heightProperty().multiply(0.1));

        //Define actions on ShowTextDirections Button
        DirectionButton.setText(bundle.getString("getDirections"));
        DirectionButton.setOnAction(e -> {
            showTextDirections();
            //Update the Floor span for the New Paths
            FloorSpan.set(PathSpansFloors());
            //TODO Set current Location to first node in the path
            //current location will be used to step through the directions one node at a time
            currFloor.set(paths.get(0).getNode(0).getLocation().getFloor());
            DisplayPaths();
            //TODO Display the Path on the Map and generate Steps
        });
        //Altering the add Destination and Directions Buttons HBox to have those buttons
        addDestandDirectionButtons.getChildren().add(addDestinationButton);
        addDestandDirectionButtons.getChildren().add(DirectionButton);
    }

    private void loadView(Locale locale) {
        Stage stage = (Stage) Home_MainSplit.getScene().getWindow();
        try {
            SplitPane root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Home.fxml"),
                    ResourceBundle.getBundle("Language", locale));
            stage.setTitle("Faulkner Kiosk");
            stage.getScene().setRoot(root);
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void setStage() {
        stage = (Stage) AdminToolButton.getScene().getWindow();
    }

    //------------------------------------MAP FUNCTIONS----------------------------------------

    private void InitializeMap() {
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
        Map_Slider.minProperty().bind(Map_AnchorPane.widthProperty());
        Map_Slider.setMax(MapPic.getWidth());
        MapImageView.fitWidthProperty().bind(Map_Slider.valueProperty());
    }

    private void InitializeZoomListener() {
        //This event is triggered when the Map Slider is moved
        //It locks the center coordinate, and as it is zoomed it pans to the desired position
        Map_Slider.valueProperty().addListener((original, oldValue, newValue) -> {
            CenterLocked = true;
            System.out.println("Zooming On: " + CenterX + ", " + CenterY);
            PanToPoint(CenterX, CenterY);
        });

        //This event is triggered when the mouse is released from the slider
        Map_Slider.setOnMouseReleased(e -> CenterLocked = false);

        //These events are triggered when the scrollpane view is panned
        Map_ScrollPane.hvalueProperty().addListener(e -> {
            if (!CenterLocked) {
                Panning();
            }
        });
        Map_ScrollPane.vvalueProperty().addListener(e -> {
            if (!CenterLocked) {
                Panning();
            }
        });
    }

    //This function is meant to be called whenever the user is panning around the map
    //ie on click and drag, not on zoom, Controlled by CenterLocked Boolean
    private void Panning() {
        double Width = MapImageView.getImage().getWidth();
        double Height = MapImageView.getImage().getHeight();
        double ImageRatio = Height / Width;
        double currWidth = MapImageView.getFitWidth();
        double currHeight = currWidth * ImageRatio;
        double ScrollX = Map_ScrollPane.getHvalue();
        double ScrollY = Map_ScrollPane.getVvalue();

        double Xprime = ((ScrollX + ((Map_ScrollPane.getWidth() / currWidth) / 2)) * (currWidth - Map_ScrollPane.getWidth()));
        double Yprime = ((ScrollY + ((Map_ScrollPane.getHeight() / currHeight) / 2)) * (currHeight - Map_ScrollPane.getHeight()));

        CenterX = Xprime / (currWidth / Width);
        CenterY = Yprime / (currHeight / Height);
        System.out.println(CenterX + ", " + CenterY);
    }

    private void PanToPoint(double X, double Y) {
        System.out.println("Panning To Point");
        double Width = MapImageView.getImage().getWidth();
        double Height = MapImageView.getImage().getHeight();
        double ImageRatio = Height / Width;
        double currWidth = MapImageView.getFitWidth();
        double currHeight = currWidth * ImageRatio;
        double Xprime = X * (currWidth / Width);
        double Yprime = Y * (currHeight / Height);

        Map_ScrollPane.setHvalue((Xprime / (currWidth - Map_ScrollPane.getWidth())) - ((Map_ScrollPane.getWidth() / currWidth) / 2));
        Map_ScrollPane.setVvalue((Yprime / (currHeight - Map_ScrollPane.getHeight())) - ((Map_ScrollPane.getHeight() / currHeight) / 2));
    }

    private void ClearMapGroup() {
        Group group1 = (Group) Map_ScrollPane.getContent();
        group1.getChildren().remove(1, group1.getChildren().size());
    }

    private void MakeCircleInGroup(MapNode N) {
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

        //MapGroup.getChildren().addAll(circle);
    }

    private void MakeLineInGroup(Edge e) {
        double x1 = e.getStart().getLocation().getX();
        double y1 = e.getStart().getLocation().getY();
        double x2 = e.getEnd().getLocation().getX();
        double y2 = e.getEnd().getLocation().getY();

        ImageView Map1 = (ImageView) MapGroup.getChildren().get(0);

        double ImgW = Map1.getImage().getWidth();
        double ImgH = Map1.getImage().getHeight();
        double ImgR = ImgH / ImgW;

        Line edge = new Line();
        //the points are bound to the fit width property of the image and scaled by the initial image ratio
        edge.startXProperty().bind(Map1.fitWidthProperty().multiply((x1 / ImgW)));
        edge.startYProperty().bind(Map1.fitWidthProperty().multiply(ImgR).multiply((y1 / ImgH)));
        edge.endXProperty().bind(Map1.fitWidthProperty().multiply((x2 / ImgW)));
        edge.endYProperty().bind(Map1.fitWidthProperty().multiply(ImgR).multiply((y2 / ImgH)));

        if (e.isDisabled()) {
            edge.getStrokeDashArray().addAll(2d, 10d);
        }

        MapGroup.getChildren().add(edge);
    }

    private void InitializeFloorButtons() {
        FirstFloor_Button.setOnMouseClicked(e -> currFloor.set(1));
        SecondFloor_Button.setOnMouseClicked(e -> currFloor.set(2));
        ThirdFloor_Button.setOnMouseClicked(e -> currFloor.set(3));
        FourthFloor_Button.setOnMouseClicked(e -> currFloor.set(4));
        FifthFloor_Button.setOnMouseClicked(e -> currFloor.set(5));
        SixthFloor_Button.setOnMouseClicked(e -> currFloor.set(6));
        SeventhFloor_Button.setOnMouseClicked(e -> currFloor.set(7));

        //Triggered anytime the currFloor Changes
        //Updates what nodes are being displayed
        currFloor.addListener(e -> DisplayPaths());

        //whenever the floor span changes, update the floor buttons that are disabled
        FloorSpan.addListener((ListChangeListener<Integer>) c -> {
            System.out.println("FloorSpanProperty Changed");
            for (int i = 0; i < 7; i++) {
                Button B = (Button) FloorButtons_VBox.getChildren().get(i);
                B.setDisable(false);
                if (!FloorSpan.contains(i + 1)) {
                    B.setDisable(true);
                }
            }
        });
    }

    //-------------------------------------Path Finding----------------------------------------

    //This function takes a path and resets the Group in the Scrollpane to have the correct Image and circles
    private void DisplayPath(Path path) {
        System.out.println("displayPath");
        int floor = path.getNode(0).getLocation().getFloor();
        MapImageView.setImage(ImageProvider.getImageByFloor(floor));
        ClearMapGroup();
        List<MapNode> NodesInPath = path.getPath();
        for (MapNode N : NodesInPath) {
            MakeCircleInGroup(N);
        }

        String first;
        for (int i = 0; i < path.edges().size(); i++) {
            first = path.getNode(i).getModelNode().getName();
            if (path.edges().get(i).getStart().getName().equals(first)) {
                arrow(path.edges().get(i));
            } else {
                Edge temp = new Edge(path.edges().get(i).getEnd(), path.edges().get(i).getStart(),
                        path.edges().get(i).getStart().getLocation().getFloor());
                arrow(temp);
            }
        }
        for (Edge E : path.edges()) {
            MakeLineInGroup(E);
        }
    }

    private static void arrow(Edge e) {

        double arrowLength = 4;
        double arrowWidth = 7;

        double ex = e.getEnd().getLocation().getX();
        double ey = e.getEnd().getLocation().getY();
        double sx = e.getStart().getLocation().getX();
        double sy = e.getStart().getLocation().getY();

        Line arrow1 = new Line(0, 0, ex, ey);
        Line arrow2 = new Line(0, 0, ex, ey);

        arrow1.setEndX(ex);
        arrow1.setEndY(ey);
        arrow2.setEndX(ex);
        arrow2.setEndY(ey);

        if (ex == sx && ey == sy) {
            // arrow parts of length 0
            arrow1.setStartX(ex);
            arrow1.setStartY(ey);
            arrow2.setStartX(ex);
            arrow2.setStartY(ey);
        } else {
            double factor = arrowLength / Math.hypot(sx - ex, sy - ey);
            double factorO = arrowWidth / Math.hypot(sx - ex, sy - ey);

            double dx = (sx - ex) * factor;
            double dy = (sy - ey) * factor;

            double ox = (sx - ex) * factorO;
            double oy = (sy - ey) * factorO;

            arrow1.setStartX(ex + dx - oy);
            arrow1.setStartY(ey + dy + ox);
            arrow2.setStartX(ex + dx + oy);
            arrow2.setStartY(ey + dy - ox);
        }

        double xdiff1 = arrow1.getStartX() - arrow1.getEndX();
        double ydiff1 = arrow1.getStartY() - arrow1.getEndY();

        double xdiff2 = arrow2.getStartX() - arrow2.getEndX();
        double ydiff2 = arrow2.getStartY() - arrow2.getEndY();


        double x1 = e.getEnd().getLocation().getX();
        double y1 = e.getEnd().getLocation().getY();
        double x2 = x1 + xdiff1;
        double y2 = y1 + ydiff1;

        double x3 = x1 + xdiff2;
        double y3 = y1 + ydiff2;

        ImageView Map1 = (ImageView) MapGroup.getChildren().get(0);

        double ImgW = Map1.getImage().getWidth();
        double ImgH = Map1.getImage().getHeight();
        double ImgR = ImgH / ImgW;

        arrow1.startXProperty().bind(Map1.fitWidthProperty().multiply((x1 / ImgW)));
        arrow1.startYProperty().bind(Map1.fitWidthProperty().multiply(ImgR).multiply((y1 / ImgH)));
        arrow1.endXProperty().bind(Map1.fitWidthProperty().multiply((x2 / ImgW)));
        arrow1.endYProperty().bind(Map1.fitWidthProperty().multiply(ImgR).multiply((y2 / ImgH)));

        arrow2.startXProperty().bind(Map1.fitWidthProperty().multiply((x1 / ImgW)));
        arrow2.startYProperty().bind(Map1.fitWidthProperty().multiply(ImgR).multiply((y1 / ImgH)));
        arrow2.endXProperty().bind(Map1.fitWidthProperty().multiply((x3 / ImgW)));
        arrow2.endYProperty().bind(Map1.fitWidthProperty().multiply(ImgR).multiply((y3 / ImgH)));

        MapGroup.getChildren().addAll(arrow1, arrow2);

    }

    //This function displays all the paths on the floor you are currently on
    private void DisplayPaths() {
        System.out.println("currFloorPropertyChanged");
        System.out.println("Paths: " + paths);
        MapImageView.setImage(ImageProvider.getImageByFloor(currFloor.get()));
        ClearMapGroup();
        List<Path> subPaths = new ArrayList<>();
        //get a list of all subpaths deivided by floors
        System.out.println("paths: " + paths);
        for (Path p : paths) {
            System.out.println("groupedByFloor: " + p.groupedByFloor());
            subPaths.addAll(p.groupedByFloor());
        }
        //if the subpath is on that floor, display that ish
        System.out.println("subPaths: " + subPaths);
        for (Path p : subPaths) {
            if (p.floorsSpanned().contains(currFloor.get())) {
                System.out.println("Path: " + p);
                DisplayPath(p);
            }
        }
    }

    private ObservableList<Integer> PathSpansFloors() {
        System.out.println("PathSpansFloors");
        List<Integer> span = new ArrayList<>();
        Set<Integer> floors;
        for (Path p : paths) {
            floors = p.floorsSpanned();
            for (int i : floors) {
                if (!span.contains(i)) {
                    span.add(i);
                }
            }
        }
        return FXCollections.observableList(span);
    }

    //-----------------------------------------------------------

    private void initializeDirectory() {
        Search_ScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Search_ScrollPane.setPrefHeight(1000);
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
            while (c.next()) {
            } // Apply all the changes
            ObservableList<? extends Navigable> dests = c.getList();
            paths.clear();
            if (c.getList().size() >= 2) {
                Navigable start = dests.get(0);
                for (Navigable dest : dests.subList(1, dests.size())) {
                    paths.add(map.shortestPath(start.getNode(), dest.getNode()));
                    start = dest;
                }
            }
            ClearMapGroup();
            List<Integer> nums = new ArrayList<>();
            nums.add(1);
            nums.add(2);
            nums.add(3);
            nums.add(4);
            nums.add(5);
            nums.add(6);
            nums.add(7);
            ObservableList<Integer> AllFloors = FXCollections.observableList(nums);
            FloorSpan.setValue(AllFloors);
            System.out.println(paths);
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
        Searching_VBox.getChildren().add(addDestandDirectionButtons);
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

    private void showDestinationInfo() {
        Searching_VBox = makeVBox();
        Searching_VBox.getChildren().addAll(destinationNodes);
        Searching_VBox.getChildren().add(addDestandDirectionButtons);
        Searching_VBox.getChildren().add(MakeInfoTextArea(selectedDestination.get()));
    }

    private void showTextDirections() {
        Searching_VBox = makeVBox();
        Searching_VBox.getChildren().addAll(destinationNodes);
        Searching_VBox.getChildren().add(addDestandDirectionButtons);
        Searching_VBox.getChildren().add(MakeTextDirectionsListView(paths));
        TextArea text = new TextArea();
        String str = "";
        for(Path p: paths){
            List<String> ls = pathSteps(p, bundle);
            for(String s: ls){
                str += s + "\n";
            }
            text.setText(str);
        }
        Searching_VBox.getChildren().add(text);
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
        field.setPrefWidth(700);
        field.setMaxWidth(Region.USE_COMPUTED_SIZE);
        field.setOnMouseClicked(e -> {
            if (e.getClickCount() >= 2) {
                if (currentSearchField == null) {
                    field.setEditable(true);
                    field.setText("");
                    search("");
                    showEditDestination(field);
                    currentDestinationIndex = destinations.indexOf(location);
                }
            }
            if (currentDestinationIndex < 0) {
                selectedDestination.set(location);
                showDestinationInfo();
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

    private TextArea MakeInfoTextArea(Navigable location) {
        TextArea Info = new TextArea();
        Info.setText(location.getInfo());
        Info.setPrefWidth(Region.USE_COMPUTED_SIZE);
        Info.setPrefHeight(Region.USE_COMPUTED_SIZE);
        return Info;
    }

    private ListView<String> MakeTextDirectionsListView(List<Path> paths) {
        //Create List of all directions given List of Paths
        List<String> TextDirections = new ArrayList<>();
        for (Path p : paths) {
            for (MapNode M : p.getPath()) {
                TextDirections.add(M.toString());
            }
        }
        ListView<String> textdirs = new ListView<>();
        textdirs.setItems(FXCollections.observableList(TextDirections));
        textdirs.setPrefWidth(Region.USE_COMPUTED_SIZE);
        textdirs.setPrefHeight(Region.USE_COMPUTED_SIZE);
        return textdirs;
    }

    // -------------------------------------------------- buttons ---------------------------------------------------------------------------------------

    @FXML
    public void OpenAboutUs() {
        setStage();
        switchScreen("view/AboutUs.fxml", "About Us", stage);
    }

    @FXML
    public void OpenAdminTool() {
        setStage();
        switchScreen("view/LoginPage.fxml", "Login", stage);
    }

    public void HandleHelpButton() {
        Hours hours = hoursService.find(1L);
        String message;
        if (hours != null) {
            Date morningStart = hours.getVisitingHoursMorningStart();
            Date morningEnd = hours.getVisitingHoursMorningEnd();

            Date eveningStart = hours.getVisitingHoursEveningStart();
            Date eveningEnd = hours.getVisitingHoursEveningEnd();

            SimpleDateFormat hoursFormat = new SimpleDateFormat("h:mm a");
            String morningHours = hoursFormat.format(morningStart) + " - " + hoursFormat.format(morningEnd);
            String eveningHours = hoursFormat.format(eveningStart) + " - " + hoursFormat.format(eveningEnd);

            message = bundle.getString("helpMessage") + "\n\n" +
                    bundle.getString("operatingHours") + "\n" +
                    bundle.getString("morningHours") + morningHours + "\n" +
                    bundle.getString("eveningHours") + eveningHours;

        } else {
            Date morningStart = new Date();
            Date morningEnd = new Date();

            Date eveningStart = new Date();
            Date eveningEnd = new Date();

            SimpleDateFormat hoursFormat = new SimpleDateFormat("h:mm a");
            String morningHours = hoursFormat.format(morningStart) + " - " + hoursFormat.format(morningEnd);
            String eveningHours = hoursFormat.format(eveningStart) + " - " + hoursFormat.format(eveningEnd);

            message = bundle.getString("helpMessage") + "\n\n" +
                    bundle.getString("operatingHours") + "\n" +
                    bundle.getString("morningHours") + morningHours + "\n" +
                    bundle.getString("eveningHours") + eveningHours;
        }
        // <TODO> place the text somewhere
//        StartInfo_TextArea.setText(message);
    }

    // <TODO> make this work
    public void HandlePanicButton() {
//
//        StartInfo_TextArea.setText(bundle.getString("panicMessage"));
//        HospitalProfessional HP_Start = professionalService.findHospitalProfessionalByName("Floor 1 Kiosk");
//        HospitalService HS_Dest = serviceService.findHospitalServiceByName("Emergency Department");
//
//        FindandDisplayPath(HP_Start, null, null, HS_Dest);
    }
}
