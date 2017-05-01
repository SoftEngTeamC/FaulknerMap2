package controller;

import Memento.LoginStatusMemento;
import Singleton.LoginStatusSingleton;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Coordinate;
import model.Edge;
import model.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class MapEditorController extends Controller {

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
    //---------------------
    @FXML
    private Text MapEditorInstructionText;
    @FXML
    private TabPane FloorViewsTabPane;
    @FXML
    private TabPane MapEditorTabPane;

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
    @FXML
    private Text RemoveNodeIndicatorText;


    // Add node objects
    @FXML
    private TextField addNode_nameField;
    @FXML
    private TextField addNode_xPos;
    @FXML
    private TextField addNode_yPos;
    @FXML
    private TextField addNode_floor;
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
    private AutocompletionlTextField editNode_addField;
    @FXML
    private Button editNode_addBtn;
    @FXML
    private Text AddNodeIndicatorText;
    @FXML
    private VBox EditNode_VBox;
    @FXML
    private AnchorPane EditNode_AnchorPane;

    // objects for the timeout editor
    @FXML
    private Button timeoutEditor_submitBtn;

    @FXML
    private TextField timeoutEditor_textField;

    @FXML
    private Text loginTimeout_errorText;

    @FXML
    private Text loginTimeout_SuccessText;

    @FXML
    protected TabPane tabPane;

    // Map imageview and anchorpane
    @FXML
    private ImageView imageView;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ListView<String> disableEdge_searchResultsList;

    @FXML
    private ListView<String> disableEdge_neighborsList;

    @FXML
    private Text node1NameText;

    @FXML
    private Text node2NameText;

    @FXML
    private Text ifDisableText;

    @FXML
    private Text ifUndoDisableText;

    @FXML
    private SplitPane MapEditor_SplitPane;

    // Images
    private Image floor4Image;

    // arraylist of search terms
    private ArrayList<String> searchList;
    private ArrayList<String> nodeList;

    private Node[] currNodes = new Node[2];

    private static int currFloor;

    class floorCircles {
        int floor;
        List<Circle> circles;

        floorCircles(int floor, List<Circle> circles) {
            this.floor = floor;
            this.circles = circles;
        }
    }

    private Stage stage;

    public MapEditorController() {
    }

    public void initialize() {
        startIdleListener(MapEditor_SplitPane, backBtn);
        List<Node> nodes = nodeService.getNodesByFloor(1);
        List<String> names = new ArrayList<>();
        for (Node n : nodes) {
            names.add(n.getName());
        }
        editNode_addField.getEntries().addAll(names);
        //Visual Initializations
        new ShowNodesEdgesHelper(FirstFloorScrollPane, SecondFloorScrollPane, ThirdFloorScrollPane,
                FourthFloorScrollPane, FifthFloorScrollPane, SixthFloorScrollPane,
                SeventhFloorScrollPane, FirstFloorSlider, SecondFloorSlider,
                ThirdFloorSlider, FourthFloorSlider, FifthFloorSlider, SixthFloorSlider,
                SeventhFloorSlider, FloorViewsTabPane);

        ShowNodesEdgesHelper.InitializeMapViews();
        editNode_searchResultsList.minWidthProperty().bind(EditNode_AnchorPane.widthProperty());
        editNode_searchResultsList.setMinHeight(200);
        editNode_neighborsList.setMinHeight(80);
        //EditNode_VBox.prefHeightProperty().bind(MapEditorTabPane.heightProperty());
        EditNode_VBox.prefHeightProperty().bind(MapEditorTabPane.heightProperty());
        InitializeIndicatorTextListeners();
        removeNode_searchFieldValueListener();

        // init local lists
        searchList = new ArrayList<>();
        nodeList = new ArrayList<>();

        //Populate the list of all nodes
        ArrayList<Node> allNode = new ArrayList<>(this.nodeService.getAllNodes());
        for (Node aNode : allNode) {
            this.nodeList.add(aNode.getName());
        }

        currFloor = 1;

        ArrayList<String> nameList = new ArrayList<>();
        for (Node n : nodeService.getNodesByFloor(currFloor)) {
            nameList.add(n.getName());
        }
        nameList.sort(String.CASE_INSENSITIVE_ORDER);
        ObservableList<String> obList = FXCollections.observableArrayList(nameList);

        editNode_searchResultsList.setItems(obList);
        removeNode_searchList.setItems(obList);


        tabPaneListen();
        System.out.println("INITRemoveNeighbourListener:");
        setEditNode_searchResultsListening();
        System.out.println("INITShowNodes:");
        List<Circle> circles = ShowNodesEdgesHelper.showNodes(currFloor);
        System.out.println("INITcirclesListen:");
        circlesListen(circles, currFloor);

        // make timeout button disabled
        timeoutEditor_submitBtn.setDisable(true);
        loginTimeout_errorText.setVisible(false);
        loginTimeout_SuccessText.setVisible(false);

        timeoutEditor_textField.textProperty().addListener((observable, oldValue, newValue) -> timeoutTextFieldChanged());

        startIdleListener(MapEditor_SplitPane, backBtn);
    }

    private void setStage(){
        stage = (Stage) backBtn.getScene().getWindow();
    }

    //-------------------------------------------Listeners---------------------------------------------
    private void circlesListen(List<Circle> circles, int floor) {
        final Circle[] firstCircle = new Circle[1];
        final double[] orgx = new double[1];
        final double[] orgy = new double[1];
        final double[] transx = new double[1];
        final double[] transy = new double[1];
        final boolean[] set = {false};
        for (Circle circle : circles) {
            circle.setOnMouseClicked(event -> {
                List<String> ItemsInListView = editNode_searchResultsList.getItems();
                int i = 0;
                for (i = 0; !(ItemsInListView.get(i).equals(nodeService.find(Long.parseLong(circle.getId())).getName())); i++) {
                }
                editNode_searchResultsList.getSelectionModel().select(i);
                removeNode_searchList.getSelectionModel().select(i);
                if (firstCircle[0] == null) {
                    System.out.println("two0 is null");
                    firstCircle[0] = circle;
                    circle.fillProperty().setValue(Paint.valueOf(Color.TEAL.toString()));
                } else {
                    System.out.println("two0 is not null");
                    circle.fillProperty().setValue(Paint.valueOf(Color.TEAL.toString()));

                    Edge edge1 = new Edge(nodeService.find(Long.parseLong(firstCircle[0].getId())),
                            nodeService.find(Long.parseLong(circle.getId())), 0);
                    Edge edge2 = new Edge(nodeService.find(Long.parseLong(circle.getId())),
                            nodeService.find(Long.parseLong(firstCircle[0].getId())), 0);

                    firstCircle[0] = null;
                    edgeService.persist(edge1);

                    circlesListen(ShowNodesEdgesHelper.showNodes(currFloor), currFloor);
                }
            });

            //Listener on the Map such that when map is clicked it removes any highlights
            //also resets the value for firstCircle such that it will begin the path adding again
            ScrollPane scrolly = ShowNodesEdgesHelper.checkScroll(floor);
            Group group = (Group) scrolly.getContent();
            ImageView Map = (ImageView) group.getChildren().get(0);
            Map.setOnMouseClicked(event -> {
                System.out.println("ClickedOnMap");
                ShowNodesEdgesHelper.resetDrawnShapeColors(floor);
                firstCircle[0] = null;
                ScrollPane tempScrollPane = ShowNodesEdgesHelper.checkScroll(currFloor);
                tempScrollPane.setPannable(true);
            });

            circle.setOnMouseDragged(event -> {
                ScrollPane tempScrollPane = ShowNodesEdgesHelper.checkScroll(currFloor);
                tempScrollPane.setPannable(false);

                if (!set[0]) {
                    orgx[0] = event.getSceneX();
                    orgy[0] = event.getSceneY();
                    transx[0] = ((Circle) (event.getSource())).getTranslateX();
                    transy[0] = ((Circle) (event.getSource())).getTranslateY();
                    set[0] = true;
                }

                double offsetX = event.getSceneX() - orgx[0];
                double offsetY = event.getSceneY() - orgy[0];
                double newTranslateX = transx[0] + offsetX;
                double newTranslateY = transy[0] + offsetY;

                ((Circle) (event.getSource())).setTranslateX(newTranslateX);
                ((Circle) (event.getSource())).setTranslateY(newTranslateY);


                Group group1 = (Group) tempScrollPane.getContent();

                ImageView Map1 = (ImageView) group1.getChildren().get(0);

                double ImgW = Map1.getImage().getWidth();
                double ImgH = Map1.getImage().getHeight();

                Node node = nodeService.find(Long.parseLong(circle.getId()));
                Coordinate coor = coordinateService.find(node.getLocation().getId());
                System.out.println("Before: " + coor.toString());
                System.out.println("Offsetx: " + offsetX);
                System.out.println("Offsety: " + offsetY);
                System.out.println("Offsetx/: " + (Map1.fitWidthProperty().multiply(offsetX / ImgW)).doubleValue());
                System.out.println("Offsety/: " + ((Map1.fitWidthProperty().multiply(offsetY / ImgH)).doubleValue()));
                coor.setX(coor.getX() + ((Map1.fitWidthProperty().multiply(offsetX / ImgW)).doubleValue()));
                coor.setY(coor.getY() + ((Map1.fitWidthProperty().multiply(offsetY / ImgH)).doubleValue()));
                System.out.println("After: " + coor.toString());
                coordinateService.merge(coor);

            });
        }
    }


    //This Listener is triggered when a different MapTab is selected
    private void tabPaneListen() {
        FloorViewsTabPane.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> {
                    // Update Current Floor
                    currFloor = Integer.parseInt(t1.getText().charAt(6) + "");
                    // Update EditTab Listviews with new floors data
                    ArrayList<String> nameList = new ArrayList<>();
                    for (Node n : nodeService.getNodesByFloor(currFloor)) {
                        nameList.add(n.getName());
                    }
                    nameList.sort(String.CASE_INSENSITIVE_ORDER);
                    ObservableList<String> obList = FXCollections.observableArrayList(nameList);
                    editNode_searchResultsList.setItems(obList);
                    removeNode_searchList.setItems(obList);
                    //Update AutoComplete suggestion of neighbors with new data
                    List<Node> nodes = nodeService.getNodesByFloor(currFloor);
                    List<String> names = new ArrayList<>();
                    for (Node n : nodes) {
                        names.add(n.getName());
                    }
                    editNode_addField.getEntries().clear();
                    editNode_addField.getEntries().addAll(names);

                    List<Circle> circles = ShowNodesEdgesHelper.showNodes(currFloor);
                    List<Edge> Edges = ShowNodesEdgesHelper.getEdges(currFloor);

                    circlesListen(circles, currFloor);
                }
        );
    }

    /**
     *
     * ensures submit button is on or off
     */
    public void timeoutTextFieldChanged() {
        // check to see if there's stuff in the textfield, if so, enable button, else disable it
        if(timeoutEditor_textField.getText().isEmpty()){
            timeoutEditor_submitBtn.setDisable(true);
        } else timeoutEditor_submitBtn.setDisable(false);
    }

    //This Listener is triggered when an item in the EditNode_SearchResults List is selected
    private void setEditNode_searchResultsListening() {
        editNode_searchResultsList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    System.out.println("editNode_SearchResultsList Listener");
                    //Update Current Node
                    Node selectedNode = nodeService.findNodeByName(newValue);
                    currNodes[0] = selectedNode;
                    ObservableList<String> RemoveNodeList = removeNode_searchList.getItems();
                    int j = 0;
                    for (j = 0; !newValue.equals(removeNode_searchList.getItems().get(j)); j++) {
                        //find the index of the item in the list
                    }
                    removeNode_searchList.getSelectionModel().select(j);
                    //Update Neighbors List
                    ObservableList<String> nList = FXCollections.observableArrayList(neighborNames(currNodes[0]));
                    editNode_neighborsList.setItems(nList);
                    //Clear old highlights
                    ShowNodesEdgesHelper.resetDrawnShapeColors(currFloor);
                    //HighLight Selected Node
                    // - get all Drawn Objects on current Map
                    // - find the ones with the same ID
                    // - If its a Circle Highlight it
                    String SelectedNodeID = nodeService.findNodeByName(newValue).getId().toString();
                    ScrollPane Scrolly = ShowNodesEdgesHelper.checkScroll(currFloor);
                    Group group = (Group) Scrolly.getContent();
                    List<javafx.scene.Node> DrawnObjects = group.getChildren();
                    for (int i = 1; i < DrawnObjects.size(); i++) {
                        if (DrawnObjects.get(i).getId().equals(SelectedNodeID)) {
                            try {
                                Circle circle = (Circle) DrawnObjects.get(i);
                                circle.fillProperty().setValue(Color.TEAL);
                            }
                            //found an edge instead
                            catch (Exception e) {
                                // TODO: This should do something
                            }
                        }
                    }
                });

        //This listener is triggered when the EditNode_Neighbors List is selected
        editNode_neighborsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("EditNode_NeighborsList Listener");
            //clear Old Highlights
            ShowNodesEdgesHelper.resetDrawnShapeColors(currFloor);
            //find start and end nodes
            Node end = nodeService.findNodeByName(newValue);
            Node start = nodeService.findNodeByName(editNode_searchResultsList.getSelectionModel().getSelectedItem());
            //Search Through Edges on Floor for one with same start/end
            String ID = null;
            List<Edge> edges = ShowNodesEdgesHelper.getEdges(currFloor);
            for (Edge e : edges) {
                if ((e.getStart().getId().equals(start.getId())) && (e.getEnd().getId().equals(end.getId()))) {
                    ID = e.getId().toString();
                }
            }
            //HighLight Selected Edge
            // - get all Drawn Objects on current Map
            // - find the ones with the same ID
            // - If its a Line, Highlight it
            ScrollPane Scrolly = ShowNodesEdgesHelper.checkScroll(currFloor);
            Group group = (Group) Scrolly.getContent();
            List<javafx.scene.Node> DrawnObjects = group.getChildren();
            for (int i = 1; i < DrawnObjects.size(); i++) {
                if (DrawnObjects.get(i).getId().equals(ID)) {
                    try {
                        //Line has been found
                        Line line = (Line) DrawnObjects.get(i);
                        line.setStroke(Color.BLUEVIOLET);
                        line.setStrokeWidth(3);
                    }
                    //Found a circle Instead
                    catch (Exception e) {
                        // TODO: This should also do something
                    }
                }
            }
            //Update Current Node
            currNodes[1] = end;
        });

    }


    /**
     * @author Samuel Coache
     * <p>
     * event handler for RemoveNode when the search button is pressed
     * Back button action event handler. Opens the Admin page
     */
    public void removeNode_searchBtnPressed() {
        try {
            String searchField = removeNode_searchField.getText();
            if (searchField.equals("")) {
                ObservableList<String> allOList = FXCollections.observableArrayList(this.nodeList);
                removeNode_searchList.setItems(allOList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * @author Samuel Coache
     * <p>
     * remove node tab: remove button event handler
     * Action event handler for logout button being pressed. Goes to main screen.
     */
    public void removeNode_removeBtnPressed() {
        String selectedItem = removeNode_searchList.getSelectionModel().getSelectedItem();
        Node selectNode = nodeService.findNodeByName(selectedItem);

        // print out the node from the database
        try {
            this.nodeService.remove(selectNode);
            this.searchList.remove(selectNode.getName());
            RemoveNodeIndicatorText.setText("Successfully Removed Node");
            RemoveNodeIndicatorText.setFill(Color.GREEN);
        } catch (Exception e) {
            RemoveNodeIndicatorText.setText("Unable to Remove Node");
            RemoveNodeIndicatorText.setFill(Color.RED);
            e.printStackTrace();
        }

        //repopulate the search list
        ObservableList<String> OList = FXCollections.observableArrayList(this.searchList);
        removeNode_searchList.setItems(OList);

    }

    /**
     * @author Paul
     *
     * event handler for the submit button.
     * if input is in the valid format, changes the timeout time in the memento singleton.
     *
     */
    public void timeoutEditor_submitBtnAction() {

        double seconds;

        // check valid input
        try {
            seconds = Double.parseDouble(timeoutEditor_textField.getText());
        } catch (NumberFormatException E) {
            seconds = -1;
        }

        // Either proceed, or throw error
        if(seconds < 0){
            // error
            loginTimeout_errorText.setVisible(true);
            loginTimeout_SuccessText.setVisible(false);
        } else{
            // submit
            LoginStatusSingleton status = LoginStatusSingleton.getInstance();
            status.setTimeout((int)seconds);
            loginTimeout_errorText.setVisible(false);
            loginTimeout_SuccessText.setVisible(true);
        }

    }


    /**
     * method that populates the search results with the search query
     */
    private void removeNode_searchFieldValueListener() {
        removeNode_searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // get the query from the field
            ArrayList<String> queryList = new ArrayList<>();
            // add each query to the list
            List<Node> nodeList = this.nodeService.getNodesByFloor(currFloor);
            for (Node n : nodeList) {
                if (n.getName().contains(newValue)) {
                    queryList.add(n.getName());
                }
            }
            // Make the list view show the results
            queryList.sort(String.CASE_INSENSITIVE_ORDER);
            removeNode_searchList.setItems(FXCollections.observableArrayList(queryList));
        });
    }


    /**
     * @author Samuel Coache
     * <p>
     * add node tab: create button event handler
     */
    public void addNode_createNodeBtnPressed() {
        float x = Float.parseFloat(addNode_xPos.getText());
        float y = Float.parseFloat(addNode_yPos.getText());
        float floor = Float.parseFloat(addNode_floor.getText());
        Coordinate addCoord = new Coordinate(x, y, (int) floor);
        coordinateService.persist(addCoord);
        Node newNode = new Node(addNode_nameField.getText(), addCoord);

        try {
            this.nodeService.merge(newNode);
            this.searchList.add(newNode.getName());
            AddNodeIndicatorText.setText("Successfully Added Node");
            AddNodeIndicatorText.setFill(Color.GREEN);
        } catch (Exception e) {
            AddNodeIndicatorText.setText("Unable to Add Node");
            AddNodeIndicatorText.setFill(Color.RED);
            e.printStackTrace();
        }

    }



    public void editNode_removeNeighborBtnPressed() {
        Node start = nodeService.findNodeByName(editNode_searchResultsList.getSelectionModel().getSelectedItem());
        Node end = nodeService.findNodeByName(editNode_neighborsList.getSelectionModel().getSelectedItem());
        Edge currEdge = edgeService.findByNodes(start, end);
        edgeService.remove(currEdge);

        ObservableList<String> nList = FXCollections.observableArrayList(neighborNames(currNodes[0]));
        editNode_neighborsList.setItems(nList);

        List<Circle> circles = ShowNodesEdgesHelper.showNodes(currFloor);
        circlesListen(circles, currFloor);

    }

    private List<String> neighborNames(Node node) {

        Set<Node> neighbors = nodeService.neighbors(node.getId());
        List<String> neighborsS = new ArrayList<>();

        for (Node n : neighbors) {
                neighborsS.add(n.getName());
        }
        neighborsS.sort(String.CASE_INSENSITIVE_ORDER);
        return neighborsS;

    }


    public void editNode_addBtnPressed() {
        Node newNode = nodeService.findNodeByName(editNode_addField.getText());

        if (newNode != null) {
            edgeService.persist(new Edge(currNodes[0], newNode, 0));
            ObservableList<String> nList = FXCollections.observableArrayList(neighborNames(currNodes[0]));
            editNode_neighborsList.setItems(nList);
        }
        List<Circle> circles = ShowNodesEdgesHelper.showNodes(currFloor);
        circlesListen(circles, currFloor);

    }

    public void HandleEditNodes_NeighborsListClicked() {

    }


    public void DisableEdgeButtonPressed() {
        Node start = nodeService.findNodeByName(editNode_searchResultsList.getSelectionModel().getSelectedItem());
        Node end = nodeService.findNodeByName(editNode_neighborsList.getSelectionModel().getSelectedItem());
        Edge selectedEdge = edgeService.findByNodes(start, end);

        selectedEdge.setDisabled(true);
        edgeService.merge(selectedEdge);

        List<Circle> circles = ShowNodesEdgesHelper.showNodes(currFloor);
        circlesListen(circles, currFloor);


    }


    public void UndoDisableEdgeButtonPressed() {
        Node start = nodeService.findNodeByName(editNode_searchResultsList.getSelectionModel().getSelectedItem());
        Node end = nodeService.findNodeByName(editNode_neighborsList.getSelectionModel().getSelectedItem());
        Edge selectedEdge = edgeService.findByNodes(start, end);

        selectedEdge.setDisabled(false);
        edgeService.merge(selectedEdge);

        List<Circle> circles = ShowNodesEdgesHelper.showNodes(currFloor);
        circlesListen(circles, currFloor);

    }

    //----------------------------------Indicator Text Listeners------------------------------------

    private void InitializeIndicatorTextListeners() {
        addNode_xPos.textProperty().addListener((arg0, arg1, arg2) -> AddNodeIndicatorText.setText(""));
        addNode_yPos.textProperty().addListener((arg0, arg1, arg2) -> AddNodeIndicatorText.setText(""));
        addNode_floor.textProperty().addListener((arg0, arg1, arg2) -> AddNodeIndicatorText.setText(""));
        addNode_nameField.textProperty().addListener((arg0, arg1, arg2) -> AddNodeIndicatorText.setText(""));
        removeNode_searchField.textProperty().addListener((arg0, arg1, arg2) -> AddNodeIndicatorText.setText(""));
    }

    //----------------------------------Screen Changing Functions-------------------------------------

    /**
     * Back button action event handler. Opens the Admin page
     */
    public void back() {
        setStage();
        switchScreen("view/AdminToolMenu.fxml", "Directory Editor", stage);
    }

    /**
     * Action event handler for logout button being pressed. Goes to main screen.
     */
    public void logout() {

        LoginStatusSingleton.getInstance().addMemento(new LoginStatusMemento(false));

        setStage();
        switchToMainScreen(logoutBtn);
    }
}


