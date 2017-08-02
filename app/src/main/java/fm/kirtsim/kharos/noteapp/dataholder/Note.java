package fm.kirtsim.kharos.noteapp.dataholder;

/**
 * Created by kharos on 29/07/2017
 */

public class Note {
    private int id;
    private String title;
    private String text;
    private long timestamp;

    public Note(int id, String title, String text, long timestamp) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.timestamp = timestamp;
    }

    public Note(String title, String text, long timestamp) {
        this(0, title, text, timestamp);
    }

    public Note(Note other) {
        this(other.getId(), other.getTitle(), other.getText(), other.getTimestamp());
    }

    public int getId() {
        return id;
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
                    note.getText().equals(text) &&
                    note.getTitle().equals(title) &&
                    note.getTimestamp() == timestamp;
        }
        return false;
    }
}
