package controller;


import Memento.LoginStatusMemento;
import Singleton.LoginStatusSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.HospitalProfessional;

import java.util.LinkedList;


public class AddPersonController extends PersonController {
    @FXML
    public AnchorPane Parent;
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
    private TextField locationsSearchField;
    @FXML
    private Text successText;
    @FXML
    private Text errorText;

    @FXML
    public void initialize() {
        super.init();
        successText.setVisible(false);
        errorText.setVisible(false);
        locationsSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            locationsSearchFieldKeyPressed();
        });
        startIdleListener(Parent, addPersonBtn);

        // init the available locations list
        locationsSearchFieldKeyPressed();
    }

    public void logoutBtnPressed() {

        LoginStatusSingleton.getInstance().addMemento(new LoginStatusMemento(false));
        switchToMainScreen(logoutBtn);

    }

    public void addPersonBtnPressed() {
        if(!(nameField.getText().isEmpty() || titleField.getText().isEmpty() || currentLocations.isEmpty())) {
            professionalService.merge(new HospitalProfessional(nameField.getText(), titleField.getText(), new LinkedList<>(currentLocations)));
            successText.setVisible(true);
            errorText.setVisible(false);
        } else
            errorText.setVisible(true);
    }

    public void locationsSearchFieldKeyPressed(){
        String query = locationsSearchField.getText();
        availableLocations.clear();
        availableLocations.addAll(nodeService.search(query));
    }

}
