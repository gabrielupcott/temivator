package com.robotemi.sdk.sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathFinder {
    public List<String> directions = new ArrayList<>();
    String[] floorList = new String[] {"EA0", "EA1", "EA2", "EA3", "EA4", "E0", "E1", "E2", "E3"};
    String[] wings = new String[] {"E", "EA"};
    int[] numFloors = new int[] {4, 5};
    String[][] floorPaths = new String[][] {
            {"EA1", "EA2", "EA3", "EA4", "E0"},
            {"EA0", "EA2", "EA3", "EA4", "E1"},
            {"EA0", "EA1", "EA3", "EA4", "E2"},
            {"EA0", "EA1", "EA2", "EA4", "E3"},
            {"EA0", "EA1", "EA2", "EA3"},
            {"E1", "E2", "E3", "EA0"},
            {"E0", "E2", "E3", "EA1"},
            {"E0", "E1", "E3", "EA2"},
            {"E0", "E1", "E2", "EA3"},
    };

    String[] from;
    String[] to;

    public List<String> getDirections() {
        List<String> decodedDirections = new ArrayList<>();
        decodedDirections.add(String.format("SPEAK We are going to location %s%s%s.  Please follow me.",
                to[0], to[1], to[2]));
        for (int i = 1; i < this.directions.size(); i++) {
            String[] pathFrom = extractInfo(this.directions.get(i - 1) + "00");
            String[] pathTo = extractInfo(this.directions.get(i) + "00");
            //decodedDirections.add(String.format("From: %s To: %s", this.directions.get(i - 1), this.directions.get(i)));
            if (!pathFrom[0].equals(pathTo[0])) { // Same floor different wing
                decodedDirections.add(String.format("GOTO %s%sto%s%s",
                        pathFrom[0], pathFrom[1], pathTo[0], pathTo[1]));
                decodedDirections.add(String.format("LOADMAP %s%s",
                        pathTo[0], pathTo[1]));
            } else { // Same wing different floor
                decodedDirections.add(String.format("GOTO %s%soutpasselev",
                        pathFrom[0], pathFrom[1]));
                decodedDirections.add(String.format("SPEAK Push the elevator %s button.  When the doors open, please hold them open for me",
                        (pathFrom[1].charAt(0) > pathTo[1].charAt(0))?"Down":"Up"));
                decodedDirections.add(String.format("GOTO %s%sinpasselev",
                        pathFrom[0], pathFrom[1]));
                decodedDirections.add(String.format("SPEAK Push the button to go to floor %s",
                        pathTo[1]));
                decodedDirections.add(String.format("LOADMAP %s%s",
                        pathTo[0], pathTo[1]));
                decodedDirections.add("SPEAK When the doors open, please hold the doors open for me");
                decodedDirections.add(String.format("GOTO %s%sexitpasselev",
                        pathTo[0], pathTo[1]));
            }
        }
        decodedDirections.add(String.format("GOTO %s%s%s", to[0], to[1], to[2]));
        decodedDirections.add("SPEAK We have arrived.  Enjoy the rest of your time at Mohawk College.");
        decodedDirections.add("Done");
        return decodedDirections;
    }

    public PathFinder(String locationFrom, String locationTo) {
        from = extractInfo(locationFrom);
        to = extractInfo(locationTo);
        //directions.add(String.format("From: %s%s To: %s%s", from[0], from[1], to[0], to[1]));
        int flooradds = 0;
        int pathadds = 0;
        BuildingGraph graph = new BuildingGraph();
        for (int i = 0; i < wings.length; i++){
            for (int j = 0; j < numFloors[i]; j++) {
                graph.addFloor(new WingFloor(wings[i], j));
                flooradds++;
            }
        }
        for (int i = 0; i < floorList.length; i++) {
            String w1 = floorList[i].substring(0, floorList[i].length() - 1);
            int f1 = Integer.parseInt(floorList[i].substring(floorList[i].length() - 1));
            for (int j = 0; j < floorPaths[i].length; j++) {
                String w2 = floorPaths[i][j].substring(0, floorPaths[i][j].length() - 1);
                int f2 = Integer.parseInt(floorPaths[i][j].substring(floorPaths[i][j].length() - 1));
                graph.connectFloors(new WingFloor(w1, f1), new WingFloor(w2, f2));
                pathadds++;
            }
        }

        WingFloor start = new WingFloor(from[0], Integer.parseInt(from[1]));
        WingFloor goal = new WingFloor(to[0], Integer.parseInt(to[1]));
        //directions.add(String.format("Floors: %d, Paths: %d", flooradds, pathadds));
        List<WingFloor> path = graph.findPath(start, goal);

        for (WingFloor step : path) {
            directions.add(step.toString());
        }

    }

    public String[] extractInfo(String buildingCode) {
        String wing = "";
        String floor = "";
        String room = "";
        int i = 0; // Index to keep track of the position
        while (i < buildingCode.length() && !Character.isDigit(buildingCode.charAt(i))) {
            wing += buildingCode.charAt(i);
            i++;
        }
        if (i < buildingCode.length()) {
            floor += buildingCode.charAt(i);
            i++;
        }
        while (i < buildingCode.length()) {
            room += buildingCode.charAt(i);
            i++;
        }
        return new String[] { wing, floor, room };
    }
}
