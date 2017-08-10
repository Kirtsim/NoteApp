package fm.kirtsim.kharos.noteapp.utils;

/**
 * Created by kharos on 08/08/2017
 */

public class StringUtils {


    /**
     * Extracts first number of words that together the total length of which does not exceed
     * the length specified as a parameter to this method. The method filters all line feeds
     * and blank spaces that might occur before the first word. If only one word can be extracted
     * but its length is too big, the word is cut to fit the desired length. In case of multiple
     * words, only words on the first line or those that entirely fit into the specified length are
     * are extracted.
     * @param text text from which the words are going to be extracted
     * @param length the maximum length of the return string (extracted words)
     * @return string of words that have been extracted from the passed text, or an empty string if
     * no words could be extracted.
     */
    public static String extractFirstWordsUpToLength(String text, int length) {
        int startIndex = getFirstWordIndex(text);
        if (startIndex == -1)
            return "";

        final int textLength = text.length();
        int endIndexExc = startIndex + length;
        if (endIndexExc > textLength)
            endIndexExc = textLength;

        int endIndexCandidate = text.lastIndexOf('\n', endIndexExc-1);
        while (endIndexCandidate > startIndex) {
            endIndexExc = endIndexCandidate;
            endIndexCandidate = text.lastIndexOf('\n', endIndexExc-1);
        }

        if (endIndexExc != textLength && text.charAt(endIndexExc) != '\n') {
            endIndexCandidate = text.lastIndexOf(' ', endIndexExc-1);
            if (endIndexCandidate > startIndex)
                endIndexExc = endIndexCandidate;
        }

        return text.substring(startIndex, endIndexExc);
    }


    /**
     * Finds the starting index of the first word in a text, ignoring all initial line feeds and
     * empty spaces.
     * @param text the text in which the index is going to be found
     * @return the starting index (inclusive) of the first word or -1 otherwise.
     */
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
