package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.stage.Stage;
import model.Edge;
import model.Node;
import service.EdgeService;
import service.NodeService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class MapEditorController extends Controller {

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

    private Node[] currNodes = new Node[2];

    private int currFloor;


    public void initialize() {

        // Set the image view to populate the image
        floor4Image = new Image("file:../Resources/floor4.png");
        //floor4Image.widthProperty().bind(anchorPane.widthProperty());
        imageView = new ImageView(floor4Image);

        //mouse clicked handler, send x,y data to function
        anchorPane.setOnMouseClicked(event -> {
            // get the coordinates
            double x = event.getX();
            double y = event.getY();
            // send to function
            mouseClicked(x, y);
        });

        currFloor = 1;
        NodeService ns = new NodeService();
        ArrayList<String> nameList = new ArrayList<>();
        for (Node n : ns.getNodesByFloor(currFloor)) {
            nameList.add(n.getName());
        }
        ObservableList<String> obList = FXCollections.observableArrayList(nameList);

        editNode_searchResultsList.setItems(obList);

        tabPaneListen();
        removeNeighborListen();

    }

    public void tabPaneListen() {
        tabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        System.out.println("Tab Selection changed " + t.getText() + " to " + t1.getText());
                        NodeService ns = new NodeService();
                        currFloor = Integer.parseInt(t1.getText().charAt(0) + "");
                        ArrayList<String> nameList = new ArrayList<>();
                        for (Node n : ns.getNodesByFloor(currFloor)) {
                            nameList.add(n.getName());
                        }
                        ObservableList<String> obList = FXCollections.observableArrayList(nameList);

                        editNode_searchResultsList.setItems(obList);
                    }
                }
        );

    }

    public void removeNeighborListen() {
        editNode_searchResultsList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    NodeService ns = new NodeService();
                    Node selectedNode = ns.findNodeByName(newValue);

                    currNodes[0] = selectedNode;

                    Set<Node> neighbors = ns.neighbors(selectedNode.getId());
                    ArrayList<String> neighborsS = new ArrayList<>();
                    for (Node node : neighbors) {
                        neighborsS.add(node.getName());
                    }
                    ObservableList<String> nList = FXCollections.observableArrayList(neighborsS);
                    editNode_neighborsList.setItems(nList);
                });

        editNode_neighborsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            NodeService ns = new NodeService();
            Node selectedNode = ns.findNodeByName(newValue);
            currNodes[1] = selectedNode;
        });
    }

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

    // Methods for the remove node tab

    /**
     * remove node tab: search button event handler
     */
    public void removeNode_searchBtnPressed() {
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
    public void removeNode_removeBtnPressed() {

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
    public void addNode_connectToNodeBtnPressed() {

    }

    //
//
    public void addNode_createNodeBtnPressed() {

//        float x = Float.parseFloat(addNode_xPos.getText());
//        float y = Float.parseFloat(addNode_yPos.getText());
//        Node newNode = new Node(null, new Coordinate(x, y, 4), addNode_nameField.getText());
//        nodesHelper.addNode(newNode);
    }

    //
//    // methods for the edit node tab
//
    public void editNode_searchBtnPressed() {
    }

    public void editNode_removeNeighborBtnPressed() {
        NodeService ns = new NodeService();
        EdgeService es = new EdgeService();


        List<Edge> currEdges = es.findByNodes(currNodes[0], currNodes[1]);

        for(Edge curr : currEdges){
            es.remove(curr);
        }

        Set<Node> neighbors = ns.neighbors(currNodes[0].getId());
        List<String> neighborsS = new ArrayList<>();
        for(Node node: neighbors){
            neighborsS.add(node.getName());
        }
        ObservableList<String> nList = FXCollections.observableArrayList(neighborsS);
        editNode_neighborsList.setItems(nList);
    }

    //
//
    public void editNode_addBtnPressed() {

//        NodeService ns = new NodeService();
//        Node newNode = ns.findNodeByName(editNode_addField.getText());
//        if (newNode != null) {
//            currNodes[0].addEdge(newNode);
//
//            ArrayList<Node> neighbors = EdgesHelper.getNeighbors(currNodes[0]);
//            ArrayList<String> neighborsS = new ArrayList<>();
//            for (Node node : neighbors) {
//                neighborsS.add(node.getName());
//            }
//            ObservableList<String> nList = FXCollections.observableArrayList(neighborsS);
//            editNode_neighborsList.setItems(nList);
//        }

    }


    public void imageClicked() {

    }

    /**
     * Handles what happens when mouse is clicked
     *
     * @param x value
     * @param y value
     */
    private void mouseClicked(double x, double y) {

    }
}
