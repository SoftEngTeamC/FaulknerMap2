import Memento.LoginStatusEditor;
import Memento.LoginStatusMemento;
import Singleton.IdleMonitor;
import Singleton.LoginStatusSingleton;
import controller.MainController;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.EMFProvider;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("view/Main.fxml"),
                    ResourceBundle.getBundle("Language", new Locale("en", "US")));

            LoginStatusEditor originator = new LoginStatusEditor();
            LoginStatusSingleton careTaker = LoginStatusSingleton.getInstance();
            LoginStatusMemento memento = new LoginStatusMemento(false);

            originator.setStatus(false);
            careTaker.addMemento(originator.save());

            IdleMonitor idleMonitor = new IdleMonitor(Duration.seconds(careTaker.getTimeout()), () -> {
            }, careTaker.getMemento().getStatus());
            idleMonitor.register(root, Event.ANY);
            idleMonitor.register(root, Event.ANY);

            primaryStage.setTitle("Faulkner Kiosk");
            Scene sc = new Scene(root, 800, 500);
            primaryStage.setScene(sc);
            primaryStage.setFullScreen(true);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Couldn't load the main screen.");
            stop();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        EMFProvider.getInstance().getEMFactory().close();
    }

}

