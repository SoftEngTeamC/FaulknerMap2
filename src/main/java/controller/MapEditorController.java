package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import model.Coordinate;
import model.Edge;
import model.Node;
import service.CoordinateService;
import service.EdgeService;
import service.NodeService;

import java.io.IOException;
import java.util.*;


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
    private VBox MapEditorVBox;

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
    protected TabPane tabPane;

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
    private ArrayList<String> nodeList;
    private NodeService NS;
    private EdgeService ES;

    private Node[] currNodes = new Node[2];

    private int currFloor;

    public void initialize() {
        this.NS = new NodeService();
        this.ES = new EdgeService();
        List<Node> nodes = NS.getNodesByFloor(1);
        List<String> names = new ArrayList<>();
        for (Node n : nodes) {
            names.add(n.getName());
        }
        editNode_addField.getEntries().addAll(names);

        new ShowNodesEdgesHelper( FirstFloorScrollPane,  SecondFloorScrollPane, ThirdFloorScrollPane,
                FourthFloorScrollPane, FifthFloorScrollPane,  SixthFloorScrollPane,
                 SeventhFloorScrollPane, FirstFloorSlider,  SecondFloorSlider,
                 ThirdFloorSlider,  FourthFloorSlider, FifthFloorSlider,  SixthFloorSlider,
                 SeventhFloorSlider, FloorViewsTabPane);

        ShowNodesEdgesHelper.InitializeMapViews();

        InitializeIndicatorTextListeners();

        // Set the image view to populate the image
        floor4Image = new Image("file:../Resources/floor4.png");
        //floor4Image.widthProperty().bind(anchorPane.widthProperty());
        imageView = new ImageView(floor4Image);

        // init local lists
        searchList = new ArrayList<>();
        nodeList = new ArrayList<>();

        //Populate the list of all nodes
        ArrayList<Node> allNode = new ArrayList<>(this.NS.getAllNodes());
        for (Node aNode : allNode) {
            this.nodeList.add(aNode.getName());
        }

//        //mouse clicked handler, send x,y data to function
//        anchorPane.setOnMouseClicked(event -> {
//            // get the coordinates
//            double x = event.getX();
//            double y = event.getY();
//            // send to function
//            mouseClicked(x,y);
//        });


        currFloor = 1;

        ArrayList<String> nameList = new ArrayList<>();
        for (Node n : NS.getNodesByFloor(currFloor)) {
            nameList.add(n.getName());
        }
        Collections.sort(nameList, String.CASE_INSENSITIVE_ORDER);
        ObservableList<String> obList = FXCollections.observableArrayList(nameList);

        editNode_searchResultsList.setItems(obList);

        tabPaneListen();

        removeNeighborListen();

        List<Circle> circles = ShowNodesEdgesHelper.showNodes(currFloor);

        circlesListen(circles);
    }

    public void circlesListen(List<Circle> circles){
        final Circle[] firstCircle = new Circle[1];
        for(Circle circle: circles) {
            circle.setOnMouseClicked(event -> {
                System.out.println("Clicked on node: " +
                        NS.find(Long.parseLong(circle.getId())).getName());
                if(firstCircle[0] == null){
                    System.out.println("two0 is null");
                    firstCircle[0] = circle;
                    circle.fillProperty().setValue(Paint.valueOf(Color.TEAL.toString()));
                } else {
                    System.out.println("two0 is not null");
                    circle.fillProperty().setValue(Paint.valueOf(Color.TEAL.toString()));

                    Edge edge1 = new Edge(NS.find(Long.parseLong(firstCircle[0].getId())),
                            NS.find(Long.parseLong(circle.getId())), 0);
                    Edge edge2 = new Edge(NS.find(Long.parseLong(circle.getId())),
                            NS.find(Long.parseLong(firstCircle[0].getId())), 0);

                    firstCircle[0] = null;
                    ES.persist(edge1);
                    ES.persist(edge2);

                    circlesListen(ShowNodesEdgesHelper.showNodes(currFloor));
                    return;
                }

            });
        }
    }

    public void tabPaneListen() {
        FloorViewsTabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        System.out.println("Tab Selection changed " + t.getText() + " to " + t1.getText());
                        currFloor = Integer.parseInt(t1.getText().charAt(6) + "");
                        ArrayList<String> nameList = new ArrayList<>();
                        for (Node n : NS.getNodesByFloor(currFloor)) {
                            nameList.add(n.getName());
                        }

                        Collections.sort(nameList, String.CASE_INSENSITIVE_ORDER);
                        ObservableList<String> obList = FXCollections.observableArrayList(nameList);

                        editNode_searchResultsList.setItems(obList);

                        List<Node> nodes = NS.getNodesByFloor(currFloor);
                        List<String> names = new ArrayList<>();
                        for (Node n : nodes) {
                            names.add(n.getName());
                        }
                        editNode_addField.getEntries().clear();
                        editNode_addField.getEntries().addAll(names);

                        List<Circle> circles = ShowNodesEdgesHelper.showNodes(currFloor);

                        circlesListen(circles);
                    }
                }
        );

    }

    public void removeNeighborListen() {
        editNode_searchResultsList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    Node selectedNode = NS.findNodeByName(newValue);

                    currNodes[0] = selectedNode;
                    ObservableList<String> nList = FXCollections.observableArrayList(neighborNames(currNodes[0]));
                    editNode_neighborsList.setItems(nList);
                });

        editNode_neighborsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Node selectedNode = NS.findNodeByName(newValue);
            currNodes[1] = selectedNode;
        });


    }


    // Methods for the remove node tab

    /**
     * @author Samuel Coache
     * <p>
     * event handler for RemoveNode when the search button is pressed
     * Back button action event handler. Opens the Admin page
     */
    public void removeNode_searchBtnPressed() {
        try {
            String searchField = removeNode_searchField.getText();
            //System.out.println("searchField is: " + searchField);
            if (searchField.equals("")) {
                ObservableList<String> allOList = FXCollections.observableArrayList(this.nodeList);
                removeNode_searchList.setItems(allOList);
                //} else {
                //String selectedName = (this.NS.findNodeByName(searchField)).getName();
                //System.out.println("selectName is: " + selectedName);
                //ArrayList<String> nodeName = new ArrayList<>();
                //nodeName.add(selectedName);
                //System.out.println("nodeName is: " + nodeName);
                //ObservableList<String> OList = FXCollections.observableArrayList(nodeName);
                //removeNode_searchList.setItems(OList);
            }
        } catch (Exception E) {
            System.out.println("Searching Error");
            E.printStackTrace();
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
        System.out.println(selectedItem);
        Node selectNode = NS.findNodeByName(selectedItem);
        System.out.println(selectNode.getName());
        this.searchList.remove(selectNode.getName());
        // print out the node we made
        System.out.println(selectNode.getId());
        // print out the node from the database
        try {
            this.NS.remove(selectNode);
            RemoveNodeIndicatorText.setText("Successfully Removed Node");
            RemoveNodeIndicatorText.setFill(Color.GREEN);
        } catch (Exception e) {
            RemoveNodeIndicatorText.setText("Unable to Remove Node");
            RemoveNodeIndicatorText.setFill(Color.RED);
        }

        //repopulate the search list
        ObservableList<String> OList = FXCollections.observableArrayList(this.searchList);
        removeNode_searchList.setItems(OList);
    }

    /**
     * method that populates the search results with the search query
     */
    public void removeNode_searchFieldKeyPressed() {
        // get the query from the field
        String query = removeNode_searchField.getText();
        ArrayList<String> queryList = new ArrayList<>();
        // add each query to the list
        List<Node> nodeList = this.NS.getAllNodes();
        for (Node n : nodeList) {
            if (n.getName().contains(query))
                queryList.add(n.getName());
        }
        // Make the list view show the results
        Collections.sort(queryList, String.CASE_INSENSITIVE_ORDER);
        removeNode_searchList.setItems(FXCollections.observableArrayList(queryList));
    }

    /**
     * @author Samuel Coache
     * <p>
     * add node tab: connect button event handler
     */
    public void addNode_connectToNodeBtnPressed() {

    }

    /**
     * @author Samuel Coache
     * <p>
     * add node tab: create button event handler
     */
    public void addNode_createNodeBtnPressed() {
        CoordinateService CS = new CoordinateService();
        float x = Float.parseFloat(addNode_xPos.getText());
        float y = Float.parseFloat(addNode_yPos.getText());
        float floor = Float.parseFloat(addNode_floor.getText());
        Coordinate addCoord = new Coordinate(x, y, 4);
        CS.persist(addCoord);
        Node newNode = new Node(addNode_nameField.getText(), addCoord);
        try {
            //TODO make successful text
            NS.merge(newNode);
            AddNodeIndicatorText.setText("Successfully Added Node");
            AddNodeIndicatorText.setFill(Color.GREEN);
        } catch (Exception e) {
            //TODO make warning text visible
            AddNodeIndicatorText.setText("Unable to Add Node");
            AddNodeIndicatorText.setFill(Color.RED);
        }
    }

    //    // methods for the edit node tab
//
    public void editNode_searchBtnPressed() {
    }

    public void editNode_removeNeighborBtnPressed() {
        EdgeService es = new EdgeService();


        List<Edge> currEdges = es.findByNodes(currNodes[0], currNodes[1]);

        for (Edge curr : currEdges) {
            es.remove(curr);
        }

        ObservableList<String> nList = FXCollections.observableArrayList(neighborNames(currNodes[0]));
        editNode_neighborsList.setItems(nList);

        List<Circle> circles = ShowNodesEdgesHelper.showNodes(currFloor);

        circlesListen(circles);
    }

    private List<String> neighborNames(Node node){
        Set<Node> neighbors = NS.neighbors(node.getId());
        System.out.println("currNode: " + node.getId());
        List<String> neighborsS = new ArrayList<>();
        for (Node n : neighbors) {
            if (!Objects.equals(n.getId(), node.getId())) {
                neighborsS.add(n.getName());
            }
        }
        Collections.sort(neighborsS, String.CASE_INSENSITIVE_ORDER);
        return neighborsS;
    }


    public void editNode_addBtnPressed() {

        Node newNode = NS.findNodeByName(editNode_addField.getText());
        if (newNode != null) {
            EdgeService es = new EdgeService();
            es.persist(new Edge(currNodes[0], newNode, 0));
            es.persist(new Edge(newNode, currNodes[0], 0));

            ObservableList<String> nList = FXCollections.observableArrayList(neighborNames(currNodes[0]));
            editNode_neighborsList.setItems(nList);
        }
        List<Circle> circles = ShowNodesEdgesHelper.showNodes(currFloor);

        circlesListen(circles);
    }

    //----------------------------------Indicator Text Listeners------------------------------------
    public void InitializeIndicatorTextListeners() {
        addNode_xPos.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                AddNodeIndicatorText.setText("");
            }
        });
        addNode_yPos.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                AddNodeIndicatorText.setText("");
            }
        });
        addNode_floor.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                AddNodeIndicatorText.setText("");
            }
        });
        addNode_nameField.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                AddNodeIndicatorText.setText("");
            }
        });
        removeNode_searchField.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                AddNodeIndicatorText.setText("");
            }
        });
    }

    //----------------------------------Sreen Changing Functions-------------------------------------

    /**
     * Back button action event handler. Opens the Admin page
     */
    public void back() throws IOException {
        switchScreen("view/AdminToolMenu.fxml", "Directory Editor", backBtn);
    }

    /**
     * Action event handler for logout button being pressed. Goes to main screen.
     */
    public void logout() throws IOException {
        switchScreen("view/Main.fxml", "Main", logoutBtn);
    }
}


