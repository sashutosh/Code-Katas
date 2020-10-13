import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int size = 0;
    private Node first, last;

    // construct an empty randomized queue
    public RandomizedQueue() {
        first=last=null;
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        for (int i = 0; i < 5; i++) {
            rq.enqueue(i);
        }
        StdOut.printf("Queue size %d",rq.size());
        StdOut.printf("Random sample %d",rq.sample());
        for (int i = 0; i < 5; i++) {
            rq.dequeue();
        }
        StdOut.printf("Queue size after dequeue %d",rq.size());
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        validateItemNotNull(item);
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.prev = oldLast;
        if (isEmpty()) {
            first = last;
        } else {
            oldLast.next = last;
        }
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        validateQueueNotEmpty();
        int randomIndex = StdRandom.uniform(0, size);
        return getAtIndex(randomIndex,true);
    }

    private Item getAtIndex(int randomIndex, boolean delete) {
        Node current = first;
        Item item;
        int currentIndex = 0;
        while (currentIndex != randomIndex) {
            current = current.next;
            currentIndex++;
        }
        item = current.item;
        if(delete) {
            if (current == first) {
                first = current.next;
            } else {
                current.prev.next = current.next;
            }
            if (current == last) {
                last = current.prev;
            } else {

                current.next.prev = current.prev;
            }

            size--;
        }
        return item;
    }

    // return a random item (but do not remove it)

    public Item sample() {
        validateQueueNotEmpty();
        int randomIndex = StdRandom.uniform(0, size);
        return getAtIndex(randomIndex,false);
    }
    // return an independent iterator over items in random order

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }
    private void validateQueueNotEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
    }

    private void validateItemNotNull(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Invalid item to insert");
        }
    }

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        int[] indexIterated =  new int[size];
        int iteratedCount=0;

        @Override
        public boolean hasNext() {
            return iteratedCount !=size;
        }

        @Override
        public Item next() {
            if(iteratedCount<size) {
                int randomIndex = StdRandom.uniform(0, size);
                while (isVisited(randomIndex)) {
                    randomIndex = StdRandom.uniform(0, size);
                }
                indexIterated[iteratedCount++] = randomIndex;
                return getAtIndex(randomIndex, false);
            }
            else
                throw new NoSuchElementException("No more items in queue");
        }

        private boolean isVisited(int randomIndex) {
            for (int i=0;i<iteratedCount;i++) {
                if(indexIterated[i]==randomIndex) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void remove() {
            throw new NoSuchElementException("Remove items is not supported");
        }
    }
}
