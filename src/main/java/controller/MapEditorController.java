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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapEditorController extends Controller{
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

    // Back and logout buttons
    @FXML
    private Button backBtn;
    @FXML
    private Button logoutBtn;

    // Remove Node objects
    @FXML
    private TextField removeNode_searchField;
    @FXML
    private ListView<String> removeNode_searchList;
    @FXML
    private Button removeNode_searchBtn;
    @FXML
    private Button removeNode_removeBtn;


    // Add node objects
    @FXML
    private TextField addNode_nameField;
    @FXML
    private TextField addNode_xPos;
    @FXML
    private TextField addNode_yPos;
    @FXML
    private ListView<String> addNode_connectedNodesList;
    @FXML
    private Button addNode_createNodeBtn;
    @FXML
    private ListView<String> addNode_unconnectedNodesList;
    @FXML
    private Button addNode_connectToNodeBtn;

    // Edit node objects
    @FXML
    private TextField editNode_searchField;
    @FXML
    private Button editNode_searchBtn;
    @FXML
    private ListView<String> editNode_searchResultsList;
    @FXML
    private ListView<String> editNode_neighborsList;
    @FXML
    private Button editNode_removeNeighborBtn;
    @FXML
    private TextField editNode_addField;
    @FXML
    private Button editNode_addBtn;



    // Map imageview and anchorpane
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane anchorPane;

    // Images
    private Image floor4Image;

    // database helper

    // arraylist of search terms
    private ArrayList<String> searchList;


    public void initialize(){
        InitializeMapViews();

        // Set the image view to populate the image
        floor4Image = new Image("file:../Resources/floor4.png");
        //floor4Image.widthProperty().bind(anchorPane.widthProperty());
        imageView = new ImageView(floor4Image);

//        //mouse clicked handler, send x,y data to function
//        anchorPane.setOnMouseClicked(event -> {
//            // get the coordinates
//            double x = event.getX();
//            double y = event.getY();
//            // send to function
//            mouseClicked(x,y);
//        });

    }

    /**
     * Back button action event handler. Opens the Admin page
     *
     */
    public void back() throws IOException {
        switchScreen("view/AdminToolMenu.fxml", "Directory Editor", backBtn);
    }

    /**
     * Action event handler for logout button being pressed. Goes to main screen.
     *
     */
    public void logout() throws IOException {
        switchScreen("view/Main.fxml", "Main", logoutBtn);
    }

    // Methods for the remove node tab

    /**
     * remove node tab: search button event handler
     *
     */
    public void removeNode_searchBtnPressed(){
//        try {
//            String searchField = removeNode_searchField.getText();
//            System.out.println("searchField is: " + searchField);
//            if(searchField.equals("")){
//                ArrayList<Node> allNode = NodesHelper.getNodes("NAME");
//                this.searchList = new ArrayList<>();
//
//                for (Node anAllNode : allNode) {
//                    this.searchList.add(anAllNode.getName());
//                }
//                ObservableList<String> allOList = FXCollections.observableArrayList(this.searchList);
//                removeNode_searchList.setItems(allOList);
//
//            }
//            else {
//                String selectedName = NodesHelper.getNodeByName(searchField).getName();
//                System.out.println("selectName is: " + selectedName);
//                ArrayList<String> nodeName = new ArrayList<>();
//                nodeName.add(selectedName);
//                System.out.println("nodeName is: " + nodeName);
//                ObservableList<String> OList = FXCollections.observableArrayList(nodeName);
//                removeNode_searchList.setItems(OList);
//            }
//
//        }
//        catch (Exception E){
//            System.out.println("Searching Error");
//            E.printStackTrace();
//        }
    }
//
//    /**
//     * remove node tab: remove button event handler
//     *
//     */
    public void removeNode_removeBtnPressed(){

//        String selectedItem = removeNode_searchList.getSelectionModel().getSelectedItem();
//        System.out.println(selectedItem);
//        Node selectNode = NodesHelper.getNodeByName(selectedItem);
//        this.searchList.remove(selectNode.getName());
//        NodesHelper.deleteNode(selectNode);
//
//        //repopulate the search list
//        ObservableList<String> OList = FXCollections.observableArrayList(this.searchList);
//        System.out.println("We got to this point in the code");
//        removeNode_searchList.setItems(OList);


    }
//
//    // Methods for the add node tab
//
////    /**
////     * @author Paul
////     *
////     * add node tab: remove button event handler
////     *
////     */
    public void addNode_connectToNodeBtnPressed(){

    }
//
//
    public void addNode_createNodeBtnPressed(){

//        float x = Float.parseFloat(addNode_xPos.getText());
//        float y = Float.parseFloat(addNode_yPos.getText());
//        Node newNode = new Node(null, new Coordinate(x, y, 4), addNode_nameField.getText());
//        nodesHelper.addNode(newNode);
    }
//
//    // methods for the edit node tab
//
    public void editNode_searchBtnPressed(){
//        List<Node> list = NodesHelper.getNodes(null);
//        ArrayList<String> nameList = new ArrayList<>();
//        for(Node node: list){
//            nameList.add(node.getName());
        }
//
//        ObservableList<String> obList = FXCollections.observableArrayList(nameList);
//
//        editNode_searchResultsList.setItems(obList);
//
//        editNode_searchResultsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            Node selectedNode = NodesHelper.getNodeByName(newValue);
//            currNodes[0] = selectedNode;
//            ArrayList<Node> neighbors = EdgesHelper.getNeighbors(selectedNode);
//            ArrayList<String> neighborsS = new ArrayList<>();
//            for(Node node: neighbors){
//                neighborsS.add(node.getName());
//            }
//            ObservableList<String> nList = FXCollections.observableArrayList(neighborsS);
//            editNode_neighborsList.setItems(nList);
//        });
//
//        editNode_neighborsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            Node selectedNode = NodesHelper.getNodeByName(newValue);
//            currNodes[1] = selectedNode;
//        });
//    }
//
//
    public void editNode_removeNeighborBtnPressed(){

//        ArrayList<Edge> currEdges = edgesHelper.getEdgeByNode(currNodes[0], currNodes[1]);
//
//        for(Edge curr : currEdges){
//            edgesHelper.deleteEdge(curr);
//        }
//
//        ArrayList<Node> neighbors = EdgesHelper.getNeighbors(currNodes[0]);
//        ArrayList<String> neighborsS = new ArrayList<>();
//        for(Node node: neighbors){
//            neighborsS.add(node.getName());
//        }
//        ObservableList<String> nList = FXCollections.observableArrayList(neighborsS);
//        editNode_neighborsList.setItems(nList);

    }
//
//
    public void editNode_addBtnPressed(){

//        Node newNode = NodesHelper.getNodeByName(editNode_addField.getText());
//        if (newNode != null){
//            currNodes[0].addEdge(newNode);
//
//            ArrayList<Node> neighbors = EdgesHelper.getNeighbors(currNodes[0]);
//            ArrayList<String> neighborsS = new ArrayList<>();
//            for(Node node: neighbors){
//                neighborsS.add(node.getName());
//            }
//            ObservableList<String> nList = FXCollections.observableArrayList(neighborsS);
//            editNode_neighborsList.setItems(nList);
//        }

    }

    public void imageClicked(){

    }

    /**
     * Handles what happens when mouse is clicked
     *
     * @param x value
     * @param y value
     *
     */
    private void mouseClicked(double x, double y){
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
        SecondFloorImageView.setImage(FirstFloorMapPic);
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
}
