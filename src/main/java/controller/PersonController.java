package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import model.Node;

public abstract class PersonController extends Controller {
    @FXML
    protected Button backBtn;
    @FXML
    protected Button logoutBtn;
    @FXML
    protected Button removeNodeBtn;
    @FXML
    protected Button addNodeBtn;
    @FXML
    protected ListView<Node> currentLocationsListView;
    @FXML
    protected ListView<Node> availableLocationsListView;

    ObservableList<Node> currentLocations = FXCollections.observableArrayList();
    ObservableList<Node> availableLocations = FXCollections.observableArrayList();

    public void initialize() {
        currentLocationsListView.setItems(currentLocations);
        availableLocationsListView.setItems(availableLocations);

        currentLocationsListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> removeNodeBtn.setDisable(newValue == null));

        availableLocationsListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> addNodeBtn.setDisable(newValue == null));
    }

    private void sendNodeToCurrent(Node node) {
        if (node == null) {
            System.err.println("Cannot move null node to current nodes.");
            return;
        }
        availableLocations.remove(node);
        currentLocations.add(node);
    }

    private void sendNodeToAvailable(Node node) {
        if (node == null) {
            System.err.println("Cannot move null node to available nodes.");
            return;
        }
        currentLocations.remove(node);
        availableLocations.add(node);
    }

    @FXML
    public void removeNodeBtnAction(){
        Node n = currentLocationsListView.getSelectionModel().getSelectedItem();
        sendNodeToAvailable(n);
    }

    @FXML
    public void addNodeBtnAction() {
        Node n = availableLocationsListView.getSelectionModel().getSelectedItem();
        sendNodeToCurrent(n);
    }

    @FXML
    protected void logout() {
        switchToMainScreen(logoutBtn);
    }

    @FXML
    protected void back() {
            switchScreen("view/DirectoryEditor.fxml","Directory Editor", backBtn);
    }
}
