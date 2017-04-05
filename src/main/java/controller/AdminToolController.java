package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class AdminToolController {

    @FXML
    private Button logoutBtn;
    @FXML
    private Button editDirectoryBtn;
    @FXML
    private Button editHoursBtn;
    @FXML
    private Button mapEditorBtn;


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
