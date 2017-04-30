package controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.HospitalProfessional;
import model.HospitalService;

public class EditServiceController extends ServiceController {
    @FXML
    private Button deleteBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField idField;
    @FXML
    private Text updateText;
    @FXML
    private Text deletedText;
    @FXML
    private AnchorPane Parent;

    // Hospital service on which we are editing
    private HospitalService hs;

    @FXML
    public void initialize() {
        setButton(deleteBtn);
        super.init();
        updateText.setVisible(false);
        deletedText.setVisible(false);


        nameField.textProperty()
                .addListener((observable, oldValue, newValue) -> updateBtn.setDisable(false));

       startIdleListener(Parent, deleteBtn);
    }

    public void updateBtnPressed() {
        hs.setName(nameField.getText());
        hs.setLocations(currentLocationsListView.getItems());

        serviceService.merge(hs);

        updateText.setVisible(true);

    }

    public void deleteBtnPressed() {
        serviceService.remove(hs);
        deletedText.setVisible(true);

    }

    void setSelectedUser(HospitalService hs) {
        this.hs = hs;

        nameField.setText(hs.getName());
        idField.setText(hs.getId().toString());

        currentLocations.addAll(hs.getLocations());
        availableLocations.addAll(nodeService.getAllNodes());
        availableLocations.removeAll(currentLocations);
    }

}


