package com.applehack.rsinha.salesapp;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applehack.rsinha.salesapp.database.MyData;
import com.applehack.rsinha.salesapp.database.MyDataBase;
import com.applehack.rsinha.salesapp.database.SoldObject;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShowReport extends AppCompatActivity {
    public static String TAG="ShowReport";
    String id;
    boolean admin;
    ArrayList<String> tables;
    LinearLayout holder;
    ArrayList<SoldObject> reportData;
    LayoutInflater layoutInflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_report);

        Bundle b=getIntent().getExtras();
        if(b!=null){
            id=b.getString("id","xyz");
            admin=b.getBoolean("admin",false);
        }

        bindViews();
        showRecord();
    }

    private void bindViews() {
        layoutInflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        holder=(LinearLayout)findViewById(R.id.report_holder);
    }

    private void showRecord(){
        tables= MyDataBase.getAllTables(this);
        holder.removeAllViews();
        populateTextField();
        //holder.addView();
    }
    void populateTextField(){
        for(int i=0;i<tables.size();i++){
            View view=layoutInflater.inflate(R.layout.inflate_report,null);
            LinearLayout lay=(LinearLayout) view.findViewById(R.id.report_details_holder);
            TextView t=(TextView) view.findViewById(R.id.details_table_name);
            t.setText(tables.get(i));
            Log.i(TAG,tables.get(i)+" : "+id);
            if(tables.get(i).equals(id)){
                Log.i(TAG,"Its Here");
                LinearLayout l=(LinearLayout) view.findViewById(R.id.details_backgroud);
                l.setBackgroundColor(Color.GREEN);
            }
            reportData= MyDataBase.getData(this, tables.get(i));
            if(admin) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LinearLayout lay = (LinearLayout) view.findViewById(R.id.report_details_holder);
                        if (lay.getVisibility() == View.GONE) {
                            lay.setVisibility(View.VISIBLE);
                        } else {
                            lay.setVisibility(View.GONE);
                        }
                    }
                });
            }
            for(int j=0;j<reportData.size();j++){
                View v=layoutInflater.inflate(R.layout.inflate_report_field,null);
                TextView t1=(TextView) v.findViewById(R.id.txt_field_product_name);
                TextView t2=(TextView) v.findViewById(R.id.txt_field_product_quantity);
                t1.setText(reportData.get(j).name);
                t2.setText(String.valueOf(reportData.get(j).quantity));
                lay.addView(v);
            }
            holder.addView(view);
        }
    }
}
