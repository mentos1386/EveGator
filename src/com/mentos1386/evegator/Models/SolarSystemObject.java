package com.mentos1386.evegator.Models;

import java.util.HashMap;
import java.util.Map;

public class SolarSystemObject {

    // Solar system info
    private int id;
    private String name;
    private String securityClass;
    private float security;

    // Stargates

    // Sargates   < toSolarSystemObject, StargateObject>
    private Map<SolarSystemObject, StargateObject> stargates =
            new HashMap<SolarSystemObject, StargateObject>();

    // Relations
    private ConstellationObject constellation;
    private RegionObject region;

    public SolarSystemObject(int id,
                             String name,
                             RegionObject region,
                             ConstellationObject constellation,
                             String securityClass,
                             float security) {
        this.id = id;
        this.name = name;
        this.securityClass = securityClass;
        this.security = security;
        this.constellation = constellation;
        this.region = region;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSecurityClass() {
        return securityClass;
    }

    public Map<SolarSystemObject, StargateObject> getStargates() {
        return stargates;
    }

    public float getSecurity() {
        return security;
    }

    public ConstellationObject getConstellation() {
        return constellation;
    }

    public RegionObject getRegion() {
        return region;
    }

    public void addStargate(StargateObject stargateObject) {
        this.stargates.put(stargateObject.getToSolarSystem(), stargateObject);
    }
}
