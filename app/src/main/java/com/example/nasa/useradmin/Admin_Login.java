package com.example.nasa.useradmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class Admin_Login extends AppCompatActivity
{
    EditText e1,e2;
    Button b1;
    String s1,s2;
    boolean flag = false;
    Firebase fb_db;
    ProgressDialog progressDialog;
    String BaseUrl="https://broadcaster-68002.firebaseio.com/Admin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__login);

        Firebase.setAndroidContext(this);

        e1=(EditText)findViewById(R.id.email);
        e2=(EditText)findViewById(R.id.pass);
        b1=(Button)findViewById(R.id.alog);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s1 = e1.getText().toString();
                s2 = e2.getText().toString();

                progressDialog=new ProgressDialog(Admin_Login.this);
                progressDialog.setMessage("Logging In...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                fb_db = new Firebase(BaseUrl);
                fb_db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Admin_Adapter obj = postSnapshot.getValue(Admin_Adapter.class);
                            {
                                if (obj.getS1().equals(s1) && obj.getS3().equals(s2)) {
                                    flag = true;
                                    Intent j = new Intent(Admin_Login.this, AfterLogin.class);

                                    Bundle extras = new Bundle();
                                    extras.putString("email", obj.getS1());
                                    extras.putString("name", obj.getS2());
                                    extras.putString("caller","Admin_Login");
                                    j.putExtras(extras);

                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    startActivity(j);
                                    finish();
                                }
                            }
                        }
                        if (flag == false) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Invalid Credentialz", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });
    }
}
