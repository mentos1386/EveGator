package com.mentos1386.evegator.Controllers;

import com.mentos1386.evegator.EveGator;
import com.mentos1386.evegator.Models.ConstellationObject;
import com.mentos1386.evegator.Models.RegionObject;
import com.mentos1386.evegator.Models.SolarSystemObject;
import com.mentos1386.evegator.Models.StargateObject;

import java.awt.geom.Point2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DataController {

    private Map<Integer, SolarSystemObject> solarSystems =
            new HashMap<Integer, SolarSystemObject>();
    private Map<Integer, RegionObject> regions =
            new HashMap<Integer, RegionObject>();

    public Map<Integer, SolarSystemObject> solarSystems() {
        return solarSystems;
    }

    public Map<Integer, RegionObject> regions() {
        return regions;
    }

    public Map<Integer, ConstellationObject> constellations() {
        return constellations;
    }

    private Map<Integer, ConstellationObject> constellations =
            new HashMap<Integer, ConstellationObject>();

    private SQLiteController sqlite = EveGator.SQLite;

    public void getRegions(DataTask task) throws SQLException {
        Statement stmt = this.sqlite.getCon().createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT" +
                        " regionID," +
                        " regionName" +
                        " FROM  mapRegions");
        stmt = this.sqlite.getCon().createStatement();
        ResultSet rows = stmt.executeQuery(
                "SELECT" +
                        " count(*)" +
                        " FROM  mapRegions LIMIT 1");


        while (rs.next()) {
            // Update progress
            task.updateDataLoading(rs.getRow(), rows.getInt(1));

            // Create region
            RegionObject region = new RegionObject(
                    rs.getInt(1),
                    rs.getString(2));

            regions.put(region.getId(), region);
        }
        rs.close();
    }

    public void getConstellations(DataTask task) throws SQLException {
        Statement stmt = this.sqlite.getCon().createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT" +
                        " constellationName," +
                        " constellationID," +
                        " regionID" +
                        " FROM  mapConstellations");
        stmt = this.sqlite.getCon().createStatement();
        ResultSet rows = stmt.executeQuery(
                "SELECT" +
                        " count(*)" +
                        " FROM  mapConstellations LIMIT 1");

        while (rs.next()) {
            // Update progress
            task.updateDataLoading(rs.getRow(), rows.getInt(1));

            // Create region
            ConstellationObject constellation = new ConstellationObject(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getInt(3));

            constellations.put(constellation.getId(), constellation);
        }
        rs.close();
    }

    public void getSolarsystems(DataTask task) throws SQLException {
        Statement stmt = this.sqlite.getCon().createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT" +
                        " solarSystemID," +
                        " solarSystemName," +
                        " regionID," +
                        " constellationID," +
                        " securityClass," +
                        " security," +
                        " x," +
                        " y" +
                        " FROM  mapSolarSystems");
        stmt = this.sqlite.getCon().createStatement();
        ResultSet rows = stmt.executeQuery(
                "SELECT" +
                        " count(*)" +
                        " FROM  mapSolarSystems LIMIT 1");

        while (rs.next()) {
            // Update progress
            task.updateDataLoading(rs.getRow(), rows.getInt(1));

            // Create SolarSystem
            SolarSystemObject ss = new SolarSystemObject(
                    rs.getInt(1),
                    rs.getString(2),
                    regions.get(rs.getInt(3)),
                    constellations.get(rs.getInt(4)),
                    rs.getString(5),
                    rs.getDouble(6),
                    new Point2D.Double(rs.getDouble(7), rs.getDouble(8)));

            solarSystems.put(ss.getId(), ss);
        }
        rs.close();
    }

    public void getStargates(DataTask task) throws SQLException {
        Statement stmt = this.sqlite.getCon().createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT" +
                        " fromRegionID," +
                        " toRegionID," +
                        " fromConstellationID," +
                        " toConstellationID," +
                        " fromSolarSystemID," +
                        " toSolarSystemID" +
                        " FROM  mapSolarSystemJumps");
        stmt = this.sqlite.getCon().createStatement();
        ResultSet rows = stmt.executeQuery(
                "SELECT" +
                        " count(*)" +
                        " FROM  mapSolarSystemJumps LIMIT 1");

        while (rs.next()) {
            // Update progress
            task.updateDataLoading(rs.getRow(), rows.getInt(1));

            // Create SolarSystem
            StargateObject sg = new StargateObject(
                    this.regions.get(rs.getInt(1)),
                    this.regions.get(rs.getInt(2)),
                    this.constellations.get(rs.getInt(3)),
                    this.constellations.get(rs.getInt(4)),
                    this.solarSystems.get(rs.getInt(5)),
                    this.solarSystems.get(rs.getInt(6)));

            this.solarSystems.get(rs.getInt(5)).addStargate(sg);
        }
        rs.close();
    }
}
