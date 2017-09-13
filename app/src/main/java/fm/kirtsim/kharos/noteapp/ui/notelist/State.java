package fm.kirtsim.kharos.noteapp.ui.notelist;

/**
 * Created by kharos on 11/09/2017
 */

@SuppressWarnings("unused")
class State {

    static final int START = 0;
    static final int DEFAULT = 1;
    static final int SELECTION = 2;
    static final int SELECTION_COLOR = 3;
    static final int COLOR = 4;
    static final int REORDER = 5;
    static final int NEW_NOTE = 6;
    private static final int STATE_COUNT = 7;

    private int state;

    State() {
        this(START);
    }

    State(int initialState) {
        setState(initialState);
    }

    int getState() {
        return state;
    }

    void setState(int newState) {
        if (STATE_COUNT < newState || newState < 0)
            throw new IllegalArgumentException("the set state is undefined");
        state = newState;
    }

    boolean isInStartState() { return state == START; }

    boolean isInDefaultState() {
        return state == DEFAULT;
    }

    boolean isInSelectionState() {
        return state == SELECTION;
    }

    boolean isInSelectionColoringState() {
        return state == SELECTION_COLOR;
    }

    boolean isInReorderState() {
        return state == REORDER;
    }

    boolean isInColoringState() {
        return state == COLOR;
    }

    boolean isInNewNoteState() { return state == NEW_NOTE; }
}
