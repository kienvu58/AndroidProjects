package com.example.kienvu.ringtones.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kienvu.ringtones.R;
import com.example.kienvu.ringtones.model.Ringtone;

import java.util.List;

/**
 * Created by Kien Vu on 24/01/2016.
 */
public class RingtonesAdapter extends BaseAdapter {
    private List<Ringtone> ringtones;
    private Context context;
    private MediaPlayer mediaPlayer;
    private int lastPlayed;
    private ViewHolder lastHolder;

    public RingtonesAdapter(List<Ringtone> ringtones, Context context) {
        this.ringtones = ringtones;
        this.context = context;
        this.mediaPlayer = new MediaPlayer();
    }

    @Override
    public int getCount() {
        return ringtones.size();
    }

    @Override
    public Object getItem(int position) {
        return ringtones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ringtone_list_row, parent, false);
            holder = new ViewHolder();
            holder.playButton = (ImageButton) convertView.findViewById(R.id.play_button);
            holder.ringtoneTitle = (TextView) convertView.findViewById(R.id.ringtone_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ringtoneTitle.setText(ringtones.get(position).getTitle());
        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.reset();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        holder.playButton.setImageResource(R.drawable.imagesbuttonplay);
                    }
                });
                holder.playButton.setImageResource(R.drawable.imagesbuttonpause);
                ringtones.get(position).play(context, mediaPlayer);
                lastPlayed = position;
            }
        });
        return convertView;
    }

    private class ViewHolder {
        ImageButton playButton;
        TextView ringtoneTitle;
    }
}
