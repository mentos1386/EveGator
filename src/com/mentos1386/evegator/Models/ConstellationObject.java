package com.mentos1386.evegator.Models;

public class ConstellationObject extends Object {

    private int regionId;

    public ConstellationObject(int id, String name, int regionId) {
        this.id = id;
        this.name = name;
        this.regionId = regionId;
    }

    public int getRegionId() {
        return regionId;
    }
}
