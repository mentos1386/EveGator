package com.mentos1386.evegator.Views;

import com.mentos1386.evegator.Controllers.DataController;
import com.mentos1386.evegator.Controllers.DataThreads.Constellation;
import com.mentos1386.evegator.Controllers.DataThreads.Region;
import com.mentos1386.evegator.Controllers.DataThreads.SolarSystem;
import com.mentos1386.evegator.Interfaces.ViewInterface;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class LoadingData implements ViewInterface {

    public Pane build() {

        Task<Void> regionUpdate = new Task<Void>() {
            @Override
            public Void call() {
                Region region = new Region();

                Thread regionThread = new Thread(region);
                regionThread.setDaemon(true);
                regionThread.start();

                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException exc) {
                        Thread.interrupted();
                        break;
                    }
                    System.out.println("[LOAD] Region " + DataController.regionsLoaded + "/" + DataController.allRegions);
                    updateProgress(DataController.regionsLoaded, DataController.allRegions);
                }
                return null;
            }
        };
        Task<Void> constellationUpdate = new Task<Void>() {
            @Override
            public Void call() {
                Constellation constellation = new Constellation();

                Thread constellationThread = new Thread(constellation);
                constellationThread.setDaemon(true);
                constellationThread.start();

                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException exc) {
                        Thread.interrupted();
                        break;
                    }
                    System.out.println("[LOAD] Constellation " + DataController.constellationsLoaded + "/" + DataController.allConstellations);
                    updateProgress(DataController.constellationsLoaded, DataController.allConstellations);
                }
                return null;
            }
        };
        Task<Void> solarSystemUpdate = new Task<Void>() {
            @Override
            public Void call() {
                SolarSystem solarSystem = new SolarSystem();

                Thread solarSystemThread = new Thread(solarSystem);
                solarSystemThread.setDaemon(true);
                solarSystemThread.start();

                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException exc) {
                        Thread.interrupted();
                        break;
                    }
                    updateProgress(DataController.solarSystemsLoaded, DataController.allSolarSystems);
                }
                return null;
            }
        };

        ProgressBar regionProgress = new ProgressBar();
        regionProgress.progressProperty().bind(regionUpdate.progressProperty());

        ProgressBar constellationProgress = new ProgressBar();
        constellationProgress.progressProperty().bind(constellationUpdate.progressProperty());

        ProgressBar solarSystemProgress = new ProgressBar();
        solarSystemProgress.progressProperty().bind(solarSystemUpdate.progressProperty());

        Thread rt = new Thread(regionUpdate);
        rt.setDaemon(true);
        rt.start();

        Thread ct = new Thread(constellationUpdate);
        ct.setDaemon(true);
        ct.start();

        Thread st = new Thread(solarSystemUpdate);
        st.setDaemon(true);
        st.start();

        Label loading = new Label("Loading data... Please wait, this can take a while.");

        VBox layout = new VBox(10);
        layout.getChildren().addAll(loading, regionProgress, constellationProgress, solarSystemProgress);
        layout.setAlignment(Pos.CENTER);

        return layout;
    }
}
