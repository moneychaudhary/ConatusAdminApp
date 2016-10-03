package com.example.money.conatusadminapp.MagazineGaller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.money.conatusadminapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ListofMagazineAndGalleyActivity extends AppCompatActivity {
    private final static String DATABASE_NAME = "database";
    private final static String ACTIVITY_TITLE = "title";

    private String mDatabaseName;
    private DatabaseReference mDatabase;
    private RecyclerView mRecyclerView;
    private static Context sContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_magazine_and_galley);
        Intent intent = getIntent();
        mDatabaseName = intent.getStringExtra(DATABASE_NAME);
        String title = intent.getStringExtra(ACTIVITY_TITLE);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(mDatabaseName);
        mRecyclerView = (RecyclerView) findViewById(R.id.magazine_galley_recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<ListOfView, ListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ListOfView, ListViewHolder>(
                ListOfView.class,
                R.layout.magazine_gallery_recycler_view_layout,
                ListViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(ListViewHolder viewHolder, ListOfView model, int position) {
                viewHolder.titleView.setText(model.getTitle());
                Picasso.with(sContext).load(model.getImage()).resize(150, 150).into(viewHolder.coverImage);

            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static Intent getMyIntent(Context context, String databaseName,String title) {
        sContext = context;
        Intent intent = new Intent(context, ListofMagazineAndGalleyActivity.class);
        intent.putExtra(DATABASE_NAME, databaseName);
        intent.putExtra(ACTIVITY_TITLE, title);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            super.onBackPressed();
        else if(item.getItemId()==R.id.addmember)
        {
            Intent intent =CreateNewEntryActivity.getCreateEntryActivityIntent(getApplicationContext(),mDatabaseName);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        public ImageButton coverImage;
        public TextView titleView;

        public ListViewHolder(View itemView) {
            super(itemView);
            coverImage = (ImageButton) itemView.findViewById(R.id.image_view);
            titleView = (TextView) itemView.findViewById(R.id.title_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
