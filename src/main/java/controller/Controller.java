package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Paul on 4/9/2017.
 */
public class Controller {

    public void switchScreen(String file, String title,Button b) throws IOException {
        Stage stage = (Stage) b.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(file));
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600, 400));
        stage.setFullScreen(true);
        stage.show();
    }

    /**
     *
     * TODO: make a method that switches screens while passing an object
     */

    /**
     *
     * TODO: make a method that opens a popup
     */

}
