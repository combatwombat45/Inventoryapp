package com.example.inventory;

import java.io.Serializable;

public class InventoryObject implements Serializable {

    Integer count;
    String name;
    String unit;


    public InventoryObject(String n, Integer c, String u){
        name = n;
        count = c;
        unit = u;
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

    public String getName() {
        return name;
    }

    public String getUnit() { return unit; }

    public void setCount(Integer c) {
        count = c;
    }

    public void setName(String n) {
        name = n;
    }

    public void setUnit(String u) {
        unit = u; }
}
