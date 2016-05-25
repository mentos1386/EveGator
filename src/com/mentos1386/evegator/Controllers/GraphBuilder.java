package com.mentos1386.evegator.Controllers;

import com.mentos1386.evegator.EveGator;
import com.mentos1386.evegator.Interfaces.ViewInterface;
import com.mentos1386.evegator.Models.SolarSystemObject;
import com.mentos1386.evegator.Models.StargateObject;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.Graph;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;


public class GraphBuilder implements ViewInterface {

    static Map<Integer, SolarSystemObject> solarsystems = new HashMap<>();
    static Map<SolarSystemObject, StargateObject> stargates = new HashMap<>();
    ISOMLayout<SolarSystemObject, StargateObject> layout;
    Graph<SolarSystemObject, StargateObject> graph;
    private Group map;
    private ZoomableScrollPane zsp;

    public void init() {

        VBox root = new VBox(10);
        this.zsp = new ZoomableScrollPane(root);
        this.map = new Group();

        EveGator.dataCon.solarSystems().values().forEach((solarSystem) -> {
            // Remove solarsystems without stargates (wormhole space)
            if (solarSystem.getStargates() != null) {
                renderSolarSystem(solarSystem);
                solarsystems.put(solarSystem.getId(), solarSystem);
            }

        });

        for (SolarSystemObject ss : EveGator.dataCon.solarSystems().values()) {
            for (StargateObject sg : ss.getStargates().values()) {
                renderPath(sg);
                stargates.put(sg.getFromSolarSystem(), sg);
            }
        }

        System.out.println("[GraphLoad] Edge loaded: " + solarsystems.size());
        System.out.println("[GraphLoad] Vertex loaded: " + stargates.size());

        root.getChildren().add(this.map);
    }


    private void renderSolarSystem(SolarSystemObject ss) {

        // draw the vertex as a circle
        Circle circle = new Circle(ss.getLocation().getX(), ss.getLocation().getY(), 5);
        circle.setFill(Paint.valueOf(this.color(ss.getSecurity())));

        // add it to the group, so it is shown on screen
        this.map.getChildren().add(circle);
    }

    private void renderPath(StargateObject ss) {
        Point2D pStart = ss.getFromSolarSystem().getLocation();
        Point2D pEnd = ss.getToSolarSystem().getLocation();

        // Draw the line
        Line line = new Line(pStart.getX(), pStart.getY(), pEnd.getX(), pEnd.getY());

        Double sec = ss.getToSolarSystem().getSecurity();
        line.setStroke(Paint.valueOf(this.color(sec)));

        // add the edges to the screen
        map.getChildren().add(line);
    }

    private String color(double sec) {
        String color;
        if (sec >= 1.0) {
            color = "#2FEFEF";
        } else if (sec >= 0.9) {
            color = "#48F0C0";
        } else if (sec >= 0.8) {
            color = "#00EF47";
        } else if (sec >= 0.7) {
            color = "#00F000";
        } else if (sec >= 0.6) {
            color = "#8FEF2F";
        } else if (sec >= 0.5) {
            color = "#EFEF00";
        } else if (sec >= 0.4) {
            color = "#D77700";
        } else if (sec >= 0.3) {
            color = "#F06000";
        } else if (sec >= 0.2) {
            color = "#F04800";
        } else if (sec >= 0.1) {
            color = "#D73000";
        } else {
            color = "#F00000";
        }
        return color;
    }

    @Override
    public Scene build() {
        return new Scene(this.zsp);
    }
}
