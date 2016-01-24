package com.example.kienvu.ringtones.helper;

import android.content.Context;

import com.example.kienvu.ringtones.model.Ringtone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kien Vu on 24/01/2016.
 */
public class RingtoneHelper {
    public static List<Ringtone> getAllRingtones(Context context) {
        List<Ringtone> ringtones = new ArrayList<>();
        try {
            String[] filenames = context.getAssets().list("ringtones");
            for (String filename : filenames) {
                Ringtone ringtone = new Ringtone(filename);
                ringtones.add(ringtone);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ringtones;
    }
}
