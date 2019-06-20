package com.example.sunita.myfirebasedbapp.Model;

public class Artists {

    private  String artistid;
    private String artistname;
    private String artistgenre;

    public Artists() {
    }

    public Artists(String artistid, String artistname, String artistgenre) {
        this.artistid = artistid;
        this.artistname = artistname;
        this.artistgenre = artistgenre;
    }

    public String getArtistid() {
        return artistid;
    }

    public String getArtistname() {
        return artistname;
    }

    public String getArtistgenre() {
        return artistgenre;
    }
}
