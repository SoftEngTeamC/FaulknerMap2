package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.HospitalProfessional;
import model.HospitalService;

import java.io.IOException;
import java.util.*;


public class DirectoryEditorController extends Controller {
    @FXML
    public TextField searchServiceField;
    @FXML
    public ListView<HospitalService> searchServiceList;
    @FXML
    public Button editServiceBtn;
    @FXML
    public Button addServiceBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button editPrsnBtn;
    @FXML
    private Button addPrsnBtn;
    @FXML
    private TextField searchPersonField;
    @FXML
    private ListView<HospitalProfessional> searchPersonList;
    @FXML
    private VBox DirectoryEditor_VBox;
    @FXML
    private AnchorPane DirectoryEditor_AnchorPane;

    private ObservableList<HospitalProfessional> professionals;
    private ObservableList<HospitalService> services;

    private Stage stage;

    @FXML
    public void initialize() {

        List<HospitalProfessional> hp = professionalService.getAllProfessionals();
        professionals = FXCollections.observableArrayList(hp);
        searchPersonList.setItems(professionals);

        List<HospitalService> hs = serviceService.getAllServices();
        services = FXCollections.observableArrayList(hs);
        searchServiceList.setItems(services);

        // disable the edit person button
        editPrsnBtn.setDisable(true);
        editServiceBtn.setDisable(true);

        // add a listener to the listview
        searchPersonList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> editPrsnBtn.setDisable(newValue == null));
        searchServiceList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> editServiceBtn.setDisable(newValue == null));

        // add a listener to the textfield being changed to fix the search lag
        searchPersonField.textProperty()
                .addListener((observable, oldValue, newValue) -> textPersonFieldChanged(newValue));
        searchServiceField.textProperty()
                .addListener((observable, oldValue, newValue) -> textServiceFieldChanged(newValue));

        startIdleListener(DirectoryEditor_AnchorPane, addPrsnBtn);
    }

    private void setStage(){
        stage = (Stage)backBtn.getScene().getWindow();
    }

    @FXML
    public void back() {
        setStage();
        switchScreen("view/AdminToolMenu.fxml", "Admin tool menu", stage);
    }

    @FXML
    public void logout() {
        setStage();
        switchToMainScreen(backBtn);
    }

    @FXML
    public void editPersonBtnPressed() throws IOException {

        // get the current hospital professional that is selected in the list
        HospitalProfessional hp = searchPersonList.getSelectionModel().getSelectedItem();

        // pass it to the next screen
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/EditPersonScreen.fxml"));
        loader.load();
        Parent p = loader.getRoot();
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.getScene().setRoot(p);
        EditPersonController controller = loader.getController();
        controller.setSelectedUser(hp);
        stage.show();

    }

    @FXML
    public void editServiceBtnPressed() throws IOException {

        // get the current hospital service that is selected in the list
        HospitalService hs = searchServiceList.getSelectionModel().getSelectedItem();

        // pass it to the next screen
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/EditServiceScreen.fxml"));
        loader.load();
        Parent p = loader.getRoot();
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.getScene().setRoot(p);
        EditServiceController controller = loader.getController();
        controller.setSelectedUser(hs);
        stage.show();

    }

    @FXML
    public void addPersonBtnCPressed() {
        setStage();
        switchScreen("view/AddPerson.fxml", "Add Person Menu", stage);
    }

    @FXML
    public void addServiceBtnCPressed() {
        setStage();
        switchScreen("view/AddService.fxml", "Add Service Menu", stage);
    }


    /**
     * @author Paul
     *
     * search field key press event handler
     * queries database for results, populates list
     *
     */
    @FXML
    private void textPersonFieldChanged(String query) {
        professionals.clear();
        professionals.addAll(professionalService.search(query));

    }

    @FXML
    private void textServiceFieldChanged(String query) {
        services.clear();
        services.addAll(serviceService.search(query));

    }

}
