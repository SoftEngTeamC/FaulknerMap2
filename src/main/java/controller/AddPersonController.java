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


public class AddPersonController extends Controller{

    @FXML
    private Button backBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private Button addPersonBtn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField idField;
    @FXML
    private Text warningText;
    @FXML
    private TextField titleField;
    @FXML
    private Text successField;
    @FXML
    private ListView<String> locationsListView;
    @FXML
    private ListView<String> availableLocationsListView;
    @FXML
    private Button removeNodeBtn;
    @FXML
    private Button addNodeBtn;
    @FXML
    private TextField locationsSearchField;
    @FXML
    private Text successText;
    @FXML
    private Text errorText;

    //lists of nodes
    List<Node> availableLocationsList;
    List<Node> currentLocationsList;

    @FXML
    public void initialize() {

        // disable buttons in init
        addNodeBtn.setDisable(true);
        removeNodeBtn.setDisable(true);

        // set success text invisible
        successText.setVisible(false);
        errorText.setVisible(false);

        //init available nodes
        availableLocationsList = nodeService.getAllNodes();

        // init current locations
        currentLocationsList = new ArrayList<>();

        // show them
        updateListView(locationsListView,currentLocationsList);
        updateListView(availableLocationsListView, availableLocationsList);

        locationsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                removeNodeBtn.setDisable(false);
            }
        });

        availableLocationsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                addNodeBtn.setDisable(false);
            }
        });

    }

    public void addNodeBtnAction(){

        // get referenced node string
        String selected = availableLocationsListView.getSelectionModel().getSelectedItem();

        // Add and remove node from respective lists
        addNode(selected);

        // refresh the lists
        updateListView(availableLocationsListView, availableLocationsList);
        updateListView(locationsListView, currentLocationsList);

        // disable add node button
        addNodeBtn.setDisable(true);

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

        Node n = null;
        // find the node referenced by string
        for(Node an: nodeService.getAllNodes()) {
            if(an.getName().equals(selected)){
                n = an;
            }
        }

        // add to this list
        currentLocationsList.add(n);

        // remove it from available list
        deleteNode(n, availableLocationsList);

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

    public void removeNodeBtnAction(){

        // Get the selected string
        String selected = locationsListView.getSelectionModel().getSelectedItem();

        // remove the selected node from the current nodes list.
        removeNode(selected);

        // refresh both visible lists
        updateListView(locationsListView, currentLocationsList);
        updateListView(availableLocationsListView, availableLocationsList);

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

     //   System.out.println(n.getName());

        // remove from current
        deleteNode(n, currentLocationsList);

        // add it to available
        availableLocationsList.add(n);

        //disable the button
        removeNodeBtn.setDisable(true);

    }

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
         //   System.out.println(n.getName());
        }

        // populate listview
        locationsList.setItems(FXCollections.observableList(nameList));

    }

    /**
     * handler for the back button being pressed. Brings it back to the directory editor screen.
     */
    public void backBtnPressed() throws java.io.IOException{

        // switch screens to directory editor
        switchScreen("view/DirectoryEditor.fxml", "Directory editor", backBtn);
    }

    /**
     * Handler for the logout button. Switches back to the main screen.
     */
    public void logoutBtnPressed() throws java.io.IOException{

        // switch screens to main
        switchScreen("view/Main.fxml", "Main screen", logoutBtn);

    }

    /**
     * @author Paul
     * handler for the add person button. Adds to the hospital professional database if possible.
     */
    public void addPersonBtnPressed() {
        if(!(nameField.getText().isEmpty() || titleField.getText().isEmpty() || currentLocationsList.isEmpty())) {
            professionalService.merge(new HospitalProfessional(nameField.getText(), titleField.getText(), currentLocationsList));
            successText.setVisible(true);
            errorText.setVisible(false);
        } else
            errorText.setVisible(true);
    }

    public void locationsSeachFieldKeyPressed(){

        // get the query from the search field
        String query = locationsSearchField.getText();

        // reset list
        availableLocationsListView.getItems().clear();

        // temp list
        ArrayList<String> results = new ArrayList<>();

        // add search results
        for(Node n : nodeService.getAllNodes()){
            if(n.getName().toLowerCase().contains(query.toLowerCase())){
                results.add(n.getName());
            }
        }
        // display the list
        availableLocationsListView.setItems(FXCollections.observableList(results));
    }

}
