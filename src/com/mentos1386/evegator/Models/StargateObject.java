package com.mentos1386.evegator.Models;

import java.awt.geom.Point2D;

public class StargateObject {

    private RegionObject fromRegion;
    private RegionObject toRegion;

    private ConstellationObject fromConstellation;
    private ConstellationObject toConstellation;

    private SolarSystemObject fromSolarSystem;
    private SolarSystemObject toSolarSystem;

    public StargateObject(
            RegionObject fromRegion,
            RegionObject toRegion,
            ConstellationObject fromConstellation,
            ConstellationObject toConstellation,
            SolarSystemObject fromSolarSystem,
            SolarSystemObject toSolarSystem

    ) {
        this.fromRegion = fromRegion;
        this.toRegion = toRegion;
        this.fromConstellation = fromConstellation;
        this.toConstellation = toConstellation;
        this.fromSolarSystem = fromSolarSystem;
        this.toSolarSystem = toSolarSystem;
    }

    public RegionObject getFromRegion() {
        return fromRegion;
    }

    public RegionObject getToRegion() {
        return toRegion;
    }

    public ConstellationObject getFromConstellation() {
        return fromConstellation;
    }

    public ConstellationObject getToConstellation() {
        return toConstellation;
    }

    public SolarSystemObject getFromSolarSystem() {
        return fromSolarSystem;
    }

    public SolarSystemObject getToSolarSystem() {
        return toSolarSystem;
    }

    public String toString() {
        return "From: " + this.fromSolarSystem.getName() +
                " To: " + this.toSolarSystem.getName();
    }
}
