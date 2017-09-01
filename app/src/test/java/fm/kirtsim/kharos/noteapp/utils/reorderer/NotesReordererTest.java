package fm.kirtsim.kharos.noteapp.utils.reorderer;

import com.google.common.collect.Lists;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesReorderer;


/**
 * Created by kharos on 31/08/2017
 */

public class NotesReordererTest {

    private static final int NOTE_COUNT = 10;

    private static List<Note> notesList;
    private NotesReorderer reorderer;

    private List<Note> resultAll;
    private List<Note> resultUpdated;

    private List<Note> expectedAll;
    private List<Note> expectedUpdated;

    @BeforeClass
    public static void initializeNotes() {
        notesList = Lists.newArrayListWithCapacity(NOTE_COUNT);
        for (int i = 0; i < NOTE_COUNT; ++i) {
            int orderNo = i + 1;
            notesList.add(new TestNote(100 + orderNo, orderNo, -1, false,
                    "Title " + orderNo, "text " + orderNo, 10000 + i));
        }
    }

    @Test
    public void positionChangeLastToFirstTest() {
        final int FROM = NOTE_COUNT -1, TO = 0;
        initializeReordererForPositionChange(FROM, TO);
        changePositionFromToAndInitResults(FROM, TO);
        createExpectedResultsForMovingNoteFromTo(FROM, TO);
        assertEqualsWithMessage("positionChangeLastToFirstTest");
    }

    @Test
    public void positionChangeFirstToLastTest() {
        final int FROM = 0, TO = NOTE_COUNT -1;
        initializeReordererForPositionChange(FROM, TO);
        changePositionFromToAndInitResults(FROM, TO);
        createExpectedResultsForMovingNoteFromTo(FROM, TO);
        assertEqualsWithMessage("positionChangeFirstToLastTest");
    }

    @Test
    public void positionChangeMiddleToLastTest() {
        final int FROM = NOTE_COUNT/2, TO = NOTE_COUNT -1;
        initializeReordererForPositionChange(FROM, TO);
        changePositionFromToAndInitResults(FROM, TO);
        createExpectedResultsForMovingNoteFromTo(FROM, TO);
        assertEqualsWithMessage("positionChangeMiddleToLastTest");
    }

    @Test
    public void positionChangeMiddleToFirstTest() {
        final int FROM = NOTE_COUNT/2, TO = 0;
        initializeReordererForPositionChange(FROM, TO);
        changePositionFromToAndInitResults(FROM, TO);
        createExpectedResultsForMovingNoteFromTo(FROM, TO);
        assertEqualsWithMessage("positionChangeMiddleToFirstTest");
    }

    @Test
    public void positionChangeSecondToPenultimateTest() {
        final int FROM = NOTE_COUNT/2, TO = NOTE_COUNT -2;
        initializeReordererForPositionChange(FROM, TO);
        changePositionFromToAndInitResults(FROM, TO);
        createExpectedResultsForMovingNoteFromTo(FROM, TO);
        assertEqualsWithMessage("positionChangeSecondToPenultimateTest");
    }

    @Test
    public void noPositionChangeTest() {
        final int FROM = NOTE_COUNT/2, TO = NOTE_COUNT /2;
        initializeReordererForPositionChange(FROM, TO);
        changePositionFromToAndInitResults(FROM, TO);
        createExpectedResultsForMovingNoteFromTo(FROM, TO);
        assertEqualsWithMessage("positionChangeSecondToPenultimateTest");
    }

    @Test
    public void changeOrderNumbersFromFirstStartingWith10() {
        final int START_NUMBER = 10, START_INDEX = 0;
        initializeReordererWithOrdererdNotesList();
        reorderer.changeOrderNumberOfNotesFromIndexStartingWithNumber(START_INDEX, START_NUMBER);
        initReordererResults();
        createExpectedResultsForChangingOrderNumbersAtStartingFrom(START_INDEX, START_NUMBER);
        assertEqualsWithMessage("changeOrderNumbersFromFirstStartingWith10");
    }

    @Test
    public void changeOrderNumbersFromMiddleStartingWith10() {
        final int START_NUMBER = 10, START_INDEX = NOTE_COUNT /2;
        initializeReordererWithOrdererdNotesList();
        reorderer.changeOrderNumberOfNotesFromIndexStartingWithNumber(START_INDEX, START_NUMBER);
        initReordererResults();
        createExpectedResultsForChangingOrderNumbersAtStartingFrom(START_INDEX, START_NUMBER);
        assertEqualsWithMessage("changeOrderNumbersFromMiddleStartingWith10");
    }

    @Test
    public void changeOrderNumbersOfLastTo100() {
        final int START_NUMBER = 100, START_INDEX = NOTE_COUNT -1;
        initializeReordererWithOrdererdNotesList();
        reorderer.changeOrderNumberOfNotesFromIndexStartingWithNumber(START_INDEX, START_NUMBER);
        initReordererResults();
        createExpectedResultsForChangingOrderNumbersAtStartingFrom(START_INDEX, START_NUMBER);
        assertEqualsWithMessage("changeOrderNumbersOfLastTo100");
    }


    private void initializeReordererForPositionChange(int from, int to) {
        List<Note> notes = Lists.newArrayList(notesList);
        Note moved = notes.remove(from);
        notes.add(to, moved);
        reorderer = new NotesReorderer(notes);
    }

    private void initializeReordererWithOrdererdNotesList() {
        reorderer = new NotesReorderer(notesList);
    }

    private void changePositionFromToAndInitResults(int from, int to) {
        reorderer.changeOrderNumbersDueToPositionChange(from, to);
        initReordererResults();
    }

    private void initReordererResults() {
        resultAll = castNotesToTestNotes(reorderer.getNotes());
        resultUpdated = castNotesToTestNotes(reorderer.getUpdatedNotes());
    }

    private void createExpectedResultsForMovingNoteFromTo(int from, int to) {
        expectedAll = Lists.newArrayList(notesList);
        Note note = expectedAll.remove(from);
        expectedAll.add(to, note);
        for (int i = 0; i < expectedAll.size(); ++i) {
            Note oldNote = expectedAll.get(i);
            expectedAll.set(i, copyNoteWithNewOrderNumber(oldNote, i+1));
        }
        createExpectedUpdatedNotesList(from, to);
    }

    private void assertEqualsWithMessage(String message) {
        assertEquals(message, expectedAll, resultAll);
        assertEquals(message, expectedUpdated, resultUpdated);
    }

    private TestNote copyNoteWithNewOrderNumber(Note note, int orderNo) {
        return new TestNote(
                note.getId(), orderNo, note.getColor(), note.isPinned(), note.getTitle(),
                note.getText(), note.getTimestamp()
        );
    }

    private void createExpectedUpdatedNotesList(int from, int to) {
        int lowerIndex = Math.min(from, to);
        int higherIndex = Math.max(from, to);
        final int size = higherIndex - lowerIndex + 1;
        expectedUpdated = Lists.newArrayListWithCapacity(size);
        for (int i = lowerIndex; i <= higherIndex; ++i) {
            expectedUpdated.add(expectedAll.get(i));
        }
    }

    private void createExpectedResultsForChangingOrderNumbersAtStartingFrom(int at, int orderNo) {
        int orderNumber = orderNo;
        expectedAll = Lists.newArrayList(notesList);
        expectedUpdated = Lists.newArrayListWithCapacity(NOTE_COUNT - at);
        for (int i = at; i < NOTE_COUNT; ++i) {
            Note updated = copyNoteWithNewOrderNumber(expectedAll.get(i), orderNumber++);
            expectedAll.set(i, updated);
            expectedUpdated.add(updated);
        }
    }

    private List<Note> castNotesToTestNotes(List<Note> notes) {
        List<Note> casted = Lists.newArrayListWithCapacity(notes.size());
        notes.forEach(note -> casted.add(new TestNote(note)));
        return casted;
    }

    private static class TestNote extends Note {

        TestNote(int id, int orderNo, int color, boolean isPinned, String title, String text, long timestamp) {
            super(id, orderNo, color, isPinned, title, text, timestamp);
        }

        TestNote(Note other) {
            super(other);
        }

        @Override
        public String toString() {
            return "(" + getId() + " - " + getOrderNo() + ")";
        }
    }
}


