package com.mentos1386.evegator;

import javafx.application.Application;
import javafx.stage.Stage;

public class EveGatorUI extends Application {

    private boolean authenticated = false;
    private Authentication auth = new Authentication();

    public void start(Stage stage) {

        if(!this.authenticated)
        {
            this.auth.authenticate(stage);
        }

    }
}
