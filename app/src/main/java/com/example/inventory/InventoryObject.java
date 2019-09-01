package com.example.inventory;

import java.io.Serializable;
import java.util.UUID;

public class InventoryObject implements Serializable {

    Integer count;
    Integer minimumCount;
    String name;
    String unit;
    String id;


    public InventoryObject(String n, Integer c, String u, Integer min){
        name = n;
        count = c;
        minimumCount = min;
        unit = u;
        id = UUID.randomUUID().toString();
    }

    public void incrementCount() {
        count++;
    }

    public void decrementCount() {
        count--;
    }

    public int getCount() {
            return count;
    }

    public String getName() { return name; }

    public String getUnit() { return unit; }

    public int getMinimumCount() { return minimumCount; }

    public String getID() { return id; }

    public void setCount(Integer c) {
        count = c;
    }

    public void setName(String n) {
        name = n;
    }

    public void setUnit(String u) { unit = u; }

    public void setMinimumCount(Integer c) { minimumCount = c; }
}
