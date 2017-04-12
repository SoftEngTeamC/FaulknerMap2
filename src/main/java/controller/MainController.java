package controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import model.HospitalProfessional;
import pathfinding.MapNode;
import pathfinding.PathFinder;
import service.HospitalProfessionalService;
import service.NodeService;

import java.util.LinkedList;
import java.util.List;
public class MainController extends Controller{

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
    
    private static int language; // 1: english, 2: spanish, 3: chinese, 4: french


    //-------------------------------------------------INTIALIZE--------------------------------------------------------
    public void initialize() {
        InitializeMapViews();
        PopulateSearchResults(null);
        SearchResultsListView.prefHeightProperty().bind(MainVbox.heightProperty().multiply(0.3));
        DisplayInformationTextArea.prefHeightProperty().bind(MainVbox.heightProperty().multiply(0.5));
        DisplayInformationTextArea.prefHeightProperty().bind(MainVbox.heightProperty().multiply(0.5));
        CheckBoxesHBox.setPrefHeight(30);

        PathLocationHBox.prefHeightProperty().bind(MainVbox.heightProperty().multiply(0.05));



        MakeCircle(1000,1000,4);
        MakeLine(1000,1000,2000,2000,2);

        //default is english
        // 1: english, 2: spanish, 3: chinese, 4: french
        language = 1;

    }

//-------------------------------------------DISPLAY PATH DRAWING FUNCTIONS---------------------------------------------
    //DisplayMap function takes a list of points(X,Y) and creates circles at all their positions and lines between them
    public void DisplayMap(List<MapNode> nodes){
        System.out.println(nodes);
        ClearOldPaths();
        if (nodes == null) {System.out.println("There is no path.");return;}

        for(int i=0;i<nodes.size();i++){
            MakeCircle(nodes.get(i).getLocation().getX(),
                        nodes.get(i).getLocation().getY(),
                        nodes.get(i).getLocation().getFloor());
            if((i>0) && (nodes.get(i).getLocation().getFloor() == nodes.get(i++).getLocation().getFloor())){
                MakeLine(nodes.get(i-1).getLocation().getX(), //
                         nodes.get(i-1).getLocation().getY(),
                         nodes.get(i).getLocation().getX(),
                         nodes.get(i).getLocation().getY(),
                         nodes.get(i).getLocation().getFloor());
            }
        }
    }

    //MakeCircle creates a circle centered at the given X,Y relative to the initial size of the image
    //It locks the points to their position on the image,
    //Resizing the image does not effect the relative position of the nodes and the image
    public void MakeCircle(double x, double y, int z) {
        // initial size of image and the image ratior
        ScrollPane Scrolly = null;
        switch(z){
            case 1:
                Scrolly = FirstFloorScrollPane;
                break;
            case 2:
                Scrolly = SecondFloorScrollPane;
                break;
            case 3:
                Scrolly = ThirdFloorScrollPane;
                break;
            case 4:
                Scrolly = FourthFloorScrollPane;
                break;
            case 5:
                Scrolly = FifthFloorScrollPane;
                break;
            case 6:
                Scrolly = SixthFloorScrollPane;
                break;
            case 7:
                Scrolly = SeventhFloorScrollPane;
                break;
            default:
                System.out.println("You gave MakeCircle() a floor that doesnt exist, or it isnt an int");
                break;
        }
        Group group1 = (Group)Scrolly.getContent();
        ImageView Map1 = (ImageView)group1.getChildren().get(0);

        double ImgW = Map1.getImage().getWidth();
        double ImgH = Map1.getImage().getHeight();
        double ImgR = ImgH / ImgW;

        Circle circle = new Circle();
        //These bind the center positions relative to the width property of the image
        //the new center is calculated using the initial ratios
        circle.centerXProperty().bind(Map1.fitWidthProperty().multiply(x / ImgW));
        circle.centerYProperty().bind(Map1.fitWidthProperty().multiply(ImgR).multiply(y / ImgH));
        circle.radiusProperty().bind(Map1.fitWidthProperty().multiply(10/ImgW));
        circle.fillProperty().setValue(Paint.valueOf("#ff2d1f"));
        group1.getChildren().add(circle);
    }

    //MakeLine take 2 points (effectively) and draws a line from point to point
    //this line is bounded to the image such that resizing does not effect the relative position of the line and image
    public void MakeLine(double x1, double y1, double x2, double y2, int z){
        ScrollPane Scrolly = null;
        switch(z){
            case 1:
                Scrolly = FirstFloorScrollPane;
                break;
            case 2:
                Scrolly = SecondFloorScrollPane;
                break;
            case 3:
                Scrolly = ThirdFloorScrollPane;
                break;
            case 4:
                Scrolly = FourthFloorScrollPane;
                break;
            case 5:
                Scrolly = FifthFloorScrollPane;
                break;
            case 6:
                Scrolly = SixthFloorScrollPane;
                break;
            case 7:
                Scrolly = SeventhFloorScrollPane;
                break;
            default:
                System.out.println("You gave MakeCircle() a floor that doesnt exist, or it isnt an int");
                break;
        }
        Group group1 = (Group)Scrolly.getContent();
        ImageView Map1 = (ImageView)group1.getChildren().get(0);

        double ImgW = Map1.getImage().getWidth();
        double ImgH = Map1.getImage().getHeight();
        double ImgR = ImgH / ImgW;

        Line edge = new Line();
        //the points are bound to the fit width property of the image and scaled by the initial image ratio
        edge.startXProperty().bind(Map1.fitWidthProperty().multiply((x1 / ImgW)));
        edge.startYProperty().bind(Map1.fitWidthProperty().multiply(ImgR).multiply((y1 / ImgH)));
        edge.endXProperty().bind(Map1.fitWidthProperty().multiply((x2 / ImgW)));
        edge.endYProperty().bind(Map1.fitWidthProperty().multiply(ImgR).multiply((y2 / ImgH)));
        group1.getChildren().add(edge);
    }

    public void ClearOldPaths(){
        Group group1 = (Group) FirstFloorScrollPane.getContent();
        group1.getChildren().remove(1,group1.getChildren().size());
        Group group2 = (Group) SecondFloorScrollPane.getContent();
        group2.getChildren().remove(1,group2.getChildren().size());
        Group group3 = (Group) ThirdFloorScrollPane.getContent();
        group3.getChildren().remove(1,group3.getChildren().size());
        Group group4 = (Group) FourthFloorScrollPane.getContent();
        group4.getChildren().remove(1,group4.getChildren().size());
        Group group5 = (Group) FifthFloorScrollPane.getContent();
        group5.getChildren().remove(1,group5.getChildren().size());
        Group group6 = (Group) SixthFloorScrollPane.getContent();
        group6.getChildren().remove(1,group6.getChildren().size());
        Group group7 = (Group) SeventhFloorScrollPane.getContent();
        group7.getChildren().remove(1,group7.getChildren().size());
    }

    //------------------------------------UPDATING VISUAL DATA FUNCTIONS------------------------------------------------

    //This function takes a list of strings and updates the SearchResult ListView to contain those strings
    public void UpdateSearchResults(LinkedList<String> results) {
        ObservableList<String> data = FXCollections.observableArrayList();
        data.addAll(results);
        SearchResultsListView.setItems(data);
    }

    public void FindandDisplayPath(HospitalProfessional HP_Start, HospitalProfessional HP_Dest) {
        NodeService NS = new NodeService();
        pathfinding.Map map = new pathfinding.Map(NS.getAllNodes());
        MapNode start = map.getNode(HP_Start.getId());
        MapNode dest = map.getNode(HP_Dest.getId());
        List<MapNode> path = PathFinder.shortestPath(start, dest);
        DisplayMap(path);
    }

    public void PopulateSearchResults(String S) {
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
            SearchResultsListView.setItems(names);
        } else {
            for (HospitalProfessional HP : Professionals) {
                if (HP.getName().contains(S)) {
                    names.add(HP.getName());
                }
            }
            SearchResultsListView.setItems(names);
        }
    }

    //This function takes a HospitalProfessional edits the DisplayInformation TextArea
    //with all the HP's associated information
    public void PopulateInformationDisplay(HospitalProfessional HP){
        HospitalProfessionalService hs = new HospitalProfessionalService();
        //System.out.println(hs.find(HP.getId()).getOffices());
        String offices = "\nOffices:\n" + hs.find(HP.getId()).getOffices().get(0).getName();
        DisplayInformationTextArea.setText(HP.getName()+"\n\n"+HP.getTitle()+"\n"+offices);
    }

    //--------------------------------------------EVENT HANDLERS--------------------------------------------------

    //This function is called when the user clicks on a Search Result.
    //Information unique to the ListView Item can be accessed
    public void handleClickedOnSearchResult() {
        System.out.println("clicked on " + SearchResultsListView.getSelectionModel().getSelectedItem());
        HospitalProfessionalService HPS = new HospitalProfessionalService();
        PopulateInformationDisplay(HPS.findHospitalProfessionalByName(SearchResultsListView.getSelectionModel().getSelectedItem().toString()));
    }


    // function after clicking set start location
    public void SetStartLocationButtonClicked(){
        System.out.println("clicked on Set Start button");
        HospitalProfessionalService HPS = new HospitalProfessionalService();
        HPS.findHospitalProfessionalByName(SearchResultsListView.getSelectionModel().getSelectedItem().toString());
        HospitalProfessional HP = new HospitalProfessional();
        HP = HPS.findHospitalProfessionalByName(SearchResultsListView.getSelectionModel().getSelectedItem().toString());
        Start_location_TextArea.setText(HP.getName());
    }

    // function after clicking set destination location
    public void SetDestLocationButtonClicked(){
        System.out.println("clicked on Set Dest button");
        HospitalProfessionalService HPS = new HospitalProfessionalService();
        HPS.findHospitalProfessionalByName(SearchResultsListView.getSelectionModel().getSelectedItem().toString());
        HospitalProfessional HP = new HospitalProfessional();
        HP = HPS.findHospitalProfessionalByName(SearchResultsListView.getSelectionModel().getSelectedItem().toString());
        Dest_location_TextArea.setText(HP.getName());
    }

    public void switchLocationButtonClicked(){
        System.out.println("clicked on switch location button");


        String tempStorage = Start_location_TextArea.getText();
        System.out.println("tempStorage is" + tempStorage);
        Start_location_TextArea.setText(Dest_location_TextArea.getText());
        Dest_location_TextArea.setText(tempStorage);

        HospitalProfessionalService switched_Dest = new HospitalProfessionalService();
        switched_Dest.findHospitalProfessionalByName(Start_location_TextArea.getText());

    }


    public void getPathButtonClicked(){
        System.out.println("clicked on get path button");
        HospitalProfessionalService HPS_Start = new HospitalProfessionalService();
        HospitalProfessional HP_Start = HPS_Start.findHospitalProfessionalByName(Start_location_TextArea.getText());

        HospitalProfessionalService HPS_Dest = new HospitalProfessionalService();
        HospitalProfessional HP_Dest = HPS_Dest.findHospitalProfessionalByName(Dest_location_TextArea.getText());

        FindandDisplayPath(HP_Start,HP_Dest);

        System.out.println("start HP:  " + HP_Start.getName());
        System.out.println("dest HP:  " + HP_Dest.getName());
    }



    public void SearchBarTextField_keyReleased() {
        System.out.println("Searching");
        System.out.println(SearchBarTextField.getText().toString());
        PopulateSearchResults(SearchBarTextField.getText().toString());
    }

    //function for Help Button
    public void HandleHelpButton() {
        System.out.println("HELP");
        System.out.println(language);
        // 1: english, 2: spanish, 3: chinese, 4: french
        //TODO: change once we set what text will actually be shown here
        switch (language) {
            case 1: //english
                DisplayInformationTextArea.setText("To contact a hospital worker\n" +
                        "please call 774-278-8517");
                break;
            case 2: //spanish
                DisplayInformationTextArea.setText("To contact a hospital worker\n" +
                        "please call 774-278-8517" +
                        "\n WILL CHANGE TO SPANISH SOON");
                break;
            case 3: //chinese
                DisplayInformationTextArea.setText("To contact a hospital worker\n" +
                        "please call 774-278-8517" +
                        "\n WILL CHANGE TO CHINESE SOON");
                break;
            case 4: //french
                DisplayInformationTextArea.setText("To contact a hospital worker\n" +
                        "please call 774-278-8517" +
                        "\n WILL CHANGE TO FRENCH SOON");
                break;
            default:
                DisplayInformationTextArea.setText("To contact a hospital worker\n" +
                        "please call 774-278-8517");
                language = 1;
        }
    }

    //function for Panic Button
    public void HandlePanicButton() {
        System.out.println(language);
        DisplayInformationTextArea.setText("Don't Panic");
        // 1: english, 2: spanish, 3: chinese, 4: french
        //TODO: change once we set what text will actually be shown here
        switch (language) {
            case 1: //english
                DisplayInformationTextArea.setText("Don't Panic");
                break;
            case 2: //spanish
                DisplayInformationTextArea.setText("Don't Panic" +
                        "\n WILL CHANGE TO SPANISH SOON");
                break;
            case 3: //chinese
                DisplayInformationTextArea.setText("Don't Panic" +
                        "\n WILL CHANGE TO CHINESE SOON");
                break;
            case 4: //french
                DisplayInformationTextArea.setText("Don't Panic" +
                        "\n WILL CHANGE TO FRENCH SOON");
                break;
            default:
                DisplayInformationTextArea.setText("Don't Panic");
                language = 1;
        }
    }

    //----------------------------------Build Zoomable Maps----------------------------------------------
    public void InitializeMapViews(){
        //FIRST FLOOR
        FirstFloorScrollPane.prefWidthProperty().bind(FloorViewsTabPane.widthProperty());
        FirstFloorScrollPane.prefHeightProperty().bind(FloorViewsTabPane.heightProperty());
        ImageView FirstFloorImageView = new ImageView();
        Image FirstFloorMapPic = new Image("images/1_thefirstfloor.png");
        FirstFloorImageView.setImage(FirstFloorMapPic);
        FirstFloorImageView.setPreserveRatio(true);
        Group FirstFloorGroup = new Group();
        FirstFloorGroup.getChildren().add(FirstFloorImageView);
        FirstFloorScrollPane.setContent(FirstFloorGroup);
        FirstFloorScrollPane.setPannable(true);
        FirstFloorScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        FirstFloorScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        FirstFloorSlider.setMax(FirstFloorMapPic.getWidth());
        FirstFloorSlider.minProperty().bind(FloorViewsTabPane.widthProperty());
        FirstFloorImageView.fitWidthProperty().bind(FirstFloorSlider.valueProperty());
        //SECOND FLOOR
        SecondFloorScrollPane.prefWidthProperty().bind(FloorViewsTabPane.widthProperty());
        SecondFloorScrollPane.prefHeightProperty().bind(FloorViewsTabPane.heightProperty());
        ImageView SecondFloorImageView = new ImageView();
        Image SecondFloorMapPic = new Image("images/2_thesecondfloor.png");
        SecondFloorImageView.setImage(SecondFloorMapPic);
        SecondFloorImageView.setPreserveRatio(true);
        Group SecondFloorGroup = new Group();
        SecondFloorGroup.getChildren().add(SecondFloorImageView);
        SecondFloorScrollPane.setContent(SecondFloorGroup);
        SecondFloorScrollPane.setPannable(true);
        SecondFloorScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        SecondFloorScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        SecondFloorSlider.setMax(SecondFloorMapPic.getWidth());
        SecondFloorSlider.minProperty().bind(FloorViewsTabPane.widthProperty());
        SecondFloorImageView.fitWidthProperty().bind(SecondFloorSlider.valueProperty());
        //THIRD FLOOR
        ThirdFloorScrollPane.prefWidthProperty().bind(FloorViewsTabPane.widthProperty());
        ThirdFloorScrollPane.prefHeightProperty().bind(FloorViewsTabPane.heightProperty());
        ImageView ThirdFloorImageView = new ImageView();
        Image ThirdFloorMapPic = new Image("images/3_thethirdfloor.png");
        ThirdFloorImageView.setImage(ThirdFloorMapPic);
        ThirdFloorImageView.setPreserveRatio(true);
        Group ThirdFloorGroup = new Group();
        ThirdFloorGroup.getChildren().add(ThirdFloorImageView);
        ThirdFloorScrollPane.setContent(ThirdFloorGroup);
        ThirdFloorScrollPane.setPannable(true);
        ThirdFloorScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        ThirdFloorScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        ThirdFloorSlider.setMax(ThirdFloorMapPic.getWidth());
        ThirdFloorSlider.minProperty().bind(FloorViewsTabPane.widthProperty());
        ThirdFloorImageView.fitWidthProperty().bind(ThirdFloorSlider.valueProperty());
        //FOURTH FLOOR
        FourthFloorScrollPane.prefWidthProperty().bind(FloorViewsTabPane.widthProperty());
        FourthFloorScrollPane.prefHeightProperty().bind(FloorViewsTabPane.heightProperty());
        ImageView FourthFloorImageView = new ImageView();
        Image FourthFloorMapPic = new Image("images/4_thefourthfloor.png");
        FourthFloorImageView.setImage(FourthFloorMapPic);
        FourthFloorImageView.setPreserveRatio(true);
        Group FourthFloorGroup = new Group();
        FourthFloorGroup.getChildren().add(FourthFloorImageView);
        FourthFloorScrollPane.setContent(FourthFloorGroup);
        FourthFloorScrollPane.setPannable(true);
        FourthFloorScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        FourthFloorScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        FourthFloorSlider.setMax(FourthFloorMapPic.getWidth());
        FourthFloorSlider.minProperty().bind(FloorViewsTabPane.widthProperty());
        FourthFloorImageView.fitWidthProperty().bind(FourthFloorSlider.valueProperty());
        //FIFTH FLOOR
        FifthFloorScrollPane.prefWidthProperty().bind(FloorViewsTabPane.widthProperty());
        FifthFloorScrollPane.prefHeightProperty().bind(FloorViewsTabPane.heightProperty());
        ImageView FifthFloorImageView = new ImageView();
        Image FifthFloorMapPic = new Image("images/5_thefifthfloor.png");
        FifthFloorImageView.setImage(FifthFloorMapPic);
        FifthFloorImageView.setPreserveRatio(true);
        Group FifthFloorGroup = new Group();
        FifthFloorGroup.getChildren().add(FifthFloorImageView);
        FifthFloorScrollPane.setContent(FifthFloorGroup);
        FifthFloorScrollPane.setPannable(true);
        FifthFloorScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        FifthFloorScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        FifthFloorSlider.setMax(FifthFloorMapPic.getWidth());
        FifthFloorSlider.minProperty().bind(FloorViewsTabPane.widthProperty());
        FifthFloorImageView.fitWidthProperty().bind(FifthFloorSlider.valueProperty());
        //SIXTH FLOOR
        SixthFloorScrollPane.prefWidthProperty().bind(FloorViewsTabPane.widthProperty());
        SixthFloorScrollPane.prefHeightProperty().bind(FloorViewsTabPane.heightProperty());
        ImageView SixthFloorImageView = new ImageView();
        Image SixthFloorMapPic = new Image("images/6_thesixthfloor.png");
        SixthFloorImageView.setImage(SixthFloorMapPic);
        SixthFloorImageView.setPreserveRatio(true);
        Group SixthFloorGroup = new Group();
        SixthFloorGroup.getChildren().add(SixthFloorImageView);
        SixthFloorScrollPane.setContent(SixthFloorGroup);
        SixthFloorScrollPane.setPannable(true);
        SixthFloorScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        SixthFloorScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        SixthFloorSlider.setMax(SixthFloorMapPic.getWidth());
        SixthFloorSlider.minProperty().bind(FloorViewsTabPane.widthProperty());
        SixthFloorImageView.fitWidthProperty().bind(SixthFloorSlider.valueProperty());
        //SEVENTH FLOOR
        SeventhFloorScrollPane.prefWidthProperty().bind(FloorViewsTabPane.widthProperty());
        SeventhFloorScrollPane.prefHeightProperty().bind(FloorViewsTabPane.heightProperty());
        ImageView SeventhFloorImageView = new ImageView();
        Image SeventhFloorMapPic = new Image("images/7_theseventhfloor.png");
        SeventhFloorImageView.setImage(SeventhFloorMapPic);
        SeventhFloorImageView.setPreserveRatio(true);
        Group SeventhFloorGroup = new Group();
        SeventhFloorGroup.getChildren().add(SeventhFloorImageView);
        SeventhFloorScrollPane.setContent(SeventhFloorGroup);
        SeventhFloorScrollPane.setPannable(true);
        SeventhFloorScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        SeventhFloorScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        SeventhFloorSlider.setMax(SeventhFloorMapPic.getWidth());
        SeventhFloorSlider.minProperty().bind(FloorViewsTabPane.widthProperty());
        SeventhFloorImageView.fitWidthProperty().bind(SeventhFloorSlider.valueProperty());
    }

    //-------------------------------------SCREEN CHANGING FUNCTIONS---------------------------------------------------
    @FXML
    public void OpenAdminTool() throws Exception {
        // goto genres screen
        System.out.println("HERE WE ARE");
        Stage stage = (Stage) AdminToolButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/AdminToolMenu.fxml"));
        stage.setTitle("AdminToolMenu");
        stage.setScene(new Scene(root, 300, 300));
        stage.show();
    }
    //SCREEN CHANGING FUNCTIONS
//    @FXML
//    public void OpenAdminTool() throws Exception {
//        // goto genres screen
//        switchScreen("view/AdminToolMenu.fxml", "AdminToolMenu", AdminToolButton);
//    }


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


