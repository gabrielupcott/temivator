package com.robotemi.sdk.sample;
import java.util.*;

public class Dijkstra {

    static class Room {
        String wing;
        int floor;

        public Room(String wing, int floor) {
            this.wing = wing;
            this.floor = floor;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Room other = (Room) obj;
            return wing.equals(other.wing) && floor == other.floor;
        }

        @Override
        public int hashCode() {
            return Objects.hash(wing, floor);
        }
    }

    static class DistanceInfo {
        Room room;
        int distance;

        public DistanceInfo(Room room, int distance) {
            this.room = room;
            this.distance = distance;
        }
    }

    static class DistanceInfoComparator implements Comparator<DistanceInfo> {
        @Override
        public int compare(DistanceInfo info1, DistanceInfo info2) {
            return Integer.compare(info1.distance, info2.distance);
        }
    }
    public static List<Dijkstra.Room> getConnections(RoomGraph graph, Dijkstra.Room room) {
        List<Dijkstra.Room> connections = graph.graph.get(room);
        if (connections == null) {
            return new ArrayList<>();
        }
        return connections;
    }

    public static List<Room> dijkstraWithWeights(RoomGraph graph, Room start, Room end) {
        Map<Room, Integer> distances = new HashMap<>();
        Map<Room, Room> predecessors = new HashMap<>();
        TreeSet<DistanceInfo> pq = new TreeSet<>(new DistanceInfoComparator());


        for (Room room : graph.graph.keySet()) {
            distances.put(room, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        pq.add(new DistanceInfo(start, 0));

        while (!pq.isEmpty()) {
            DistanceInfo currentInfo = pq.first();
            pq.remove(currentInfo);
            Room currentRoom = currentInfo.room;
            int currentDistance = currentInfo.distance;

            if (currentDistance > distances.get(currentRoom)) {
                continue;
            }

            if (currentRoom.equals(end)) {
                return reconstructPath(predecessors, end);
            }

            for (Room neighbor : getConnections(graph, currentRoom)) {
                int weight = 1; // Default weight
                if (!neighbor.wing.equals(currentRoom.wing)) {
                    weight = 10; // Weight for moving between wings
                } else if (neighbor.floor != currentRoom.floor) {
                    weight = 5; // Weight for using an elevator
                }

                int distanceThroughCurrent = currentDistance + weight;
                if (distanceThroughCurrent < distances.get(neighbor)) {
                    distances.put(neighbor, distanceThroughCurrent);
                    predecessors.put(neighbor, currentRoom);
                    pq.add(new DistanceInfo(neighbor, distanceThroughCurrent));
                }
            }
        }
        return null; // No path found
    }

    public static List<Room> reconstructPath(Map<Room, Room> predecessors, Room end) {
        List<Room> path = new ArrayList<>();
        Room current = end;
        while (predecessors.containsKey(current)) {
            path.add(0, current);
            current = predecessors.get(current);
        }
        path.add(0, current);
        return path;
    }

}
