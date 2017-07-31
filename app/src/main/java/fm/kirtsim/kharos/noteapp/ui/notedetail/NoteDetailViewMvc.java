package fm.kirtsim.kharos.noteapp.ui.notedetail;

import fm.kirtsim.kharos.noteapp.dataholder.Note;
import fm.kirtsim.kharos.noteapp.ui.main.ViewMvc;

/**
 * Created by kharos on 31/07/2017
 */

public interface NoteDetailViewMvc extends ViewMvc {
    interface NoteDetailViewMvcListener {
        void onNoteTitleChange(String title);
        void onNoteTextChange(String text);
    }

    void setNoteTitle(String title);
    void setNoteText(String text);
}
