package com.mentos1386.evegator.Views;

import com.google.gson.JsonObject;
import com.mentos1386.evegator.Controllers.AuthController;
import com.mentos1386.evegator.Endpoint;
import com.mentos1386.evegator.Controllers.InterfaceController;
import com.mentos1386.evegator.Interfaces.ViewInterface;
import com.mentos1386.evegator.Models.AuthObject;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Location implements ViewInterface {

    public Scene build() {
        AuthObject AUTH = AuthController.getAuth();

        Button welcome = new Button("Back to welcome");
        Button refresh = new Button("Refresh");

        // Authenticate user and save data to AuthController
        welcome.setOnAction(e -> {
            // Show Welcome
            InterfaceController.setScene(new Welcome().build());
        });

        refresh.setOnAction(e -> {
            // Re Show location TODO: there should be better way!
            InterfaceController.setScene(build());
        });


        // Get crest data
        JsonObject locationData = new Endpoint("characters/" + AUTH.OWNER + "/location/").get();

        String solarSystem = "No Data";
        String station = "No Data";

        if (locationData.has("solarSystem")) {
            solarSystem = locationData.get("solarSystem").getAsJsonObject().get("name").toString();
        }

        if (locationData.has("station")) {
            station = locationData.get("station").getAsJsonObject().get("name").toString();
        }

        Label location = new Label("Your location: \nSolarSystem: " + solarSystem + "\nStation: " + station);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(welcome, refresh, location);
        layout.setAlignment(Pos.CENTER);

        return new Scene(layout);
    }
}
