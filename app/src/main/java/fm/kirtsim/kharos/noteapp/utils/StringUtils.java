package fm.kirtsim.kharos.noteapp.utils;

/**
 * Created by kharos on 08/08/2017
 */

public class StringUtils {

    public static String extractFirstWordsUpToLength(String text, int length) {
        return "";
    }

    public static int getFirstWordIndex(String text) {
        if (text != null) {
            final int length = text.length();
            char c;
            for (int i = 0; i < length; ++i) {
                c = text.charAt(i);
                if (c != '\n' && c != ' ')
                    return i;
            }
        }
        return -1;
    }
}
