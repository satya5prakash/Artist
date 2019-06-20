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

import com.example.sunita.myfirebasedbapp.Model.Tracks;
import com.example.sunita.myfirebasedbapp.R;

import java.util.List;

public class Track_list extends ArrayAdapter<Tracks> {

    private Activity context;
    List<Tracks> tracks;

    public Track_list(Activity context, List<Tracks> tracks) {
        super(context, R.layout.list__layout,tracks);
        this.context = context;
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();

        View ListItemView=inflater.inflate(R.layout.list__layout,parent,false);

        TextView tvnName=ListItemView.findViewById(R.id.textViewName);
        TextView tvRAting=ListItemView.findViewById(R.id.textGenre);

        Tracks track=tracks.get(position);
        tvnName.setText(track.getTrackName());
        tvRAting.setText(String.valueOf(track.getTrackRating()));


        return ListItemView;
    }
}
