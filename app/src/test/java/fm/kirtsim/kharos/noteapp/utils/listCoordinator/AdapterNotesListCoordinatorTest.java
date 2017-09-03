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
import static org.junit.Assert.fail;

/**
 * Created by kharos on 01/09/2017
 */

public class AdapterNotesListCoordinatorTest {

    private static Note.NoteBuilder noteBuilder;
    private AdapterNotesListCoordinator coordinator;

    private List<Note> expectedNotesList;
    private List<Note> resultNotesList;

    @BeforeClass
    public static void initNoteBuilder() {
        noteBuilder = new Note.NoteBuilder();
    }

    @Before
    public void initCoordinator(){
        coordinator = new AdapterNotesListCoordinatorImpl();
    }

    @Test
    public void addNoteToEmptyCoordinatorTest() {
        final int NOTE_COUNT = 1;
        initCoordinatorWithNotes(NOTE_COUNT);
        initExpectedNotesListWithNotes(NOTE_COUNT);
        assignResultNotesList();
        assertNotesListsEqualWithAMessage("addNoteToEmptyCoordinatorTest");
    }

    @Test
    public void addNoteToTheEndInCoordinatorTest() {
        final int NOTE_COUNT = 5;
        initCoordinatorWithNotes(NOTE_COUNT);
        initExpectedNotesListWithNotes(NOTE_COUNT);
        assignResultNotesList();
        assertNotesListsEqualWithAMessage("addNoteToTheEndInCoordinatorTest");
    }

    @Test
    public void addMultipleNotesToEmptyCoordinatorTest() {
        final int NOTE_COUNT = 0, NOTES_ADDED = 5, START_ORDER_NO = 5;
        initCoordinatorWithNotes(NOTE_COUNT);
        final List<Note> addedNotes = createListOfNotesStartingWithOrderNumber(NOTES_ADDED, START_ORDER_NO);
        coordinator.addNotes(addedNotes);

        initExpectedNotesListWithNotes(NOTE_COUNT);
        expectedNotesList.addAll(addedNotes);

        assignResultNotesList();
        assertNotesListsEqualWithAMessage("addMultipleNotesToEmptyCoordinatorTest");
    }


    @Test
    public void addMultipleNotesToCoordinatorTest() {
        final int NOTE_COUNT = 5, NOTES_ADDED = 5, START_ORDER_NO = 5;
        initCoordinatorWithNotes(NOTE_COUNT);
        final List<Note> addedNotes = createListOfNotesStartingWithOrderNumber(NOTES_ADDED, START_ORDER_NO);
        coordinator.addNotes(addedNotes);

        initExpectedNotesListWithNotes(NOTE_COUNT);
        expectedNotesList.addAll(addedNotes);

        assignResultNotesList();
        assertNotesListsEqualWithAMessage("addMultipleNotesToCoordinatorTest");
    }

    @Test
    public void addNoteWithIdThatIsAlreadyInTheListTest() {
        final int NOTE_COUNT = 5;
        initCoordinatorWithNotes(NOTE_COUNT);

        noteBuilder.copyValuesFrom(coordinator.getNoteAt(0));
        Note addedNote = noteBuilder.text("random")
                .title("title")
                .orderNumber(coordinator.getNoteCount())
                .build();
        try {
            coordinator.addNote(addedNote);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException ignored) {}

        initExpectedNotesListWithNotes(NOTE_COUNT);
        assignResultNotesList();
        assertNotesListsEqualWithAMessage("addNoteWithIdThatIsAlreadyInTheListTest");
    }

    @Test
    public void addMultipleNotesWithMiddleOneHavingAlreadyExistingIdTest() {
        final int NOTE_COUNT = 5, ADDED_COUNT = 3, DUPLICATE_INDEX = 1;
        initCoordinatorWithNotes(NOTE_COUNT);

        List<Note> addedNotes = createListOfNotesStartingWithOrderNumber(ADDED_COUNT, NOTE_COUNT);
        noteBuilder.copyValuesFrom(addedNotes.get(DUPLICATE_INDEX));
        Note duplicateNote = noteBuilder.id(coordinator.getNoteAt(NOTE_COUNT -1).getId()).build();
        addedNotes.set(DUPLICATE_INDEX, duplicateNote);

        try {
            coordinator.addNotes(addedNotes);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException ignored) {}

        initExpectedNotesListWithNotes(NOTE_COUNT);
        expectedNotesList.add(Note.copyOf(addedNotes.get(0)));

        assignResultNotesList();
        assertNotesListsEqualWithAMessage("addMultipleNotesWithMiddleOneHavingAlreadyExistingIdTest");
    }


    @Test
    public void removeLastNoteTest() {
        final int NOTE_COUNT = 5, INDEX_REMOVED = 4;
        initCoordinatorWithNotes(NOTE_COUNT);
        Note toRemove = createANote(INDEX_REMOVED);
        Note removed = coordinator.getNoteAt(INDEX_REMOVED);
        coordinator.removeNote(toRemove);

        initExpectedNotesListWithNotes(NOTE_COUNT - 1);
        assignResultNotesList();
        assertEquals("removeLastNoteTest", toRemove, removed);
        assertNotesListsEqualWithAMessage("removeLastNoteTest");
    }

    @Test
    public void removeTwoConsecutiveNotesTest() {
        final int NOTE_COUNT = 5, FIRST_REMOVED_AT = 1, REMOVED_COUNT = 2;
        initCoordinatorWithNotes(NOTE_COUNT);
        initExpectedNotesListWithNotes(NOTE_COUNT);

        List<Note> notesToRemove = Lists.newArrayListWithCapacity(REMOVED_COUNT);
        notesToRemove.add(noteBuilder.copyValuesFrom(expectedNotesList.get(FIRST_REMOVED_AT)).build());
        notesToRemove.add(noteBuilder.copyValuesFrom(expectedNotesList.get(FIRST_REMOVED_AT + 1)).build());

        coordinator.removeNotes(notesToRemove);
        expectedNotesList.remove(FIRST_REMOVED_AT);
        expectedNotesList.remove(FIRST_REMOVED_AT + 1);

        assignResultNotesList();
        assertNotesListsEqualWithAMessage("removeTwpConsecutiveNotesTest");
    }







    /* ********************************************************************************* *
                                        HELPER METHODS
     * ********************************************************************************* */

    private void initCoordinatorWithNotes(int notesCount) {
        for (int i = 0; i < notesCount; ++i) {
            coordinator.addNote(createANote(i));
        }
    }

    private void assignResultNotesList() {
        resultNotesList = coordinator.getListOfAllNotes();
    }

    private void assertNotesListsEqualWithAMessage(String methodName) {
        final int size1 = expectedNotesList.size();
        final int size2 = resultNotesList.size();
        assertEquals(methodName + ": length1 = " + size1 + "  length2 = " + size2,
                expectedNotesList.size(), resultNotesList.size());
        for (int i = 0; i < size1; ++i) {
            assertEquals(methodName + ": diff at index " + i +
                            "\n expected Note:\n" + expectedNotesList.get(i) +
                            "\n actual Note:\n" + resultNotesList.get(i),
                    expectedNotesList.get(i), resultNotesList.get(i));
        }
    }

    private void initExpectedNotesListWithNotes(int noteCount) {
        expectedNotesList = createListOfNotesStartingWithOrderNumber(noteCount, 0);
    }

    private List<Note> createListOfNotesStartingWithOrderNumber(int count, int orderNo) {
        int orderNumber = orderNo;
        List<Note> notes = Lists.newArrayListWithCapacity(count);
        for (int i = 0; i < count; ++i) {
            notes.add(createANote(orderNumber++));
        }
        return notes;
    }

    private Note createANote(int orderNo) {
        return new Note(100 + orderNo, orderNo, -1, false, "" + orderNo, "" + orderNo,
                1000000 + orderNo);
    }
}
