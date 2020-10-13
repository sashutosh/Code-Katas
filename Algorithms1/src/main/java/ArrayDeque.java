import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDeque<Item> implements Iterable<Item> {

    private final int SIZE_MULT = 4;
    int itemCount = 0;
    private Item[] items;
    private int frontIndex;
    private int endIndex;

    // construct an empty deque
    public ArrayDeque() {
        items = (Item[]) new Object[SIZE_MULT];
        frontIndex = -1;
        endIndex = -1;
    }

    private boolean isFull() {
        return itemCount == items.length;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return itemCount == 0;
    }

    // return the number of items on the deque
    public int size() {
        //return abs(endIndex - frontIndex);
        return itemCount;
    }

    // add the item to the front
    public void addFirst(Item item) {
        validate(item);
        if (isFull()) {
            resize(2 * items.length);
        }
        items[++frontIndex] = item;
        itemCount++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (!isEmpty()) {
            Item item = items[frontIndex];
            items[frontIndex] = null;
            frontIndex--;
            itemCount--;
            if (size() < items.length / 4 && items.length > SIZE_MULT) {
                resize(items.length / 2);
            }

            return item;
        } else {
            throw new NoSuchElementException();
        }
    }

    private void validate(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        validate(item);
        if (isFull()) {
            resize(2 * items.length);
        }
        if (endIndex < 0) {
            endIndex = items.length - 1;
        }
        items[endIndex] = item;
        itemCount++;
        if (size() == 1) {
            frontIndex = endIndex;
        }
        endIndex--;
    }

    // remove and return the item from the back
    public Item removeLast() {
        int indexToFetch;
        if (!isEmpty()) {
            endIndex++;
            if (endIndex < 0) {
                indexToFetch = endIndex + items.length;
            } else {
                indexToFetch = endIndex;
            }
            Item item = items[indexToFetch];
            items[indexToFetch] = null;
            itemCount--;
            if (size() < items.length / 4 && items.length > SIZE_MULT) {
                resize(items.length / 2);
            }
            return item;
        } else {
            throw new NoSuchElementException();
        }
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        int totalElements = size();//items.length > capacity? capacity:items.length;
        for (int i = 0; i < totalElements; i++) {
            if (frontIndex == -1) {
                frontIndex = items.length - 1;
            }
            copy[((capacity / 2) - i - 1)] = items[frontIndex--];

        }
        endIndex = -1;
        frontIndex = capacity / 2 - 1;
        items = copy;
    }


    private class DequeIterator implements Iterator<Item> {

        @Override
        public boolean hasNext() {
            return !isEmpty();
        }

        @Override
        public Item next() {
            if (isEmpty()) {
                throw new NoSuchElementException("Deque empty");
            }
            return removeFirst();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }
}