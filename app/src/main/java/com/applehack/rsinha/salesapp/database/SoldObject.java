package com.applehack.rsinha.salesapp.database;

/**
 * Created by rsinha on 12/3/16.
 */

public class SoldObject {
    public String name;
    public int quantity;
    public int price;


    public SoldObject(String name, int quan, int price){
        this.name=name;
        this.quantity=quan;
    }

    public SoldObject(String name, String quan, String price){
        this.name=name;
        this.quantity=Integer.parseInt(quan);
        this.price=Integer.parseInt(price);
    }
}
