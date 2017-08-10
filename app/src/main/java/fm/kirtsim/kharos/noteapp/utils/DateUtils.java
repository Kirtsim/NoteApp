package fm.kirtsim.kharos.noteapp.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kharos on 09/08/2017
 */

public class DateUtils {

    private static DateFormat dateFormatter;

    public static String getDateStringFromTimestamp(long timestamp) {
        if (dateFormatter == null)
            initializeDateFormatter();
        return dateFormatter.format(new Date(timestamp));
    }

    private static void initializeDateFormatter() {
        dateFormatter = SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
    }
}
