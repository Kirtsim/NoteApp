package fm.kirtsim.kharos.noteapp.utils.listCoordinator;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.ui.adapterItemCoordinator.AdapterNotesListCoordinator;
import fm.kirtsim.kharos.noteapp.ui.adapterItemCoordinator.AdapterNotesListCoordinatorImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by kharos on 01/09/2017
 */

public class AdapterNotesListCoordinatorTest {

    private static final int COUNT_5 = 5;
    private static final int START_ORDER_NO_DFLT = 1;

    private static Note.NoteBuilder noteBuilder;
    private AdapterNotesListCoordinator coordinator;

    private List<Note> expectedNotesList;
    private List<Note> resultNotesList;

    private List<Note> expectedHighlightedNotes;

    @BeforeClass
    public static void initNoteBuilder() {
        noteBuilder = new Note.NoteBuilder();
    }

    @Before
    public void init(){
        coordinator = new AdapterNotesListCoordinatorImpl();
        resultNotesList = Lists.newArrayList();
        expectedHighlightedNotes = Lists.newArrayList();
        expectedNotesList = Lists.newArrayList();
    }

    @Test
    public void addNoteToEmptyCoordinatorTest() {
        final int NOTE_COUNT = 1;
        fillCoordinatorWithNotes(NOTE_COUNT);
        fillListWithNotesStartingWithOrderNumber(expectedNotesList, START_ORDER_NO_DFLT, NOTE_COUNT);
        assignResultNotesList();
        assertNotesListsEqual("addNoteToEmptyCoordinatorTest");
    }

    @Test
    public void addNoteToTheEndInCoordinatorTest() {
        final int NOTE_COUNT = 5;
        fillCoordinatorWithNotes(NOTE_COUNT);
        fillListWithNotesStartingWithOrderNumber(expectedNotesList, START_ORDER_NO_DFLT, NOTE_COUNT);
        assignResultNotesList();
        assertNotesListsEqual("addNoteToTheEndInCoordinatorTest");
    }

    @Test
    public void addMultipleNotesToEmptyCoordinatorTest() {
        final int NOTE_COUNT = 0, NOTES_ADDED = 5, START_ORDER_NO = 5;
        fillCoordinatorWithNotes(NOTE_COUNT);
        final List<Note> addedNotes =
                createListOfNotesStartingWithOrderNumber(NOTES_ADDED, START_ORDER_NO);
        coordinator.addNotes(addedNotes);

        fillListWithNotesStartingWithOrderNumber(expectedNotesList, START_ORDER_NO_DFLT, NOTE_COUNT);
        expectedNotesList.addAll(addedNotes);

        assignResultNotesList();
        assertNotesListsEqual("addMultipleNotesToEmptyCoordinatorTest");
    }


    @Test
    public void addMultipleNotesToCoordinatorTest() {
        final int NOTE_COUNT = 5, NOTES_ADDED = 5, START_ORDER_NO = 5;
        fillCoordinatorWithNotes(NOTE_COUNT);
        final List<Note> addedNotes = createListOfNotesStartingWithOrderNumber(NOTES_ADDED, START_ORDER_NO);
        coordinator.addNotes(addedNotes);

        fillListWithNotesStartingWithOrderNumber(expectedNotesList, START_ORDER_NO_DFLT, NOTE_COUNT);
        expectedNotesList.addAll(addedNotes);

        assignResultNotesList();
        assertNotesListsEqual("addMultipleNotesToCoordinatorTest");
    }

    @Test
    public void addNoteWithIdThatIsAlreadyInTheListTest() {
        final int NOTE_COUNT = 5;
        fillCoordinatorWithNotes(NOTE_COUNT);

        noteBuilder.copyValuesFrom(coordinator.getNoteAt(0));
        Note addedNote = noteBuilder.text("random")
                .title("title")
                .orderNumber(coordinator.getNoteCount())
                .build();
        try {
            coordinator.addNote(addedNote);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertExceptionMessageForAlreadyExistingNote(ex, addedNote);
        }

        fillListWithNotesStartingWithOrderNumber(expectedNotesList, START_ORDER_NO_DFLT, NOTE_COUNT);
        assignResultNotesList();
        assertNotesListsEqual("addNoteWithIdThatIsAlreadyInTheListTest");
    }

    @Test
    public void addMultipleNotesWithMiddleOneHavingAlreadyExistingIdTest() {
        final int NOTE_COUNT = 5, ADDED_COUNT = 3, DUPLICATE_INDEX = 1;
        fillCoordinatorWithNotes(NOTE_COUNT);

        List<Note> addedNotes = createListOfNotesStartingWithOrderNumber(
                ADDED_COUNT, coordinator.getNoteAt(NOTE_COUNT -1).getOrderNo() + 1);
        noteBuilder.copyValuesFrom(addedNotes.get(DUPLICATE_INDEX));
        Note duplicateNote = noteBuilder.id(coordinator.getNoteAt(NOTE_COUNT -1).getId()).build();
        addedNotes.set(DUPLICATE_INDEX, duplicateNote);

        try {
            coordinator.addNotes(addedNotes);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertExceptionMessageForAlreadyExistingNote(ex, duplicateNote);
        }

        fillListWithNotesStartingWithOrderNumber(expectedNotesList, START_ORDER_NO_DFLT, NOTE_COUNT);
        expectedNotesList.add(Note.copyOf(addedNotes.get(0)));

        assignResultNotesList();
        assertNotesListsEqual("addMultipleNotesWithMiddleOneHavingAlreadyExistingIdTest");
    }


    @Test
    public void removeLastNoteTest() {
        final int NOTE_COUNT = 5, INDEX_REMOVED = 4;
        fillCoordinatorWithNotes(NOTE_COUNT);
        Note toRemove = createANote(coordinator.getNoteAt(INDEX_REMOVED).getOrderNo());
        Note removed = coordinator.getNoteAt(INDEX_REMOVED);
        coordinator.removeNote(toRemove);

        fillListWithNotesStartingWithOrderNumber(expectedNotesList, START_ORDER_NO_DFLT,
                NOTE_COUNT - 1);
        assignResultNotesList();
        assertEquals("removeLastNoteTest", toRemove, removed);
        assertNotesListsEqual("removeLastNoteTest");
    }

    @Test
    public void removeTwoConsecutiveNotesTest() {
        final int NOTE_COUNT = 5, FIRST_REMOVED_AT = 1, REMOVED_COUNT = 2;
        fillCoordinatorWithNotes(NOTE_COUNT);
        fillListWithNotesStartingWithOrderNumber(expectedNotesList, START_ORDER_NO_DFLT, NOTE_COUNT);

        List<Note> notesToRemove = Lists.newArrayListWithCapacity(REMOVED_COUNT);
        notesToRemove.add(noteBuilder.copyValuesFrom(expectedNotesList.get(FIRST_REMOVED_AT)).build());
        notesToRemove.add(noteBuilder.copyValuesFrom(expectedNotesList.get(FIRST_REMOVED_AT + 1)).build());

        coordinator.removeNotes(notesToRemove);
        expectedNotesList.remove(FIRST_REMOVED_AT);
        expectedNotesList.remove(FIRST_REMOVED_AT);

        assignResultNotesList();
        assertNotesListsEqual("removeTwoConsecutiveNotesTest");
    }

    @Test
    public void addNoteToHighlightedTest() {
        final int NOTE_COUNT = 5, HIGHLIGHTED_NOTE_INDEX = 1;
        fillCoordinatorWithNotes(NOTE_COUNT);
        coordinator.addNoteToHighlighted(coordinator.getNoteAt(HIGHLIGHTED_NOTE_INDEX));

        expectedHighlightedNotes.add(coordinator.getNoteAt(HIGHLIGHTED_NOTE_INDEX));

        assertHighlightedAreCorrect("addNoteToHighlightedTest");
    }

    @Test
    public void addAlreadyHighlightedNoteToHighlightedTest() {
        final int NOTE_COUNT = 5, HIGHLIGHTED_NOTE_INDEX = 1;
        fillCoordinatorWithNotes(NOTE_COUNT);
        Note highlightedNote = coordinator.getNoteAt(HIGHLIGHTED_NOTE_INDEX);
        coordinator.addNoteToHighlighted(highlightedNote);
        coordinator.addNoteToHighlighted(highlightedNote);

        expectedHighlightedNotes.add(highlightedNote);

        assertHighlightedAreCorrect("addNoteToHighlightedTest");
    }

    @Test
    public void addNotExistingNoteToHighlightedTest() {
        fillCoordinatorWithNotes(COUNT_5);
        Note existingNote = coordinator.getNoteAt(0);
        Note differentNote = noteBuilder.id(123).orderNumber(123).build();

        assertTrue(coordinator.addNoteToHighlighted(existingNote));
        assertFalse(coordinator.addNoteToHighlighted(differentNote));

        expectedHighlightedNotes.add(existingNote);

        assertHighlightedAreCorrect("addNotExistingNoteToHighlightedTest");
        assertFalse("addNotExistingNoteToHighlightedTest", coordinator.isNoteHighlighted(differentNote));
    }

    @Test
    public void addAllNotesToHighlightedTest() {
        fillCoordinatorWithNotes(COUNT_5);
        fillListWithNotesStartingWithOrderNumber(expectedHighlightedNotes, START_ORDER_NO_DFLT, COUNT_5);

        coordinator.addAllNotesToHighlighted();

        assertHighlightedAreCorrect("addAllNotesToHighlightedTest");
    }

    @Test
    public void removeNoteFromHighlightedTest() {
        final int REMOVED_INDEX = 1;
        fillCoordinatorWithNotes(COUNT_5);
        fillListWithNotesStartingWithOrderNumber(expectedHighlightedNotes, START_ORDER_NO_DFLT, COUNT_5);

        Note removedNote = coordinator.getNoteAt(REMOVED_INDEX);

        coordinator.addAllNotesToHighlighted();

        coordinator.removeNoteFromHighlighted(removedNote);
        expectedHighlightedNotes.remove(REMOVED_INDEX);

        assertFalse("removeNoteFromHighlightedTest", coordinator.isNoteHighlighted(removedNote));
    }

    @Test
    public void removeAllNotesFromHighlightedTest() {
        fillCoordinatorWithNotes(COUNT_5);
        fillListWithNotesStartingWithOrderNumber(expectedHighlightedNotes, START_ORDER_NO_DFLT, COUNT_5);

        coordinator.addAllNotesToHighlighted();
        coordinator.removeAllNotesFromHighlighted();

        assertNotInHighlighted();
    }

    @Test
    public void removeHighlightedNotesTest() {
        final int REMOVED_INDEX = 1;
        fillCoordinatorWithNotes(COUNT_5);
        fillListWithNotesStartingWithOrderNumber(expectedNotesList, START_ORDER_NO_DFLT, COUNT_5);

        Note highlightedNote = coordinator.getNoteAt(REMOVED_INDEX);
        expectedHighlightedNotes.add(highlightedNote);
        coordinator.addNoteToHighlighted(highlightedNote);

        coordinator.removeHighlightedNotes();
        expectedNotesList.remove(REMOVED_INDEX);
        assignResultNotesList();

        assertNotesListsEqual("removeHighlightedNotesTest");
        assertNotInHighlighted();
    }

    @Test
    public void removeNoteThatIsAlsoHighlightedTest() {
        final int NOTE_INDEX = 1;
        fillCoordinatorWithNotes(COUNT_5);
        fillListWithNotesStartingWithOrderNumber(expectedNotesList, START_ORDER_NO_DFLT, COUNT_5);

        Note highlightedAndRemovedNote = coordinator.getNoteAt(NOTE_INDEX);
        coordinator.addNoteToHighlighted(highlightedAndRemovedNote);
        expectedHighlightedNotes.add(highlightedAndRemovedNote);

        assertTrue("Note removal fail", coordinator.removeNote(highlightedAndRemovedNote));
        assertNotInHighlighted();
    }






    /* ********************************************************************************* *
                                        HELPER METHODS
     * ********************************************************************************* */

    private void assertNotesListsEqual(String methodName) {
        final int size1 = expectedNotesList.size();
        final int size2 = resultNotesList.size();
        assertEquals(methodName + ": Expected length = " + size1 + "  Actual length = " + size2,
                expectedNotesList.size(), resultNotesList.size());
        for (int i = 0; i < size1; ++i) {
            assertEquals(methodName + ": diff at index " + i +
                            "\n expected Note:\n" + expectedNotesList.get(i) +
                            "\n actual Note:\n" + resultNotesList.get(i),
                    expectedNotesList.get(i), resultNotesList.get(i));
        }
    }

    private void assertHighlightedAreCorrect(String methodName) {
        expectedHighlightedNotes.forEach(note ->
                assertTrue(createMessageNoteNoteAmongstHighlighted(note),
                        coordinator.isNoteHighlighted(note)));
    }

    private void assertNotInHighlighted() {
        expectedHighlightedNotes.forEach(note ->
                assertFalse(createMessageNoteNoteAmongstHighlighted(note),
                        coordinator.isNoteHighlighted(note)));
    }

    private void assertExceptionMessageForAlreadyExistingNote(IllegalArgumentException ex,
                                                              Note note) {
        assertEquals(ex.getMessage(), "note with id " + note.getId() + " already exists");
    }

    private String createMessageNoteNoteAmongstHighlighted(Note note) {
        return note.toString() + "\n is not highlighted";
    }

    private void fillCoordinatorWithNotes(int notesCount) {
        int orderNo = START_ORDER_NO_DFLT;
        for (int i = 0; i < notesCount; ++i) {
            coordinator.addNote(createANote(orderNo++));
        }
    }

    private List<Note> createListOfNotesStartingWithOrderNumber(int count, int orderNo) {
        List<Note> notes = Lists.newArrayListWithCapacity(count);
        fillListWithNotesStartingWithOrderNumber(notes, orderNo, count);
        return notes;
    }

    private void fillListWithNotesStartingWithOrderNumber(List<Note> list, int orderNo,
                                                          int noteCount) {
        int orderNumber = orderNo;
        for (int i = 0; i < noteCount; ++i) {
            list.add(createANote(orderNumber++));
        }
    }

    private void assignResultNotesList() {
        resultNotesList = coordinator.getListOfAllNotes();
    }

    private Note createANote(int orderNo) {
        return new Note(100 + orderNo, orderNo, -1, false, "" + orderNo, "" + orderNo,
                1000000 + orderNo);
    }
}
