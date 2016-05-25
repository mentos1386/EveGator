package com.mentos1386.evegator;

import com.mentos1386.evegator.Controllers.DataController;
import com.mentos1386.evegator.Controllers.InterfaceController;
import com.mentos1386.evegator.Controllers.SQLiteController;
import com.mentos1386.evegator.Controllers.GraphBuilder;
import com.mentos1386.evegator.Views.LoadingData;

public class EveGator {

    // Create new Database Controller
    public static SQLiteController SQLite = new SQLiteController();
    public static DataController dataCon = new DataController();
    public static GraphBuilder gb = new GraphBuilder();


    public static void main(String[] args) {

        // Initialize database
        try {
            System.out.println("[SQLite] Opening connection");
            SQLite.init();
        } catch (Exception e) {
            new ExceptionHandler(e);
            System.exit(1);
        }

        InterfaceController.run(new LoadingData(), "EVE Gator");

        try {
            // Close connection
            System.out.println("[SQLite] Closing connection");
            SQLite.close();
        } catch (Exception e) {
            new ExceptionHandler(e);
            System.exit(1);
        }
    }

}