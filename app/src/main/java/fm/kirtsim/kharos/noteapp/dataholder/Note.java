package fm.kirtsim.kharos.noteapp.dataholder;

import android.support.annotation.ColorInt;

/**
 * Created by kharos on 29/07/2017
 */

public class Note {
    private int id;
    private int orderNo;
    private int color;
    private boolean isPinned;
    private String title;
    private String text;
    private long timestamp;

    public Note(int id, int orderNo, int color, boolean isPinned, String title, String text,
                long timestamp) {
        this.id = id;
        this.orderNo = orderNo;
        this.color = color;
        this.isPinned = isPinned;
        this.title = title;
        this.text = text;
        this.timestamp = timestamp;
    }

    public Note(int orderNo, int color, boolean isPinned, String title, String text,
                long timestamp) {
        this(0, orderNo, color, isPinned, title, text, timestamp);
    }

    public Note(Note other) {
        this(other.getId(), other.getOrderNo(), other.getColor(), other.isPinned(),
                other.getTitle(), other.getText(), other.getTimestamp());
    }

    public int getId() {
        return id;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public int getColor() {
        return color;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Note) {
            final Note note = (Note) obj;
            return note.getId() == id &&
                    note.getOrderNo() == orderNo &&
                    note.getColor() == color &&
                    note.isPinned == isPinned &&
                    note.getText().equals(text) &&
                    note.getTitle().equals(title) &&
                    note.getTimestamp() == timestamp;
        }
        return false;
    }

    public static Note copyOf(Note note) {
        return new Note(
                note.getId(),
                note.getOrderNo(),
                note.getColor(),
                note.isPinned(),
                note.getTitle(),
                note.getText(),
                note.getTimestamp()
        );
    }

    @Override
    public String toString() {
        return "Note" +
                "\nid:        " + id +
                "\norderNo:   " + orderNo +
                "\ncolor:     " + color +
                "\npinned:    " + isPinned +
                "\ntitle:    \"" + title + "\"" +
                "\ntext:     \"" + text + "\"" +
                "\ntimestamp: " + timestamp;
    }

    public static class NoteBuilder {

        private Note note;

        public NoteBuilder() {
            note = new Note(-1, -1, -1, false, "", "", 0);
        }

        public NoteBuilder(Note baseNote) {
            this.note = Note.copyOf(baseNote);
        }

        public NoteBuilder id(int id) {
            note.id = id;
            return this;
        }

        public NoteBuilder orderNumber(int orderNumber) {
            note.orderNo = orderNumber;
            return this;
        }

        public NoteBuilder color(@ColorInt int color) {
            note.color = color;
            return this;
        }

        public NoteBuilder pinned(boolean isPinned) {
            note.isPinned = isPinned;
            return this;
        }

        public NoteBuilder title(String title) {
            note.title = title;
            return this;
        }

        public NoteBuilder text(String text) {
            note.text = text;
            return this;
        }

        public NoteBuilder timeStamp(long timestamp) {
            note.timestamp = timestamp;
            return this;
        }

        public NoteBuilder copyValuesFrom(Note srcNote) {
            note.id = srcNote.id;
            note.orderNo = srcNote.orderNo;
            note.color = srcNote.color;
            note.isPinned = srcNote.isPinned;
            note.title = srcNote.title;
            note.text = srcNote.text;
            note.timestamp = srcNote.timestamp;
            return this;
        }

        public Note build() {
            return Note.copyOf(note);
        }

        public static Note createDefaultNote() {
            return new Note(-1, -1, -1, false, "title", "text", 0);
        }
    }
}
