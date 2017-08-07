package fm.kirtsim.kharos.noteapp.ui.adapter;

/**
 * Created by kharos on 07/08/2017
 */

public interface ListAdapter<AdapterListener> {

    void setListener(AdapterListener listener);

    void unregisterListener();

    void updateDataSet();

    void updateItemAtPosition(int position);

    void updateRemovedAtPosition(int position);
}
