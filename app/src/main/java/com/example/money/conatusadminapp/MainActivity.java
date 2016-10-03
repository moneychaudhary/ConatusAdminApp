package com.example.money.conatusadminapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import static java.util.Calendar.MONDAY;

public class MainActivity extends AppCompatActivity {
    private ImageButton mImageButton;
    private EditText mPostTitle;
    private EditText mPostDescription;
    private Spinner mPostType;
    private EditText mSubHeading;
    private Button mSubmitButton;
    private static final int REQUEST = 111;
    private Uri mImageUri;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageButton = (ImageButton) findViewById(R.id.uploadPostImage);
        mPostTitle = (EditText) findViewById(R.id.title);
        mSubHeading= (EditText) findViewById(R.id.sub_heading);
        mPostType= (Spinner) findViewById(R.id.post_type);
        mPostDescription = (EditText) findViewById(R.id.content);
        mSubmitButton = (Button) findViewById(R.id.submitbutton);
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("posts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Post");





        mProgress = new ProgressDialog(this);
        mPostType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0)
                {
                    mPostDescription.setText("null");
                    mPostDescription.setVisibility(View.GONE);
                }
                else
                {
                    mPostDescription.setText("");
                    mPostDescription.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUploading();
            }
        });

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST);

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void startUploading() {
        Calendar cal = Calendar.getInstance();
        int dd = cal.get(Calendar.DATE);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int minute = cal.get(Calendar.MINUTE);
        int hour = cal.get(Calendar.HOUR);
        String ampm = DateUtils.getAMPMString(cal.get(Calendar.AM_PM));


        final String titleValue = mPostTitle.getText().toString().trim();
        final String description = mPostDescription.getText().toString().trim();
        final String subHeading = mSubHeading.getText().toString().trim();
        final String time=hour+":"+minute+""+ampm;;
        final String date=dd+"/"+month+"/"+year;
        if (!TextUtils.isEmpty(titleValue) && !TextUtils.isEmpty(description) && mImageUri != null) {
            mProgress.setMessage("Uploading........");
            mProgress.show();
            String uuid = UUID.randomUUID().toString();
            final StorageReference filePath = mStorage.child("post_Images").child(uuid);
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = mDatabase.push();
                    newPost.child("title").setValue(titleValue);
                    newPost.child("subhead").setValue(subHeading);
                    newPost.child("time").setValue(time);
                    newPost.child("date").setValue(date);
                    newPost.child("desc").setValue(description);
                    newPost.child("image").setValue(filePath.getPath() );
                    mProgress.dismiss();
                    Toast.makeText(MainActivity.this, "Uploaded Sucessfully.", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgress.dismiss();
                    Toast.makeText(MainActivity.this, "Uploading Failed......", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(this, "Please fill all the details......", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            mImageButton.setImageURI(mImageUri);
        }
    }





}
