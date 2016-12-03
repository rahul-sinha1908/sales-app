package com.applehack.rsinha.salesapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by rsinha on 12/3/16.
 */

public class MyDataBase {
    public static SQLiteDatabase sdb;
    public static String T="database";

    public static SQLiteDatabase initiate(Context cont){
        if(sdb==null)
            sdb=cont.openOrCreateDatabase("SalesApp",Context.MODE_PRIVATE,null);
        return sdb;
    }
    public static ArrayList<String> getAllTables(Context context){
        initiate(context);
        ArrayList<String> arrayList=new ArrayList<String>();
        String sql="SELECT name FROM sqlite_master WHERE type='table'";
        Cursor cursor=sdb.rawQuery(sql, null);
        if(cursor!=null&& cursor.moveToFirst()){
            do{
                String name=cursor.getString(0);
                if(!name.equals("DATA") && !name.equals("android_metadata"))
                    arrayList.add(name);
            }while(cursor.moveToNext());
        }
        return arrayList;
    }

    public static void createTable(Context cont){
        initiate(cont);
        try {
            String sql = "CREATE TABLE IF NOT EXISTS `DATA`(Name varchar, URL varchar, Description varchar, Price varchar)";
            sdb.execSQL(sql);
        }catch(Exception ex){
            Log.i(T, ex.getMessage());
        }
    }

    public static void insert(Context cont, MyData d){
        createTable(cont);
        try{
            String sql="INSERT INTO `DATA` VALUES('"+d.name+"','"+d.url+"','"+d.description+"','"+d.price+"')";
            sdb.execSQL(sql);
        }catch(Exception ex){
            Log.i(T, ex.getMessage());
        }
    }
    public static void delete(Context cont, MyData d){
        createTable(cont);
        try{
            String sql="DELETE FROM `DATA` WHERE Name='"+d.name+"'";
            sdb.execSQL(sql);
        }catch(Exception ex){
            Log.i(T, ex.getMessage());
        }
    }
    public static ArrayList<MyData> getData(Context context){
        createTable(context);
        ArrayList<MyData> data = new ArrayList<MyData>();
        try{
            String sql="SELECT * FROM `DATA`";
            Cursor cursor = sdb.rawQuery(sql,null);

            if(cursor!=null && cursor.moveToFirst()){
                do{
                    Log.i(T,cursor.getString(0)+" : "+cursor.getString(3)+" : "+cursor.getString(2)+" : "+cursor.getString(1));
                    MyData d=new MyData(cursor.getString(0),cursor.getString(3),cursor.getString(2), cursor.getString(1));
                    data.add(d);
                }while(cursor.moveToNext());
            }
        }catch(Exception ex){
            Log.i(T, ex.getMessage());
        }
        return data;
    }

    public static void createTable(Context cont, String tName){
        initiate(cont);
        try {
            String sql = "CREATE TABLE IF NOT EXISTS `"+tName+"`(Name varchar, Quantity integer)";
            sdb.execSQL(sql);
        }catch(Exception ex){
            Log.i(T, ex.getMessage());
        }
    }

    public static void insert(Context cont, String tName,SoldObject ob){
        createTable(cont,tName);
        try{
            String sql="INSERT INTO `"+tName+"` VALUES('"+ob.name+"',"+ob.quantity+")";
            sdb.execSQL(sql);
        }catch(Exception ex){
            Log.i(T, ex.getMessage());
        }
    }
    public static ArrayList<SoldObject> getData(Context context, String tName){
        createTable(context, tName);
        ArrayList<SoldObject> data = new ArrayList<SoldObject>();
        try{
            String sql="SELECT Name, Quantity FROM `"+tName+"` ";
            Cursor cursor = sdb.rawQuery(sql,null);

            if(cursor!=null && cursor.moveToFirst()){
                do{
                    //Log.i(T,cursor.getString(0)+" : "+cursor.getString(3)+" : "+cursor.getString(2)+" : "+cursor.getString(1));
                    SoldObject d=new SoldObject(cursor.getString(0),cursor.getString(1));
                    data.add(d);
                }while(cursor.moveToNext());
            }
        }catch(Exception ex){
            Log.i(T, ex.getMessage());
        }
        return data;
    }


}
