package com.example.sunita.myfirebasedbapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sunita.myfirebasedbapp.Model.Artists;
import com.example.sunita.myfirebasedbapp.Model.Tracks;
import com.example.sunita.myfirebasedbapp.R;

import java.util.List;

public class Artist_list extends ArrayAdapter<Artists> {


        private Activity context;
        List<Artists> artists;

    public Artist_list(Activity context, List<Artists> artists) {
            super(context, R.layout.list__layout,artists);
            this.context = context;
            this.artists = artists;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater=context.getLayoutInflater();

            View ListItemView=inflater.inflate(R.layout.list__layout,parent,false);

            TextView tvnName=ListItemView.findViewById(R.id.textViewName);
            TextView tvRAting=ListItemView.findViewById(R.id.textGenre);

            Artists artist=artists.get(position);
            tvnName.setText(artist.getArtistname());
            tvRAting.setText(artist.getArtistgenre());


            return ListItemView;
        }
    }

