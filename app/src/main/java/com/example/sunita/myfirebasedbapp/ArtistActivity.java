package com.example.sunita.myfirebasedbapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunita.myfirebasedbapp.Adapter.Track_list;
import com.example.sunita.myfirebasedbapp.Model.Tracks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ArtistActivity extends AppCompatActivity {

    EditText track;
    SeekBar seek;
    TextView tvRating,artist;
    Button addTrack;
    ListView list;

    DatabaseReference databaseReference;

    List<Tracks> tracks;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        intent=getIntent();
        artist=findViewById(R.id.tvartist);
        track=findViewById(R.id.edTrackname);
        seek=findViewById(R.id.seedbar);
        tvRating=findViewById(R.id.tvrating);
        addTrack=findViewById(R.id.btadd);
        list=findViewById(R.id.lv1);

        databaseReference= FirebaseDatabase.getInstance().getReference("Tracks").child(intent.getStringExtra(MainActivity.ARTIST_ID));

        tracks=new ArrayList<>();

        artist.setText(intent.getStringExtra(MainActivity.ARTIST_NAME));

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                tvRating.setText(String.valueOf(i));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        addTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveTracks();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clear the list
                tracks.clear();

                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Tracks track=postSnapshot.getValue(Tracks.class);
                    tracks.add(track);
                }
                    //attach the values to list
                Track_list adapter=new Track_list(ArtistActivity.this,tracks);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(ArtistActivity.this,"database error"+databaseError,Toast.LENGTH_LONG).show();
            }
        });
    }

    public void saveTracks()
    {
        String trackName=track.getText().toString().trim();
        int rating=seek.getProgress();

        if(!TextUtils.isEmpty(trackName))
        {
            String id=databaseReference.push().getKey();

            Tracks tracks=new Tracks(id,trackName,rating);

            databaseReference.child(id).setValue(tracks);
            Toast.makeText(getApplicationContext(),"Tracks Added",Toast.LENGTH_LONG).show();

            track.setText(null);
            seek.setProgress(0);


        }

        else
        {
            Toast.makeText(getApplicationContext(),"Plz Enter Track Name",Toast.LENGTH_LONG).show();
        }

    }

}
