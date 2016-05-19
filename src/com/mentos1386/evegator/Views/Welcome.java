package com.mentos1386.evegator.Views;

import com.google.gson.JsonObject;
import com.mentos1386.evegator.Controllers.AuthController;
import com.mentos1386.evegator.Controllers.Endpoint;
import com.mentos1386.evegator.Controllers.InterfaceController;
import com.mentos1386.evegator.Interfaces.ViewInterface;
import com.mentos1386.evegator.Models.AuthObject;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Welcome implements ViewInterface {

    public Pane build() {
        AuthObject AUTH = AuthController.getAuth();

        Button location = new Button("Get location");

        // Authenticate user and save data to AuthController
        location.setOnAction(e -> {
            // Show Location
            InterfaceController.setScene(new Location().build());
        });

        Label accessToken = new Label("Access Token: " + AUTH.ACCESS_TOKEN);
        Label refreshToken = new Label("Refresh Token: " + AUTH.REFRESH_TOKEN);
        Label expires = new Label("Expires: " + AUTH.EXPIRES_IN);

        // Get crest data
        JsonObject userData = new Endpoint("characters/" + AUTH.OWNER + "/").get();

        String name = userData.get("name").toString();
        Label welcome = new Label("Welcome " + name);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(welcome, accessToken, refreshToken, expires, location);
        layout.setAlignment(Pos.CENTER);

        return layout;
    }
}
