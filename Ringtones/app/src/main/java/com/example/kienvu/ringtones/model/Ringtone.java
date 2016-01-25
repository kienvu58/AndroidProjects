package com.example.kienvu.ringtones.model;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.example.kienvu.ringtones.R;

import org.apache.commons.io.FilenameUtils;

import java.io.IOException;

/**
 * Created by Kien Vu on 24/01/2016.
 */
public class Ringtone {
    private String audioFilename;
    private String title;

    public Ringtone(String audioFilename) {
        this.audioFilename = audioFilename;
        this.title = FilenameUtils.removeExtension(audioFilename);
    }

    public String getAudioFilename() {
        return this.audioFilename;
    }

    public void setAudioFilename(String audioFilename) {
        this.audioFilename = audioFilename;
        this.title = FilenameUtils.removeExtension(audioFilename);
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAsPhoneRingtone(Context context) {
        Toast.makeText(context, R.string.set_as_phone_ringtone, Toast.LENGTH_SHORT).show();
    }

    public void setAsAlarmRingtone(Context context) {
        Toast.makeText(context, R.string.set_as_alarm_ringtone, Toast.LENGTH_SHORT).show();
    }

    public void play(Context context, MediaPlayer player) {
        AssetFileDescriptor afd = null;
        try {
            afd = context.getAssets().openFd("ringtones/" + this.audioFilename);
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
