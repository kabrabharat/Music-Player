package com.world.bharatkabra.unimusicplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    //creating array list for song info
    private ArrayList<SongInfo> songs = new ArrayList<SongInfo>();
    SeekBar seekbar;
    RecyclerView recyclerview;
    SongAdapter songAdapter;

    String x="play";       //to check and switch bw stop and play

    MediaPlayer mediaPlayer;

    //Handler for asynchronous task so that process do not stuck

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerview = (RecyclerView)findViewById(R.id.recyclerview);
        seekbar = (SeekBar)findViewById(R.id.seekbar);
        //
        /*
        SongInfo s =new SongInfo("Thunder","AndroidExamples","https://www.android-examples.com/wp-content/uploads/2016/04/Thunder-rumble.mp3");
        songs.add(s);
        */
        //
        songAdapter = new SongAdapter(this,songs);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerview.getContext(),linearLayoutManager.getOrientation());

        recyclerview.addItemDecoration(dividerItemDecoration);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setAdapter(songAdapter);

        songAdapter.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final Button btn, View view, final SongInfo info, int position) {

                //After Handler
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        try {

                            if (x.equals("stop")) {
                                btn.setBackgroundResource(R.drawable.playpng);
                                x="play";
                                mediaPlayer.stop();
                                mediaPlayer.reset();
                                mediaPlayer.release();
                                mediaPlayer = null;

                            } else {
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.setDataSource(info.getSongUrl());
                                mediaPlayer.prepareAsync();
                                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @SuppressLint("NewApi")
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        mp.start();

                                        seekbar.setProgress(0);
                                        seekbar.setMax(mp.getDuration());


                                        btn.setBackgroundResource(R.drawable.stopmusic);
                                        x = "stop";
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                };

                handler.postDelayed(r,1000);

               /*
                try {

                    if(btn.getBackground().equals(R.drawable.pausemusic)){
                        mediaPlayer.pause();

                    }else {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(info.getSongUrl());
                        mediaPlayer.prepareAsync();
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @SuppressLint("NewApi")
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();

                                seekbar.setProgress(0);
                                seekbar.setMax(mp.getDuration());

                                btn.setBackgroundResource(R.drawable.pausemusic);
                                }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        });

        CheckPermission();

        Thread thread = new SeekThread();
        thread.start();


    }

    public class SeekThread extends Thread{
        @Override
        public void run() {
            try {
                //Thread.sleep(1000);
                if(mediaPlayer!=null)
                    seekbar.setProgress(mediaPlayer.getCurrentPosition()/1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void CheckPermission(){
        if(Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                return;
            }
            else{
                loadSongs();
            }
        }
        else{
            loadSongs();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 123:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    loadSongs();
                }
                else{
                    Toast.makeText(this,"Permission Denied!!",Toast.LENGTH_SHORT).show();
                    CheckPermission();
                }break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void loadSongs(){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC+"!=0";
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);

        if(cursor!=null){
            if(cursor.moveToFirst()){

                do {
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String artistname = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                    SongInfo s = new SongInfo(name,artistname,url);
                    songs.add(s);
                }while(cursor.moveToNext());
                cursor.close();
                songAdapter = new SongAdapter(this,songs);
            }
        }
    }

}
