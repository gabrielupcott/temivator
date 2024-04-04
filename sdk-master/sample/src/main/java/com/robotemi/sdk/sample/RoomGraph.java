package com.robotemi.sdk.sample;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class RoomGraph {
    Map<Dijkstra.Room, List<Dijkstra.Room>> graph = new HashMap<>();

    public void addConnection(RoomGraph graph, Dijkstra.Room room1, Dijkstra.Room room2) {
        List<Dijkstra.Room> connections = graph.graph.get(room1);
        if (connections == null) {
            connections = new ArrayList<>();
            graph.graph.put(room1, connections);
        }
        connections.add(room2);

        connections = graph.graph.get(room2);
        if (connections == null) {
            connections = new ArrayList<>();
            graph.graph.put(room2, connections);
        }
        connections.add(room1);
    }

}