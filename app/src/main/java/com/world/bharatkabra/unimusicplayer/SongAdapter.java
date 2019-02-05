package com.world.bharatkabra.unimusicplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {

    ArrayList<SongInfo> songs;
    Context context;

    OnItemClickListener onItemClickListener;

    public SongAdapter(Context context, ArrayList<SongInfo> songs) {
        this.songs = songs;
        this.context = context;
    }

    public interface OnItemClickListener{

        void onItemClick(Button btn, View view,SongInfo info,int position);

        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener){
            this.onItemClickListener = onItemClickListener;
        }



    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View listView = LayoutInflater.from(context).inflate(R.layout.songs_list,parent,false);
        return new SongHolder(listView);

    }

    @Override
    public void onBindViewHolder(@NonNull final SongHolder holder, final int position) {

        final SongInfo sinfo = songs.get(position);
        holder.songName.setText(sinfo.songName);
        holder.artist.setText(sinfo.artist);
        holder.playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(holder.playpause,v,sinfo,position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder {

        TextView songName,artist;
        Button playpause;

        public SongHolder(View itemView) {
            super(itemView);

            songName = (TextView)itemView.findViewById(R.id.tv_songname);
            artist = (TextView)itemView.findViewById(R.id.tv_artist);
            playpause = (Button)itemView.findViewById(R.id.btn_playpause);
        }
    }
}
