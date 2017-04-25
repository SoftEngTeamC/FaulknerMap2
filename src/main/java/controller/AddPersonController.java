package controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.HospitalProfessional;

import java.util.LinkedList;


public class AddPersonController extends PersonController {

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
        currentLocationsListView.setItems(currentLocations);
        availableLocationsListView.setItems(availableLocations);

        locationsSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            locationsSearchFieldKeyPressed();
        });
    }

    public void logoutBtnPressed() {
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
