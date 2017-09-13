package fm.kirtsim.kharos.noteapp.ui.notelist.actionBarVisual;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import fm.kirtsim.kharos.noteapp.R;
import fm.kirtsim.kharos.noteapp.ui.notelist.NotesListItemViewMvcImpl;

/**
 * Created by kharos on 12/09/2017
 */

public class NotesListItemViewMvcImplDebug extends NotesListItemViewMvcImpl implements
                NotesListItemViewMvcDebug{

    private TextView noteId;
    private TextView noteOrderNo;


    public NotesListItemViewMvcImplDebug(LayoutInflater inflater, ViewGroup container) {
        super(inflater, container);
        initViews();
    }

    private void initViews() {
        noteId = (TextView) rootView.findViewById(R.id.note_id);
        noteOrderNo = (TextView) rootView.findViewById(R.id.note_order_no);
    }

    @Override
    protected int getLayoutResourse() {
        return R.layout.layout_note_list_item_debug;
    }

    @Override
    public void setId(int id) {
        noteId.setText(String.valueOf(id));
    }

    @Override
    public void setOrderNo(int orderNo) {
        noteOrderNo.setText(String.valueOf(orderNo));
    }
}
