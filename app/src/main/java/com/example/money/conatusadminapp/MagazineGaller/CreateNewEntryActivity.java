package com.example.money.conatusadminapp.MagazineGaller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.money.conatusadminapp.NewMemberEntry;
import com.example.money.conatusadminapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class CreateNewEntryActivity extends AppCompatActivity {
    private static final String DATABASE_NAME="db";
    private ImageButton mUploadImage;
    private EditText mTitle;
    private Button submitButton;
    private static final int REQUEST_CODE = 112;
    private Uri mMemberImageUri;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_entry);
        Intent intent = getIntent();
        String database = intent.getStringExtra(DATABASE_NAME);
        mUploadImage = (ImageButton) findViewById(R.id.create_new_entry_image);
        mUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE);
            }
        });
        mTitle = (EditText) findViewById(R.id.create_new_entry_title);
        submitButton = (Button) findViewById(R.id.create_new_entry_submit_button);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        mStorage = FirebaseStorage.getInstance().getReference().child(database+"_cover");
        if (database.equals("gallery"))
        {
            mTitle.setVisibility(View.GONE);
        }
        mDatabase= FirebaseDatabase.getInstance().getReference().child(database);
        mProgress = new ProgressDialog(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUploading();
            }
        });
    }

    private void startUploading() {

        final String title = mTitle.getText().toString().trim();
        if ( mMemberImageUri != null){
            mProgress.setMessage("Uploading........");
            mProgress.show();
            String uuid = UUID.randomUUID().toString();
            final StorageReference filePath = mStorage.child(uuid);
            filePath.putFile(mMemberImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    DatabaseReference newMember = mDatabase.push();
                    if (title!=null)
                    newMember.child("title").setValue(title);
                    newMember.child("image").setValue(filePath.getPath());

                    mProgress.dismiss();
                    Toast.makeText(CreateNewEntryActivity.this, "Uploaded Sucessfully.", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgress.dismiss();
                    Toast.makeText(CreateNewEntryActivity.this, "Uploading Failed......", Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            Toast.makeText(this, "Please fill all the details......", Toast.LENGTH_SHORT).show();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            mMemberImageUri = data.getData();
            mUploadImage.setImageURI(mMemberImageUri);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public static Intent getCreateEntryActivityIntent(Context context, String databaseName)
    {
        Intent intent  =  new Intent(context,CreateNewEntryActivity.class);
        intent.putExtra(DATABASE_NAME,databaseName);
        return intent;
    }
}
