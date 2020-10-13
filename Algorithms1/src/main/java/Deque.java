import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

public class Deque<Item> implements Iterable<Item> {
    private int count;
    private Node first, last;

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        int maxSize = 5;
        IntStream stream = IntStream.range(0, maxSize);
        StdOut.println("---------FIFO Queue-------------");
        for (int i : stream.toArray()
        ) {
            deque.addLast(i);
        }
        StdOut.printf("Size of queue = %d ", deque.size());
        StdOut.println("Queue iteration");
        for (Integer i : deque) {
            StdOut.println(i);
        }
        StdOut.println("Queue removal");
        while (!deque.isEmpty()) {
            StdOut.println(deque.removeFirst());
        }
        StdOut.println("----------LIFO Queue-------------");
        stream = IntStream.range(0, maxSize);
        for (int j : stream.toArray()
        ) {
            deque.addFirst(j);
        }
        StdOut.println("Queue iteration");
        for (Integer i : deque) {
            StdOut.println(i);
        }
        StdOut.println("Queue removal");
        while (!deque.isEmpty()) {
            StdOut.println(deque.removeLast());
        }


    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void addLast(Item item) {

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
        count++;
    }

    public Item removeFirst() {
        validateQueueNotEmpty();
        Item item = first.item;
        first = first.next;
        count--;
        if (isEmpty()) {
            last = null;
        }
        return item;
    }

    public void addFirst(Item item) {
        validateItemNotNull(item);
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        if (isEmpty()) {
            last = first;
        } else {
            oldFirst.prev = first;
        }
        first.next = oldFirst;
        count++;
    }

    public Item removeLast() {

        validateQueueNotEmpty();
        Node newLast = last.prev;
        Item item = last.item;
        last = newLast;
        count--;
        if (isEmpty()) {
            first = null;
        }
        return item;

    }

    public int size() {
        return count;
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

    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (current != null) {
                Item item = current.item;
                current = current.next;
                return item;
            }
            throw new NoSuchElementException("Queue empty");
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove items is not supported");
        }
    }
}