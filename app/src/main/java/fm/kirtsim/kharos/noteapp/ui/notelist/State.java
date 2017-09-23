package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kharos on 11/09/2017
 */

@SuppressWarnings({"unused", "WeakerAccess"})
class State implements Parcelable{

    static final int START = 0;
    static final int DEFAULT = 1;
    static final int SELECTION = 2;
    static final int SELECTION_COLOR = 3;
    static final int COLOR = 4;
    static final int REORDER = 5;
    static final int NEW_NOTE = 6;
    private static final int STATE_COUNT = 7;

    private int state;
    private boolean wasRestored;

    State() {
        this(START);
    }

    State(int initialState) {
        setState(initialState);
        setWasRestored(false);
    }

    State(Parcel parcel) {
        state = parcel.readInt();
        wasRestored = parcel.readByte() != 0;
    }

    public static final Creator<State> CREATOR = new Creator<State>() {
        @Override
        public State createFromParcel(Parcel in) {
            return new State(in);
        }

        @Override
        public State[] newArray(int size) {
            return new State[size];
        }
    };

    int getState() {
        return state;
    }

    boolean wasRestored() {
        return wasRestored;
    }

    void setState(int newState) {
        if (STATE_COUNT < newState || newState < 0)
            throw new IllegalArgumentException("the set state is undefined");
        state = newState;
    }

    void setWasRestored(boolean restored) {
        wasRestored = restored;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(state);
        dest.writeByte((byte) (wasRestored ? 1 : 0));
    }
}
