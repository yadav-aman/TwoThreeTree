package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application{
    @Override
    public void start(Stage primaryStage){
        // Set Title
        primaryStage.setTitle("2-3 Tree");
        // open app maximised
        primaryStage.setMaximized(true);
        // Show Stage
        primaryStage.show();
    }
}
