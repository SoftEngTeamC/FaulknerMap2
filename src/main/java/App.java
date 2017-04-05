import javafx.application.Application;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class App extends Application {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory( "derby" );

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
