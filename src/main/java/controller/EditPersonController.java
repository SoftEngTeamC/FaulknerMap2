package controller;


import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.HospitalProfessional;
import model.Node;
import service.HospitalProfessionalService;
import service.NodeService;

import java.util.*;

public class EditPersonController extends Controller{

    @FXML
    private Button logoutBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button removeNodeBtn;
    @FXML
    private Button addNodeBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField idField;
    @FXML
    private ListView<String> availableLocationsList;
    @FXML
    private ListView<String> locationsList;
    @FXML
    private Text updateText;
    @FXML
    private Text deletedText;

    // database helpers
    NodeService ns;
    HospitalProfessionalService hps;

    // Hospital professional on which we are editing
    HospitalProfessional hp;

    // Available nodes list
    List<Node> availableNodeList;

    // current locations list
    List<Node> currentNodeList;




    @FXML
    public void initialize() {

        // disable update and delete text fields
        updateText.setVisible(false);
        deletedText.setVisible(false);

        // initialize database helpers
        ns = new NodeService();
        hps = new HospitalProfessionalService();

        // set the name, title, and ID field
        nameField.setText(this.hp.getName());
        titleField.setText(this.hp.getTitle());
        idField.setText(this.hp.getId().toString());

        //remove node button shouldn't be clickable yet
        removeNodeBtn.setDisable(true);

        // update button shouldn't do anything until something is changed
        updateBtn.setDisable(true);

        // Add node not enabled yet
        addNodeBtn.setDisable(true);

        // available nodes list is populated
        // initialize the private field list
        availableNodeList = ns.getAllNodes();
        // make it the set - the person's set of nodes
        updateAvailableNodesList();
        // populate the locations listview
        currentNodeList = hp.getOffice();
        setListItems(currentNodeList, locationsList);

        // add event handlers for when an item is clicked in both lists
        locationsList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            /**
             * Handler for when someone selects an item. Enables remove node option.
             */
            public void handle(MouseEvent event) {
                removeNodeBtn.setDisable(false);
            }});
        availableLocationsList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            /**
             * Handler for when the available nodes list is clicked. Enables add node button.
             */
            public void handle(MouseEvent event) {
                addNodeBtn.setDisable(false);
            }});


    }


    @FXML
    /**
     * @author paul
     * goes to admintoolmenu screen
     */
    public void back() throws Exception {

        switchScreen("view/AdminToolMenu.fxml","Admin tool menu",backBtn);

    }

    @FXML
    /**
     * @author paul
     * goes to main screen
     */
    public void logout() throws Exception {

        switchScreen("view/Main.fxml", "Main menu", logoutBtn);

    }

    @FXML
    /**
     * @author paul
     *
     * Event handler for when the remove node button is clicked.
     * gets node from database, removes a node, edits database,
     * refreshes list, disables remove node button.
     *
     */
    public void removeNodeBtnAction(){

        // Get the selected string
        String selected = locationsList.getSelectionModel().getSelectedItem();

        // remove the selected node from this persons list.
        removeNode(selected);

        // refresh the lists
        setListItems(hp.getOffice(), locationsList);


        // disable remove node button
        removeNodeBtn.setDisable(true);

    }



    @FXML
    /**
     * @author paul
     *
     * event handler for when the add node butotn is clicked.
     * gets node from database, edits the person's data in database, refreshes both lists
     * disables the add node button
     *
     */
    public void addNodeBtnAction(){

        // get referenced node string
        String selected = availableLocationsList.getSelectionModel().getSelectedItem();

        // edit this person's data in database
        addNode(selected);

        // refresh the list
        setListItems(hp.getOffice(), locationsList);

        // refresh available list
        updateAvailableNodesList();

        // disable add node button
        addNodeBtn.setDisable(true);


    }

    /**
     * @author paul
     *
     * this function is called by the directory editor screen to set the selected professional.
     *
     * @param hp
     */
    public void setSelectedUser(HospitalProfessional hp) {

        this.hp = hp;

    }

    /**
     * @author paul
     *
     * event handler for the update button being pressed. Updates person in database,
     * sets the update text to visible
     */
    public void updateBtnPressed() {
        // push to database
        hp.setName(nameField.getText());
        hp.setTitle(titleField.getText());
        hp.setOffice(currentNodeList);
        // remove old, insert new
        HospitalProfessional old = hps.find(hp.getId());
        hps.remove(old);
        hps.merge(hp);
        // alert user
        updateText.setVisible(true);

    }

    /**
     * @author paul
     *
     * deletes person from database, sets deleted text field to visible.
     *
     *
     */
    public void deleteBtnPressed() {
        //remove person
        hps.remove(hp);
        // indicate to user
        deletedText.setVisible(true);
    }

    // ~~~~~~~~~ helpers ~~~~~~~~~

    /**
     * Updates the local list, then refreshes the available list
     */
    private void updateAvailableNodesList() {
        // remove from the nodeList the elements in the person's office list, if contained
        for(Node n : hp.getOffice()){
            if (availableNodeList.contains(n)){ // if contained, remove
                availableNodeList.remove(n);
            }
        }
        // refresh the list in the view
        setListItems(availableNodeList, availableLocationsList);
    }

    /**
     * @author paul
     *
     * Refreshes a given listview given a list of nodes
     *
     * @param nodeList list of nodes
     * @param lv listview to populate
     */
    private void setListItems(List<Node> nodeList, ListView lv) {
        // private list of node names
        ArrayList<String> names = new ArrayList<>();
        // loop through nodes, get name and add to list
        for(Node n : nodeList){
            names.add(n.getName());
        }
        // create FX compatible list and set the LV to this list
        lv.setItems(FXCollections.observableArrayList(names));
    }

    /**
     * @author paul
     *
     * removes a node from this person's locations list in database
     *
     * @param selected
     */
    private void removeNode(String selected) {

        // find the node referenced by string
        Node n = ns.findNodeByName(selected);

        // edit this list of locations by removing it, then set his offices to updated list
        currentNodeList.remove(n);
        hp.setOffice(currentNodeList);

    }
    /**
     * @author paul
     *
     * adds given node to person's locations list in database
     *
     * @param selected
     *
     */
    private void addNode(String selected){

        // find the node referenced by string
        Node n = ns.findNodeByName(selected);

        // add to this list
        currentNodeList.add(n);
        hp.setOffice(currentNodeList);
    }

}


