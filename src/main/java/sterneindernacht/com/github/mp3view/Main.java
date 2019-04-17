package sterneindernacht.com.github.mp3view;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException{
        BorderPane mainPane = FXMLLoader.load(getClass().getResource("/mainPane.fxml"));
        Scene scene = new Scene(mainPane);
        stage.setScene(scene);
        stage.setTitle("Mp3 Player");
        stage.show();
    }
}
