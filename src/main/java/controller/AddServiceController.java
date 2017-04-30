package controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.HospitalProfessional;
import model.HospitalService;

import java.util.LinkedList;


public class AddServiceController extends PersonController {
    @FXML
    public AnchorPane Parent;
    @FXML
    private Button addServiceBtn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField idField;
    @FXML
    private Text warningText;
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
        setButton(addServiceBtn);
        super.init();
        successText.setVisible(false);
        errorText.setVisible(false);
        locationsSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            locationsSearchFieldKeyPressed();
        });
        startIdleListener(Parent, addServiceBtn);
    }

    public void logoutBtnPressed() {
        switchToMainScreen(logoutBtn);

    }

    public void addServiceBtnPressed() {
        if(!(nameField.getText().isEmpty() || currentLocations.isEmpty())) {
            serviceService.merge(new HospitalService(nameField.getText(), new LinkedList<>(currentLocations)));
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
