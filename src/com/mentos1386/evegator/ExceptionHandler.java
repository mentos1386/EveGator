package com.mentos1386.evegator;

import com.mentos1386.evegator.Controllers.InterfaceController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class ExceptionHandler {

    public ExceptionHandler(Exception e) {

        VBox p = new VBox(10);
        p.setMaxWidth(400);
        p.setMinWidth(200);
        p.setAlignment(Pos.CENTER);
        p.setPadding(new Insets(10));

        Text error = new Text(e.getMessage());
        error.setWrappingWidth(400);
        error.setTextAlignment(TextAlignment.CENTER);

        Button close = new Button("Close");
        close.setOnAction(a -> {
            Stage s = (Stage) close.getScene().getWindow();
            s.close();
        });

        p.getChildren().addAll(error, close);

        InterfaceController.newStageAndWait(new Scene(p), "Error", "error");
    }
}
