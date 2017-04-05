import db.Driver.*;
import db.dbHelpers.*;
import db.dbClasses.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import java.util.Arrays;
import java.util.LinkedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;


public class AdminToolController {

    @FXML
    private Button logoutBtn;
    @FXML
    private Button editDirectoryBtn;
    @FXML
    private Button editHoursBtn;
    @FXML
    private Button mapEditorBtn;

    /**
     * @author Paul
     *
     * When map editor button is pressed, it goes to the map editing screen.
     *
     */
    public void editMap() throws Exception{
            Stage stage = (Stage) mapEditorBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("MapEditor.fxml"));
            stage.setTitle("Map Editor");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
    }
    @FXML
    public void editHours()throws Exception{
        Stage stage = (Stage) editHoursBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("HoursEditorScreen.fxml"));
        stage.setTitle("Directory Editor");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();;
    }
    @FXML
    public void editDirectory() throws Exception{
        Stage stage = (Stage) editDirectoryBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("DirectoryEditor.fxml"));
        stage.setTitle("Directory Editor");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();;

    }

    @FXML
    public void logout() throws Exception{
        Stage stage = (Stage) logoutBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Main.fxml"));
        stage.setTitle("Directory Editor");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();;
    }

}
