package com.mentos1386.evegator.Controllers;

import com.mentos1386.evegator.Interfaces.ViewInterface;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class InterfaceController extends Application {

    private static String title;
    private static ViewInterface initView;
    private static Map<String, Stage> stages = new HashMap<>();

    public static void newStage(ViewInterface view, String title, String name) {
        Stage stage = new Stage();
        stages.put(name, stage);

        stage.setTitle(title);
        stage.setScene(view.build());

        stage.show();
    }

    public static void newStageAndWait(Scene scene, String title, String name) {
        Stage stage = new Stage();
        stages.put(name, stage);

        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setTitle(title);
        stage.setScene(scene);

        stage.showAndWait();
    }

    public static void newStage(Scene scene, String title, String name) {
        Stage stage = new Stage();
        stages.put(name, stage);

        stage.setTitle(title);
        stage.setScene(scene);

        stage.show();
    }

    public static void setScene(Scene scene) {
        stages.get("primary").setScene(scene);
    }

    public static void setView(ViewInterface view) {
        stages.get("primary").setScene(view.build());
    }

    public static void setScene(Scene scene, String name) {
        stages.get(name).setScene(scene);
    }

    public static void run(ViewInterface view, String t) {
        initView = view;
        title = t;
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle(title);
        stages.put("primary", stage);
        setScene(initView.build());
        stage.show();
    }
}
