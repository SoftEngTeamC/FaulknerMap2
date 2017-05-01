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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Hours;
import model.Navigable;
import pathfinding.MapNode;
import pathfinding.Path;
import textDirections.Step;
import util.ImageViewPane;
import util.MappedList;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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


        initializeMap();
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
            displayPaths();
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

    private void initializeMap() {
        ImageViewPane mapView = new ImageViewPane(ImageProvider.getImageByFloor(1));
        mapView.prefHeightProperty().bind(mapContainer.heightProperty());
        mapView.prefWidthProperty().bind(mapContainer.widthProperty());
        mapContainer.getChildren().add(mapView);
        mapView.toBack();
        this.mapView = mapView;
    }

    private void displayPaths() {
        mapView.setPath(paths.get(0).groupedByFloor().get(0));
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
        destinations.addListener(new ListChangeListener<Navigable>() {
            @Override
            public void onChanged(Change<? extends Navigable> c) {
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
//                ClearMapGroup();
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
        List<String> TextDirections = new ArrayList<String>();
        for (Path p : paths) {
            for (MapNode M : p.getPath()) {
                TextDirections.add(M.toString());
            }
        }
        ListView<String> textdirs = new ListView<String>();
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
