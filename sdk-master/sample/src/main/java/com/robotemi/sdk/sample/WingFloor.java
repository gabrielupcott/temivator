package com.robotemi.sdk.sample;

import java.util.Objects;

class WingFloor {
    String wing;
    int floor;

    public WingFloor(String wing, int floor) {
        this.wing = wing;
        this.floor = floor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WingFloor)) return false;
        WingFloor that = (WingFloor) o;
        return floor == that.floor && Objects.equals(wing, that.wing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wing, floor);
    }

    @Override
    public String toString() {
        return wing + floor;
    }
}

