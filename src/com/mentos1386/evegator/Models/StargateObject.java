package com.mentos1386.evegator.Models;

public class StargateObject {

    private final RegionObject fromRegion;
    private final RegionObject toRegion;

    private final ConstellationObject fromConstellation;
    private final ConstellationObject toConstellation;

    private final SolarSystemObject fromSolarSystem;
    private final SolarSystemObject toSolarSystem;

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
