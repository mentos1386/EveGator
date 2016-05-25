package com.mentos1386.evegator.Views;

import com.mentos1386.evegator.Controllers.DataTask;
import com.mentos1386.evegator.Controllers.InterfaceController;
import com.mentos1386.evegator.EveGator;
import com.mentos1386.evegator.Interfaces.ViewInterface;
import com.mentos1386.evegator.Models.RegionObject;
import javafx.application.Platform;
import javafx.scene.Scene;

public class Main implements ViewInterface {

    public void setRegion(RegionObject region) {
        this.region = region;
    }

    private RegionObject region;

    @Override
    public Scene build() {

        if (!EveGator.gb.wasInit) {
            EveGator.gb.init(this.region);
        }

        DataTask<Void> graphRefresh = new DataTask<Void>() {
            @Override
            public Void call() {

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
                Platform.runLater(() -> {
                    System.out.println("[GraphUI] Refresh");
                    InterfaceController.setScene(new Main().build());
                });
                return null;
            }
        };

        // For big graphs
        //if(!EveGator.gb.done)
        //{
        // Refreshing UI
        //    Thread rt = new Thread(graphRefresh);
        //    rt.setDaemon(true);
        //    rt.start();
        //}
        // Updating Graph For big graphs
        //if (!EveGator.gb.wasUpdating) {
        //    InterfaceController.newStage(new GraphLoading(), "Graph Loading", "loading");
        //}

        return EveGator.gb.build();
    }
}
