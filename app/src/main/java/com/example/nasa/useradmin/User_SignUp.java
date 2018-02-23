package com.example.nasa.useradmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class User_SignUp extends AppCompatActivity {
    EditText e1,e2,e3,e4;
    FloatingActionButton submit;
    String s1,s2,s3,s4;
    String BaseUrl="https://broadcaster-68002.firebaseio.com/";
    Firebase fb_db;
    ProgressDialog progressDialog;
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__sign_up);
        Firebase.setAndroidContext(this);

        e1=(EditText)findViewById(R.id.em);
        e2=(EditText)findViewById(R.id.name);
        e3=(EditText)findViewById(R.id.pass);
        e4=(EditText)findViewById(R.id.cpass);

        submit =(FloatingActionButton) findViewById(R.id.done);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s1=e1.getText().toString();
                s2=e2.getText().toString();
                s3=e3.getText().toString();
                s4=e4.getText().toString();

                if((s1.equals(""))||(s2.equals(""))||(s3.equals(""))||(s4.equals("")))
                {
                    Toast.makeText(User_SignUp.this, "No Feild must be empty", Toast.LENGTH_SHORT).show();
                }
                else if((s3.equals(s4)))
                {
                    System.out.println("Bow "+s1+s2+s3+s4);
                    new MyTask().execute();
                }
                else
                {
                    Toast.makeText(User_SignUp.this, "Password does not match", Toast.LENGTH_SHORT).show();
                }            }
        });
    }
    public class MyTask extends AsyncTask<String,Integer,String> {

        @Override
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(User_SignUp.this);
            progressDialog.setMessage("Signing Up...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings)
        {
            fb_db = new Firebase(BaseUrl);
            System.out.println("bow Came in task");
            fb_db.child("User").addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("bow Came in checking condition"+dataSnapshot.getChildrenCount());
                    if(dataSnapshot.hasChildren()){
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            User_Adapter obj = postSnapshot.getValue(User_Adapter.class);
                            String Name = obj.getS1();
                            if (Name.equals(s1)) {
                                flag = false;
                                progressDialog.dismiss();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Email Already exists.\n Try with new Email id", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        if (flag)
                        {
                            User_Adapter obj = new User_Adapter();
                            obj.setS1(s1);
                            obj.setS2(s2);
                            obj.setS3(s3);
                            obj.setS4(s4);

                            String[] splited = s1.split("@");


                            fb_db = new Firebase(BaseUrl);
                            fb_db.child("User").child(splited[0] + "_Data").setValue(obj);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(User_SignUp.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                }
                            });

                            Intent i = new Intent(User_SignUp.this, User_Activity.class);
                            progressDialog.dismiss();
                            startActivity(i);
                            finish();


                        }
                    }
                    else{
                        User_Adapter obj = new User_Adapter();
                        obj.setS1(s1);
                        obj.setS2(s2);
                        obj.setS3(s3);
                        obj.setS4(s4);

                        String[] splited = s1.split("@");
                        fb_db = new Firebase(BaseUrl);
                        fb_db.child("User").child(splited[0]+ "_Data").setValue(obj);
                        Intent i = new Intent(User_SignUp.this, User_Activity.class);
                        progressDialog.dismiss();
                        startActivity(i);
                        finish();
                    }

                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            return null;
        }
    }
}
