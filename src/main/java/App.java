import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import service.EMFProvider;

import java.net.URL;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL mainView = getClass().getClassLoader().getResource("view/Main.fxml");
        if (mainView == null) {
            throw new Exception("The root view cannot be null.");
        }
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root = FXMLLoader.load(mainView);
        primaryStage.setTitle("Faulkner Kiosk");
        primaryStage.setScene(new Scene(root, screenSize.getWidth(), screenSize.getHeight()));
        primaryStage.setMaximized(true);
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
