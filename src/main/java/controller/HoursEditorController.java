import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Created by jack on 3/30/17.
 */
public class HoursEditorController {

    public void initialize(){}
    @FXML
    private Button logoutBtn;
    @FXML
    private Button backBtn;

    @FXML
    public void back() throws Exception{
        Stage stage = (Stage) backBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("AdminToolMenu.fxml"));
        stage.setTitle("Directory Editor");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    @FXML
    public void logout()throws Exception {
        Stage stage = (Stage) logoutBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Main.fxml"));
        stage.setTitle("Main");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }
    @FXML
    public void update(){}


    public void saveHours(){}
}
