package com.mentos1386.evegator.Controllers;

import com.mentos1386.evegator.EveGator;
import com.mentos1386.evegator.Interfaces.ViewInterface;
import com.mentos1386.evegator.Models.RegionObject;
import com.mentos1386.evegator.Models.SolarSystemObject;
import com.mentos1386.evegator.Models.StargateObject;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.awt.*;
import java.awt.geom.Point2D;


public class GraphBuilder implements ViewInterface {

    final int LITERATIONS = 7000;
    public boolean wasInit = false;
    public boolean wasUpdating = false;
    public boolean done;
    ISOMLayout<SolarSystemObject, StargateObject> layout;
    Graph<SolarSystemObject, StargateObject> graph;

    public Scene build() {
        // setup up the scene.
        Group root = new Group();
        VBox zsp = new VBox(root);
        Group viz = new Group();

        layout.lock(true);
        renderGraph(viz);
        layout.lock(false);

        root.getChildren().add(viz);

        return new Scene(zsp);
    }

    public void init(RegionObject region) {

        this.wasInit = true;

        this.graph = new UndirectedSparseMultigraph<>();

        EveGator.dataCon.solarSystems().values().forEach((solarSystem) -> {
            // Remove solarsystems without stargates (wormhole space)
            if (solarSystem.getRegion() != null) {
                if (solarSystem.getRegion().getId() == region.getId()) {
                    graph.addVertex(solarSystem);
                }
            }

        });

        for (SolarSystemObject ss : EveGator.dataCon.solarSystems().values()) {
            for (StargateObject sg : ss.getStargates().values()) {
                if (ss.getRegion() != null || sg.getFromRegion() != null) {
                    if (ss.getRegion().getId() == region.getId() || sg.getToRegion().getId() == region.getId()) {
                        graph.addEdge(sg, ss, sg.getToSolarSystem());
                    }
                }
            }
        }

        System.out.println("[GraphLoad] Edge loaded: " + graph.getEdgeCount());
        System.out.println("[GraphLoad] Vertex loaded: " + graph.getVertexCount());

        // define the layout we want to use for the graph
        // The layout will be modified by the VisualizationModel
        this.layout = new ISOMLayout<SolarSystemObject, StargateObject>(graph);
        new DefaultVisualizationModel<SolarSystemObject, StargateObject>(layout, new Dimension(900, 900));
    }

    // For big graphs
    //public void update(DataTask updateTask) {
    //    this.wasUpdating = true;
    //    this.layout.setMaxIterations(this.LITERATIONS);
    //    for(int i = 1; i <= this.LITERATIONS; i++) {
    //        this.layout.step();
    //        System.out.println("[GraphLoad] " + i + "/" + this.LITERATIONS);
    //        updateTask.updateDataLoading(i, this.LITERATIONS);
    //    }
    //    this.done = true;
    //}

    private void renderGraph(Group viz) {
        // draw the vertices in the graph
        for (SolarSystemObject v : this.graph.getVertices()) {

            // draw the vertex as a circle
            Circle circle = new Circle(this.layout.getX(v), this.layout.getY(v), 5);

            // add it to the group, so it is shown on screen
            viz.getChildren().add(circle);
        }

        // draw the edges
        for (StargateObject n : this.graph.getEdges()) {
            // get the end points of the edge
            Pair<SolarSystemObject> endpoints = this.graph.getEndpoints(n);

            // Get the end points as Point2D objects so we can use them in the
            // builder
            Point2D pStart = layout.apply(endpoints.getFirst());
            Point2D pEnd = layout.apply(endpoints.getSecond());

            // Draw the line
            Line line = new Line(pStart.getX(), pStart.getY(), pEnd.getX(), pEnd.getY());
            String color;
            Double sec = endpoints.getFirst().getSecurity();
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

            line.setStroke(javafx.scene.paint.Paint.valueOf(color));

            // add the edges to the screen
            viz.getChildren().add(line);
        }
    }
}
