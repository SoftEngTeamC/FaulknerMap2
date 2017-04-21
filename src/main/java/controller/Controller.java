package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.HospitalService;
import service.*;

import java.io.IOException;

/**
 * Created by Paul on 4/9/2017.
 * edited by JVB on 4/17/17
 */
public class Controller {

    protected CoordinateService coordinateService = new CoordinateService();
    protected EdgeService edgeService = new EdgeService();
    protected HospitalProfessionalService professionalService = new HospitalProfessionalService();
    protected HospitalService hospitalService = new HospitalService();
    protected HospitalServiceService serviceService = new HospitalServiceService();
    protected NodeService nodeService = new NodeService();
    protected TagService tagService = new TagService();

    public void switchScreen(String file, String title,Button b) throws IOException {
        Stage stage = (Stage) b.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(file));
        stage.setTitle(title);
        stage.getScene().setRoot(root);
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
