package fm.kirtsim.kharos.noteapp.dataholder;

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
}
