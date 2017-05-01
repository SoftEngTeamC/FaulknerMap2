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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Hours;
import model.Navigable;
import pathfinding.MapNode;
import pathfinding.Path;
import quickResponse.QR;
import service.HoursService;
import textDirections.Step;
import util.ImageViewPane;
import util.MappedList;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static textDirections.TextualDirections.pathSteps;

public class HomeController extends Controller implements Initializable {
    Date date = new Date();

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

    private Boolean SteppingThroughDirections = false;

    private Stage stage;
    private List<Button> floorButtons = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;

        clearFloorArray();

        english_button.setOnAction(event -> loadView(new Locale("en", "US")));
        spanish_button.setOnAction(event -> loadView(new Locale("es", "PR")));
        french_button.setOnAction(event -> loadView(new Locale("fr", "FR")));
        japanese_button.setOnAction(event -> loadView(new Locale("jp", "JP")));
        chinese_button.setOnAction(event -> loadView(new Locale("zh", "CN")));
        portuguese_button.setOnAction(event -> loadView(new Locale("pt", "BR")));
        italian_button.setOnAction(event -> loadView(new Locale("it", "IT")));


        initializeMap();
        initializeDirectory();
        MakeGetDirectionsButton();
        Logo_ImageView.setImage(ImageProvider.getImage("images/logo.png"));
        Logo_ImageView.setPreserveRatio(true);
        Logo_ImageView.fitHeightProperty().bind(Main_VBox.heightProperty().multiply(0.1));
        // make hours service
        HoursService hs = new HoursService();
        timeofdaywarning( hs.find(1L).getVisitingHoursMorningStart(),hs.find(1L).getVisitingHoursEveningEnd(),date);

    }

    void clearFloorArray() {
        floorButtons.clear();
        floorButtons.add(FirstFloor_Button);
        floorButtons.add(SecondFloor_Button);
        floorButtons.add(ThirdFloor_Button);
        floorButtons.add(FourthFloor_Button);
        floorButtons.add(FifthFloor_Button);
        floorButtons.add(SixthFloor_Button);
        floorButtons.add(SeventhFloor_Button);

        for (int i = 0; i < floorButtons.size();i++) {
            floorButtons.get(i).setDisable(false);
            int finalI = i;
            floorButtons.get(i).setOnMouseClicked(event -> {
                mapView = new ImageViewPane(ImageProvider.getImageByFloor(finalI +1));
                mapView.prefHeightProperty().bind(mapContainer.heightProperty());
                mapView.prefWidthProperty().bind(mapContainer.widthProperty());
                mapView.toBack();
                mapContainer.getChildren().clear();
                mapContainer.getChildren().add(mapView);
            });
        }
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

    void initializeMap() {
        ImageViewPane mapView = new ImageViewPane(ImageProvider.getImageByFloor(1));
        mapView.prefHeightProperty().bind(mapContainer.heightProperty());
        mapView.prefWidthProperty().bind(mapContainer.widthProperty());
        mapContainer.getChildren().add(mapView);
        mapView.toBack();
        this.mapView = mapView;
    }

    private void displayPaths() {
        mapView = new ImageViewPane(ImageProvider.getImageByFloor(paths.get(0).
                groupedByFloor().get(0).getFloor()));
        mapView.prefHeightProperty().bind(mapContainer.heightProperty());
        mapView.prefWidthProperty().bind(mapContainer.widthProperty());
        mapView.setPath(paths.get(0).groupedByFloor().get(0));
        mapView.toBack();
        mapContainer.getChildren().clear();
        mapContainer.getChildren().add(mapView);
        for (int floor : paths.get(0).floorsNotSpanned()) {
            floorButtons.get(floor-1).setDisable(true);
        }

        for (Path path : paths.get(0).groupedByFloor()) {
            int floor = path.getFloor();
            floorButtons.get(floor - 1).setDisable(false);
            floorButtons.get(floor - 1).setOnMouseClicked(event -> {
                mapView = new ImageViewPane(ImageProvider.getImageByFloor(floor));
                mapView.prefHeightProperty().bind(mapContainer.heightProperty());
                mapView.prefWidthProperty().bind(mapContainer.widthProperty());
                mapView.setPath(path);
                mapView.toBack();
                mapContainer.getChildren().clear();
                mapContainer.getChildren().add(mapView);
//                System.out.println(path.nodes());
            });
        }
    }


    private ObservableList<Integer> PathSpansFloors() {
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

    void initializeDirectory() {
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
        List<? extends Navigable> results1 = serviceService.search(query);
        searchResults.clear();
        searchResults.addAll(results);
        searchResults.addAll(results1);
        searchResults.removeIf(result -> destinations.stream().map(Object::toString).collect(Collectors.toList()).contains(result.toString()));
    }

    private VBox makeVBox() {
        VBox newVBox = new VBox();
        newVBox.prefWidthProperty().bind(Search_ScrollPane.widthProperty());
        Search_ScrollPane.setContent(newVBox);
        return newVBox;
    }

    // --------------- View State Changers --------------- //
    private BooleanProperty showWarning = new SimpleBooleanProperty(false);

    private void showDirections() {
        Searching_VBox = makeVBox();
        Searching_VBox.getChildren().addAll(destinationNodes);
        Searching_VBox.getChildren().add(addDestandDirectionButtons);
        Searching_VBox.getChildren().add(stepsView);
        Label warningLabel = new Label("The destination may not be open.");
        warningLabel.visibleProperty().bind(showWarning);
        Searching_VBox.getChildren().add(warningLabel);
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
        //Take List of Strings from Directions ListView and Pass it to Construct QR Code. Display QR
        List<String> DirectionsList = new ArrayList<String>();
        for(String S: MakeTextDirectionsListView(paths).getItems()){ DirectionsList.add(S.toString());}
        if(addDestandDirectionButtons.getChildren().size()>2){
            addDestandDirectionButtons.getChildren().remove(2);
        }
        addDestandDirectionButtons.getChildren().add(MakeQRButton(DirectionsList));

        //Display Text Directions
        TextArea text = new TextArea();
        String str = "";
        for (Path p : paths) {
            List<String> ls = pathSteps(p, bundle);
            for (String s : ls) {
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

    private void showQRcode(List<String> directions){
        Searching_VBox = makeVBox();
        Searching_VBox.getChildren().addAll(destinationNodes);
        Searching_VBox.getChildren().add(addDestandDirectionButtons);
        Searching_VBox.getChildren().add(MakeQRImage(directions));
    }

    // ------------------------- Destination View Factories ----------------------------- //
    HBox makeDestinationView(Navigable location){
        if (destinationNodeCache.containsKey(location)) return destinationNodeCache.get(location);
        HBox destinationView = makeDestinationNodeElement(location);
        destinationNodeCache.put(location, destinationView);
        return destinationView;
    }

    private HBox makeDestinationNodeElement(Navigable location){
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
            ImageViewPane mapView = new ImageViewPane(ImageProvider.getImageByFloor(1));
            mapView.prefHeightProperty().bind(mapContainer.heightProperty());
            mapView.prefWidthProperty().bind(mapContainer.widthProperty());
            mapView.toBack();
            mapContainer.getChildren().clear();
            mapContainer.getChildren().add(mapView);

            destinations.remove(location);
            clearFloorArray();
            destinationNodeCache.remove(location);
            currentDestinationIndex = -1;
            if (destinations.isEmpty()) showSearch();
            else showDirections();
        });
        deleteButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        deleteButton.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.REMOVE));
        return deleteButton;
    }

    void MakeGetDirectionsButton(){
        //Define actions on ShowTextDirections Button
        DirectionButton.setText(bundle.getString("getDirections"));
        DirectionButton.setOnAction(e -> {
         //   System.out.print("SteppingThroughDirections: "+SteppingThroughDirections);
            if(SteppingThroughDirections){
                System.out.println("in step through");
                DirectionButton.setText(bundle.getString("getOverview"));
                FloorButtons_VBox.setVisible(false);
                showTextDirections();
                //Update the Floor span for the New Paths
                FloorSpan.set(PathSpansFloors());
                //TODO Set current Location to first node in the path
                //current location will be used to step through the directions one node at a time
                displayPaths();
                //TODO Display the Path on the Map and generate Steps
                SteppingThroughDirections = false;
            }
            else{
                System.out.println("not in step through");
                //The Overview Of the Path should be the Text Directions
                // also allowing navigability of all relevant floors as opposed to step through
                DirectionButton.setText(bundle.getString("getDirections"));
                FloorButtons_VBox.setVisible(true);
                FloorSpan.set(PathSpansFloors());
                SteppingThroughDirections = true;
                showTextDirections();
                displayPaths();
                //If QRCode button Exists. Get Rid of it.
                if(addDestandDirectionButtons.getChildren().size()>2){
                    addDestandDirectionButtons.getChildren().remove(2);
                }
            }
        });
        //Altering the add Destination and Directions Buttons HBox to have those buttons
        addDestandDirectionButtons.getChildren().add(addDestinationButton);
        addDestandDirectionButtons.getChildren().add(DirectionButton);
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
        if(paths.isEmpty() || paths.size() < 1){
            TextDirections.add("You do not have access to this location");
        } else {
            for (Path p : paths) {
                try {
                    for (MapNode M : p.getPath()) {
                        TextDirections.add(M.toString());
                    }
                } catch (Exception e){
                    TextDirections.clear();
                    TextDirections.add("You do not have access to this location");
                }
            }
        }
        ListView<String> textdirs = new ListView<>();
        textdirs.setItems(FXCollections.observableList(TextDirections));
        textdirs.setPrefWidth(Region.USE_COMPUTED_SIZE);
        textdirs.setPrefHeight(Region.USE_COMPUTED_SIZE);

        textdirs.selectionModelProperty().addListener(e ->{
            //TODO GINA THIS IS WHERE I LEFT OFF. TRYING TO ACT WHEN THE SELECTION INDEX CHANGES
            System.out.println("TextDirection#: "+textdirs.getSelectionModel());
        });

        return textdirs;
    }

    private Button MakeQRButton(List<String> directions){
        Button QRButton = new Button();
        QRButton.setText(bundle.getString("QRdirections"));
        QRButton.setOnAction(e -> {
            showQRcode(directions);
        });
        return QRButton;
    }

    private ImageView MakeQRImage(List<String> directions){
        ImageView QRImageView = new ImageView();
        String Directs = "";
        for(String S: directions){
            Directs = Directs+S+"\n";
        }
        try {
            Image QRImage = QR.buildQR(Directs);
            QRImageView.setImage(QRImage);
            QRImageView.setPreserveRatio(true);
            QRImageView.setFitWidth(300);
        } catch (Exception e){
            System.out.println("Error with QR code");
        }
        return QRImageView;
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
        TextArea text = new TextArea();
        if (hours != null) {
            Date morningStart = hours.getVisitingHoursMorningStart();
            Date morningEnd = hours.getVisitingHoursMorningEnd();

            Date eveningStart = hours.getVisitingHoursEveningStart();
            Date eveningEnd = hours.getVisitingHoursEveningEnd();

            SimpleDateFormat hoursFormat = new SimpleDateFormat("h:mm a");
            String morningHours = hoursFormat.format(morningStart) + " - " + hoursFormat.format(morningEnd);
            String eveningHours = hoursFormat.format(eveningStart) + " - " + hoursFormat.format(eveningEnd);

            text.setText(bundle.getString("helpMessage") + "\n\n" +
                    bundle.getString("operatingHours") + "\n" +
                    bundle.getString("morningHours") + morningHours + "\n" +
                    bundle.getString("eveningHours") + eveningHours);
//            message = bundle.getString("helpMessage") + "\n\n" +
//                    bundle.getString("operatingHours") + "\n" +
//                    bundle.getString("morningHours") + morningHours + "\n" +
//                    bundle.getString("eveningHours") + eveningHours;

        } else {
            Date morningStart = new Date();
            Date morningEnd = new Date();

            Date eveningStart = new Date();
            Date eveningEnd = new Date();

            SimpleDateFormat hoursFormat = new SimpleDateFormat("h:mm a");
            String morningHours = hoursFormat.format(morningStart) + " - " + hoursFormat.format(morningEnd);
            String eveningHours = hoursFormat.format(eveningStart) + " - " + hoursFormat.format(eveningEnd);

            text.setText(bundle.getString("helpMessage") + "\n\n" +
                    bundle.getString("operatingHours") + "\n" +
                    bundle.getString("morningHours") + morningHours + "\n" +
                    bundle.getString("eveningHours") + eveningHours);
            message = bundle.getString("helpMessage") + "\n\n" +
                    bundle.getString("operatingHours") + "\n" +
                    bundle.getString("morningHours") + morningHours + "\n" +
                    bundle.getString("eveningHours") + eveningHours;
        }

        Searching_VBox = makeVBox();
        Searching_VBox.getChildren().add(searchBox);
        Searching_VBox.getChildren().add(searchResultsView);
        searchResultsView.getSelectionModel().clearSelection();
        setCurrentSearchField(searchBox);
        Searching_VBox.getChildren().add(text);
    }

    // <TODO> make this work
    public void HandlePanicButton() {

        Searching_VBox = makeVBox();
        Searching_VBox.getChildren().add(searchBox);
        Searching_VBox.getChildren().add(searchResultsView);
        searchResultsView.getSelectionModel().clearSelection();
        setCurrentSearchField(searchBox);
        Searching_VBox.getChildren().add(new TextField(bundle.getString("panicMessage")));
//
//        StartInfo_TextArea.setText(bundle.getString("panicMessage"));
//        HospitalProfessional HP_Start = professionalService.findHospitalProfessionalByName("Floor 1 Kiosk");
//        HospitalService HS_Dest = serviceService.findHospitalServiceByName("Emergency Department");
//
//        FindandDisplayPath(HP_Start, null, null, HS_Dest);
    }
    public void timeofdaywarning(Date visitingHoursMorningStart, Date visitingHoursMorningEnd, Date date){
            date.setYear(visitingHoursMorningEnd.getYear());
            date.setMonth(visitingHoursMorningEnd.getMonth());
            date.setDate(visitingHoursMorningEnd.getDate());
        if (date.after(hoursService.find(1L).getVisitingHoursMorningStart()) &&
                date.before(hoursService.find(1L).getVisitingHoursMorningEnd())){
            System.out.println("works");
            showWarning.set(false);
        }
        else if (date.after(hoursService.find(1L).getVisitingHoursEveningStart()) &&
                date.before(hoursService.find(1L).getVisitingHoursEveningEnd())){
            System.out.println("works");
            showWarning.set(false);
        }
        else{
            System.out.println("time is out of bounds");
            showWarning.set(true);
        }

    }
}
