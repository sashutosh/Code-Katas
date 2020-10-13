import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class RandomizedQueueTest {
    RandomizedQueue<Integer> rq;
    @Before
    public void setup(){
        rq = new RandomizedQueue<>();
    }

    @Test
    public void dequeSingleItem(){
        rq.enqueue(1);
        int result = rq.dequeue();
        Assert.assertEquals(1,result);
    }

    @Test(expected = NoSuchElementException.class)
    public void dequeEmptyThrowsException(){
        int result = rq.dequeue();
    }

    @Test
    public void testAddRemoveMultipleIteration() {

        for(int index=11;index<77;index=index+3) {
            int maxSize = index;
            List<Integer> result = new ArrayList<>();
            IntStream stream = IntStream.range(0, maxSize);
            for (int i : stream.toArray()
            ) {
                rq.enqueue(i);
            }
            assertEquals(maxSize, rq.size());
            for (int j : IntStream.range(0, maxSize).toArray()
            ) {
                result.add(rq.dequeue());
            }
            result.sort(Comparator.naturalOrder());
            assertEquals(0, rq.size());
            assertEquals(0, (int) result.get(0));
            assertEquals(maxSize - 1, (int) result.get(maxSize - 1));
        }
    }

    @Test
    public void testEnqueueAfterEmptying(){
        int maxSize = 5;
        List<Integer> result = new ArrayList<>();
        IntStream stream = IntStream.range(0, maxSize);
        for (int i : stream.toArray()
        ) {
            rq.enqueue(i);
        }
        assertEquals(maxSize, rq.size());
        for (int j : IntStream.range(0, maxSize).toArray()
        ) {
            result.add(rq.dequeue());
        }
        rq.enqueue(1);
        assertEquals(1,rq.size());
    }

    @Test
    public void testSampleGetsAllElements() {
        int maxSize = 5;
        List<Integer> result = new ArrayList<>();
        IntStream stream = IntStream.range(0, maxSize);
        for (int i : stream.toArray()
        ) {
            rq.enqueue(i);
        }
        assertEquals(maxSize, rq.size());
        for (int j : IntStream.range(0, maxSize).toArray()
        ) {
            result.add(rq.sample());
        }
        assertEquals(maxSize, rq.size());
    }

    @Test
    public void testRQIteration() {
        int maxSize = 1023;
        List<Integer> result = new ArrayList<>();
        IntStream stream = IntStream.range(0, maxSize);
        for (int i : stream.toArray()
        ) {
            rq.enqueue(i);
        }
        assertEquals(maxSize, rq.size());
        for (Integer item: rq) {
            result.add(item);
        }
        result.sort(Comparator.naturalOrder());
        assertEquals(maxSize, rq.size());
        assertEquals((Integer) (maxSize - 1), result.get(maxSize - 1));
        assertEquals((Integer) (0), result.get(0));
    }

}
