package com.applehack.rsinha.salesapp.database;

/**
 * Created by rsinha on 12/3/16.
 */

public class SoldObject {
    public String name;
    public int quantity;


    public SoldObject(String name, int quan){
        this.name=name;
        this.quantity=quan;
    }

    public SoldObject(String name, String quan){
        this.name=name;
        this.quantity=Integer.parseInt(quan);
    }
}
