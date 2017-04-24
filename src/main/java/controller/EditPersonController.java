package controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.HospitalProfessional;

public class EditPersonController extends PersonController {
    @FXML
    private Button deleteBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField idField;
    @FXML
    private Text updateText;
    @FXML
    private Text deletedText;

    // Hospital professional on which we are editing
    private HospitalProfessional hp;

    @FXML
    public void initialize() {
        updateText.setVisible(false);
        deletedText.setVisible(false);
        currentLocationsListView.setItems(currentLocations);
        availableLocationsListView.setItems(availableLocations);

        currentLocationsListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> removeNodeBtn.setDisable(newValue == null));

        availableLocationsListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> addNodeBtn.setDisable(newValue == null));

        nameField.textProperty()
                .addListener((observable, oldValue, newValue) -> updateBtn.setDisable(false));
        titleField.textProperty()
                .addListener((observable, oldValue, newValue) -> updateBtn.setDisable(false));
    }

    public void updateBtnPressed() {
        hp.setName(nameField.getText());
        hp.setTitle(titleField.getText());
        hp.setOffices(currentLocationsListView.getItems());

        professionalService.merge(hp);

        updateText.setVisible(true);

    }

    public void deleteBtnPressed() {
        professionalService.remove(hp);
        deletedText.setVisible(true);

    }

    void setSelectedUser(HospitalProfessional hp) {
        this.hp = hp;

        nameField.setText(hp.getName());
        titleField.setText(hp.getTitle());
        idField.setText(hp.getId().toString());

        currentLocations.addAll(hp.getOffices());
        availableLocations.addAll(nodeService.getAllNodes());
        availableLocations.removeAll(currentLocations);
    }

}


