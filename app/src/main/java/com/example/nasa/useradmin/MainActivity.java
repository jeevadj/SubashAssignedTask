package com.example.nasa.useradmin;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
{
    Button user,admin;
    public final int requestPermission=1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case requestPermission : Boolean storage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                                     if(storage){
                                         Toast.makeText(MainActivity.this, "Permission granted...", Toast.LENGTH_SHORT).show();
                                     }
                                     else{
                                         Toast.makeText(MainActivity.this, "Permission denied...", Toast.LENGTH_SHORT).show();
                                     }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user=(Button) findViewById(R.id.user);
        admin=(Button) findViewById(R.id.admin);

        int storage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(storage == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(MainActivity.this, "Permission Granted...", Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},requestPermission);
        }

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Admin_Activity.class);
                startActivity(i);
                finish();
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,User_Activity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
