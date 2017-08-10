package fm.kirtsim.kharos.noteapp.utils.stringUtils;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import static fm.kirtsim.kharos.noteapp.utils.StringUtils.extractFirstWordsUpToLength;

/**
 * Created by kharos on 08/08/2017
 */

public class ExtractFirstWordsUpToLengthUT {

    @Test
    public void nullString() {
        String input = null;
        int length = 5;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "";

        assertEquals("nullString", expected, result);
    }

    @Test
    public void emptyString() {
        String input = "";
        int length = 5;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "";

        assertEquals("emptyString", expected, result);
    }

    @Test
    public void shortWord() {
        String input = "word";
        int length = 7;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "word";

        assertEquals("shortWord", expected, result);
    }




    /*
              Too long word
     */

    @Test
    public void tooLongWord() {
        String input = "hippopotamus";
        int length = 5;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "hippo";

        assertEquals("tooLongWord", expected, result);
    }


    @Test
    public void spaceOffsetTooLongWord() {
        String input = "   hippopotamus";
        int length = 5;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "hippo";

        assertEquals("spaceOffsetTooLongWord", expected, result);
    }

    @Test
    public void newLineOffsetTooLongWord() {
        String input = "\n\nhippopotamus";
        int length = 5;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "hippo";

        assertEquals("newLineOffsetTooLongWord", expected, result);
    }

    @Test
    public void mixedOffsetEndWithSpaceTooLongWord() {
        String input = "\n \n hippopotamus";
        int length = 5;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "hippo";

        assertEquals("mixedOffsetEndWithSpaceTooLongWord", expected, result);
    }

    @Test
    public void mixedOffsetEndWithNewLineTooLongWord() {
        String input = "\n \n \nhippopotamus";
        int length = 5;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "hippo";

        assertEquals("mixedOffsetEndWithNewLineTooLongWord", expected, result);
    }




    /*
        Two words on single line
     */

    @Test
    public void upToTheHalfOfSecondWord() {
        String input = "angry doggie";
        int length = 9;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "angry";

        assertEquals("upToTheHalfOfSecondWord", expected, result);
    }

    @Test
    public void spaceOffsetUpToTheHalfOfSecondWord() {
        String input = "     angry doggie";
        int length = 9;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "angry";

        assertEquals("spaceOffsetUpToTheHalfOfSecondWord", expected, result);
    }

    @Test
    public void newLineOffsetUpToTheHalfOfSecondWord() {
        String input = "\n\n\nangry doggie";
        int length = 9;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "angry";

        assertEquals("newLineOffsetUpToTheHalfOfSecondWord", expected, result);
    }

    @Test
    public void mixedOffsetEndingWithSpaceUpToTheHalfOfSecondWord() {
        String input = " \n \n   angry doggie";
        int length = 9;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "angry";

        assertEquals("mixedOffsetEndingWithSpaceUpToTheHalfOfSecondWord", expected, result);
    }

    @Test
    public void mixedOffsetEndingWithNewLineUpToTheHalfOfSecondWord() {
        String input = "\n \n \nangry doggie";
        int length = 9;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "angry";

        assertEquals("mixedOffsetEndingWithNewLineUpToTheHalfOfSecondWord", expected, result);
    }





    /*
        Two words on single line      (EXACT length)
     */

    @Test
    public void twoWordsExact() {
        String input = "angry doggie";
        int length = 5;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "angry";

        assertEquals("twoWordsExact", expected, result);
    }

    @Test
    public void spaceOffsetTwoWordsExact() {
        String input = "   angry doggie";
        int length = 5;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "angry";

        assertEquals("spaceOffsetTwoWordsExact", expected, result);
    }

    @Test
    public void newLineOffsetTwoWordsExact() {
        String input = "\n\nangry doggie";
        int length = 5;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "angry";

        assertEquals("newLineOffsetTwoWordsExact", expected, result);
    }

    @Test
    public void mixedOffsetEndingWithTwoWordsExact() {
        String input = "\n  \n angry doggie";
        int length = 5;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "angry";

        assertEquals("mixedOffsetEndingWithTwoWordsExact", expected, result);
    }

    @Test
    public void mixedOffsetEndingWithNewLineTwoWordsExact() {
        String input = "\n \n  \nangry doggie";
        int length = 5;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "angry";

        assertEquals("mixedOffsetEndingWithNewLineTwoWordsExact", expected, result);
    }







    /*
        Two words on separate lines
     */

    @Test
    public void twoWordsOnSeparateLines() {
        String input = "first\nsecond";
        int length = 8;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "first";

        assertEquals("twoWordsOnSeparateLines", expected, result);
    }


    @Test
    public void spaceOffsetTwoWordsOnSeparateLines() {
        String input = "  first\nsecond";
        int length = 8;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "first";

        assertEquals("spaceOffsetTwoWordsOnSeparateLines", expected, result);
    }

    @Test
    public void newLineOffsetTwoWordsOnSeparateLines() {
        String input = "\n\nfirst\nsecond";
        int length = 8;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "first";

        assertEquals("newLineOffsetTwoWordsOnSeparateLines", expected, result);
    }

    @Test
    public void mixedOffsetEndingWithSpaceTwoWordsOnSeparateLines() {
        String input = " \n \n first\nsecond";
        int length = 8;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "first";

        assertEquals("mixedOffsetEndingWithNewLineTwoWordsOnSeparateLines", expected, result);
    }

    @Test
    public void mixedOffsetEndingWithNewLineTwoWordsOnSeparateLines() {
        String input = " \n \nfirst\nsecond";
        int length = 8;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "first";

        assertEquals("mixedOffsetEndingWithNewLineTwoWordsOnSeparateLines", expected, result);
    }




    /*
        Two words on separate lines     (EXACT length)
     */

    @Test
    public void twoWordsOnSeparateLinesExact() {
        String input = "first\nsecond";
        int length = 5;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "first";

        assertEquals("twoWordsOnSeparateLinesExact", expected, result);
    }


    @Test
    public void spaceOffsetTwoWordsOnSeparateLinesExact() {
        String input = "  first\nsecond";
        int length = 5;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "first";

        assertEquals("spaceOffsetTwoWordsOnSeparateLinesExact", expected, result);
    }

    @Test
    public void newLineOffsetTwoWordsOnSeparateLinesExact() {
        String input = "\n\nfirst\nsecond";
        int length = 5;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "first";

        assertEquals("newLineOffsetTwoWordsOnSeparateLinesExact", expected, result);
    }

    @Test
    public void mixedOffsetEndingWithSpaceTwoWordsOnSeparateLinesExact() {
        String input = " \n\n first\nsecond";
        int length = 5;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "first";

        assertEquals("mixedOffsetEndingWithSpaceTwoWordsOnSeparateLinesExact", expected, result);
    }

    @Test
    public void mixedOffsetEndingWithNewLineTwoWordsOnSeparateLinesExact() {
        String input = " \n\n \nfirst\nsecond";
        int length = 5;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "first";

        assertEquals("mixedOffsetEndingWithNewLineTwoWordsOnSeparateLinesExact", expected, result);
    }






    /*
        Two words on separate lines     (first TOO LONG)
     */

    @Test
    public void twoWordsOnSeparateLinesFirstTooLong() {
        String input = "first\nsecond";
        int length = 3;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "fir";

        assertEquals("twoWordsOnSeparateLinesFirstTooLong", expected, result);
    }

    @Test
    public void spaceOffsetTwoWordsOnSeparateLinesFirstTooLong() {
        String input = "   first\nsecond";
        int length = 3;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "fir";

        assertEquals("spaceOffsetTwoWordsOnSeparateLinesFirstTooLong", expected, result);
    }

    @Test
    public void lineFeedOffsetTwoWordsOnSeparateLinesFirstTooLong() {
        String input = "\n\nfirst\nsecond";
        int length = 3;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "fir";

        assertEquals("lineFeedOffsetTwoWordsOnSeparateLinesFirstTooLong", expected, result);
    }

    @Test
    public void mixedOffsetEndingWithSpaceTwoWordsOnSeparateLinesFirstTooLong() {
        String input = " \n first\nsecond";
        int length = 3;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "fir";

        assertEquals("mixedOffsetEndingWithSpaceTwoWordsOnSeparateLinesFirstTooLong",
                expected, result);
    }

    @Test
    public void mixedOffsetEndingWithNewLineTwoWordsOnSeparateLinesFirstTooLong() {
        String input = " \nfirst\nsecond";
        int length = 3;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "fir";

        assertEquals("mixedOffsetEndingWithNewLineTwoWordsOnSeparateLinesFirstTooLong",
                expected, result);
    }



    @Test
    public void multipleWordsOnSingleLine() {
        String input = "and my new message.";
        int length = 26;

        String result = extractFirstWordsUpToLength(input, length);
        String expected = "and my new message.";

        assertEquals("multipleWordsOnSingleLine", expected, result);
    }


}
