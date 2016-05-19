package com.mentos1386.evegator.Controllers;

import com.mentos1386.evegator.Views.Authentication;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class InterfaceController extends Application {

    private static Stage window;

    public static void start() {
        launch();
    }

    public static void setScene(Pane layout) {
        Scene scene = new Scene(layout);
        window.setScene(scene);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        window.setTitle("EveGator");

        // Start with authentication
        setScene(new Authentication().build());

        window.show();
    }
}
