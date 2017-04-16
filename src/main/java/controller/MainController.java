package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import model.Edge;
import model.HospitalProfessional;

import model.Node;
import pathfinding.MapNode;
import pathfinding.PathFinder;
import service.EdgeService;

import pathfinding.Map;
import pathfinding.MapNode;
import pathfinding.PathFinder;
import service.EMFProvider;
import service.HospitalProfessionalService;
import service.NodeService;
import model.Hours;
import textDirections.MakeDirections;

import java.awt.event.MouseEvent;
import java.util.Collections;

import java.util.LinkedList;
import java.util.List;

public class MainController extends Controller {
    //ImageView Objects
    @FXML
    private ScrollPane FirstFloorScrollPane;
    @FXML
    private Slider FirstFloorSlider;
    @FXML
    private ScrollPane SecondFloorScrollPane;
    @FXML
    private Slider SecondFloorSlider;
    @FXML
    private ScrollPane ThirdFloorScrollPane;
    @FXML
    private Slider ThirdFloorSlider;
    @FXML
    private ScrollPane FourthFloorScrollPane;
    @FXML
    private Slider FourthFloorSlider;
    @FXML
    private ScrollPane FifthFloorScrollPane;
    @FXML
    private Slider FifthFloorSlider;
    @FXML
    private ScrollPane SixthFloorScrollPane;
    @FXML
    private Slider SixthFloorSlider;
    @FXML
    private ScrollPane SeventhFloorScrollPane;
    @FXML
    private Slider SeventhFloorSlider;
    //---------------------------------------------------
    @FXML
    private AnchorPane Map1AnchorPane;
    @FXML
    private Button AdminToolButton;
    @FXML
    private TextField SearchBarTextField;
    @FXML
    private ListView SearchResultsListView;
    @FXML
    private TextArea DisplayInformationTextArea;
    @FXML
    private Button HelpButton;
    @FXML
    private TabPane FloorViewsTabPane;
    @FXML
    private MenuButton languageMenuButton;
    @FXML
    private VBox MainVbox;
    @FXML
    private HBox CheckBoxesHBox;
    @FXML
    private SplitPane MainSplitPane;
    @FXML
    private TextArea Start_location_TextArea;
    @FXML
    private TextArea Dest_location_TextArea;
    @FXML
    private HBox PathLocationHBox;
    @FXML
    private Button SetStartLocationButton;
    @FXML
    private Button SetDestLocationButton;
    @FXML
    private Button getPathButton;

    //private static int language; // 1: english, 2: spanish, 3: chinese, 4: french
    private NodeService NS;
    private EdgeService ES;

    /* public String hours1;
     public String minutes1;
     public String ampm1;
     public String hours2;
     public String minutes2;
     public String ampm2;
     public String hours3;
     public String minutes3;
     public String ampm3;
     public String hours4;
     public String minutes4;
     public String ampm4;
     */
    public Hours hours;

    @FXML
    private ImageView LogoImageView;
    @FXML
    private VBox StartLocationVBox;
    @FXML
    private Button StartAtKioskButton;
    @FXML
    private TabPane DisplayInformationTabPane;
    @FXML
    private TextArea TextDirectionsTextArea;

    private static int language; // 1: english, 2: spanish, 3: chinese, 4: french

    //-------------------------------------------------INTIALIZE--------------------------------------------------------
    public void initialize() {

        NS = new NodeService();
        EMFProvider emf = new EMFProvider();
        hours = emf.hours;

        new ShowNodesEdgesHelper(FirstFloorScrollPane, SecondFloorScrollPane, ThirdFloorScrollPane,
                FourthFloorScrollPane, FifthFloorScrollPane, SixthFloorScrollPane,
                SeventhFloorScrollPane, FirstFloorSlider, SecondFloorSlider,
                ThirdFloorSlider, FourthFloorSlider, FifthFloorSlider, SixthFloorSlider,
                SeventhFloorSlider, FloorViewsTabPane);

        ShowNodesEdgesHelper.InitializeMapViews();

        PopulateSearchResults(null);
        SearchResultsListView.prefHeightProperty().bind(MainVbox.heightProperty().multiply(0.2));
        DisplayInformationTextArea.prefWidthProperty().bind(MainVbox.widthProperty());
        TextDirectionsTextArea.prefWidthProperty().bind(MainVbox.widthProperty());
        CheckBoxesHBox.setPrefHeight(30);
        //System.out.println(MainSplitPane.getDividers());
        //MainSplitPane.setDividerPosition(1,0.3);

        Image logo = new Image("images/logo.png");
        LogoImageView.setImage(logo);
        LogoImageView.setPreserveRatio(true);
        LogoImageView.fitHeightProperty().bind(MainVbox.heightProperty().multiply(0.1));

        //TODO: delete
//        ShowNodesEdgesHelper.MakeCircle(1000,1000,4, new Node());

        //ShowNodesEdgesHelper.MakeLine(1000,1000,2000,2000,2);


        //default is english
        // 1: english, 2: spanish, 3: chinese, 4: french
        language = 1;

//        List<Node> temp = NS.getNodesByFloor(1);
//        for(Node n: temp){
//            ShowNodesEdgesHelper.MakeCircle(n.getLocation().getX(), n.getLocation().getY(), 1, n);
//        }
//        EdgeService es = new EdgeService();
//        List<Edge> edges = es.getAllEdges();
//        for(Edge e: edges){
//            if(e.getStart().getLocation().getFloor() == 1){
//                ShowNodesEdgesHelper.MakeLine(e.getStart().getLocation().getX(), e.getStart().getLocation().getY(),
//                        e.getEnd().getLocation().getX(), e.getEnd().getLocation().getY(), 1);
//                TextField text = new TextField();
//
//            }
//        }

    }

    //-------------------------------------------DISPLAY PATH DRAWING FUNCTIONS---------------------------------------------
    //DisplayMap function takes a list of points(X,Y) and creates circles at all their positions and lines between them
    public void DisplayMap(List<MapNode> nodes) {
        for (int i = 0; i < nodes.size(); i++) {
            System.out.println(nodes.get(i).getLocation().getFloor());
        }
        System.out.println(nodes);
        ShowNodesEdgesHelper.ClearOldPaths();

        if (nodes.size() < 1) {
            System.out.println("There is no path.");
            return;
        }

        for (MapNode node : nodes) {
            ShowNodesEdgesHelper.MakeCircle(NS.find(node.getModelNodeID()));
        }

        for(int i = 0; i < nodes.size() - 1; i++){
            Node start = NS.find(nodes.get(i).getModelNodeID());
            Node end = NS.find(nodes.get(i+1).getModelNodeID());
            //find the edge from the database.
            // 0 index because findByNodes returns list of edges, forward and backwards
            Edge e = ES.findByNodes(start,end).get(0);
            //only draw if not last node, nodes are on same floor
            if((i<nodes.size()-1)&&(nodes.get(i).getLocation().getFloor() == nodes.get(i+1).getLocation().getFloor())){
                ShowNodesEdgesHelper.MakeLine(e);
            }

        }
    }

    //------------------------------------UPDATING VISUAL DATA FUNCTIONS------------------------------------------------

    //This function takes a list of strings and updates the SearchResult ListView to contain those strings
    public void UpdateSearchResults(LinkedList<String> results) {
        results.sort(String.CASE_INSENSITIVE_ORDER);
        ObservableList<String> data = FXCollections.observableArrayList();
        data.addAll(results);
        SearchResultsListView.setItems(data);
    }

    private void FindandDisplayPath(HospitalProfessional HP_Start, HospitalProfessional HP_Dest) {
        NodeService NS = new NodeService();
        pathfinding.Map map = new pathfinding.Map(NS.getAllNodes());

        Node nodeStart = (HP_Start.getOffices().get(0));
        Node nodeEnd = (HP_Dest.getOffices().get(0));

        MapNode start = map.getNode(nodeStart.getId());
        MapNode dest = map.getNode(nodeEnd.getId());

        List<MapNode> path = PathFinder.shortestPath(start, dest);
        if (path.size() < 2) {
            TextDirectionsTextArea.setText("You are already at your destination");
        } else {
            TextDirectionsTextArea.setText(MakeDirections.getText(path));
        }
        System.out.println("HERE");
        DisplayMap(path);
    }

    private void PopulateSearchResults(String S) {
        System.out.println("Populate Search String");
        HospitalProfessionalService HS = new HospitalProfessionalService();
        List<HospitalProfessional> Professionals = HS.getAllProfessionals();
        System.out.println(Professionals.size());
        ObservableList<String> names = FXCollections.observableArrayList();
        if (S == null) {
            System.out.println("null case");
            for (HospitalProfessional HP : Professionals) {
                names.add(HP.getName());
            }
            names.sort(String.CASE_INSENSITIVE_ORDER);
            SearchResultsListView.setItems(names);
        } else {
            for (HospitalProfessional HP : Professionals) {
                if (HP.getName().contains(S)) {
                    names.add(HP.getName());
                }
            }
            names.sort(String.CASE_INSENSITIVE_ORDER);
            SearchResultsListView.setItems(names);
        }
    }

    //This function takes a HospitalProfessional edits the DisplayInformation TextArea
    //with all the HP's associated information
    public void PopulateInformationDisplay(HospitalProfessional HP) {
        HospitalProfessionalService hs = new HospitalProfessionalService();
        //System.out.println(hs.find(HP.getId()).getOffices());
        String offices = "\nOffices:\n" + hs.find(HP.getId()).getOffices().get(0).getName();
        DisplayInformationTextArea.setText(HP.getName() + "\n\n" + HP.getTitle() + "\n" + offices);
    }

    //--------------------------------------------EVENT HANDLERS--------------------------------------------------

    public void handleClickedOnStartAtKiosk() {
        System.out.println("start at kiosk");
        Start_location_TextArea.setText("intersection18");
    }

    //This function is called when the user clicks on a Search Result.
    //Information unique to the ListView Item can be accessed
    public void handleClickedOnSearchResult() {
        System.out.println("clicked on " + SearchResultsListView.getSelectionModel().getSelectedItem());
        HospitalProfessionalService HPS = new HospitalProfessionalService();
        PopulateInformationDisplay(HPS.findHospitalProfessionalByName(SearchResultsListView.getSelectionModel().getSelectedItem().toString()));
        //FindandDisplayPath();
    }


    // function after clicking set start location
    public void SetStartLocationButtonClicked() {
        System.out.println("clicked on Set Start button");
        HospitalProfessionalService HPS = new HospitalProfessionalService();
        HPS.findHospitalProfessionalByName(SearchResultsListView.getSelectionModel().getSelectedItem().toString());
        HospitalProfessional HP = new HospitalProfessional();
        HP = HPS.findHospitalProfessionalByName(SearchResultsListView.getSelectionModel().getSelectedItem().toString());
        Start_location_TextArea.setText(HP.getName());
    }

    // function after clicking set destination location
    public void SetDestLocationButtonClicked() {
        System.out.println("clicked on Set Dest button");
        HospitalProfessionalService HPS = new HospitalProfessionalService();
        HPS.findHospitalProfessionalByName(SearchResultsListView.getSelectionModel().getSelectedItem().toString());
        HospitalProfessional HP = new HospitalProfessional();
        HP = HPS.findHospitalProfessionalByName(SearchResultsListView.getSelectionModel().getSelectedItem().toString());
        Dest_location_TextArea.setText(HP.getName());
    }

    public void switchLocationButtonClicked() {
        System.out.println("clicked on switch location button");


        String tempStorage = Start_location_TextArea.getText();
        System.out.println("tempStorage is" + tempStorage);
        Start_location_TextArea.setText(Dest_location_TextArea.getText());
        Dest_location_TextArea.setText(tempStorage);

        HospitalProfessionalService switched_Dest = new HospitalProfessionalService();
        switched_Dest.findHospitalProfessionalByName(Start_location_TextArea.getText());

    }


    public void getPathButtonClicked() {
        System.out.println("clicked on get path button");
        HospitalProfessionalService HPS = new HospitalProfessionalService();
        HospitalProfessional HP_Start = HPS.findHospitalProfessionalByName(Start_location_TextArea.getText());
        HospitalProfessional HP_Dest = HPS.findHospitalProfessionalByName(Dest_location_TextArea.getText());
        FindandDisplayPath(HP_Start, HP_Dest);

        System.out.println("start HP:  " + HP_Start.getName());
        System.out.println("dest HP:  " + HP_Dest.getName());
    }


    public void SearchBarTextField_keyReleased() {
        System.out.println("Searching");
        System.out.println(SearchBarTextField.getText().toString());
        PopulateSearchResults(SearchBarTextField.getText().toString());
    }

    //function for Help Button
    public void HandleHelpButton() throws Exception {
        System.out.println("HELP");
        System.out.println(language);
        // 1: english, 2: spanish, 3: chinese, 4: french
        //TODO: change once we set what te public void HandleHelpButton() {
        System.out.println("HELP");
        System.out.println(language);
        // 1: english, 2: spanish, 3: chinese, 4: french
        //TODO: change once we set what text will actually be shown here
        switch (language) {
            case 1: //english
//                System.out.println("Hours:  "+hours.hours1+":"+hours.minutes1+" "+hours.ampm1);
//                System.out.println("Hours:  "+hours.hours2+":"+hours.minutes2+" "+hours.ampm2);
//                System.out.println("Hours:  "+hours.hours3+":"+hours.minutes3+" "+hours.ampm3);
//                System.out.println("Hours:  "+hours.hours4+":"+hours.minutes4+" "+hours.ampm4);

                DisplayInformationTextArea.setText("To contact a hospital worker\n" +
                        "please call 774-278-8517\n\n"
                        + "Hospital Operating Hour:\n" +
                        "Morning Hours: " + hours.hours1 + ":" + hours.minutes1 + " " + hours.ampm1 + "-" +
                        hours.hours2 + ":" + hours.minutes2 + " " + hours.ampm2 + "\n" +
                        "Evening Hours: " + hours.hours3 + ":" + hours.minutes3 + " " + hours.ampm3 + "-" +
                        hours.hours4 + ":" + hours.minutes4 + " " + hours.ampm4);
                break;
            case 2: //spanish
                DisplayInformationTextArea.setText("Para contactar a un empleado\n" +
                        "porfavor llame 774-278-8517\n\n"
                        + "Horas de operacíon:\n" +
                        "Mañana : " + hours.hours1 + ":" + hours.minutes1 + " " + hours.ampm1 + "-" +
                        hours.hours2 + ":" + hours.minutes2 + " " + hours.ampm2 + "\n" +
                        "Atardecer : " + hours.hours3 + ":" + hours.minutes3 + " " + hours.ampm3 + "-" +
                        hours.hours4 + ":" + hours.minutes4 + " " + hours.ampm4);
                break;
            case 3: //chinese
                DisplayInformationTextArea.setText("拨打电话 774-278-8517 呼叫医院工作人员\n\n"
                        + "医院营业时间:\n" +
                        "白日: " + hours.hours1 + ":" + hours.minutes1 + " " + hours.ampm1 + "-" +
                        hours.hours2 + ":" + hours.minutes2 + " " + hours.ampm2 + "\n" +
                        "夜晚: " + hours.hours3 + ":" + hours.minutes3 + " " + hours.ampm3 + "-" +
                        hours.hours4 + ":" + hours.minutes4 + " " + hours.ampm4);
                break;
            case 4: //french
                DisplayInformationTextArea.setText("Contactez un employé de l'hôpital\n" +
                        "appelez s'il vous plaît 774-278-8517\n\n"
                        + "Heures d'ouverture:\n" +
                        "Matin: " + hours.hours1 + ":" + hours.minutes1 + " " + hours.ampm1 + "-" +
                        hours.hours2 + ":" + hours.minutes2 + " " + hours.ampm2 + "\n" +
                        "Soir: " + hours.hours3 + ":" + hours.minutes3 + " " + hours.ampm3 + "-" +
                        hours.hours4 + ":" + hours.minutes4 + " " + hours.ampm4);
                break;
            case 5: //Italian
                DisplayInformationTextArea.setText("Per contattare un dipendente dell'ospedale\n" +
                        "chiamare 774-278-8517\n\n"
                        + "Ore di servizio:\n" +
                        "Mattina: " + hours.hours1 + ":" + hours.minutes1 + " " + hours.ampm1 + "-" +
                        hours.hours2 + ":" + hours.minutes2 + " " + hours.ampm2 + "\n" +
                        "Notte: " + hours.hours3 + ":" + hours.minutes3 + " " + hours.ampm3 + "-" +
                        hours.hours4 + ":" + hours.minutes4 + " " + hours.ampm4);
                break;
            case 6: //Japanese
                DisplayInformationTextArea.setText("病院のスタッフを呼び出し、電話番号：774-278-8617\n\n"
                        + "病院ビジネス時間:\n" +
                        "日: " + hours.hours1 + ":" + hours.minutes1 + " " + hours.ampm1 + "-" +
                        hours.hours2 + ":" + hours.minutes2 + " " + hours.ampm2 + "\n" +
                        "夜: " + hours.hours3 + ":" + hours.minutes3 + " " + hours.ampm3 + "-" +
                        hours.hours4 + ":" + hours.minutes4 + " " + hours.ampm4);
                break;
            case 7: //Portuguese
                DisplayInformationTextArea.setText("Para entrar em contato com um funcionário do hospital\n" +
                        "ligue para 774-278-8517\n\n"
                        + "horas de operação:\n" +
                        "Manhã: " + hours.hours1 + ":" + hours.minutes1 + " " + hours.ampm1 + "-" +
                        hours.hours2 + ":" + hours.minutes2 + " " + hours.ampm2 + "\n" +
                        "Tarde: " + hours.hours3 + ":" + hours.minutes3 + " " + hours.ampm3 + "-" +
                        hours.hours4 + ":" + hours.minutes4 + " " + hours.ampm4);
                break;
            default:
                DisplayInformationTextArea.setText("To contact a hospital worker\n" +
                        "please call 774-278-8517");
                language = 1;
        }
    }

    //function for Panic Button
    public void HandlePanicButton() {
        NS = new NodeService();
        System.out.println(language);
        DisplayInformationTextArea.setText("Don't Panic");
        pathfinding.Map map = new Map(NS.getAllNodes());
        // 1: english, 2: spanish, 3: chinese, 4: french
        //TODO: change once we set what text will actually be shown here
        switch (language) {
            case 1: //english
                DisplayInformationTextArea.setText("Don't Panic! Call 774-278-8517!");
                break;
            case 2: //spanish
                DisplayInformationTextArea.setText("No se preocupe");
                break;
            case 3: //chinese
                DisplayInformationTextArea.setText("不要惊慌");
                break;
            case 4: //french
                DisplayInformationTextArea.setText("Ne paniquez pas");
                break;
            case 5: //Italian
                DisplayInformationTextArea.setText("Non fatevi prendere dal panico");
                break;
            case 6: //Japanese
                DisplayInformationTextArea.setText("パニックしないでください");
                break;
            case 7: //Portuguese
                DisplayInformationTextArea.setText("Não entre em pânico");
                break;
            default:
                DisplayInformationTextArea.setText("Don't Panic");
                language = 1;
        }
        DisplayMap(PathFinder.shortestPath(map.getNode(NS.findNodeByName("intersection18").getId()), map.getNode(NS.findNodeByName("Emergency Department").getId())));
    }

    //    //-------------------------------------SCREEN CHANGING FUNCTIONS---------------------------------------------------
//    @FXML
//    public void OpenAdminTool() throws Exception {
//        // goto genres screen
//        System.out.println("HERE WE ARE");
//        Stage stage = (Stage) AdminToolButton.getScene().getWindow();
//        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/AdminToolMenu.fxml"));
//        stage.setTitle("AdminToolMenu");
//        stage.setScene(new Scene(root, 300, 300));
//        stage.show();
//    }
    //SCREEN CHANGING FUNCTIONS
    @FXML
    public void OpenAdminTool() throws Exception {
        // goto genres screen
        switchScreen("view/LoginPage.fxml", "Login", AdminToolButton);
    }


    @FXML
    public void toEnglish() throws Exception {
        Stage stage = (Stage) languageMenuButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Main.fxml"));
        stage.setTitle("Faulkner Kiosk");
        stage.setScene(new Scene(root, 600, 400));
        stage.setMaximized(true);
        stage.show();
        // 1: english, 2: spanish, 3: chinese, 4: french
        language = 1;
    }

    @FXML
    public void toSpanish() throws Exception {
        Stage stage = (Stage) languageMenuButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Main_SP.fxml"));
        stage.setTitle("Faulkner Kiosk");
        stage.setScene(new Scene(root, 600, 400));
        stage.setMaximized(true);
        stage.show();
        // 1: english, 2: spanish, 3: chinese, 4: french
        language = 2;
    }

    @FXML
    public void toChinese() throws Exception {
        Stage stage = (Stage) languageMenuButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Main_CN.fxml"));
        stage.setTitle("Faulkner Kiosk");
        stage.setScene(new Scene(root, 600, 400));
        stage.setMaximized(true);
        stage.show();
        // 1: english, 2: spanish, 3: chinese, 4: french
        language = 3;
    }

    @FXML
    public void toFrench() throws Exception {
        Stage stage = (Stage) languageMenuButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Main_FR.fxml"));
        stage.setTitle("Faulkner Kiosk");
        stage.setScene(new Scene(root, 600, 400));
        stage.setMaximized(true);
        stage.show();
        // 1: english, 2: spanish, 3: chinese, 4: french
        language = 4;
    }

    @FXML
    public void toItalian() throws Exception {
        Stage stage = (Stage) languageMenuButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Main_IT.fxml"));
        stage.setTitle("Faulkner Kiosk");
        stage.setScene(new Scene(root, 600, 400));
        stage.setMaximized(true);
        stage.show();
        // 1: english, 2: spanish, 3: chinese, 4: french
        language = 5;
    }

    @FXML
    public void toJapanese() throws Exception {
        Stage stage = (Stage) languageMenuButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Main_JP.fxml"));
        stage.setTitle("Faulkner Kiosk");
        stage.setScene(new Scene(root, 600, 400));
        stage.setMaximized(true);
        stage.show();
        // 1: english, 2: spanish, 3: chinese, 4: french
        language = 6;
    }

    @FXML
    public void toPortuguese() throws Exception {
        Stage stage = (Stage) languageMenuButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Main_PG.fxml"));
        stage.setTitle("Faulkner Kiosk");
        stage.setScene(new Scene(root, 600, 400));
        stage.setMaximized(true);
        stage.show();
        // 1: english, 2: spanish, 3: chinese, 4: french
        language = 7;
    }
}

//  THIS COMMENTED CODE MAY BE NEEDED FOR MAINTAING VIEW OF MAP DURING ZOOM
//
//        FirstFloorSlider.valueProperty().addListener(new ChangeListener() {
//            @Override
//            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
//                System.out.println(FirstFloorSlider.getValue());
//                FirstFloorImageView.setFitWidth(FirstFloorSlider.getValue());
////                System.out.println("arg0: "+arg0); //entire new object
////                System.out.println("arg1: "+arg1); //old value
////                System.out.println("arg2: "+arg2); //new value
//            }
//        });
//        FirstFloorScrollPane.vvalueProperty().addListener(new ChangeListener(){
//           @Override
//           public void changed(ObservableValue arg0, Object arg1, Object arg2){
//               //System.out.println(FirstFloorScrollPane.getVvalue());
//               System.out.println("arg0: "+arg0); //entire new object
//               System.out.println("arg1: "+arg1); //old value
//               System.out.println("arg2: "+arg2); //new value
//               //FirstFloorScrollPane.setVvalue();
//           }
//        });


