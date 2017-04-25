package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.HospitalProfessional;
import model.HospitalService;
import model.Hours;
import model.Node;
import pathfinding.Map;
import pathfinding.MapNode;
import pathfinding.Path;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainController extends Controller implements Initializable{
    @FXML private ScrollPane FirstFloorScrollPane;
    @FXML private Slider FirstFloorSlider;
    @FXML private ScrollPane SecondFloorScrollPane;
    @FXML private Slider SecondFloorSlider;
    @FXML private ScrollPane ThirdFloorScrollPane;
    @FXML private Slider ThirdFloorSlider;
    @FXML private ScrollPane FourthFloorScrollPane;
    @FXML private Slider FourthFloorSlider;
    @FXML private ScrollPane FifthFloorScrollPane;
    @FXML private Slider FifthFloorSlider;
    @FXML private ScrollPane SixthFloorScrollPane;
    @FXML private Slider SixthFloorSlider;
    @FXML private ScrollPane SeventhFloorScrollPane;
    @FXML private Slider SeventhFloorSlider;
    @FXML private TabPane FloorViewsTabPane;
    @FXML private VBox MainVbox;
    @FXML private ImageView LogoImageView;
    @FXML private AutocompletionlTextField StartLocationField;
    @FXML private AutocompletionlTextField EndLocationField;
    @FXML private TabPane Info_TabPane;
    @FXML private TextArea StartInfo_TextArea;
    @FXML private TextArea EndInfo_TextArea;
    @FXML private TextArea TextDirectionsTextArea;
    @FXML private Button AboutUsButton;
    @FXML private Button AdminToolButton;
    @FXML private SplitPane MainSplitPane;

    @FXML private MenuItem english_button;
    @FXML private  MenuItem spanish_button;
    @FXML private  MenuItem french_button;
    @FXML private MenuItem japanese_button;
    @FXML private MenuItem chinese_button;
    @FXML private MenuItem portuguese_button;
    @FXML private MenuItem italian_button;

    private Hours hours;

    private static ResourceBundle bundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        english_button.setOnAction(event -> loadView(new Locale("en", "US")));
        spanish_button.setOnAction(event -> loadView(new Locale("es", "PR")));
        french_button.setOnAction(event -> loadView(new Locale("fr", "FR")));
        japanese_button.setOnAction(event -> loadView(new Locale("jp", "JP")));
        chinese_button.setOnAction(event -> loadView(new Locale("zh", "CN")));
        portuguese_button.setOnAction(event -> loadView(new Locale("pt", "BR")));
        italian_button.setOnAction(event -> loadView(new Locale("it", "IT")));

        bundle = resources;

        new ShowNodesEdgesHelper(FirstFloorScrollPane, SecondFloorScrollPane, ThirdFloorScrollPane,
                FourthFloorScrollPane, FifthFloorScrollPane, SixthFloorScrollPane,
                SeventhFloorScrollPane, FirstFloorSlider, SecondFloorSlider,
                ThirdFloorSlider, FourthFloorSlider, FifthFloorSlider, SixthFloorSlider,
                SeventhFloorSlider, FloorViewsTabPane);

        ShowNodesEdgesHelper.InitializeMapViews();
        TextDirectionsTextArea.prefWidthProperty().bind(MainVbox.widthProperty());
        Image logo = new Image("images/logo.png");
        LogoImageView.setImage(logo);
        LogoImageView.setPreserveRatio(true);
        LogoImageView.fitHeightProperty().bind(MainVbox.heightProperty().multiply(0.1));
        Info_TabPane.prefHeightProperty().bind(MainVbox.heightProperty().multiply(0.6));

        initializeStartAndEnd();

    }

    public void loadView(Locale locale) {
        Stage stage = (Stage) MainSplitPane.getScene().getWindow();
        try {
            SplitPane root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Main.fxml"),
                    ResourceBundle.getBundle("Language", locale));
            stage.setTitle("Faulkner Kiosk");
            stage.getScene().setRoot(root);
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void initializeStartAndEnd() {
        List<HospitalProfessional> HPs = professionalService.getAllProfessionals();
        List<HospitalService> HSs = serviceService.getAllServices();
        List<String> names = new ArrayList<>();
        for (HospitalProfessional HP : HPs) {
            names.add(HP.getName());
        }
        for (HospitalService HS : HSs) {
            names.add(HS.getName());
        }
        StartLocationField.getEntries().addAll(names);
        EndLocationField.getEntries().addAll(names);
    }

    //-------------------------------------------DISPLAY PATH DRAWING FUNCTIONS---------------------------------------------
    //DisplayMap function takes a list of points(X,Y) and creates circles at all their positions and lines between them
    private void DisplayMap(Path path) {
        ShowNodesEdgesHelper.ClearOldPaths();

        if (path.numNodes() < 1) {
            System.err.println("Can't display map because there is no path.");
            return;
        }

        for (MapNode node : path) ShowNodesEdgesHelper.MakeCircle(node.getModelNode());

        path.edges().stream().map(ShowNodesEdgesHelper::MakeLine);

        HideTabs(path);
    }

    //-----------------------------------FUNCTIONS------------------------------------------
    private void FindandDisplayPath(HospitalProfessional HP_Start, HospitalProfessional HP_Dest,
                                    HospitalService HS_Start, HospitalService HS_Dest) {
        Map map = new Map(nodeService.getAllNodes());

        Node nodeStart, nodeEnd;
        if (HP_Start != null) {
            nodeStart = HP_Start.getOffices().get(0);
        } else {
            nodeStart = HS_Start.getLocations().get(0);
        }

        if (HP_Dest != null) {
            nodeEnd = HP_Dest.getOffices().get(0);
        } else {
            nodeEnd = HS_Dest.getLocations().get(0);
        }

        MapNode start = map.getNode(nodeStart.getId());
        MapNode dest = map.getNode(nodeEnd.getId());

        Path path = map.shortestPath(start, dest);
        pathText(path);
    }

    private void pathText(Path path) {
        if (path.isEmpty()) {
            TextDirectionsTextArea.setText(bundle.getString("noPath"));
        } else if (path.numNodes() < 2) {
            TextDirectionsTextArea.setText(bundle.getString("alreadyThere"));
            DisplayMap(path);
        } else {
//            TextDirectionsTextArea.setText(textDirections.MakeDirections.getText(path));
            DisplayMap(path);
        }
    }

    private void HideTabs(Path path) {
        Set<Integer> floors = path.floorsNotSpanned();
        ObservableList<Tab> tabs = FloorViewsTabPane.getTabs();
        //Turn all tabs on
        for (Tab t : tabs) {
            t.setDisable(false);
        }
        //disable tabs that are not included in path

        for (int n : floors) {
            tabs.get(n - 1).setDisable(true);
        }
    }

    //This function updates the StartInfo and EndInfo Text Areas
    private void PopulateInformationDisplay(HospitalProfessional HP_Start, HospitalProfessional HP_Dest,
                                            HospitalService HS_Start, HospitalService HS_Dest) {
        if (HP_Start != null) {
            StartInfo_TextArea.setText(HP_Start.getTitle() + " " + HP_Start.getName() + "\n\n" +
                    bundle.getString("offices") + "\n\n" + HP_Start.getOffices());
        } else {
            StartInfo_TextArea.setText(HS_Start.getName() + "\n\n" +
                    bundle.getString("offices") + "\n\n" + HS_Start.getLocations());
        }

        if (HP_Dest != null) {
            EndInfo_TextArea.setText(HP_Dest.getTitle() + " " + HP_Dest.getName() + "\n\n" +
                    bundle.getString("location") + "\n\n" + HP_Dest.getOffices());
        } else {
            EndInfo_TextArea.setText(HS_Dest.getName() + "\n\n" +
                     bundle.getString("location") + "\n\n" + HS_Dest.getLocations());
        }
    }


    //--------------------------------------------EVENT HANDLERS--------------------------------------------------

    public void handleClickedOnStartAtKiosk() {
        //System.out.println("start at kiosk");
        StartLocationField.setText("Floor 1 Kiosk");
    }

    //This function switches the text in the StartLocation and EndLocation Fields
    public void switchLocationButtonClicked() {
        //System.out.println("clicked on switch location button");
        String tempStorage = StartLocationField.getText();
        StartLocationField.setText(EndLocationField.getText());
        EndLocationField.setText(tempStorage);
    }

    public void getPathButtonClicked() {
        if (!(StartLocationField.getText().isEmpty() || EndLocationField.getText().isEmpty())) {
            //Do path finding
            HospitalProfessional HP_Start = professionalService.findHospitalProfessionalByName(StartLocationField.getText());
            HospitalProfessional HP_Dest = professionalService.findHospitalProfessionalByName(EndLocationField.getText());
            HospitalService HS_Start = serviceService.findHospitalServiceByName(StartLocationField.getText());
            HospitalService HS_Dest = serviceService.findHospitalServiceByName(EndLocationField.getText());

            FindandDisplayPath(HP_Start, HP_Dest, HS_Start, HS_Dest);
            //Set Information Displays

            PopulateInformationDisplay(HP_Start, HP_Dest, HS_Start, HS_Dest);
//
        } else {
            System.out.print("Give a Start and an End"); // TODO: make this meaningful in the gui
        }
    }

    //-------------------------------------SCREEN CHANGING FUNCTIONS---------------------------------------------------
    @FXML
    public void OpenAdminTool() {
        switchScreen("view/LoginPage.fxml", "Login", AdminToolButton);
    }

    @FXML
    public void OpenAboutUs() {
        switchScreen("view/AboutUs.fxml", "About Us", AboutUsButton);
    }

    //--------------------Buttons that have language--------------------------//
    public void HandleHelpButton() {
        Hours hours = hoursService.find(1L);
        String message;
        if(hours != null) {
            Date morningStart = hours.getVisitingHoursMorningStart();
            Date morningEnd = hours.getVisitingHoursMorningEnd();

            Date eveningStart = hours.getVisitingHoursEveningStart();
            Date eveningEnd = hours.getVisitingHorusEveningEnd();

            SimpleDateFormat hoursFormat = new SimpleDateFormat("h:mm a");
            String morningHours = hoursFormat.format(morningStart) + " - " + hoursFormat.format(morningEnd);
            String eveningHours = hoursFormat.format(eveningStart) + " - " + hoursFormat.format(eveningEnd);

            message = bundle.getString("helpMessage") + "\n\n" +
                    bundle.getString("operatingHours") + "\n" +
                    bundle.getString("morningHours") + morningHours + "\n" +
                    bundle.getString("eveningHours") + eveningHours;

        } else {
            Date morningStart = new Date(0,0,0,9,30);
            Date morningEnd = new Date(0,0,0,12,0);

            Date eveningStart = new Date(0,0,0,14,0);
            Date eveningEnd = new Date(0,0,0,17,45);

            SimpleDateFormat hoursFormat = new SimpleDateFormat("h:mm a");
            String morningHours = hoursFormat.format(morningStart) + " - " + hoursFormat.format(morningEnd);
            String eveningHours = hoursFormat.format(eveningStart) + " - " + hoursFormat.format(eveningEnd);

            message = bundle.getString("helpMessage") + "\n\n" +
                    bundle.getString("operatingHours") + "\n" +
                    bundle.getString("morningHours") + morningHours + "\n" +
                    bundle.getString("eveningHours") + eveningHours;
        }
        StartInfo_TextArea.setText(message);
    }

    public void HandlePanicButton() {
        StartInfo_TextArea.setText(bundle.getString("panicMessage"));
        HospitalProfessional HP_Start = professionalService.findHospitalProfessionalByName("Floor 1 Kiosk");
        HospitalService HS_Dest = serviceService.findHospitalServiceByName("Emergency Department");

        FindandDisplayPath(HP_Start, null, null, HS_Dest);
    }

    public static ResourceBundle getBundle(){
        return bundle;
    }

}
