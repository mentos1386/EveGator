package com.mentos1386.evegator.Controllers;

import com.mentos1386.evegator.EveGator;
import com.mentos1386.evegator.Models.ConstellationObject;
import com.mentos1386.evegator.Models.RegionObject;
import com.mentos1386.evegator.Models.SolarSystemObject;
import com.mentos1386.evegator.Models.StargateObject;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PathController {

    public static List<RegionObject> avoidListRegions = new ArrayList<>();
    public static List<ConstellationObject> avoidListConstellations = new ArrayList<>();
    public static List<SolarSystemObject> avoidListSolarSystems = new ArrayList<>();
    // 2 = Safer
    // 1 = Less Secure
    // 0 = Shorter
    public static int modifier = 2;
    public static boolean avoid = true;

    Graph<SolarSystemObject, StargateObject> graph;

    public Graph<SolarSystemObject, StargateObject> init()
    {

        graph = new DirectedSparseGraph<>();

        EveGator.dataCon.solarSystems().values().forEach((solarSystem) -> {
            // Remove solarsystems without stargates (wormhole space)
            if (solarSystem.getStargates() != null) {
                graph.addVertex(solarSystem);
            }

        });

        for (SolarSystemObject ss : EveGator.dataCon.solarSystems().values()) {
            for (StargateObject sg : ss.getStargates().values()) {
                    graph.addEdge(sg, sg.getFromSolarSystem(), sg.getToSolarSystem());
            }
        }

        System.out.println("[GraphLoad] Edge loaded: " + graph.getEdgeCount());
        System.out.println("[GraphLoad] Vertex loaded: " + graph.getVertexCount());

        return graph;
    }
}
