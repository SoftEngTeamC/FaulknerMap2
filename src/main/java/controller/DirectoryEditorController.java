package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
    @FXML
    private SplitPane splitPane;
    @FXML
    private VBox DirectoryEditor_VBox;
    @FXML
    private AnchorPane DirectoryEditor_AnchorPane;

    // database helpers
    HospitalProfessionalService hps;

    // Arraylist of search results
    ArrayList<String> searchResults;

    // List of all hospital people
    List<HospitalProfessional> people;

    @FXML
    public void initialize() {
        //organize visual elements
        DirectoryEditor_VBox.prefWidthProperty().bind(DirectoryEditor_AnchorPane.widthProperty());
        searchField.prefWidthProperty().bind(DirectoryEditor_AnchorPane.widthProperty().multiply(0.4));
        searchList.prefWidthProperty().bind(DirectoryEditor_AnchorPane.widthProperty().multiply(0.4));
        searchList.prefHeightProperty().bind(DirectoryEditor_AnchorPane.heightProperty().multiply(0.3));

        //init hps
        hps = new HospitalProfessionalService();

        // init search results empty list, hospital professionals list
        people = hps.getAllProfessionals();
        searchResults = new ArrayList<>();

        // disable the edit person button
        editPrsnBtn.setDisable(true);

        // add a listener to the listview
        searchList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                editPrsnBtn.setDisable(false);
            }
        });

    }

    @FXML
    public void back() throws Exception {
        System.out.println("Directory Editor Back Button");
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

        // get the current hospital professional that is selected in the list
        String selectedName = searchList.getSelectionModel().getSelectedItem();
        HospitalProfessional hp = hps.findHospitalProfessionalByName(selectedName);

        // pass it to the next screen
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/EditPersonScreen.fxml"));
        loader.load();
        Parent p = loader.getRoot();
        Stage stage = (Stage) logoutBtn.getScene().getWindow();
        stage.setScene(new Scene(p));
        EditPersonController controller = loader.getController();
        controller.setSelectedUser(hp);
        stage.setFullScreen(true);
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

        switchScreen("view/AddPerson.fxml", "Add person menu", addPrsnBtn);

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

        // get the query from the search field
        String query = searchField.getText();

        // reset list
        searchResults.removeAll(searchResults);

        // add search results
        for(HospitalProfessional p : people){
            if(p.getName().toLowerCase().contains(query.toLowerCase())){
                searchResults.add(p.getName());
            }
        }

        // display the list
        searchList.setItems(FXCollections.observableList(searchResults));


    }

}
