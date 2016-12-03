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

}
