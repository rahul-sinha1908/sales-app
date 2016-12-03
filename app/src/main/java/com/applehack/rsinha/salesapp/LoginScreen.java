package com.applehack.rsinha.salesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class LoginScreen extends AppCompatActivity {

    private Button btn;
    private TextView id,name,pass;
    private CheckBox cb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        bindListener();
    }
    private void bindListener(){
        btn=(Button)findViewById(R.id.btn_login);
        id=(TextView)findViewById(R.id.txt_login_id);
        pass=(TextView)findViewById(R.id.password);
        name=(TextView)findViewById(R.id.txt_login_name);
        cb=(CheckBox)findViewById(R.id.checkBox2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LoginScreen.this ,MainActivity.class);
                i.putExtra("id",id.getText().toString());
                i.putExtra("name",name.getText().toString());
                Log.i("database",pass.getText().toString());
                if(pass.getText().toString().equals("Hello")) {
                    Toast.makeText(LoginScreen.this,"Logged In as Admin",Toast.LENGTH_LONG).show();
                    i.putExtra("admin", cb.isChecked());
                }else
                    i.putExtra("admin",false);
                startActivity(i);
            }
        });
    }
}
