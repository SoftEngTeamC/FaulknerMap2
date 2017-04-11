package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.Coordinate;
import model.Node;
import service.NodeService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapEditorController extends Controller{

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
    private ArrayList<String> nodeList;
    private NodeService NS;


    public void initialize(){

        // Set the image view to populate the image
        floor4Image = new Image("file:../Resources/floor4.png");
        //floor4Image.widthProperty().bind(anchorPane.widthProperty());
        imageView = new ImageView(floor4Image);

        //Populate the list of all nodes
        this.NS = new NodeService();
        ArrayList<Node> allNode = new ArrayList<Node>(NS.getAllNodes());
        for (Node aNode : allNode) {
            this.nodeList.add(aNode.getName());
        }

        //mouse clicked handler, send x,y data to function
        anchorPane.setOnMouseClicked(event -> {
            // get the coordinates
            double x = event.getX();
            double y = event.getY();
            // send to function
            mouseClicked(x,y);
        });

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
     * @author Samuel Coache
     *
     * event handler for RemoveNode when the search button is pressed
     *
     */
    public void removeNode_searchBtnPressed(){
        try {
            String searchField = removeNode_searchField.getText();
            //System.out.println("searchField is: " + searchField);

            if(searchField.equals("")){
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

        }
        catch (Exception E){
            System.out.println("Searching Error");
            E.printStackTrace();
        }

    }

    /**
     * @author Samuel Coache
     * <p>
     * remove node tab: remove button event handler
     *
     */
    public void removeNode_removeBtnPressed(){
        String selectedItem = removeNode_searchList.getSelectionModel().getSelectedItem();
        System.out.println(selectedItem);
        Node selectNode = (this.NS.findNodeByName(selectedItem));
        this.searchList.remove(selectNode.getName());
        this.NS.remove(selectNode);

        //repopulate the search list
        ObservableList<String> OList = FXCollections.observableArrayList(this.searchList);
        removeNode_searchList.setItems(OList);
    }

    /**
     * method that populates the search results with the search query
     */
    public void removeNode_searchFieldKeyPressed(){
        // get the query from the field
        String query = removeNode_searchField.getText();
        ArrayList<String> queryList = new ArrayList<>();
        // add each query to the list
        List<Node> nodeList = this.NS.getAllNodes();
        for(Node n: nodeList){
            if (n.getName().contains(query))
                queryList.add(n.getName());
        }
        // Make the list view show the results
        removeNode_searchList.setItems(FXCollections.observableArrayList(queryList));
    }

    /**
     * @author Samuel Coache
     *
     * add node tab: connect button event handler
     *
     */
    public void addNode_connectToNodeBtnPressed(){

    }

    /**
     * @author Samuel Coache
     *
     * add node tab: create button event handler
     *
     */
    public void addNode_createNodeBtnPressed(){
        float x = Float.parseFloat(addNode_xPos.getText());
        float y = Float.parseFloat(addNode_yPos.getText());
        //TODO get floor information
        //float floor = Float.parseFloat(addNode_floor.getText());
        Coordinate addCoord = new Coordinate(x, y, 4);
        Node newNode = new Node(addCoord, addNode_nameField.getText());
        this.NS.merge(newNode);
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
}
