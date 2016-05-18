package com.mentos1386.evegator;


import com.google.gson.JsonObject;
import com.mentos1386.evegator.Objects.AuthObject;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;

public class EveGator extends Application {


    static public AuthObject AUTHENTICATION;
    private Stage window;
    private Scene scene;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.window = primaryStage;
        this.window.setTitle("EveGator");

        // Start with authentication
        this.authentication();

        this.window.setScene(this.scene);
        this.window.show();
    }

    private void authentication() {

        // Create button
        Button authenticateButton = new Button("Authenticate");

        // Authenticate user and save data to Authentication
        authenticateButton.setOnAction(e -> {
            // Authenticate
            AUTHENTICATION = Authentication.authenticate();
            // Show Welcome
            try {
                this.welcome();
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        });

        // Create layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(authenticateButton);
        layout.setAlignment(Pos.CENTER);

        // Set scene
        this.setScene(layout);
    }

    private void welcome() throws IOException {

        Button location = new Button("Get location");

        // Authenticate user and save data to Authentication
        location.setOnAction(e -> {
            // Show Welcome
            try {
                this.location();
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        });

        Label accessToken = new Label("Access Token: " + AUTHENTICATION.ACCESS_TOKEN);
        Label refreshToken = new Label("Refresh Token: " + AUTHENTICATION.REFRESH_TOKEN);
        Label expires = new Label("Expires: " + AUTHENTICATION.EXPIRES_IN);

        // Get crest data
        JsonObject userData = new Endpoint("characters/" + AUTHENTICATION.OWNER + "/").get();

        String name = userData.get("name").toString();
        Label welcome = new Label("Welcome " + name);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(welcome, accessToken, refreshToken, expires, location);
        layout.setAlignment(Pos.CENTER);

        this.setScene(layout);
    }

    private void location() throws IOException {

        Button welcome = new Button("Back to welcome");
        Button refresh = new Button("Refresh");

        // Authenticate user and save data to Authentication
        welcome.setOnAction(e -> {
            // Show Welcome
            try {
                this.welcome();
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        });

        refresh.setOnAction( e -> {
            // Re Show location
            try {
                this.location();
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        });


        // Get crest data
        JsonObject locationData = new Endpoint("characters/" + AUTHENTICATION.OWNER + "/location/").get();

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

        this.setScene(layout);
    }

    private void setScene(Pane layout) {
        this.scene = new Scene(layout);
        this.window.setScene(scene);
    }
}
