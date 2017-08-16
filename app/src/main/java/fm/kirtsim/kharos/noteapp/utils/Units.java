package fm.kirtsim.kharos.noteapp.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by kharos on 15/08/2017
 */

public class Units {

    public static int px2dp(int px, DisplayMetrics metrics) {
        final float dp = px / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return Math.round(dp);
    }

    public static int dp2px(int dp, DisplayMetrics metrics) {
        final float px = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return Math.round(px);
    }

}
