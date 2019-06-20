package com.example.sunita.myfirebasedbapp;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sunita.myfirebasedbapp.Adapter.Artist_list;
import com.example.sunita.myfirebasedbapp.Model.Artists;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //define the constants of artists
    public static  final String ARTIST_NAME="com.example.sunita.myfirebasedbapp.artistname";
    public static  final String ARTIST_ID="com.example.sunita.myfirebasedbapp.artistid";

    //UI elements
    EditText etArtistName;
    Spinner spinnerGenre;
    ListView lvArtists;
    Button btnAddArtist;

    List<Artists> artistlist;

    //DB reference
    DatabaseReference dbArtists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbArtists= FirebaseDatabase.getInstance().getReference("Artists");

        artistlist=new ArrayList<>();

        etArtistName=findViewById(R.id.ed1);
        spinnerGenre=findViewById(R.id.spinnergenre);
        lvArtists=findViewById(R.id.lv1);
        btnAddArtist=findViewById(R.id.add);

        btnAddArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addArtists();
            }
        });

        lvArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //getting the selected artist
                Artists artist= artistlist.get(i);
                Intent intent=new Intent(getApplicationContext(),ArtistActivity.class);

                intent.putExtra(ARTIST_ID,artist.getArtistid());
                intent.putExtra(ARTIST_NAME,artist.getArtistname());

                startActivity(intent);
            }
        });

        lvArtists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

               Artists artist=artistlist.get(i);
               showUpdateDeleteDialog(artist.getArtistid(),artist.getArtistname());

                return true;
            }
        });
    }

    private void showUpdateDeleteDialog(final String artistid,final String artistname) {

        AlertDialog.Builder dialogbuilder= new AlertDialog.Builder(this);

        LayoutInflater inflater=getLayoutInflater();
        final View dialogview=inflater.inflate(R.layout.update_layout,null);
        dialogbuilder.setView(dialogview);

        final EditText etArtistName=dialogview.findViewById(R.id.edittextName);
       final Spinner spnrgenre=dialogview.findViewById(R.id.spinnergenre);

        Button btnupdate=dialogview.findViewById(R.id.btn1);
        Button btndelete=dialogview.findViewById(R.id.btn2);

        dialogbuilder.setTitle(artistname);

        final AlertDialog alert=dialogbuilder.create();
        alert.show();

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name=etArtistName.getText().toString().trim();
                String genre= spnrgenre.getSelectedItem().toString();

                if(!TextUtils.isEmpty(name))

                {
                    updateArtist(artistid,name,genre);
                    alert.dismiss();
                }
            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteArtist(artistid);
                alert.dismiss();
            }
        });
    }

    private boolean deleteArtist(String artistid) {

        //getting the specified artist reference
        DatabaseReference dbArtref=FirebaseDatabase.getInstance().getReference("Artists").child(artistid);

        dbArtref.removeValue();

        DatabaseReference dbTrackref=FirebaseDatabase.getInstance().getReference("Tracks");
        dbTrackref.removeValue();

        Toast.makeText(this, "Artist deleted", Toast.LENGTH_SHORT).show();
        return true;

    }

    private boolean updateArtist(String artistid, String name, String genre) {

        //getting the specified artist reference
        DatabaseReference dbref=FirebaseDatabase.getInstance().getReference("Artists").child(artistid);

        Artists artists=new Artists(artistid,name,genre);
        dbref.setValue(artists);

        Toast.makeText(this, "Artist:'+name+' has been Updated", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        //add value event listener
        dbArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                artistlist.clear();

                for (DataSnapshot artistSnapshot:dataSnapshot.getChildren()){

                    Artists artist=artistSnapshot.getValue(Artists.class);
                    artistlist.add(artist);
                }

                Artist_list artistAdapter=new Artist_list(MainActivity.this,artistlist);
                lvArtists.setAdapter(artistAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addArtists() {

        String artistName=etArtistName.getText().toString().trim();
        String artistGenre=spinnerGenre.getSelectedItem().toString();

        if(!TextUtils.isEmpty(artistName))
        {
            //get the id of the artist
            String id=dbArtists.push().getKey();

            //add the artists
            Artists artist=new Artists(id,artistName,artistGenre);

            //send value to database
            dbArtists.child(id).setValue(artist);
            Toast.makeText(this,"Artist Added",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this,"Plz Enter Artist Name",Toast.LENGTH_LONG).show();
        }

        etArtistName.setText(null);
    }
}
