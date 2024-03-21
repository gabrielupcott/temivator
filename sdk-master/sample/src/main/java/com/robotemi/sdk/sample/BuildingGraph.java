package com.robotemi.sdk.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.*;

class BuildingGraph {
    private Map<WingFloor, List<WingFloor>> adjacencyList;

    public BuildingGraph() {
        this.adjacencyList = new HashMap<>();
    }

    public void addFloor(WingFloor wingFloor) {
        adjacencyList.put(wingFloor, new ArrayList<>());
    }

    public void connectFloors(WingFloor src, WingFloor dest) {
        adjacencyList.get(src).add(dest);
        adjacencyList.get(dest).add(src);
    }

    public List<WingFloor> getConnectedFloors(WingFloor wingFloor) {
        return adjacencyList.get(wingFloor);
    }

    // BFS pathfinding
    public List<WingFloor> findPath(WingFloor start, WingFloor goal) {
        if (!adjacencyList.containsKey(start) || !adjacencyList.containsKey(goal)) {
            return Collections.emptyList(); // Start or goal does not exist
        }

        Queue<WingFloor> toVisit = new LinkedList<>();
        Map<WingFloor, WingFloor> cameFrom = new HashMap<>();
        Set<WingFloor> visited = new HashSet<>(); // To keep track of visited nodes

        toVisit.add(start);
        visited.add(start); // Mark start as visited

        while (!toVisit.isEmpty()) {
            WingFloor current = toVisit.poll();

            if (current.equals(goal)) {
                return reconstructPath(cameFrom, goal);
            }

            for (WingFloor neighbor : getConnectedFloors(current)) {
                if (!visited.contains(neighbor)) { // Check if the neighbor has not been visited
                    visited.add(neighbor); // Mark neighbor as visited
                    cameFrom.put(neighbor, current);
                    toVisit.add(neighbor);
                }
            }
        }

        return Collections.emptyList(); // No path found
    }

    private List<WingFloor> reconstructPath(Map<WingFloor, WingFloor> cameFrom, WingFloor goal) {
        List<WingFloor> path = new ArrayList<>();
        for (WingFloor at = goal; at != null; at = cameFrom.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }
}
