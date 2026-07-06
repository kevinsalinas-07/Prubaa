package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.ui.SceneRouter;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Remindly");
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(640);
        SceneRouter.init(primaryStage);
        SceneRouter.goToLogin();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}