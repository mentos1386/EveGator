package com.mentos1386.evegator.Controllers;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class SQLiteController {

    Connection con;
    Boolean fresh;

    public Boolean fresh() {
        return fresh;
    }

    public void init() throws Exception {

        this.fresh = !this.databaseExist();

        Class.forName("org.sqlite.JDBC");
        this.con = DriverManager.getConnection("jdbc:sqlite:evegator.db");

        if (this.fresh) {
            System.out.println("[SQLite] Database didn't exist, making fresh one!");
            this.createRegions();
            this.createConstellations();
            this.createSolarSystems();
            this.createStargates();
        }

    }

    public void close() throws Exception {
        con.close();
    }

    private void createRegions() throws Exception {
        Statement stmt = this.con.createStatement();

        String regions = "CREATE TABLE region " +
                "(id INT PRIMARY KEY NOT NULL," +
                " name VARCHAR(20) NOT NULL)";

        stmt.executeUpdate(regions);
        stmt.close();

        System.out.println("[SQLite] Created Regions");
    }

    private void createConstellations() throws Exception {
        Statement stmt = this.con.createStatement();

        String constellations = "CREATE TABLE constellation " +
                "(id INT PRIMARY KEY NOT NULL," +
                " region_id INT," +
                " name VARCHAR(20) NOT NULL," +
                " x REAL NOT NULL ," +
                " y REAL NOT NULL ," +
                " z REAL NOT NULL ," +
                " FOREIGN KEY(region_id) REFERENCES region(id))";

        stmt.executeUpdate(constellations);
        stmt.close();

        System.out.println("[SQLite] Created Constellations");
    }

    private void createSolarSystems() throws Exception {
        Statement stmt = this.con.createStatement();

        String solarSystems = "CREATE TABLE solarSystem " +
                "(id INT PRIMARY KEY NOT NULL," +
                " constellation_id INT," +
                " name VARCHAR(20) NOT NULL," +
                " securitystatus REAL NOT NULL ," +
                " x REAL NOT NULL ," +
                " y REAL NOT NULL ," +
                " z REAL NOT NULL ," +
                " FOREIGN KEY(constellation_id) REFERENCES constellation(id))";

        stmt.executeUpdate(solarSystems);
        stmt.close();

        System.out.println("[SQLite] Created Solar Systems");
    }

    private void createStargates() throws Exception {
        Statement stmt = this.con.createStatement();

        String stargates = "CREATE TABLE stargate " +
                "(id INT PRIMARY KEY NOT NULL ," +
                " solarsystem_id INT NOT NULL ," +
                " name VARCHAR(20) NOT NULL ," +
                " type INT NOT NULL ," +
                " x REAL NOT NULL ," +
                " y REAL NOT NULL ," +
                " z REAL NOT NULL ," +
                " FOREIGN KEY(solarSystem_id) REFERENCES solarSystem(id))";

        stmt.executeUpdate(stargates);
        stmt.close();

        System.out.println("[SQLite] Created Stargates");
    }

    public void addRegion(int id, String name) throws Exception {
        PreparedStatement stmt = this.con.prepareStatement(
                "INSERT INTO region (id, name) VALUES(?,?)");

        stmt.setInt(1, id);
        stmt.setString(2, name);

        stmt.executeUpdate();

        System.out.println("[SQLite] [Region] Inserted: " + id + " " + name);
    }

    public void addConstellation(int id, int regionId, String name, long x, long y, long z) throws Exception {
        PreparedStatement stmt = this.con.prepareStatement(
                "INSERT INTO constellation (id,region_id, name, x, y, z) VALUES(?,?,?,?,?,?)");

        stmt.setInt(1, id);
        stmt.setInt(2, regionId);
        stmt.setString(3, name);
        stmt.setLong(4, x);
        stmt.setLong(5, y);
        stmt.setLong(6, z);

        stmt.executeUpdate();

        System.out.println("[SQLite] [Constellation] Inserted: " + id + " " + name);
    }

    public void addSolarSystem(int id, int constellationId, int sovereignty, String name, long securityStatus, long x, long y, long z) throws Exception {
        PreparedStatement stmt = this.con.prepareStatement(
                "INSERT INTO solarsystem (id, constellation_id, sovereignty, name, securitystatus, x, y, z) VALUES(?,?,?,?,?,?,?,?)");

        stmt.setInt(1, id);
        stmt.setInt(2, constellationId);
        stmt.setInt(3, sovereignty);
        stmt.
        stmt.setString(5, name);
        stmt.setLong(6, securityStatus);
        stmt.setLong(7, x);
        stmt.setLong(8, y);
        stmt.setLong(9, z);

        stmt.executeUpdate();

        System.out.println("[SQLite] [Solar System] Inserted: " + id + " " + name);
    }

    public boolean databaseExist() {
        File dbFile = new File("evegator.db");
        return dbFile.exists();
    }
}
