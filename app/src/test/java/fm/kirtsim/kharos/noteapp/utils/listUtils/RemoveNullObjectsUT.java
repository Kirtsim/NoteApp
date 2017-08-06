package fm.kirtsim.kharos.noteapp.utils.listUtils;

import com.google.common.collect.Lists;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

import fm.kirtsim.kharos.noteapp.utils.ListUtils;

/**
 * Created by kharos on 06/08/2017
 */

public class RemoveNullObjectsUT {

    @Test
    public void emptyList() {
        List<Integer> myList = Lists.newArrayList();
        List<Integer> expected = Lists.newArrayList();

        ListUtils.removeNullObjects(myList);

        assertArrayEquals("emptyList", expected.toArray(), myList.toArray());
    }

    @Test
    public void removeNothing() {
        List<Integer> myList = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10 );
        List<Integer> expected = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10 );

        ListUtils.removeNullObjects(myList);

        assertArrayEquals("removeNothing", expected.toArray(), myList.toArray());
    }

    @Test
    public void removeSingleFromSingleELementList() {
        List<Integer> myList = Lists.newArrayList(null, null);
        myList.remove(myList.size() -1);
        List<Integer> expected = Lists.newArrayList();

        ListUtils.removeNullObjects(myList);

        assertArrayEquals("removeSingleFromSingleELementList", expected.toArray(), myList.toArray());
    }

    @Test
    public void removeFirst() {
        List<Integer> myList = Lists.newArrayList(null, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> expected = Lists.newArrayList(2, 3, 4, 5, 6, 7, 8, 9, 10);

        ListUtils.removeNullObjects(myList);

        assertArrayEquals("removeFirst", expected.toArray(), myList.toArray());
    }

    @Test
    public void removeLast() {
        List<Integer> myList = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, null);
        List<Integer> expected = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        ListUtils.removeNullObjects(myList);

        assertArrayEquals("removeLast", expected.toArray(), myList.toArray());
    }

    @Test
    public void removeFirstAndLast() {
        List<Integer> myList = Lists.newArrayList(null, 2, 3, 4, 5, 6, 7, 8, 9, null);
        List<Integer> expected = Lists.newArrayList(2, 3, 4, 5, 6, 7, 8, 9);

        ListUtils.removeNullObjects(myList);

        assertArrayEquals("removeFirstAndLast", expected.toArray(), myList.toArray());
    }

    @Test
    public void removeMiddle() {
        List<Integer> myList = Lists.newArrayList(1, 2, 3, 4, null, 6, 7, 8, 9, 10);
        List<Integer> expected = Lists.newArrayList(1, 2, 3, 4, 6, 7, 8, 9, 10);

        ListUtils.removeNullObjects(myList);

        assertArrayEquals("removeMiddle", expected.toArray(), myList.toArray());
    }

    @Test
    public void removeConsecutive() {
        List<Integer> myList = Lists.newArrayList(1, 2, null, null, null, 6, 7, 8, 9, 10);
        List<Integer> expected = Lists.newArrayList(1, 2, 6, 7, 8, 9, 10);

        ListUtils.removeNullObjects(myList);

        assertArrayEquals("removeConsecutive", expected.toArray(), myList.toArray());
    }

    @Test
    public void removeAll() {
        List<Integer> myList = Lists.newArrayList(null, null, null, null, null, null, null, null);
        List<Integer> expected = Lists.newArrayList();

        ListUtils.removeNullObjects(myList);

        assertArrayEquals("removeAll", expected.toArray(), myList.toArray());
    }

}
