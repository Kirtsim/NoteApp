package fm.kirtsim.kharos.noteapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fm.kirtsim.kharos.noteapp.dataholder.Note;

/**
 * Created by kharos on 31/07/2017
 */

public class NoteDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "notes_database";
    private static final int DB_VERSION = 2;

    public NoteDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(this.getClass().getSimpleName(), "upgrading the database");
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    /**
     * Selects a Note from the database that matches all attributes of the Note passed as a parameter.
     * The method does not use the Id field in the selection process.
     * @param note Note the attributes of which are to be matched in the SELECT statement
     * @return a new copy of the Note passed as a parameter if such Note exists in the database.
     * Otherwise returns a default Note with Id set to -1.
     */
    public Note selectNote(@NonNull Note note) {
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            final String[] SELECTION_ARGS = getSelectionArgsForNoteWithoutId(note);
            Cursor cursor = db.query(NoteEntry.TABLE_NAME, null,
                    WHERE_CLAUSE_TO_MATCH_ALL_ATTS_EXCEPT_ID,
                    SELECTION_ARGS, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    return extractCurrentNoteFromCursor(cursor);
                }
                cursor.close();
            }
        }
        return new Note(-1, -1, -1, false, "", "", 0);
    }

    private String[] getSelectionArgsForNoteWithoutId(Note note) {
        return new String[] {
                String.valueOf(note.getOrderNo()),
                String.valueOf(note.getColor()),
                note.isPinned() ? "1" : "0",
                note.getTitle(),
                note.getText(),
                String.valueOf(note.getTimestamp())};
    }

    public List<Note> selectAllNotes() {
        Cursor cursor = selectAll();
        if (cursor != null) {
            List<Note> notes = extractNotesFromCursor(cursor);
            cursor.close();
            return notes;
        }
        return new ArrayList<>(1);
    }

    @Nullable
    private Cursor selectAll() {
        SQLiteDatabase db = getReadableDatabase();
        if (db != null)
            return db.rawQuery("SELECT * FROM " + NoteEntry.TABLE_NAME +
                    " ORDER BY " + NoteEntry.ATT_ORDER_NO + " ASC", null);
        return null;
    }

    private List<Note> extractNotesFromCursor(@NonNull Cursor cursor) {
        if (cursor.moveToFirst()) {
            List<Note> selected = new ArrayList<>(cursor.getCount());
            do {
                Note note = extractCurrentNoteFromCursor(cursor);
                if (note != null) {
                    selected.add(note);
                }
            } while (cursor.moveToNext());
            return selected;
        }
        return new ArrayList<>(1);
    }

    private Note extractCurrentNoteFromCursor(@NonNull Cursor cursor) {
        Note note = null;
        try {
            note = new Note(
                    cursor.getInt(cursor.getColumnIndexOrThrow(NoteEntry.ATT_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(NoteEntry.ATT_ORDER_NO)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(NoteEntry.ATT_COLOR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(NoteEntry.ATT_PINNED)) == 1,
                    cursor.getString(cursor.getColumnIndexOrThrow(NoteEntry.ATT_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(NoteEntry.ATT_TEXT)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(NoteEntry.ATT_MODIFY_DATE))
            );
        } catch (IllegalArgumentException ex) {
            Log.e(this.getClass().getSimpleName(), ex.toString());
        }
        return note;
    }


    /**
     * Inserts a single Note into the database
     * @param note inserted Note, it's Id field is not used in the insertion process and is generated automatically
     * @return row Id of the inserted Note, -1 otherwise
     */
    public long insertNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues contentValues = getContentValuesFromNote(note);
            return db.insert(NoteEntry.TABLE_NAME, null, contentValues);
        }
        return -1;
    }

    /**
     * Deletes a single Note based on its Id.
     * @param id the unique Id that identifies a single Note in the database
     * @return the number of rows deleted (1) or 0 otherwise
     */
    public int deleteNote(int id) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            final String[] whereArgs = new String[] { String.valueOf(id) };
            return db.delete(NoteEntry.TABLE_NAME, NoteEntry.ATT_ID + " = ?", whereArgs);
        }
        return 0;
    }

    public int deleteNotes(@NonNull List<Integer> ids) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            final String WHERE_CLAUSE = createWhereClauseForDeletionBasedOnIds(ids.size());
            final String[] WHERE_ARGS = createWhereArgsFromIdList(ids);
            return db.delete(NoteEntry.TABLE_NAME, WHERE_CLAUSE, WHERE_ARGS);
        }
        return 0;
    }

    private String createWhereClauseForDeletionBasedOnIds(int idCount) {
        StringBuilder clauseBuilder = new StringBuilder(NoteEntry.ATT_ID);
        clauseBuilder.append(" IN (");
        for (int i = 0; i < idCount; ++i) {
            clauseBuilder.append("?,");
        }
        clauseBuilder.replace(clauseBuilder.length()-1, clauseBuilder.length(), ")");
        return clauseBuilder.toString();
    }

    private String[] createWhereArgsFromIdList(List<Integer> ids) {
        final String[] args = new String[ids.size()];
        for (int i = 0; i < ids.size(); ++i) {
            args[i] = String.valueOf(ids.get(i));
        }
        return args;
    }

    /**
     * Updates a single note in the database.
     * @param note Note containing new values
     * @return number of rows affected: 1 if successful or 0 otherwise
     */
    public int updateNote(Note note) {  // TODO: Check update operation
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            final String[] WHERE_ARGS= new String[] { String.valueOf(note.getId()) };
            final String WHERE_CLAUSE = NoteEntry.ATT_ID + " = ?";
            ContentValues updatedValues = getContentValuesFromNote(note);
            return db.update(NoteEntry.TABLE_NAME, updatedValues, WHERE_CLAUSE, WHERE_ARGS);
        }
        return 0;
    }

    /**
     * Updates a list of notes in the database.
     * @param notes Notes containing new values
     * @return number of notes updated
     */
    public int updateNotes(List<Note> notes) {
        int notesUpdated = 0;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            for (Note note : notes) {
                notesUpdated += updateNote(note);
            }
        }
        return notesUpdated;
    }

    private ContentValues getContentValuesFromNote(Note note) {
        ContentValues contentValues = new ContentValues(ALL_COLUMNS.length);
        contentValues.put(NoteEntry.ATT_ORDER_NO, note.getOrderNo());
        contentValues.put(NoteEntry.ATT_COLOR, note.getColor());
        contentValues.put(NoteEntry.ATT_PINNED, note.isPinned());
        contentValues.put(NoteEntry.ATT_TITLE, note.getTitle());
        contentValues.put(NoteEntry.ATT_TEXT, note.getText());
        contentValues.put(NoteEntry.ATT_MODIFY_DATE, note.getTimestamp());
        return contentValues;
    }

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + NoteEntry.TABLE_NAME + " (" +
            NoteEntry.ATT_ID + " INTEGER PRIMARY KEY, " +
            NoteEntry.ATT_ORDER_NO + " INTEGER, " +
            NoteEntry.ATT_COLOR + " INTEGER, " +
            NoteEntry.ATT_PINNED + " INTEGER, " +
            NoteEntry.ATT_TITLE + " TEXT, " +
            NoteEntry.ATT_TEXT + " TEXT, " +
            NoteEntry.ATT_MODIFY_DATE + " INTEGER)";

    private static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + NoteEntry.TABLE_NAME;

    private static final String[] ALL_COLUMNS = {
            NoteEntry.ATT_ID,
            NoteEntry.ATT_ORDER_NO,
            NoteEntry.ATT_COLOR,
            NoteEntry.ATT_PINNED,
            NoteEntry.ATT_TITLE,
            NoteEntry.ATT_TEXT,
            NoteEntry.ATT_MODIFY_DATE
    };

    private static final String WHERE_CLAUSE_TO_MATCH_ALL_ATTS_EXCEPT_ID =
                    NoteEntry.ATT_ORDER_NO + " = ? AND " +
                    NoteEntry.ATT_COLOR + " = ? AND " +
                    NoteEntry.ATT_PINNED + " = ? AND " +
                    NoteEntry.ATT_TITLE + " = ? AND " +
                    NoteEntry.ATT_TEXT + " = ? AND " +
                    NoteEntry.ATT_MODIFY_DATE + " = ?";
}
