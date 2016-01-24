package com.example.kienvu.ringtones.helper;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Kien Vu on 24/01/2016.
 */
public class MetricHelper {
    /**
     * Converts length from dp to pixel
     *
     * @param context Application context
     * @param dp      input dp
     * @return number of pixels
     */
    public static int convertDpToPixel(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
