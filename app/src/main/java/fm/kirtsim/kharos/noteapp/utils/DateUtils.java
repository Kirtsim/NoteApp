package fm.kirtsim.kharos.noteapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kharos on 09/08/2017
 */

public class DateUtils {

    private static SimpleDateFormat dateFormatter;
    private static SimpleDateFormat timeFormatter;
    private static SimpleDateFormat dateAndTimeFormatter;

    public static String getDateStringFromTimestamp(long timestamp) {
        if (dateFormatter == null)
            initializeDateFormatter();
        return dateFormatter.format(new Date(timestamp));
    }

    @SuppressWarnings("unused")
    public static String getTimeStringFromTimestamp(long timestamp) {
        if (timeFormatter == null)
            initializeTimeFormatter();
        return timeFormatter.format(new Date(timestamp));
    }

    @SuppressWarnings("unused")
    public static String getDateAndTimeStringFromTimeStamp(long timestamp) {
        if (dateAndTimeFormatter == null)
            initializeDateAndTimeFormatter();
        return dateAndTimeFormatter.format(new Date(timestamp));
    }

    private static void initializeTimeFormatter() {
        timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

    private static void initializeDateFormatter() {
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }

    private static void initializeDateAndTimeFormatter() {
        dateAndTimeFormatter = new SimpleDateFormat("EEE dd/MM/yyyy  -  HH:mm", Locale.getDefault());
    }
}
