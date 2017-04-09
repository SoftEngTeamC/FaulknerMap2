package controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import model.HospitalProfessional;
import pathfinding.MapNode;
import pathfinding.PathFinder;
import service.HospitalProfessionalService;
import service.NodeService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class MainController{
    //ImageView Objects
    @FXML
    private ImageView FirstFloorImage;
    @FXML
    private ImageView SecondFloorImage;
    @FXML
    private ImageView ThirdFloorImage;
    @FXML
    private ImageView FourthFloorImage;
    @FXML
    private ImageView FifthFloorImage;
    @FXML
    private ImageView SixthFloorImage;
    @FXML
    private ImageView SeventhFloorImage;
    //-------------------------------
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


    //INTIALIZE
    public void initialize() {
        //the bind function locks an element property to another elements property
        FirstFloorImage.fitWidthProperty().bind(FloorViewsTabPane.widthProperty());
        SecondFloorImage.fitWidthProperty().bind(FloorViewsTabPane.widthProperty());
        ThirdFloorImage.fitWidthProperty().bind(FloorViewsTabPane.widthProperty());
        FourthFloorImage.fitWidthProperty().bind(FloorViewsTabPane.widthProperty());
        FifthFloorImage.fitWidthProperty().bind(FloorViewsTabPane.widthProperty());
        SixthFloorImage.fitWidthProperty().bind(FloorViewsTabPane.widthProperty());
        SeventhFloorImage.fitWidthProperty().bind(FloorViewsTabPane.widthProperty());

//        PopulateSearchResults(null);
    }


    //PROXY FUNCTIONS

    //DisplayMap function takes a list of points(X,Y) and creates circles at all their positions and lines between them
    public void DisplayMap(List<MapNode> nodes){
        //MapAnchor.getChildren().clear();
        ImageView mapPic = new ImageView();
        Image floorImage = new Image("images/floor4.png");
        mapPic.setImage(floorImage);
        //mapPic.fitWidthProperty().bind(MapAnchor.widthProperty());
        mapPic.setPreserveRatio(true);
        mapPic.setPickOnBounds(true);
        //MapAnchor.getChildren().add(mapPic);

        if (nodes == null) {
            System.out.println("There is no path.");
            return;
        }
       for(int i=0;i<nodes.size();i++){
           MakeCircle(nodes.get(i).getLocation().getX(),nodes.get(i).getLocation().getY());
            if(i>0){
                MakeLine(nodes.get(i-1).getLocation().getX(),
                         nodes.get(i-1).getLocation().getY(),
                         nodes.get(i).getLocation().getX(),
                         nodes.get(i).getLocation().getY());
            }
        }
    }

    //MakeCircle creates a circle centered at the given X,Y relative to the initial size of the image
    //It locks the points to their position on the image,
    //Resizing the image does not effect the relative position of the nodes and the image
    public void MakeCircle(double x, double y) {
        // initial size of image and the image ratior
        double ImgW = FourthFloorImage.getImage().getWidth();
        double ImgH = FourthFloorImage.getImage().getHeight();
        double ImgR = ImgH / ImgW;

        Circle circle = new Circle();
        //These bind the center positions relative to the width property of the image
        //the new center is calculated using the initial ratios
        circle.centerXProperty().bind(FourthFloorImage.fitWidthProperty().multiply(x / ImgW));
        circle.centerYProperty().bind(FourthFloorImage.fitWidthProperty().multiply(ImgR).multiply(y / ImgH));
        circle.setRadius(3);
        circle.fillProperty().setValue(Paint.valueOf("#ff2d1f"));
        //MapAnchor.getChildren().add(circle);
    }

    //MakeLine take 2 points (effectively) and draws a line from point to point
    //this line is bounded to the image such that resizing does not effect the relative position of the line and image
    public void MakeLine(double x1, double y1, double x2, double y2) {
        double ImgW = FourthFloorImage.getImage().getWidth();
        double ImgH = FourthFloorImage.getImage().getHeight();
        double ImgR = ImgH / ImgW;

        Line edge = new Line();
        //the points are bound to the fit width property of the image and scaled by the initial image ratio
        edge.startXProperty().bind(FourthFloorImage.fitWidthProperty().multiply((x1 / ImgW)));
        edge.startYProperty().bind(FourthFloorImage.fitWidthProperty().multiply(ImgR).multiply((y1 / ImgH)));
        edge.endXProperty().bind(FourthFloorImage.fitWidthProperty().multiply((x2 / ImgW)));
        edge.endYProperty().bind(FourthFloorImage.fitWidthProperty().multiply(ImgR).multiply((y2 / ImgH)));
        //MapAnchor.getChildren().add(edge);
    }

    //This function takes a list of strings and updates the SearchResult ListView to contain those strings
    public void UpdateSearchResults(LinkedList<String> results){
        ObservableList<String> data = FXCollections.observableArrayList();
        data.addAll(results);
        SearchResultsListView.setItems(data);
    }

    public void FindandDisplayPath(HospitalProfessional HP){
        NodeService NS = new NodeService();
        pathfinding.Map map = new pathfinding.Map(NS.getAllNodes());
        MapNode start = map.getNode(NS.findNodeByName("UROLOGY").getId());
        MapNode dest = map.getNode(HP.getId());
        List<MapNode> path = PathFinder.shortestPath(start, dest);
        DisplayMap(path);
    }

    public void PopulateSearchResults(String S) {
        System.out.println("Populate Search String");
        HospitalProfessionalService HS = new HospitalProfessionalService();
        List<HospitalProfessional> Professionals = HS.getAllProfessionals();
        System.out.println(Professionals);
        ObservableList<String> names = FXCollections.observableArrayList();
        if(S == null)
        {
            System.out.println("null case");
            for(HospitalProfessional HP : Professionals){
                names.add(HP.getName());
            }
            SearchResultsListView.setItems(names);
        }
        else{
            for(HospitalProfessional HP : Professionals){
                if(HP.getName().contains(S)) {
                    names.add(HP.getName());
                }
            }
            SearchResultsListView.setItems(names);
        }
    }

    //This function takes a HospitalProfessional edits the DisplayInformation TextArea
    //with all the HP's associated information
    public void PopulateInformationDisplay(HospitalProfessional HP){
        //DisplayInformation.setText(HP.getName()+"\n\n"+HP.getTitle()+"\n"+HP.getLocation());
        System.out.println("trying to populate information area");
    }

    //SCREEN CHANGING FUNCTIONS
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

    // EVENT HANDLERS



    //This function is called when the user clicks on a Search Result.
    //Information unique to the ListView Item can be accessed
    public void handleClickedOnSearchResult(){
        System.out.println("clicked on " + SearchResultsListView.getSelectionModel().getSelectedItem());
        HospitalProfessionalService HPS = new HospitalProfessionalService();
        PopulateInformationDisplay(HPS.findHospitalProfessionalByName(SearchResultsListView.getSelectionModel().getSelectedItem().toString()));
        FindandDisplayPath(HPS.findHospitalProfessionalByName(SearchResultsListView.getSelectionModel().getSelectedItem().toString()));
    }

    /**
     * @author JVB
     *
     *  Method is called when the search bar has a key pressed.
     *  Populates the search ListView with search results from what's in the textfield
     *
     */
    public void SearchBarTextField_keyReleased(){
        System.out.println("Searching");
        System.out.println(SearchBarTextField.getText().toString());
        PopulateSearchResults(SearchBarTextField.getText().toString());
    }

    //function for Help Button
    public void HandleHelpButton(){
        System.out.println("HELP");
        DisplayInformationTextArea.setText("To contact a hospital worker\nplease call 774-278-8517");
    }

    //function for Panic Button
    public void HandlePanicButton(){
        DisplayInformationTextArea.setText("Don't Panic");
    }

    @FXML
    public void toEnglish() throws Exception {
        Stage stage = (Stage) languageMenuButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Main_EN.fxml"));
        stage.setTitle("Faulkner Kiosk");
        stage.setScene(new Scene(root, 600, 400));
        stage.setMaximized(true);
        stage.show();
    }

    @FXML
    public void toSpanish() throws Exception {
        Stage stage = (Stage) languageMenuButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Main_SP.fxml"));
        stage.setTitle("Faulkner Kiosk");
        stage.setScene(new Scene(root, 600, 400));
        stage.setMaximized(true);
        stage.show();
    }

    @FXML
    public void toChinese() throws Exception {
        Stage stage = (Stage) languageMenuButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Main_CN.fxml"));
        stage.setTitle("Faulkner Kiosk");
        stage.setScene(new Scene(root, 600, 400));
        stage.setMaximized(true);
        stage.show();
    }

    @FXML
    public void toFrench() throws Exception {
        Stage stage = (Stage) languageMenuButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Main_FR.fxml"));
        stage.setTitle("Faulkner Kiosk");
        stage.setScene(new Scene(root, 600, 400));
        stage.setMaximized(true);
        stage.show();
    }

}


