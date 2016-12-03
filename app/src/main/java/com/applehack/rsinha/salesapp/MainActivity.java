package com.applehack.rsinha.salesapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applehack.rsinha.salesapp.database.MyData;
import com.applehack.rsinha.salesapp.database.MyDataBase;
import com.applehack.rsinha.salesapp.database.SoldObject;

import org.w3c.dom.Text;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public String TAG = "MainActivity";
    private LayoutInflater layoutInflater;
    private LinearLayout holder;
    private ArrayList<MyData> datas;
    private ArrayList<Boolean> sold;
    private String id, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id", "XXX");
            name = bundle.getString("name", "Rahul Sinha");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
                String msg="You Sold - \n";
                for(int i=0;i<sold.size();i++){
                    if(sold.get(i)){
                        msg = msg+datas.get(i).name+"\n";
                    }
                }
                adb.setTitle("Summary of What you sold")
                        .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                storeInDatabase();
                            }
                        })
                        .setNegativeButton("Later",null)
                        .setMessage(msg)
                        .show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        bindViews();
        refreshScreen();
    }
    private void storeInDatabase(){
        for(int i=0;i<sold.size();i++) {
            if (sold.get(i)) {
                SoldObject soldO=new SoldObject(datas.get(i).name,1);
                MyDataBase.insert(this,id,soldO);
            }
        }
    }
    private void bindViews() {
        holder = (LinearLayout) findViewById(R.id.voucher_holder);
        TextView tv = (TextView) findViewById(R.id.user_name);
        tv.setText("Hi, " + name);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.add_product) {
            showFileChooser();
        } else if (id == R.id.show_report) {
            startActivity(new Intent(this, ShowReport.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private static final int FILE_SELECT_CODE = 1908;

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void showAddDetailsDialog(final String path) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        View v = layoutInflater.inflate(R.layout.add_details, null);
        adb.setView(v);
        adb.setCancelable(false);
        final AlertDialog ad = adb.create();
        ad.show();

        final EditText pName = (EditText) v.findViewById(R.id.txt_product_name);
        final EditText pPrice = (EditText) v.findViewById(R.id.txt_price);
        final EditText pDetails = (EditText) v.findViewById(R.id.txt_description);
        Button button = (Button) v.findViewById(R.id.btn_add_product);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = pName.getText().toString();
                String price = pPrice.getText().toString();
                String details = pDetails.getText().toString();
                addToDatabase(name, price, details, path);
                ad.dismiss();
            }
        });
    }

    public void addToDatabase(String name, String price, String details, String path) {
        MyData d = new MyData(name, price, details, path);
        MyDataBase.insert(this, d);

        reflectChange();
    }

    private void reflectChange() {
        refreshScreen();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());
                    // Get the path
                    String path = null;
                    try {
                        path = getPath(this, uri);
                    } catch (URISyntaxException e) {
                        Log.i(TAG, e.getMessage());
                    }
                    Log.d(TAG, "File Path: " + path);
                    if (path == null) {
                        Toast.makeText(this, "Sorry, Could load the Image", Toast.LENGTH_LONG).show();
                    } else {
                        showAddDetailsDialog(path);
                    }
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public void refreshScreen() {
        holder.removeAllViews();
        Log.i(TAG, "1");
        datas = MyDataBase.getData(this);
        sold = new ArrayList<Boolean>();
        for (int i = 0; i < datas.size(); i++) {
            sold.add(false);
        }
        Log.i(TAG, "2");

        if (datas.size() == 0) {
            TextView tv = new TextView(this);
            tv.setText("Nothing TO show");
            holder.addView(tv);
        }
        Log.i(TAG, "3");
        for (int i = 0; i < datas.size(); i++) {
            try {
                View v = layoutInflater.inflate(R.layout.inflate_horizontal, null);
                ImageView img = (ImageView) v.findViewById(R.id.img_show_product);
                TextView name = (TextView) v.findViewById(R.id.txt_show_product_name);
                TextView price = (TextView) v.findViewById(R.id.txt_show_price);
                TextView desc = (TextView) v.findViewById(R.id.txt_show_description);
                CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox);
                Log.i(TAG, "3.0");
                name.setText(datas.get(i).name);
                Log.i(TAG, "3.01");
                desc.setText(datas.get(i).description);
                Log.i(TAG, "3.02");
                price.setText(String.valueOf(datas.get(i).price));
                String path = datas.get(i).url;
                Log.i(TAG, path);
                File file = new File(path);
                Log.i(TAG, file.getAbsolutePath());
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                Log.i(TAG, "3.1");
                img.setImageBitmap(bitmap);
                Log.i(TAG, "3.2");
                //TODO Check Click Listener

                cb.setTag(i);
                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i(TAG,"Its Here");
                        CheckBox cb=(CheckBox) view;
                        int ind=(int)view.getTag();
                        sold.set(ind,cb.isChecked());
                    }
                });

                holder.addView(v);
            } catch (Exception ex) {
                Log.i(TAG, "3.3 : " + ex.getMessage());
                ex.printStackTrace();
            }
            Log.i(TAG, "4");
        }
        Log.i(TAG, "5");
    }
}
