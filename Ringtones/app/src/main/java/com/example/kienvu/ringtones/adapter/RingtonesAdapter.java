package com.example.kienvu.ringtones.adapter;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.telephony.TelephonyManager;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
    private SparseBooleanArray playingRingtoneIds;

    public RingtonesAdapter(List<Ringtone> ringtones, MediaPlayer mediaPlayer, Context context) {
        this.ringtones = ringtones;
        this.context = context;
        this.mediaPlayer = mediaPlayer;
        playingRingtoneIds = new SparseBooleanArray();
    }

    public void togglePlaying(int position) {
        selectView(position, !playingRingtoneIds.get(position));
    }

    /**
     * Set other ringtones not playing and set this playing status == value
     * @param position position of ringtone in list
     * @param value value to set
     */
    public void selectView(int position, boolean value) {
        playingRingtoneIds.clear();
        if (value) {
            playingRingtoneIds.put(position, true);
        }
        notifyDataSetChanged();
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ringtone_list_row, parent, false);
            holder = new ViewHolder();
            holder.playButton = convertView.findViewById(R.id.play_button);
            holder.ringtoneTitle = (TextView) convertView.findViewById(R.id.ringtone_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Change playButton image depends on playing status
        holder.playButton.setBackgroundResource(playingRingtoneIds.get(position) ?
                R.drawable.imagesbuttonpause : R.drawable.imagesbuttonplay);

        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.reset();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        togglePlaying(position);
                    }
                });

                // if ringtone is not playing then play it
                if (!playingRingtoneIds.get(position)) {
                    ringtones.get(position).play(context, mediaPlayer);
                }
                togglePlaying(position);
            }
        });

        // Handle when phone has incoming call
        IntentFilter filter = new IntentFilter("android.intent.action.PHONE_STATE");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);

                switch (telephonyManager.getCallState()) {
                    case TelephonyManager.CALL_STATE_RINGING :
                        mediaPlayer.reset();
                        playingRingtoneIds.clear();
                        break;
                    default:
                        mediaPlayer.reset();
                        playingRingtoneIds.clear();
                }
            }
        };
        context.registerReceiver(receiver, filter);

        // ringtoneTitle
        holder.ringtoneTitle.setText(ringtones.get(position).getTitle());

        return convertView;
    }

    private class ViewHolder {
        View playButton;
        TextView ringtoneTitle;
    }
}
