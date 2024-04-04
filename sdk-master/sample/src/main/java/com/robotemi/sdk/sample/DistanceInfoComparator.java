package com.robotemi.sdk.sample;

import java.util.Comparator;

class DistanceInfoComparator implements Comparator<Dijkstra.DistanceInfo> {
    @Override
    public int compare(Dijkstra.DistanceInfo info1, Dijkstra.DistanceInfo info2) {
        return Integer.compare(info1.distance, info2.distance);
    }
}