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
import android.graphics.drawable.RippleDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class NewMemberEntry extends AppCompatActivity {
    private ImageButton mMemberImageButton;
    private EditText mName;
    private EditText mBranch;
    private EditText mYear;
    private EditText mDomain;
    private Button mSubmit;
    private static final int REQUEST_CODE = 112;
    private Uri mMemberImageUri;
    private StorageReference mStorage;
    private ProgressDialog mProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_member_entry);
        mMemberImageButton = (ImageButton) findViewById(R.id.uploadMemberImageButton);
        mName = (EditText) findViewById(R.id.memberNameField);
        mYear = (EditText) findViewById(R.id.memberYear);
        mBranch = (EditText) findViewById(R.id.memberBranch);
        mDomain = (EditText) findViewById(R.id.domain);
        mSubmit = (Button) findViewById(R.id.submit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startUploading();
            }
        });

        mMemberImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE);

            }
        });

    }

    private void startUploading() {
        String name = mName.getText().toString().trim();
        String branch = mBranch.getText().toString().trim();
        String year = mYear.getText().toString().trim();
        String domain = mDomain.getText().toString().trim();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(branch) && (!TextUtils.isEmpty(year) && !TextUtils.isEmpty(domain) &&mMemberImageUri != null)) {
            mProgress.setMessage("Uploading........");
            mProgress.show();
            String uuid = UUID.randomUUID().toString();
            StorageReference filePath = mStorage.child("member_images").child(uuid);
            filePath.putFile(mMemberImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    mProgress.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgress.dismiss();
                    Toast.makeText(NewMemberEntry.this, "Uploading Failed......", Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            Toast.makeText(this, "Please fill all the details......", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            mMemberImageUri = data.getData();
            mMemberImageButton.setImageURI(mMemberImageUri);
        }
    }





}
