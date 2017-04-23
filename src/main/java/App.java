import controller.ImageProvider;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import service.EMFProvider;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class App extends Application {

    public final ImageProvider imageProvider = new ImageProvider();

    public static Image getImage(String url) {
        return ImageProvider.getImage(url);
    }

    public Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        URL mainView = getClass().getClassLoader().getResource("view/Main.fxml");
        if (mainView == null) {
            throw new Exception("The root view cannot be null.");
        }
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root = FXMLLoader.load(getClass().getResource("view/Main.fxml"),
                ResourceBundle.getBundle("Language", new Locale("en", "US")));
        primaryStage.setTitle("Faulkner Kiosk");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.setFullScreen(true);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        EMFProvider.getInstance().getEMFactory().close();
    }
}

