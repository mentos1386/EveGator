package com.mentos1386.evegator.Controllers;

import com.mentos1386.evegator.Models.SolarSystemObject;
import com.mentos1386.evegator.Models.StargateObject;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

import java.util.Collection;

public class GraphController implements Graph<SolarSystemObject, StargateObject>{
    @Override
    public Collection<StargateObject> getEdges() {
        return null;
    }

    @Override
    public Collection<SolarSystemObject> getVertices() {
        return null;
    }

    @Override
    public boolean containsVertex(SolarSystemObject vertex) {
        return false;
    }

    @Override
    public boolean containsEdge(StargateObject edge) {
        return false;
    }

    @Override
    public int getEdgeCount() {
        return 0;
    }

    @Override
    public int getVertexCount() {
        return 0;
    }

    @Override
    public Collection<SolarSystemObject> getNeighbors(SolarSystemObject vertex) {
        return null;
    }

    @Override
    public Collection<StargateObject> getIncidentEdges(SolarSystemObject vertex) {
        return null;
    }

    @Override
    public Collection<SolarSystemObject> getIncidentVertices(StargateObject edge) {
        return null;
    }

    @Override
    public StargateObject findEdge(SolarSystemObject v1, SolarSystemObject v2) {
        return null;
    }

    @Override
    public Collection<StargateObject> findEdgeSet(SolarSystemObject v1, SolarSystemObject v2) {
        return null;
    }

    @Override
    public boolean addVertex(SolarSystemObject vertex) {
        return false;
    }

    @Override
    public boolean addEdge(StargateObject edge, Collection<? extends SolarSystemObject> vertices) {
        return false;
    }

    @Override
    public boolean addEdge(StargateObject edge, Collection<? extends SolarSystemObject> vertices, EdgeType edge_type) {
        return false;
    }

    @Override
    public boolean removeVertex(SolarSystemObject vertex) {
        return false;
    }

    @Override
    public boolean removeEdge(StargateObject edge) {
        return false;
    }

    @Override
    public boolean isNeighbor(SolarSystemObject v1, SolarSystemObject v2) {
        return false;
    }

    @Override
    public boolean isIncident(SolarSystemObject vertex, StargateObject edge) {
        return false;
    }

    @Override
    public int degree(SolarSystemObject vertex) {
        return 0;
    }

    @Override
    public int getNeighborCount(SolarSystemObject vertex) {
        return 0;
    }

    @Override
    public int getIncidentCount(StargateObject edge) {
        return 0;
    }

    @Override
    public EdgeType getEdgeType(StargateObject edge) {
        return null;
    }

    @Override
    public EdgeType getDefaultEdgeType() {
        return null;
    }

    @Override
    public Collection<StargateObject> getEdges(EdgeType edge_type) {
        return null;
    }

    @Override
    public int getEdgeCount(EdgeType edge_type) {
        return 0;
    }

    @Override
    public Collection<StargateObject> getInEdges(SolarSystemObject vertex) {
        return null;
    }

    @Override
    public Collection<StargateObject> getOutEdges(SolarSystemObject vertex) {
        return null;
    }

    @Override
    public Collection<SolarSystemObject> getPredecessors(SolarSystemObject vertex) {
        return null;
    }

    @Override
    public Collection<SolarSystemObject> getSuccessors(SolarSystemObject vertex) {
        return null;
    }

    @Override
    public int inDegree(SolarSystemObject vertex) {
        return 0;
    }

    @Override
    public int outDegree(SolarSystemObject vertex) {
        return 0;
    }

    @Override
    public boolean isPredecessor(SolarSystemObject v1, SolarSystemObject v2) {
        return false;
    }

    @Override
    public boolean isSuccessor(SolarSystemObject v1, SolarSystemObject v2) {
        return false;
    }

    @Override
    public int getPredecessorCount(SolarSystemObject vertex) {
        return 0;
    }

    @Override
    public int getSuccessorCount(SolarSystemObject vertex) {
        return 0;
    }

    @Override
    public SolarSystemObject getSource(StargateObject directed_edge) {
        return null;
    }

    @Override
    public SolarSystemObject getDest(StargateObject directed_edge) {
        return null;
    }

    @Override
    public boolean isSource(SolarSystemObject vertex, StargateObject edge) {
        return false;
    }

    @Override
    public boolean isDest(SolarSystemObject vertex, StargateObject edge) {
        return false;
    }

    @Override
    public boolean addEdge(StargateObject stargateObject, SolarSystemObject v1, SolarSystemObject v2) {
        return false;
    }

    @Override
    public boolean addEdge(StargateObject stargateObject, SolarSystemObject v1, SolarSystemObject v2, EdgeType edgeType) {
        return false;
    }

    @Override
    public Pair<SolarSystemObject> getEndpoints(StargateObject edge) {
        return null;
    }

    @Override
    public SolarSystemObject getOpposite(SolarSystemObject vertex, StargateObject edge) {
        return null;
    }
}
