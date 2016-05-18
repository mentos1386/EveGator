package com.mentos1386.evegator;


import com.mentos1386.evegator.Objects.AuthObject;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.awt.*;

public class EveGator extends Application {


    Stage window;
    Scene scene;

    AuthObject AUTHENTICATION;

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

    private void authentication()
    {

        // Create button
        Button authenticateButton = new Button("Authenticate");

        // Authenticate user and save data to Authentication
        authenticateButton.setOnAction(e -> {
            // Authenticate
            this.AUTHENTICATION = Authentication.authenticate();
            // Show Welcome
            this.welcome();
        });

        // Create layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(authenticateButton);
        layout.setAlignment(Pos.CENTER);

        // Set scene
        this.setScene(layout);
    }

    private void welcome()
    {
        Label welcome = new Label("Banana it works");
        Label accessToken = new Label("Access Token: " + this.AUTHENTICATION.ACCESS_TOKEN);
        Label refreshToken = new Label("Refresh Token: " + this.AUTHENTICATION.REFRESH_TOKEN);
        Label expires = new Label("Expires: " + this.AUTHENTICATION.EXPIRES_IN);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(welcome, accessToken, refreshToken, expires);
        layout.setAlignment(Pos.CENTER);

        this.setScene(layout);
    }

    private void setScene(Pane layout)
    {
        this.scene = new Scene(layout);
        this.window.setScene(scene);
    }
}
