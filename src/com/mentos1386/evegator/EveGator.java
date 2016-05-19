package com.mentos1386.evegator;

import com.mentos1386.evegator.Controllers.InterfaceController;
import com.mentos1386.evegator.Controllers.SQLiteController;

public class EveGator {

    public static void main(String[] args) {

        // Create new Database Controller
        SQLiteController sqlite = new SQLiteController();
        // Initialize database
        sqlite.init();

        // Start interface
        InterfaceController.start();
    }


}
