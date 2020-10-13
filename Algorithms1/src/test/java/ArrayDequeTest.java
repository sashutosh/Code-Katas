import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class ArrayDequeTest {

    @Test
    public void testQueueSizeWhenEmpty() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        assertEquals(0, deque.size());
    }

    @Test
    public void testAddFirstRemoveLast() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(1);
        int value = deque.removeLast();
        assertEquals(1,value );
        assertEquals(0, deque.size());
    }

    @Test
    public void testAddLastRemoveFirst() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addLast(1);
        int value = deque.removeFirst();
        assertEquals(1,value );
        assertEquals(0, deque.size());
    }

    @Test(expected = NoSuchElementException.class)
    public void testUnderFlowThrowsExceptionFromFrontWhenEmpty() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.removeFirst();
    }

    @Test(expected = NoSuchElementException.class)
    public void testUnderFlowThrowsExceptionFromEndWhenEmpty() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.removeLast();
    }

    @Test
    public void testQueueEmptyAfterAddRemoveFromLast() {

        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);
        deque.removeLast();
        deque.removeLast();
        deque.removeLast();
        assertEquals(0, deque.size());
    }

    @Test
    public void testQueueOrderAfterAddRemoveFromLast() {

        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);
        int item =deque.removeLast();
        assertEquals(item,3);
        deque.removeLast();
        item =deque.removeLast();
        assertEquals(1, item);
    }

    @Test
    public void testQueueOrderAfterAddRemoveFromFront() {

        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);
        int item =deque.removeFirst();
        assertEquals(item,3);
        deque.removeFirst();
        item =deque.removeFirst();
        assertEquals(1, item);
    }


    @Test
    public void testQueueEmptyAfterAddRemoveFromFront() {

        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);
        deque.removeFirst();
        deque.removeFirst();
        deque.removeFirst();
        assertEquals(0, deque.size());
    }

    @Test
    public void testQueueSizeAfterAddFromLast() {

        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);
        assertEquals(3, deque.size());
    }

    @Test
    public void testQueueSizeAfterAddFromFront() {

        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);
        assertEquals(3, deque.size());
    }

    @Test
    public void testQueueSizeAfterMixOperations() {

        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(1);
        deque.removeLast();
        assertEquals(0, deque.size());
    }

    @Test
    public void testQueueFullAfterMixAdds() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(1);
        deque.addLast(2);
        deque.addFirst(1);
        deque.addLast(2);
        assertEquals(4, deque.size());
    }

    @Test
    public void testQueueFullAfterMixRemove() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(1);
        deque.addLast(2);
        deque.addFirst(3);
        deque.addLast(4);
        deque.removeLast();
        deque.removeLast();
        deque.removeFirst();
        int item =deque.removeFirst();
        assertEquals(1,item);
        assertEquals(0, deque.size());
    }

    @Test
    public void testQueueAdMoreThanInitialSize() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();

        int maxSize = 25;
        IntStream stream = IntStream.rangeClosed(0,maxSize);
        for (int i : stream.toArray()
                ) {
            deque.addFirst(i);
        }
        assertEquals(maxSize+1, deque.size());
    }

    @Test
    public void testQueueAddAndRemoveWithResize() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();

        int maxSize = 1024;
        IntStream stream = IntStream.generate(new Random()::nextInt).limit(maxSize);
        for (int i : stream.toArray()
        ) {
            deque.addFirst(i);
        }
        //IntStream stream1 = IntStream.generate(new Random()::nextInt).limit(maxSize);
        for (int i=0; i< maxSize ;i++
        ) {
            deque.removeLast();
        }

        assertEquals(0, deque.size());
    }
    @Test
    public void testQueueIterator() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();

        int maxSize = 1000;
        IntStream stream = IntStream.rangeClosed(0,maxSize);
        for (int i : stream.toArray()
        ) {
            deque.addLast(i);
        }
        for (int i :deque
                ) {
            System.out.println(i);
        }

        assertEquals(0, deque.size());
    }

    @Test
    public void testAddLastRemoveLast() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();

        int maxSize = 25;
        List<Integer> result = new ArrayList<>();
        IntStream stream = IntStream.range(0,maxSize);
        for (int i : stream.toArray()
        ) {
            deque.addLast(i);
        }
        assertEquals(maxSize, deque.size());
        for (int j : IntStream.range(0,maxSize).toArray()
        ) {
            result.add(deque.removeLast());
        }
        assertEquals(0, deque.size());
        assertEquals((Integer) (maxSize - 1),result.get(0));
        assertEquals((Integer) (0),result.get(24));
    }


}

