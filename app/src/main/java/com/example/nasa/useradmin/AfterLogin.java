package com.example.nasa.useradmin;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AfterLogin extends AppCompatActivity
{
    ImageView img;
    String uploadtext,username;
    FloatingActionButton upload,fab1,fab2;
    RecyclerView recyclerView;
   int a = 1;
    ProgressDialog progressDialog;
    String BaseUrl="https://broadcaster-68002.firebaseio.com/";
    Firebase fb_db;
    public ArrayList<Adapter> itemAdapter =new ArrayList<>();
    public ItemAdapter itemArrayAdapter;
    RecyclerView.LayoutManager layoutManager;
    Date currentTime ;
    Uri imageUri,selectedImageUri;

    private static final int PICK_Camera_IMAGE=2;
    private static final int PICK_IMAGE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        fab1=(FloatingActionButton)findViewById(R.id.up1);
        fab2=(FloatingActionButton)findViewById(R.id.up2);

        layoutManager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        upload=(FloatingActionButton)findViewById(R.id.up);

        Firebase.setAndroidContext(this);
        fb_db = new Firebase(BaseUrl );

        username = getIntent().getExtras().getString("name");






        new Card_Load_Task().execute();
        itemArrayAdapter = new ItemAdapter(R.layout.card,itemAdapter);
        recyclerView.setAdapter(itemArrayAdapter);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fab1.getVisibility()==View.VISIBLE) {
                    fab1.setVisibility(View.INVISIBLE);
                    fab2.setVisibility(View.INVISIBLE);
                }
                else {
                    fab1.setVisibility(View.VISIBLE);
                    fab2.setVisibility(View.VISIBLE);
                }
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AfterLogin.this, "Text Upload", Toast.LENGTH_SHORT).show();
                 a=1;
                 showChangeLangDialog();


            }
        });
      fab2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              a=2;
              Toast.makeText(AfterLogin.this, "Image With Text Upload...", Toast.LENGTH_SHORT).show();
              selectImage();




          }
      });

    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(AfterLogin.this);
        builder.setTitle("Choose picture..");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    String fileName = "new-photo-name.jpg";
                    //create parameters for Intent with filename
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fileName);
                    values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
                    //imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
                    imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    //create new Intent
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    startActivityForResult(intent, PICK_Camera_IMAGE);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            PICK_IMAGE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == PICK_Camera_IMAGE) {
                if (resultCode == RESULT_OK) {
                    //use imageUri here to access the image
                    selectedImageUri = imageUri;
                    showChangeLangDialog();


                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                }
                try {
                    Uri tmpUri=Uri.fromFile(new File(ImageCompressor.with(getApplicationContext()).compress(selectedImageUri.toString())));
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), tmpUri);



                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (requestCode == PICK_IMAGE) {
                if (resultCode == RESULT_OK) {
                    selectedImageUri = data.getData();
                    showChangeLangDialog();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(), "Picture was not selected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Picture was not selected", Toast.LENGTH_SHORT).show();
                }



            }}}
    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dailog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.activityname);

        dialogBuilder.setTitle("Text ");
        dialogBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
                currentTime= Calendar.getInstance().getTime();
                uploadtext = edt.getText().toString();
                if(a==1){
                    System.out.println("Bowwww 1 ");
                    new Text_Upload_Task().execute();
                }
                if(a == 2){
                    new Img_Upload_Task().execute();
                }


            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public class Text_Upload_Task extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            Adapter adapter = new Adapter();
            System.out.println("TExt is : "+uploadtext);
            adapter.setText(uploadtext);
            adapter.setUrl("");
            System.out.println("bowww "+adapter);
            fb_db.child("Cards").child(username).child(currentTime.toString()).setValue(adapter);

            Intent intent = new Intent(AfterLogin.this,AfterLogin.class);
            intent.putExtra("name",username);
            startActivity(intent);
            finish();




            return null;
        }
    }
    public class Img_Upload_Task extends  AsyncTask<String, Integer, String>{


        @Override
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(AfterLogin.this);
            progressDialog.setMessage("Creating event...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            StorageReference sr = FirebaseStorage.getInstance().getReference().child("RECYCLER").child(currentTime.toString());
            sr.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String url = taskSnapshot.getDownloadUrl().toString();
                    ImgAdapter imgAdapter = new ImgAdapter();
                    imgAdapter.setUrl(url);
                    imgAdapter.setText(uploadtext);
                    fb_db.child("Cards").child(username).child(currentTime.toString()).setValue(imgAdapter);
                    progressDialog.dismiss();
                    Intent intent = new Intent(AfterLogin.this,AfterLogin.class);
                    intent.putExtra("name",username);
                    startActivity(intent);
                    finish();

                }
            });
            return null;
        }
    }

    public class Card_Load_Task extends  AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            fb_db.child("Cards").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot child: dataSnapshot.getChildren()){
                        System.out.println("bow "+child.getKey());
                        ImgAdapter imgAdapter = child.getValue(ImgAdapter.class);
                        itemAdapter.add(0,new Adapter(imgAdapter.getText(),imgAdapter.getUrl()));
                    }
                    itemArrayAdapter =  new ItemAdapter(R.layout.card,itemAdapter);
                    recyclerView.setAdapter(itemArrayAdapter);
                    System.out.println("ItemAdapters are : "+itemAdapter);

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            return null;
        }
    }


}
