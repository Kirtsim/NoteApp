package fm.kirtsim.kharos.noteapp.utils;

import java.util.ArrayList;
import java.util.List;

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
}
