package com.example.money.conatusadminapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartingActivity extends AppCompatActivity {
    private Button addPostButton;
    private Button addMemberButton;
    private Button addMentorButton;
    private Button galleryButton;
    private Button magazineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        addPostButton = (Button) findViewById(R.id.add_post);
        addMemberButton = (Button) findViewById(R.id.add_member);
        addMentorButton = (Button) findViewById(R.id.add_mentor);
        galleryButton = (Button) findViewById(R.id.gallery_entry);
        magazineButton = (Button) findViewById(R.id.magazine_entry);

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(StartingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(StartingActivity.this,NewMemberEntry.class);
                startActivity(intent);

            }
        });
        addMentorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        magazineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
