package controller;

import Memento.LoginStatusEditor;
import Memento.LoginStatusMemento;
import Singleton.IdleMonitor;
import Singleton.LoginStatusSingleton;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.HospitalService;
import service.*;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;


public class Controller {

    protected CoordinateService coordinateService = new CoordinateService();
    protected EdgeService edgeService = new EdgeService();
    protected HospitalProfessionalService professionalService = new HospitalProfessionalService();
    protected HospitalService hospitalService = new HospitalService();
    protected HospitalServiceService serviceService = new HospitalServiceService();
    protected NodeService nodeService = new NodeService();
    protected TagService tagService = new TagService();
    protected HoursService hoursService = new HoursService();

    private IdleMonitor idleMonitor;

    public void startIdleListener(Parent parent, Button button) {
        LoginStatusEditor originator = new LoginStatusEditor();
        LoginStatusSingleton careTaker = LoginStatusSingleton.getInstance();
        LoginStatusMemento memento = new LoginStatusMemento(true);
//        System.out.println(memento.getStatus());
//        System.out.println(careTaker.getMemento().getStatus());
        originator.setStatus(memento.getStatus());
        careTaker.addMemento(originator.save());
//        System.out.println(careTaker.getMemento().getStatus());
//        System.out.println(originator.save().getStatus());

        idleMonitor = new IdleMonitor(Duration.seconds(careTaker.getTimeout()),
                () -> {
                    unregister(parent);
                    switchToMainScreen(button);
                }
                , careTaker.getMemento().getStatus());

        idleMonitor.register(parent, Event.ANY);
    }

    public void setButton(Button b) {
        LoginStatusSingleton.getInstance().setButton(b);
    }

    public void switchScreen(String file, String title, Button b) {
        try {
            Stage stage = (Stage) b.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(file));
            stage.setTitle(title);
            stage.getScene().setRoot(root);
            stage.show();
        } catch (IOException e) {
//            System.err.println("Couldn't find file at " + file);
            e.printStackTrace();
        }
    }

    public void unregister(Parent parent) {
        idleMonitor.unregister(parent, Event.ANY);
        idleMonitor.stopMonitoring();
    }

    public Parent switchToMainScreen(Button button) {
        try {
            Stage stage = (Stage) button.getScene().getWindow();
            SplitPane root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Home.fxml"),
                    ResourceBundle.getBundle("Language", new Locale("en", "US")));
            stage.setTitle("Faulkner Kiosk");
            stage.getScene().setRoot(root);
            stage.setFullScreen(true);
            stage.show();
            return root;
        } catch (IOException ex) {
            System.out.println("cannot switch to main screen");
         //   ex.printStackTrace();
        }
        return null;
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
