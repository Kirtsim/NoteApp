package fm.kirtsim.kharos.noteapp.ui.notelist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kharos on 11/09/2017
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class State implements Parcelable {

    public static final int START = 0;
    public static final int DEFAULT = 1;
    public static final int SELECTION = 2;
    public static final int SELECTION_COLOR = 3;
    public static final int COLOR = 4;
    public static final int REORDER = 5;
    public static final int NEW_NOTE = 6;
    private static final int STATE_COUNT = 7;

    private int state;
    private boolean wasRestored;

    public State() {
        this(START);
    }

    public State(int initialState) {
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

    public int getState() {
        return state;
    }

    public boolean wasRestored() {
        return wasRestored;
    }

    public void setState(int newState) {
        if (STATE_COUNT < newState || newState < 0)
            throw new IllegalArgumentException("the set state is undefined");
        state = newState;
    }

    public void setWasRestored(boolean restored) {
        wasRestored = restored;
    }

    public boolean isInStartState() { return state == START; }

    public boolean isInDefaultState() {
        return state == DEFAULT;
    }

    public boolean isInSelectionState() {
        return state == SELECTION;
    }

    public boolean isInSelectionColoringState() {
        return state == SELECTION_COLOR;
    }

    public boolean isInReorderState() {
        return state == REORDER;
    }

    public boolean isInColoringState() {
        return state == COLOR;
    }

    public boolean isInNewNoteState() { return state == NEW_NOTE; }

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
