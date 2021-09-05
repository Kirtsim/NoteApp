package fm.kirtsim.kharos.noteapp.ui.notedetail.viewmvc;

import androidx.annotation.ColorInt;

import fm.kirtsim.kharos.noteapp.ui.base.ObservableViewMvc;

/**
 * Created by kharos on 31/07/2017
 */

public interface NoteDetailViewMvc extends
        ObservableViewMvc<NoteDetailViewMvc.NoteDetailViewMvcListener> {
    interface NoteDetailViewMvcListener {
        void onNoteTitleFocusChanged(boolean hasFocus);
        void onNoteTextFocusChanged(boolean hasFocus);
    }

    void setNoteDateAndTime(String dateAndTime);
    void setNoteTitle(String title);
    void setNoteText(String text);
    void setTitleColor(int rgb);
    void setTextColor(int rgb);

    String getTitle();
    String getText();
    @ColorInt int getTitleColor();
    @ColorInt int getTextColor();

    void clearAllFocus();
}
