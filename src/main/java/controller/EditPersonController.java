import db.Driver;
import db.dbClasses.HospitalProfessional;
import db.dbHelpers.HospitalProfessionalsHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EditPersonController {

    //hospital professional on which we are editing
    private HospitalProfessional hp;
    private HospitalProfessionalsHelper hph;

    @FXML
    private Button logoutBtn;
    @FXML
    private Button backBtn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField idField;
    @FXML
    private Button updateBtn;

    @FXML
    private Text warningText;

    @FXML
    public void initialize() {
        hph = Driver.getHospitalProfessionalHelper();


    }

    @FXML
    public void back() throws Exception {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("DirectoryEditor.fxml"));
        stage.setTitle("Directory Editor");
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


    public void setSelectedUser(HospitalProfessional hp) {

        this.hp = hp;
        nameField.setText(this.hp.getName());
        titleField.setText(this.hp.getTitle());
        locationField.setText(this.hp.getLocation());
        idField.setText(this.hp.getId().toString());

    }

    public void updateBtnPressed() {
        // push to database
        hp.setName(nameField.getText());
        hp.setTitle(titleField.getText());
        hp.setLocation(locationField.getText());
        hph.updateHospitalProfessional(hp);
    }

    public void deleteBtnPressed() {
        // push to database
        hph.deleteHospitalProfessional(hp);
        warningText.setVisible(true);
    }

}


