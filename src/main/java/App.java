import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.EMFProvider;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("view/Home.fxml"),
                    ResourceBundle.getBundle("Language", new Locale("en", "US")));
            primaryStage.setTitle("Faulkner Kiosk");
            primaryStage.setScene(new Scene(root, 800, 500));
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

