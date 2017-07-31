package fm.kirtsim.kharos.noteapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kharos on 31/07/2017
 */

public class NoteDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "notes_database";
    public static final int DB_VERSION = 1;

    public NoteDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    private static final String SQL_CREATE_TABLE =
            "CREATE " + NoteEntry.TABLE_NAME + " (" +
            NoteEntry.ATT_ID + " INTEGER PRIMARY KEY, " +
            NoteEntry.ATT_TITLE + " TEXT, " +
            NoteEntry.ATT_TEXT + " TEXT, " +
            NoteEntry.ATT_MODIFY_DATE + " INTEGER)";

    private static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + NoteEntry.TABLE_NAME;
}
