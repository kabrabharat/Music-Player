package com.world.bharatkabra.unimusicplayer;

public class SongInfo {

    public String songName,artist,songUrl;

    public SongInfo(){

    }
    public SongInfo(String songName, String artist, String songUrl){

        this.songName = songName;
        this.artist = artist;
        this.songUrl = songUrl;
    }

    public String getSongName() {
        return songName;
    }

    public String getArtist() {
        return artist;
    }

    public String getSongUrl() {
        return songUrl;
    }
}
