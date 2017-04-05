import db.Driver;
import db.dbClasses.HospitalProfessional;
import db.dbClasses.Node;
import db.dbHelpers.HospitalProfessionalsHelper;
import db.dbHelpers.NodesHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by Paul on 4/2/2017.
 */
public class AddPersonController {

    @FXML
    private Button backBtn;
    @FXML
    private Button logoutBtn;
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

    private HospitalProfessionalsHelper professionalHelper;

    @FXML
    public void initialize() {
        professionalHelper = Driver.getHospitalProfessionalHelper();
    }

    /**
     * @author Paul
     * <p>
     * handler for the back button being pressed. Brings it back to the directory editor screen.
     */
    public void backBtnPressed() {
        // switch screens to directory editor
        // goto genres screen
        try {
            Stage stage = (Stage) backBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("DirectoryEditor.fxml"));
            stage.setTitle("Directory Editor");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (Exception E) {
            System.out.println("Couldn't switch scenes");
        }
    }

    /**
     * @author Paul
     * <p>
     * Handler for the logout button. Switches back to the main screen.
     */
    public void logoutBtnPressed() {
        // switch screens to main
        try {
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
            stage.setTitle("Main");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (Exception E) {
            System.out.println("Couldn't switch scenes");
        }
    }

    /**
     * @author Paul
     * <p>
     * handler for the add person button. Adds to the hospital professional database if possible.
     */
    public void setAddPersonBtnPressed() {
        NodesHelper nh = Driver.getNodesHelper();
        // check if fields are entered
        if (nameField.getText().isEmpty() || locationField.getText().isEmpty()) {
            // Display error text
            warningText.setText("Not enough information added");
            warningText.setVisible(true);
        } else if (nh.getNodeByName(locationField.getText()) == null) {
            warningText.setText("Location not found");
            warningText.setVisible(true);
        } else {
            // create a new professional and add to database
            HospitalProfessional newProfessional = new HospitalProfessional(
                    nameField.getText(),
                    titleField.getText(),
                    locationField.getText()
            );

            Node newNode = nh.getNodeByName(locationField.getText());
            newProfessional.setNodeId(newNode.getId());
            professionalHelper.addHospitalProfessional(newProfessional);


            // put the ID in the ID field
            idField.setText(newProfessional.getId().toString());
            // indicate success
            warningText.setVisible(false);
            successField.setVisible(true);
            //professionalHelper.printAllProfessionalRows();

        }

        // check if not already in existence??


    }


}
