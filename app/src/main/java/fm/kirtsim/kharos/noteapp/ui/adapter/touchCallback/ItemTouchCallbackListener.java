package fm.kirtsim.kharos.noteapp.ui.adapter.touchCallback;

/**
 * Created by kharos on 27/08/2017
 */

public interface ItemTouchCallbackListener {

    boolean onItemMove(int posFrom, int posTo);

    void dragFinished(int startedFrom, int finishedAt);
}
