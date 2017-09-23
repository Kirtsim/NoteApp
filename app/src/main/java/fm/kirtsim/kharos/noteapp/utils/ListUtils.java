package fm.kirtsim.kharos.noteapp.utils;

import com.google.common.collect.Collections2;

import java.util.Collections;
import java.util.List;

import fm.kirtsim.kharos.noteapp.dataholder.Note;

/**
 * Created by kharos on 06/08/2017
 */

public class ListUtils {


    public static <T extends Object> void removeNullObjects(List<T> list) {
        final int count = list.size();

        int currentIndex = list.indexOf(null);
        if (currentIndex == -1) return;
        for (; currentIndex < count -1; ++currentIndex) {
            int swapIndex = currentIndex + 1;
            while (swapIndex < count && list.get(swapIndex) == null)
                ++swapIndex;
            if (swapIndex >= count) break;
            list.set(currentIndex, list.get(swapIndex));
            list.set(swapIndex, null);
        }

        currentIndex = count -1;
        while(currentIndex > -1 && list.get(currentIndex) == null)
            list.remove(currentIndex--);
    }

    public static void sortNoteListByOrderNumber(List<Note> notes) {
        Collections.sort(notes, (note1, note2) -> {
            int orderNo1 = note1.getOrderNo();
            int orderNo2 = note2.getOrderNo();

            if (orderNo1 < orderNo2) return -1;
            if (orderNo1 == orderNo2) return 0;
            return 1;
        });
    }

    public static int[] extractNoteIdsIntoArray(List<Note> notes) {
        if (notes != null) {
            int [] ids = new int[notes.size()];
            for (int i = 0; i < ids.length; ++i)
                ids[i] = notes.get(i).getId();
            return ids;
        }
        return new int[0];
    }
}
