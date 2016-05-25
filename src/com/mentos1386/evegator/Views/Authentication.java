package com.mentos1386.evegator.Views;

import com.mentos1386.evegator.Controllers.AuthController;
import com.mentos1386.evegator.Controllers.InterfaceController;
import com.mentos1386.evegator.Interfaces.ViewInterface;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Authentication implements ViewInterface{

    public Scene build()
    {
        // Create button
        Button authenticateButton = new Button("Authenticate");

        // Authenticate user and save data to AuthController
        authenticateButton.setOnAction(e -> {
            // Authenticate
            AuthController.authenticate();

            if(AuthController.getAuth() != null)
            {
                // Show Welcome
                InterfaceController.setScene(new Welcome().build());
            }
        });

        // Create layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(authenticateButton);
        layout.setAlignment(Pos.CENTER);

        // Return Layout
        return new Scene(layout);
    }
}
