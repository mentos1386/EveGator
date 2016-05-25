package com.mentos1386.evegator.Controllers;

import com.mentos1386.evegator.EveGator;
import com.mentos1386.evegator.Models.SolarSystemObject;
import com.mentos1386.evegator.Models.StargateObject;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;

public class GraphController {

    Graph<SolarSystemObject, StargateObject> graph;

    public Graph<SolarSystemObject, StargateObject> init()
    {

        graph = new UndirectedSparseMultigraph<>();

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
