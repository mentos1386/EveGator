package com.mentos1386.evegator.Controllers;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class SQLiteController {

    Connection con;
    Boolean fresh;

    public Boolean fresh() {
        return fresh;
    }

    public void init() throws Exception {

        this.fresh = !this.databaseExist();

        Class.forName("org.sqlite.JDBC");
        this.con = DriverManager.getConnection("jdbc:sqlite:evegator.sqlite");

        if (this.fresh) {
            System.out.println("[SQLite] Database doesn't exist, downloading new one!");
            // Url to download https://www.fuzzwork.co.uk/dump/sqlite-latest.sqlite.bz2
        }

    }

    public Connection getCon()
    {
        return this.con;
    }

    public void close() throws Exception {
        con.close();
    }

    public boolean databaseExist() {
        File dbFile = new File("evegator.sqlite");
        return dbFile.exists();
    }
}
