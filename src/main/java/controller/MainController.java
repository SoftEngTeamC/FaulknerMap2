package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Edge;
import model.HospitalProfessional;
import model.Hours;
import model.Node;
import pathfinding.Map;
import pathfinding.MapNode;
import pathfinding.PathFinder;
import service.EMFProvider;
import textDirections.MakeDirections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    @FXML
    private TabPane FloorViewsTabPane;
    //---------------------------------------------------
    @FXML
    private VBox MainVbox;
    @FXML
    private ImageView LogoImageView;
    @FXML
    private AutocompletionlTextField StartLocationField;
    @FXML
    private Button SwitchStartEnd_Button;
    @FXML
    private Button StartAtKioskButton;
    @FXML
    private AutocompletionlTextField EndLocationField;
    @FXML
    private Button getPathButton;
    @FXML
    private TabPane Info_TabPane;
    @FXML
    private TextArea StartInfo_TextArea;
    @FXML
    private TextArea EndInfo_TextArea;
    @FXML
    private TextArea TextDirectionsTextArea;
    @FXML
    private Button HelpButton;
    @FXML
    private MenuButton languageMenuButton;
    @FXML
    private Button AboutUsButton;
    @FXML
    private Button AdminToolButton;

    public Hours hours;
    private static int language; // 1: english, 2: spanish, 3: chinese, 4: french

    //-------------------------------------------------INTIALIZE--------------------------------------------------------
    public void initialize() {
        //Hours initialization
        EMFProvider emf = new EMFProvider();
        hours = emf.hours;
        //-----------------------------Visual inits
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

        //-----------------------Data initialization
        List<HospitalProfessional> HPs = professionalService.getAllProfessionals();
        List<String> names = new ArrayList<>();
        for (HospitalProfessional HP : HPs) {
            names.add(HP.getName());
        }
        StartLocationField.getEntries().addAll(names);
        EndLocationField.getEntries().addAll(names);

        //default is english
        // 1: english, 2: spanish, 3: chinese, 4: french
        language = 1;
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
            ShowNodesEdgesHelper.MakeCircle(node.getModelNode());
        }

        for(int i = 0; i < nodes.size() - 1; i++){
            Node start = nodes.get(i).getModelNode();
            Node end = nodes.get(i+1).getModelNode();
            //find the edge from the database.
            // 0 index because findByNodes returns list of edges, forward and backwards
            Edge e = edgeService.findByNodes(start, end);
            //only draw if not last node, nodes are on same floor
            if((i<nodes.size()-1)&&(nodes.get(i).getLocation().getFloor() == nodes.get(i+1).getLocation().getFloor())){
                ShowNodesEdgesHelper.MakeLine(e);
            }
        }
        HideTabs(nodes);
    }

    //-----------------------------------FUNCTIONS------------------------------------------
    private void FindandDisplayPath(HospitalProfessional HP_Start, HospitalProfessional HP_Dest) {
        Map map = new Map(nodeService.getAllNodes());

        Node nodeStart = HP_Start.getOffices().get(0);
        Node nodeEnd = HP_Dest.getOffices().get(0);

        MapNode start = map.getNode(nodeStart.getId());
        MapNode dest = map.getNode(nodeEnd.getId());

        List<MapNode> path = map.shortestPath(start, dest);
        if (path.size() < 2) {
            TextDirectionsTextArea.setText("You are already at your destination");
        } else {
            TextDirectionsTextArea.setText(MakeDirections.getText(path));
        }
        DisplayMap(path);
    }

    //This function updates the StartInfo and EndInfo Text Areas
    public void PopulateInformationDisplay() {
        HospitalProfessional StartProfessional = professionalService.findHospitalProfessionalByName(StartLocationField.getText());
        StartInfo_TextArea.setText(StartProfessional.getTitle()+" "+StartProfessional.getName()+"\n\n"
                                    +"Offices:\n\n"+StartProfessional.getOffices());
        HospitalProfessional EndProfessional = professionalService.findHospitalProfessionalByName(EndLocationField.getText());
        EndInfo_TextArea.setText(EndProfessional.getTitle()+" "+EndProfessional.getName()+"\n\n"
                +"Offices:\n\n"+EndProfessional.getOffices());
    }

    public void HideTabs(List<MapNode> path){
        Set<Integer> floors = Map.floorsInPath(path);
        ObservableList<Tab> tabs = FloorViewsTabPane.getTabs();
        //Turn all tabs on
        for(Tab t: tabs){
            t.setDisable(false);
        }
        //disable tabs that are not included in path
        for(int n: floors){
            tabs.get(n-1).setDisable(true);
        }
    }


    //--------------------------------------------EVENT HANDLERS--------------------------------------------------

    public void handleClickedOnStartAtKiosk(){
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

    public void getPathButtonClicked(){
        System.out.println("clicked on get path button");
        if(!(StartLocationField.getText().isEmpty() || EndLocationField.getText().isEmpty())){
            //Do path finding
            HospitalProfessional HP_Start = professionalService.findHospitalProfessionalByName(StartLocationField.getText());
            HospitalProfessional HP_Dest = professionalService.findHospitalProfessionalByName(EndLocationField.getText());
            FindandDisplayPath(HP_Start, HP_Dest);
            System.out.println("start HP:  " + HP_Start.getName());
            System.out.println("dest HP:  " + HP_Dest.getName());
            //Set Information Displays
            PopulateInformationDisplay();
        }
        else{
            System.out.print("Give a Start and an End");
        }
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

                StartInfo_TextArea.setText("To contact a hospital worker\n" +
                        "please call 774-278-8517\n\n"
                        + "Hospital Operating Hour:\n" +
                        "Morning Hours: " + hours.hours1 + ":" + hours.minutes1 + " " + hours.ampm1 + "-" +
                        hours.hours2 + ":" + hours.minutes2 + " " + hours.ampm2 + "\n" +
                        "Evening Hours: " + hours.hours3 + ":" + hours.minutes3 + " " + hours.ampm3 + "-" +
                        hours.hours4 + ":" + hours.minutes4 + " " + hours.ampm4);
                break;
            case 2: //spanish
                StartInfo_TextArea.setText("Para contactar a un empleado\n" +
                        "porfavor llame 774-278-8517\n\n"
                        + "Horas de operacíon:\n" +
                        "Mañana : " + hours.hours1 + ":" + hours.minutes1 + " " + hours.ampm1 + "-" +
                        hours.hours2 + ":" + hours.minutes2 + " " + hours.ampm2 + "\n" +
                        "Atardecer : " + hours.hours3 + ":" + hours.minutes3 + " " + hours.ampm3 + "-" +
                        hours.hours4 + ":" + hours.minutes4 + " " + hours.ampm4);
                break;
            case 3: //chinese
                StartInfo_TextArea.setText("拨打电话 774-278-8517 呼叫医院工作人员\n\n"
                        + "医院营业时间:\n" +
                        "白日: " + hours.hours1 + ":" + hours.minutes1 + " " + hours.ampm1 + "-" +
                        hours.hours2 + ":" + hours.minutes2 + " " + hours.ampm2 + "\n" +
                        "夜晚: " + hours.hours3 + ":" + hours.minutes3 + " " + hours.ampm3 + "-" +
                        hours.hours4 + ":" + hours.minutes4 + " " + hours.ampm4);
                break;
            case 4: //french
                StartInfo_TextArea.setText("Contactez un employé de l'hôpital\n" +
                        "appelez s'il vous plaît 774-278-8517\n\n"
                        + "Heures d'ouverture:\n" +
                        "Matin: " + hours.hours1 + ":" + hours.minutes1 + " " + hours.ampm1 + "-" +
                        hours.hours2 + ":" + hours.minutes2 + " " + hours.ampm2 + "\n" +
                        "Soir: " + hours.hours3 + ":" + hours.minutes3 + " " + hours.ampm3 + "-" +
                        hours.hours4 + ":" + hours.minutes4 + " " + hours.ampm4);
                break;
            case 5: //Italian
                StartInfo_TextArea.setText("Per contattare un dipendente dell'ospedale\n" +
                        "chiamare 774-278-8517\n\n"
                        + "Ore di servizio:\n" +
                        "Mattina: " + hours.hours1 + ":" + hours.minutes1 + " " + hours.ampm1 + "-" +
                        hours.hours2 + ":" + hours.minutes2 + " " + hours.ampm2 + "\n" +
                        "Notte: " + hours.hours3 + ":" + hours.minutes3 + " " + hours.ampm3 + "-" +
                        hours.hours4 + ":" + hours.minutes4 + " " + hours.ampm4);
                break;
            case 6: //Japanese
                StartInfo_TextArea.setText("病院のスタッフを呼び出し、電話番号：774-278-8617\n\n"
                        + "病院ビジネス時間:\n" +
                        "日: " + hours.hours1 + ":" + hours.minutes1 + " " + hours.ampm1 + "-" +
                        hours.hours2 + ":" + hours.minutes2 + " " + hours.ampm2 + "\n" +
                        "夜: " + hours.hours3 + ":" + hours.minutes3 + " " + hours.ampm3 + "-" +
                        hours.hours4 + ":" + hours.minutes4 + " " + hours.ampm4);
                break;
            case 7: //Portuguese
                StartInfo_TextArea.setText("Para entrar em contato com um funcionário do hospital\n" +
                        "ligue para 774-278-8517\n\n"
                        + "horas de operação:\n" +
                        "Manhã: " + hours.hours1 + ":" + hours.minutes1 + " " + hours.ampm1 + "-" +
                        hours.hours2 + ":" + hours.minutes2 + " " + hours.ampm2 + "\n" +
                        "Tarde: " + hours.hours3 + ":" + hours.minutes3 + " " + hours.ampm3 + "-" +
                        hours.hours4 + ":" + hours.minutes4 + " " + hours.ampm4);
                break;
            default:
                StartInfo_TextArea.setText("To contact a hospital worker\n" +
                        "please call 774-278-8517");
                language = 1;
        }
    }

    //function for Panic Button
    public void HandlePanicButton() {
        System.out.println(language);
        StartInfo_TextArea.setText("Don't Panic");
        Map map = new Map(nodeService.getAllNodes());
        // 1: english, 2: spanish, 3: chinese, 4: french
        //TODO: change once we set what text will actually be shown here
        switch (language) {
            case 1: //english
                StartInfo_TextArea.setText("Don't Panic! Call 774-278-8517!");
                break;
            case 2: //spanish
                StartInfo_TextArea.setText("No se preocupe");
                break;
            case 3: //chinese
                StartInfo_TextArea.setText("不要惊慌");
                break;
            case 4: //french
                StartInfo_TextArea.setText("Ne paniquez pas");
                break;
            case 5: //Italian
                StartInfo_TextArea.setText("Non fatevi prendere dal panico");
                break;
            case 6: //Japanese
                StartInfo_TextArea.setText("パニックしないでください");
                break;
            case 7: //Portuguese
                StartInfo_TextArea.setText("Não entre em pânico");
                break;
            default:
                StartInfo_TextArea.setText("Don't Panic");
                language = 1;
        }
        DisplayMap(PathFinder.shortestPath(map.getNode(nodeService.findNodeByName("intersection18").getId()), map.getNode(nodeService.findNodeByName("Emergency Department").getId())));
    }
    
    //-------------------------------------SCREEN CHANGING FUNCTIONS---------------------------------------------------
    @FXML
    public void OpenAdminTool() throws Exception {
        switchScreen("view/LoginPage.fxml","Login", AdminToolButton);
    }

    @FXML
    public void OpenAboutUs() throws Exception {
        switchScreen("view/AboutUs.fxml", "About Us", AboutUsButton);
    }

    //LANGUAGE CHANGES
    //Note that these do not use the switchscreen function because they do not have buttons to pass
    @FXML
    public void toEnglish() throws Exception {
        //switchScreen("view/Main.fxml", "Faulkner Kiosk", languageMenuButton);
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


