package com.mentos1386.evegator.Controllers;

import com.mentos1386.evegator.Interfaces.ViewInterface;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class InterfaceController extends Application {

    private static Stage window;
    private static ViewInterface initView;

    public static void run(ViewInterface view) {
        initView = view;
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

        setScene(initView.build());

        window.show();
    }
}
