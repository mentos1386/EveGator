package com.mentos1386.evegator.Views;

import com.mentos1386.evegator.Controllers.DataTask;
import com.mentos1386.evegator.Controllers.InterfaceController;
import com.mentos1386.evegator.EveGator;
import com.mentos1386.evegator.ExceptionHandler;
import com.mentos1386.evegator.Interfaces.ViewInterface;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class LoadingData implements ViewInterface {

    public Pane build() {
        DataTask<Void> regionsLoadingUpdate = new DataTask<Void>() {
            @Override
            public Void call() {
                // Get Regions
                try {
                    EveGator.dataCon.getRegions(this);
                } catch (Exception e) {
                    new ExceptionHandler(e);
                }
                return null;
            }
        };
        DataTask<Void> constellationsLoadingUpdate = new DataTask<Void>() {
            @Override
            public Void call() {
                // Get Regions
                try {
                    EveGator.dataCon.getConstellations(this);
                } catch (SQLException e) {
                    new ExceptionHandler(e);
                }
                return null;
            }
        };
        DataTask<Void> solarSystemsLoadingUpdate = new DataTask<Void>() {
            @Override
            public Void call() {
                // Get Solar Systems
                try {
                    EveGator.dataCon.getSolarsystems(this);
                } catch (SQLException e) {
                    new ExceptionHandler(e);
                }
                return null;
            }
        };
        DataTask<Void> stargatesLoadingUpdate = new DataTask<Void>() {
            @Override
            public Void call() {
                // Get Stargates
                try {
                    EveGator.dataCon.getStargates(this);
                } catch (SQLException e) {
                    new ExceptionHandler(e);
                }
                return null;
            }
        };

        ProgressBar RegionsLoadingProgress = new ProgressBar();
        RegionsLoadingProgress.progressProperty().bind(regionsLoadingUpdate.progressProperty());
        ProgressBar ConstellationsLoadingProgress = new ProgressBar();
        ConstellationsLoadingProgress.progressProperty().bind(constellationsLoadingUpdate.progressProperty());
        ProgressBar SolarSystemsLoadingProgress = new ProgressBar();
        SolarSystemsLoadingProgress.progressProperty().bind(solarSystemsLoadingUpdate.progressProperty());
        ProgressBar StargatesLoadingProgress = new ProgressBar();
        StargatesLoadingProgress.progressProperty().bind(stargatesLoadingUpdate.progressProperty());

        // When regions finish, start constellations
        regionsLoadingUpdate.setOnSucceeded(event -> {
            System.out.println("[DataLoad] Constellations STARTED");
            Thread ct = new Thread(constellationsLoadingUpdate);
            ct.setDaemon(true);
            ct.start();
        });
        // When constellations finish, start solarSystems
        constellationsLoadingUpdate.setOnSucceeded(event -> {
            System.out.println("[DataLoad] SolarSystems STARTED");
            Thread st = new Thread(solarSystemsLoadingUpdate);
            st.setDaemon(true);
            st.start();
        });
        // When solarSystems finish, start stargates
        solarSystemsLoadingUpdate.setOnSucceeded(event -> {
            System.out.println("[DataLoad] Stargates STARTED");
            Thread st = new Thread(stargatesLoadingUpdate);
            st.setDaemon(true);
            st.start();
        });
        // When stargattes finish, switch to new view
        stargatesLoadingUpdate.setOnSucceeded(event -> {
            System.out.println("[DataLoad] ALL FINISHED");
            InterfaceController.setScene(new Authentication().build());
        });

        // Start regions
        System.out.println("[DataLoad] Regions STARTED");
        Thread rt = new Thread(regionsLoadingUpdate);
        rt.setDaemon(true);
        rt.start();

        Label loading = new Label("Loading data... Please wait, this can take a while.");

        VBox layout = new VBox(10);
        layout.getChildren().addAll(loading, RegionsLoadingProgress, ConstellationsLoadingProgress, SolarSystemsLoadingProgress, StargatesLoadingProgress);
        layout.setAlignment(Pos.CENTER);

        return layout;
    }
}
