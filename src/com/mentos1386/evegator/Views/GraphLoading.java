package com.mentos1386.evegator.Views;

import com.mentos1386.evegator.Controllers.DataTask;
import com.mentos1386.evegator.EveGator;
import com.mentos1386.evegator.Interfaces.ViewInterface;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

public class GraphLoading implements ViewInterface {

    @Override
    public Scene build() {
        DataTask<Void> graphUpdate = new DataTask<Void>() {
            @Override
            public Void call() {
                // For big graphs
                //EveGator.gb.update(this);
                return null;
            }
        };

        ProgressBar GraphUpdateProgress = new ProgressBar();
        GraphUpdateProgress.progressProperty().bind(graphUpdate.progressProperty());

        System.out.println("[GraphLoad] Started");
        Thread gu = new Thread(graphUpdate);
        gu.setDaemon(true);
        gu.setPriority(10);
        gu.start();

        Label loading = new Label("Building Graph, this can take a while.");

        VBox layout = new VBox(10);
        layout.getChildren().addAll(loading, GraphUpdateProgress);
        layout.setAlignment(Pos.CENTER);

        return new Scene(layout);
    }
}
