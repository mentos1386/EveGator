package com.mentos1386.evegator.Models;

public abstract class Object implements Comparable<Object> {

    String name;
    int id;

    public String getName() {
        return this.name;
    }

    public int getId() {
        return id;
    }

    public int compareTo(Object o) {
        return this.name.compareTo(o.getName());
    }

    public String toString() {
        return this.name;
    }

}
