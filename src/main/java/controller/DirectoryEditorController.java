import db.dbClasses.*;


import db.Driver;
import db.dbHelpers.*;
import db.dbClasses.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DirectoryEditorController {
    HospitalProfessionalsHelper hs = Driver.getHospitalProfessionalHelper();
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
    HospitalProfessionalsHelper hospitalProfessionalsHelper;

    @FXML
    public void initialize() {

        // get our helper
        hospitalProfessionalsHelper = Driver.getHospitalProfessionalHelper();
    }

    @FXML
    public void back() throws Exception {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("AdminToolMenu.fxml"));
        stage.setTitle("Admin Tool");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    @FXML
    public void logout() throws Exception {
        Stage stage = (Stage) logoutBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Main.fxml"));
        stage.setTitle("Main");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
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
        // get the current hospital professional that is selected in the list
        String selectedName = searchList.getSelectionModel().getSelectedItem();
        HospitalProfessional selectedPerson = hospitalProfessionalsHelper.getHospitalProfessionalByName(selectedName);

        // pass it to the next screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditPersonScreen.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane)loader.load()));
        EditPersonController controller = loader.<EditPersonController>getController();
        controller.setSelectedUser(selectedPerson);
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
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("AddPerson.fxml"));
        stage.setTitle("Add Person");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }


    /**
     * @author Paul
     *
     * search field entery event handler
     * for now, it just populates the list
     * <TODO> query the database and populate list with actual results </TODO>
     */
    @FXML
    public void searchFieldEntered() {

        // populate the list with the database
        ArrayList<HospitalProfessional> personList = new ArrayList<HospitalProfessional>();
        ArrayList<String> nameList = new ArrayList<String>();
        personList = hospitalProfessionalsHelper.getHospitalProfessionals(null);
        for(int i = 0; i < personList.size(); i++){
            nameList.add(personList.get(i).getName());

        }

        // load into the list
        ObservableList<String> oList = FXCollections.observableArrayList(nameList);
        searchList.setItems(oList);

    }

}
