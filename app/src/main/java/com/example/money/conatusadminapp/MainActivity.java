package com.example.money.conatusadminapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private ImageButton mImageButton;
    private EditText mPostTitle;
    private EditText mPostDescription;
    private Button mSubmitButton;
    private static final int REQUEST = 111;
    private Uri mImageUri;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.addmember)
        {
            Intent intent= new Intent(this,NewMemberEntry.class);
            startActivity(intent);
        }
        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageButton = (ImageButton) findViewById(R.id.uploadMemberImageButton);
        mPostTitle = (EditText) findViewById(R.id.memberNameField);
        mPostDescription = (EditText) findViewById(R.id.memberBranch);
        mSubmitButton = (Button) findViewById(R.id.submitbutton);
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("posts");
        mProgress = new ProgressDialog(this);
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

    private void startUploading() {
        final String titleValue = mPostTitle.getText().toString().trim();
        final String description = mPostDescription.getText().toString().trim();
        if (!TextUtils.isEmpty(titleValue) && !TextUtils.isEmpty(description) && mImageUri != null) {
            mProgress.setMessage("Uploading........");
            mProgress.show();
            String uuid = UUID.randomUUID().toString();
            StorageReference filePath = mStorage.child("post_Images").child(uuid);
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = mDatabase.push();
                    newPost.child("title").setValue(titleValue);
                    newPost.child("desc").setValue(description);
                    newPost.child("image").setValue(downloadUri.toString() );
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
