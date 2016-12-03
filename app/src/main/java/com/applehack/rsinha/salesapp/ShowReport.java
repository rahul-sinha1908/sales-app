package com.applehack.rsinha.salesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applehack.rsinha.salesapp.database.MyData;
import com.applehack.rsinha.salesapp.database.MyDataBase;
import com.applehack.rsinha.salesapp.database.SoldObject;

import java.util.ArrayList;

public class ShowReport extends AppCompatActivity {

    ArrayList<String> tables;
    LinearLayout holder;
    ArrayList<SoldObject> reportData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_report);

        bindViews();
        showRecord();
    }

    private void bindViews() {
        holder=(LinearLayout)findViewById(R.id.report_holder);
    }

    private void showRecord(){
        tables= MyDataBase.getAllTables(this);
        TextView tv=new TextView(this);
        tv.setText("");
        populateTextField(tv);
        holder.addView(tv);
    }
    void populateTextField(TextView tv){
        for(int i=0;i<tables.size();i++){
            reportData= MyDataBase.getData(this, tables.get(i));
            tv.append("Id OF User : "+tables.get(i)+"\n");
            for(int j=0;j<reportData.size();j++){
                tv.append("\t Item: "+reportData.get(j).name+"\n");
                tv.append("\t\t Quantity: "+reportData.get(j).quantity+"\n");
            }
        }
    }
}
