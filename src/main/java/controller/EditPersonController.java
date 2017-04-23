package controller;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.HospitalProfessional;
import model.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EditPersonController extends Controller{

    // ~~~~~~~~~FXML Objects~~~~~~~~~~

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

        // disable remove node button
        removeNodeBtn.setDisable(true);

        // disable update button
        updateBtn.setDisable(true);

        // disable add node button
        addNodeBtn.setDisable(true);

        locationsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                removeNodeBtn.setDisable(false);
            }
        });

        availableLocationsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                addNodeBtn.setDisable(false);
            }
        });
    }

    // ~~~~~~ Event handlers ~~~~~~~~~

    @FXML
    /**
     * @author paul
     * goes to admintoolmenu screen
     */
    public void back() throws Exception {

        switchScreen("view/DirectoryEditor.fxml","Directory Editor",backBtn);

    }

    @FXML
    /**
     * @author paul
     * goes to main screen
     */
    public void logout() throws Exception {

        switchToMainScreen(logoutBtn);

    }

    @FXML
    /**
     * @author paul
     *
     * Event handler for when the remove node button is clicked.
     * gets node from database, removes a node, edits database,
     * refreshes list, disables remove node button, enables update button
     *
     */
    public void removeNodeBtnAction(){

        // Get the selected string
        String selected = locationsList.getSelectionModel().getSelectedItem();

        // remove the selected node from the current nodes list.
        removeNode(selected);

        // refresh both visible lists
        updateListView(locationsList, currentNodeList);
        updateListView(availableLocationsList, availableNodeList);

        // disable remove node button
        removeNodeBtn.setDisable(true);

        //enable update button
        updateBtn.setDisable(false);
    }

    @FXML
    /**
     * @author paul
     *
     * event handler for when the add node butotn is clicked.
     * gets node from database, edits the person's data in database, refreshes both lists
     * disables the add node button, enables update button
     *
     */
    public void addNodeBtnAction(){

        // get referenced node string
        String selected = availableLocationsList.getSelectionModel().getSelectedItem();

        // Add and remove node from respective lists
        addNode(selected);

        // refresh the lists
        updateListView(locationsList,currentNodeList);
        updateListView(availableLocationsList, availableNodeList);

        // disable add node button
        addNodeBtn.setDisable(true);

        //enable update button
        updateBtn.setDisable(false);

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
        hp.setOffices(currentNodeList);

        // remove old, insert new
        HospitalProfessional old = professionalService.find(hp.getId());
        professionalService.remove(old);
        professionalService.merge(hp);

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
        professionalService.remove(hp);

        // indicate to user
        deletedText.setVisible(true);

    }

    // ~~~~~~~~~ helpers ~~~~~~~~~

    /**
     * @author paul
     * sets the listview to contain the names of the nodes in given nodelist
     * @param locationsList
     * @param nodeList
     */
    private void updateListView(ListView<String> locationsList, List<Node> nodeList) {

        // make list of names (string)
        ArrayList<String> nameList = new ArrayList<>();

        // populate nameslist
        for(Node n: nodeList){
            nameList.add(n.getName());
        }

        // populate listview
        locationsList.setItems(FXCollections.observableList(nameList));

    }

    /**
     * @author Paul
     * Used by the set function to initialize the availale nodes list to not include the nodes in location list
     */
    private void updateAvailableNodesList() {

        // Make iterator to go through the available nodes
        Iterator<Node> iter = availableNodeList.iterator();

        // Average case is Big Theta (n*m)
        while(iter.hasNext()){
            // name (string) of current node
            String currentName = iter.next().getName();
            // go through current nodes, if it has the same name, remove
            for(Node n: currentNodeList){
                if(n.getName().equals(currentName))
                    iter.remove();
            }
        }
    }

    /**
     * @author paul
     *
     * removes a node from the currentNodeList, adds it to availableNodesList
     *
     * @param selected
     */
    private void removeNode(String selected) {

        // get the node referenced by string
        Node n = nodeService.findNodeByName(selected);

        // remove from current
        deleteNode(n, currentNodeList);

        // add it to available
        availableNodeList.add(n);

    }
    /**
     * @author paul
     *
     * adds given node to list of current nodes, removes from available nodes
     *
     *
     * @param selected
     *
     */
    private void addNode(String selected){

        // find the node referenced by string
        Node n = nodeService.findNodeByName(selected);

        // add to this list
        currentNodeList.add(n);

        // remove it from available list
        deleteNode(n, availableNodeList);

    }

    /**
     * @author paul
     *
     * Function that safely deletes a node from a list of nodes
     * @param n
     * @param nodeList
     */
    private boolean deleteNode(Node n, List<Node> nodeList) {

        // Name (string) of node to remove
        String selected = n.getName();

        // create iterator
        Iterator<Node> iter = nodeList.iterator();

        // go through whole list, remove the one with the same name.
        while(iter.hasNext()){
            String currentName = iter.next().getName();
            if (currentName.equals(selected)) {
                iter.remove();
                return true;
            }
        }
        return false;

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

        // set the name, title, and ID field
        nameField.setText(this.hp.getName());
        titleField.setText(this.hp.getTitle());
        idField.setText(this.hp.getId().toString());

        // set the current node list
        currentNodeList = this.hp.getOffices();

        // set the available node list
        availableNodeList = nodeService.getAllNodes();

        // make it the set of all nodes - the person's set of nodes
        updateAvailableNodesList();

        // populate the locations listview
        updateListView(locationsList,currentNodeList);
        updateListView(availableLocationsList, availableNodeList);

    }

}


