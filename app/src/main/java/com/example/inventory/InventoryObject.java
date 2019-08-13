package com.example.inventory;

import java.io.Serializable;

public class InventoryObject implements Serializable {

    Integer count;
    String name;

    public InventoryObject(String n, Integer c){
        System.out.println("Passed name is :" + n );
        name = n;
        count = c;
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
}
