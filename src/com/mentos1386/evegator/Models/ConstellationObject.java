package com.mentos1386.evegator.Models;

public class ConstellationObject {

    private int id;
    private String name;
    private int regionId;

    public ConstellationObject(int id, String name, int regionId) {
        this.id = id;
        this.name = name;
        this.regionId = regionId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRegionId() {
        return regionId;
    }
}
