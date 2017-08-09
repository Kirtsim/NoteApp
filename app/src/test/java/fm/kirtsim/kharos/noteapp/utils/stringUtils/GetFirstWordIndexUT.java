package fm.kirtsim.kharos.noteapp.utils.stringUtils;

import static org.junit.Assert.*;
import org.junit.Test;

import fm.kirtsim.kharos.noteapp.utils.StringUtils;

/**
 * Created by kharos on 08/08/2017
 */

public class GetFirstWordIndexUT {

    @Test
    public void nullString() {
        int result = StringUtils.getFirstWordIndex(null);
        int expected = -1;

        assertEquals("nullString", expected, result);
    }

    @Test
    public void emptyString() {
        int result = StringUtils.getFirstWordIndex("");
        int expected = -1;

        assertEquals("emptyString", expected, result);
    }

    @Test
    public void onlySpacesAndLineFeeds() {
        int result = StringUtils.getFirstWordIndex(" \n \n \n   \n");
        int expected = -1;

        assertEquals("onlySpacesAndLineFeeds", expected, result);
    }

    @Test
    public void noSpacesOrLineFeeds() {
        int result = StringUtils.getFirstWordIndex("mytext");
        int expected = 0;

        assertEquals("noSpacesOrLineFeeds", expected, result);
    }

    @Test
    public void spacesPrecedingWord() {
        int result = StringUtils.getFirstWordIndex("   mytext");
        int expected = 3;

        assertEquals("spacesPrecedingWord", expected, result);
    }

    @Test
    public void lineFeedsPrecedingWord() {
        int result = StringUtils.getFirstWordIndex("\n\n\nmytext");
        int expected = 3;

        assertEquals("lineFeedsPrecedingWord", expected, result);
    }

    @Test
    public void consecutiveSpacesAndLineFeedsPrecedingWord() {
        int result = StringUtils.getFirstWordIndex("\n\n\n   mytext");
        int expected = 6;

        assertEquals("consecutiveSpacesAndLineFeedsPrecedingWord", expected, result);
    }
    @Test
    public void mixedSpacesAndLineFeedsPrecedingWord() {
        int result = StringUtils.getFirstWordIndex("\n \n mytext");
        int expected = 4;

        assertEquals("mixedSpacesAndLineFeedsPrecedingWord", expected, result);
    }
}
