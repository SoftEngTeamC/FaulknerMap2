package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.HospitalProfessional;
import model.Node;
import service.HospitalProfessionalService;

import java.util.ArrayList;
import java.util.List;


public class DirectoryEditorController extends Controller{
    @FXML
    private Button logoutBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button editPrsnBtn;
    @FXML
    private Button addPrsnBtn;
    @FXML
    private TextField searchField;
    @FXML
    private ListView<String> searchList;

    // database helpers
    HospitalProfessionalService hps;

    @FXML
    public void initialize() {

        //init hps
        hps = new HospitalProfessionalService();

        // disable the edit person button
        editPrsnBtn.setDisable(true);

        // add a listener to the listview
        searchList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            /**
             * @author paul
             * enables the edit person button
             */
            public void handle(MouseEvent event) {
                editPrsnBtn.setDisable(false);
            }});
    }

    @FXML
    public void back() throws Exception {

        switchScreen("view/AdminToolMenu.fxml", "Admin tool menu", backBtn);

    }

    @FXML
    public void logout() throws Exception {

        switchScreen("view/Main.fxml", "Main screen", logoutBtn);

    }

    /**
     * @throws Exception
     * @author Paul
     * <p>
     * event handler for edit person
     * passes selected person into the edit person screen
     */
    @FXML
    public void editPersonBtnPressed() throws Exception {

        // Instantiate a database helper
        HospitalProfessionalService hps = new HospitalProfessionalService();

        // get the current hospital professional that is selected in the list
        String selectedName = searchList.getSelectionModel().getSelectedItem();
        HospitalProfessional hp = hps.findHospitalProfessionalByName(selectedName);

        // pass it to the next screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/EditPersonScreen.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane)loader.load()));
        EditPersonController controller = loader.<EditPersonController>getController();
        controller.setSelectedUser(hp);
        stage.show();

    }

    /**
     * @throws Exception
     * @author Paul
     * <p>
     * add person button event handler for press
     */
    @FXML
    public void addPersonBtnCPressed() throws Exception {
        Stage stage = (Stage) addPrsnBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/AddPerson.fxml"));
        stage.setTitle("Add Person");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }


    /**
     * @author Paul
     *
     * search field key press event handler
     * queries database for results, populates list
     *
     */
    @FXML
    public void searchFieldKeyPressed() {

        // get the query from the searchfield
        String query = searchField.getText();

        // make and populate list with the names of hospital professionals containing query
        ArrayList<String> names = new ArrayList<>();
        List<HospitalProfessional> people = hps.getAllProfessionals();
        for(HospitalProfessional p : people){
            if(p.getName().contains(query)){
                names.add(p.getName());
            }
        }

        // set the list to contain the queried strings
        searchList.setItems(FXCollections.observableList(names));


    }

}
