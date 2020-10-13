import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class DequeTest {
    Deque<Integer> deque;

    @Before
    public void setup() {
        deque = new Deque();
    }

    @Test
    public void queueIsEmptyAtStart() {
        //deque.addFirst(1);
        assertEquals(true, deque.isEmpty());
        assertEquals(0, deque.size());
    }

    @Test(expected = NoSuchElementException.class)
    public void removeLastThrowsExceptionOnEmpty() {
        assertEquals(null, deque.removeLast());

    }

    @Test(expected = NoSuchElementException.class)
    public void removeFirstThrowsExceptionOnEmpty() {
        assertEquals(null, deque.removeFirst());

    }

    @Test(expected = IllegalArgumentException.class)
    public void addFirstWithNullThrowsExceptionOnEmpty() {
        deque.addFirst(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addLastWithNullThrowsExceptionOnEmpty() {
        deque.addLast(null);
    }


    @Test
    public void addFrontIncrementsSize() {
        deque.addFirst(1);
        assertEquals(1, deque.size());
    }

    @Test
    public void addLastIncrementsSize() {
        deque.addLast(1);
        assertEquals(1, deque.size());
    }

    @Test
    public void addFrontRemoveLastSizeCheck() {
        deque.addFirst(1);
        int val = deque.removeLast();
        assertEquals(1, val);
        assertEquals(0, deque.size());
    }

    @Test
    public void addLastRemoveFrontSizeCheck() {
        deque.addLast(1);
        int val = deque.removeFirst();
        assertEquals(1, val);
        assertEquals(0, deque.size());
    }

    @Test
    public void testAddFirstRemoveFirst() {

        int maxSize = 25;
        List<Integer> result = new ArrayList<>();
        IntStream stream = IntStream.range(0, maxSize);
        for (int i : stream.toArray()
        ) {
            deque.addFirst(i);
        }
        assertEquals(maxSize, deque.size());
        for (int j : IntStream.range(0, maxSize).toArray()
        ) {
            result.add(deque.removeFirst());
        }
        assertEquals(0, deque.size());
        assertEquals((Integer) (maxSize - 1), result.get(0));
        assertEquals((Integer) (0), result.get(24));
    }

    @Test
    public void testAddLastRemoveLast() {
        int maxSize = 25;
        List<Integer> result = new ArrayList<>();
        IntStream stream = IntStream.range(0, maxSize);
        for (int i : stream.toArray()
        ) {
            deque.addLast(i);
        }
        assertEquals(maxSize, deque.size());
        for (int j : IntStream.range(0, maxSize).toArray()
        ) {
            result.add(deque.removeLast());
        }
        assertEquals(0, deque.size());
        assertEquals((Integer) (maxSize - 1), result.get(0));
        assertEquals((Integer) (0), result.get(maxSize - 1));
    }

    @Test
    public void testAddFirstRemoveLast() {

        int maxSize = 25;
        List<Integer> result = new ArrayList<>();
        IntStream stream = IntStream.range(0, maxSize);
        for (int i : stream.toArray()
        ) {
            deque.addFirst(i);
        }
        assertEquals(maxSize, deque.size());
        for (int j : IntStream.range(0, maxSize).toArray()
        ) {
            result.add(deque.removeLast());
        }
        assertEquals(0, deque.size());
        assertEquals((Integer) (maxSize - 1), result.get(maxSize - 1));
        assertEquals((Integer) (0), result.get(0));
    }

    @Test
    public void testAddLastRemoveFirst() {
        int maxSize = 25;
        List<Integer> result = new ArrayList<>();
        IntStream stream = IntStream.range(0, maxSize);
        for (int i : stream.toArray()
        ) {
            deque.addLast(i);
        }
        assertEquals(maxSize, deque.size());
        for (int j : IntStream.range(0, maxSize).toArray()
        ) {
            result.add(deque.removeFirst());
        }
        assertEquals(0, deque.size());
        assertEquals((Integer) (maxSize - 1), result.get(maxSize - 1));
        assertEquals((Integer) (0), result.get(0));
    }

    @Test
    public void FIFOQueueIteration(){
        int maxSize = 25;
        List<Integer> result = new ArrayList<>();
        IntStream stream = IntStream.range(0, maxSize);
        for (int i : stream.toArray()
        ) {
            deque.addLast(i);
        }
        for (Integer i: deque)
        {
            result.add(i);
        }
        assertEquals(maxSize, deque.size());
        assertEquals(maxSize, result.size());

    }

    @Test
    public void FIFOQueueMultipleIteration(){
        int maxSize = 1111;
        List<Integer> result = new ArrayList<>();
        IntStream stream = IntStream.range(0, maxSize);
        for (int i : stream.toArray()
        ) {
            deque.addLast(i);
        }
        for (Integer i: deque)
        {
            result.add(i);
        }
        for (Integer j: deque)
        {
            result.add(j);
        }
        assertEquals(maxSize, deque.size());
        assertEquals(maxSize*2, result.size());

    }
    @Test
    public void IterationOnEmptyQueue() {
        List<Integer> result = new ArrayList<>();
        for (Integer i: deque)
        {
            result.add(i);
        }
        assertEquals(0, result.size());
    }

    @Test(expected = NoSuchElementException.class)
    public void IterationOnEmptyQueue1() {
        deque.addFirst(1);
        deque.removeFirst();
        deque.addLast(3);
        deque.removeLast();
        deque.iterator().next();
    }


    @Test(expected = NoSuchElementException.class)
    public void nextIterationOnEmptyQueue() {
        deque.iterator().next();
    }

}
