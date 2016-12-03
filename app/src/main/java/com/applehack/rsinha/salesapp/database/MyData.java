package com.applehack.rsinha.salesapp.database;

import android.util.Log;

/**
 * Created by rsinha on 12/3/16.
 */

public class MyData {
    public String name,url,description;
    public int price;

    public MyData(){
        name="";
        url="";
        description="";
        price=0;
    }
    public MyData(String name, String price, String description, String url){
        this.name=name;
        this.url=url;
        this.description=description;
        this.price=Integer.parseInt(price);
        //Log.i("database","Price : "+this.price);
    }

}
