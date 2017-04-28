package controller;


import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import model.Edge;
import model.Hours;
import model.Navigable;
import pathfinding.MapNode;
import pathfinding.Path;
import textDirections.Step;
import util.MappedList;

import java.net.URL;
import java.text.SimpleDateFormat;
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

//    @FXML
//    private ButtonBar Options_ButtonBar;

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

    //buttons at the bottom
    @FXML
    private Button AboutUsButton;
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

    ImageView MapImageView = new ImageView();
    Group MapGroup = new Group();
    //Center ScrollPane relative to Image Coordinates
    double CenterX;
    double CenterY;
    boolean CenterLocked = false;

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

    private TextField currentSearchField;
    private int currentDestinationIndex = -1;

    private pathfinding.Map map = new pathfinding.Map(nodeService.getAllNodes());
    private ObservableList<Path> paths = FXCollections.observableArrayList();

    private static ResourceBundle bundle;

    @FXML
    void initialize() {
        bundle = resources;
        InitializeMap();
        InitializeFloorButtons();
        InitializeZoomListener();
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
        Map_ScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Map_ScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Map_Slider.minProperty().bind(Map_Split.widthProperty());
        Map_Slider.setMax(MapPic.getWidth());
        MapImageView.fitWidthProperty().bind(Map_Slider.valueProperty());
    }

    private void InitializeZoomListener(){
        //This event is triggered when the Map Slider is moved
        //It locks the center coordinate, and as it is zoomed it pans to the desired position
        Map_Slider.valueProperty().addListener((original,oldValue, newValue) -> {
            CenterLocked=true;
            System.out.println("Zooming On: " + CenterX+", "+CenterY);
            PanToPoint(CenterX,CenterY);
            });

        //This event is triggered when the mouse is released from the slider
        Map_Slider.setOnMouseReleased(e->{CenterLocked=false;});

        //These events are triggered when the scrollpane view is panned
        Map_ScrollPane.hvalueProperty().addListener(e->{if(!CenterLocked) {Panning();}});
        Map_ScrollPane.vvalueProperty().addListener(e->{if(!CenterLocked) {Panning();}});
    }
    //This function is meant to be called whenever the user is panning around the map
    //ie on click and drag, not on zoom, Controlled by CenterLocked Boolean
    private void Panning(){
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
        System.out.println(CenterX+", "+CenterY);
    }

    private void PanToPoint(double X, double Y){
        System.out.println("Panning To Point");
        double Width = MapImageView.getImage().getWidth();
        double Height = MapImageView.getImage().getHeight();
        double ImageRatio = Height/Width;
        double currWidth = MapImageView.getFitWidth();
        double currHeight = currWidth*ImageRatio;
        double Xprime = X*(currWidth/Width);
        double Yprime = Y*(currHeight/Height);

        Map_ScrollPane.setHvalue((Xprime/(currWidth-Map_ScrollPane.getWidth()))-((Map_ScrollPane.getWidth()/currWidth)/2));
        Map_ScrollPane.setVvalue((Yprime/(currHeight-Map_ScrollPane.getHeight()))-((Map_ScrollPane.getHeight()/currHeight)/2));
    }

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

    public void MakeLineInGroup(Edge e){
        double x1 = e.getStart().getLocation().getX();
        double y1 = e.getStart().getLocation().getY();
        double x2 = e.getEnd().getLocation().getX();
        double y2 = e.getEnd().getLocation().getY();
        int z = e.getStart().getLocation().getFloor();

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

        if(e.isDisabled()) {
            edge.getStrokeDashArray().addAll(2d, 10d);
        }

        MapGroup.getChildren().add(edge);
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
            MapImageView.setImage(ImageProvider.getImage("images/7_theseventhfloor.png"));ClearMapGroup();});
    }

    //-------------------------------------Path Finding----------------------------------------
    private void BuildStepByStepButtons(){
        Map_HBox.getChildren().remove(0,Map_HBox.getChildren().size());
        for(int i =0; i<paths.size(); i++){
            Button button = new Button();
            button.setText(Integer.toString(i+1));
            Map_HBox.getChildren().add(button);
            button.setOnAction(e -> {
                System.out.println("Pressed: "+ button.getText());
                DisplayPath(paths.get(Integer.parseInt(button.getText())-1));
            });
        }
    }

    //This function takes a path and resets the Group in the Scrollpane to have the correct Image and circles
    private void DisplayPath(Path path){
        System.out.println("displayPath");
        int floor = path.getNode(0).getLocation().getFloor();
        MapImageView.setImage(ImageProvider.getImageByFloor(floor));
        ClearMapGroup();
        List<MapNode> NodesInPath = path.getPath();
        for(MapNode N: NodesInPath){
            MakeCircleInGroup(N);
        }
        for(Edge E : path.edges()){
            MakeLineInGroup(E);
        }
    }



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
        destinations.addListener(new ListChangeListener<Navigable>() {
            @Override
            public void onChanged(Change<? extends Navigable> c) {
                while (c.next()) {} // Apply all the changes
                ObservableList<? extends Navigable> dests = c.getList();
                paths.clear();
                if (c.getList().size() >= 2) {
                    Navigable start = dests.get(0);
                    for (Navigable dest : dests.subList(1, dests.size())) {
                        paths.add(map.shortestPath(start.getNode(), dest.getNode()));
                        start = dest;
                    }
                }
                BuildStepByStepButtons();
                System.out.println(paths);
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

    private void showDestinationInfo() {
        Searching_VBox = makeVBox();
        Searching_VBox.getChildren().addAll(destinationNodes);
        Searching_VBox.getChildren().add(addDestinationButton);
        Searching_VBox.getChildren().add(MakeInfoTextArea(selectedDestination.get()));
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
            if(e.getClickCount()>=2){
                if (currentSearchField == null) {
                    field.setEditable(true);
                    field.setText("");
                    search("");
                    showEditDestination(field);
                    currentDestinationIndex = destinations.indexOf(location);
                }
            } else {
                if (currentDestinationIndex < 0) {
                    selectedDestination.set(location);
                    showDestinationInfo();
                }
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

    // -------------------------------------------------- buttons ---------------------------------------------------------------------------------------

    @FXML
    public void OpenAboutUs() {
        switchScreen("view/AboutUs.fxml", "About Us", AboutUsButton);
    }

    @FXML
    public void OpenAdminTool() {
        switchScreen("view/LoginPage.fxml", "Login", AdminToolButton);
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
            Date morningStart = new Date(0, 0, 0, 9, 30);
            Date morningEnd = new Date(0, 0, 0, 12, 0);

            Date eveningStart = new Date(0, 0, 0, 14, 0);
            Date eveningEnd = new Date(0, 0, 0, 17, 45);

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
