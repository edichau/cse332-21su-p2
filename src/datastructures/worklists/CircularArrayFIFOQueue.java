package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FixedSizeFIFOWorkList;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/FixedSizeFIFOWorkList.java
 * for method specifications.
 */
public class CircularArrayFIFOQueue<E extends Comparable<E>> extends FixedSizeFIFOWorkList<E> {
    private int start;
    private int size;
    private E[] elements;

    @SuppressWarnings("unchecked")
    public CircularArrayFIFOQueue(int capacity) {
        super(capacity);
        // casts array to generic, new Comparable[N] creates an array with N elements which can hold Comparable or any subtype of Comparable.
        // No Comparable is created, just an array.
        this.elements = (E[]) new Comparable[capacity];
        this.size = 0;
        this.start = 0;
    }

    @Override
    public void add(E work) {
        if (this.isFull()){
            throw new IllegalStateException();
        } else {
            elements[(start + size) % elements.length] = work;
            size++;
        }
    }

    @Override
    public E peek() {
        return peek(0);
    }
    
    @Override
    public E peek(int i) {
        if (!hasWork()) {
            throw new NoSuchElementException();
        }
        if (i < 0 || i >= size()) {
            throw new IndexOutOfBoundsException();
        }
        return this.elements[(start + i) % elements.length];
    }
    
    @Override
    public E next() {
        if (!this.hasWork()) {
            throw new NoSuchElementException();
        }
        int prevStart = start;
        start = (prevStart + 1) % elements.length;
        size--;
        return this.elements[prevStart];
    }
    
    @Override
    public void update(int i, E value) {
        if (!hasWork()) {
            throw new NoSuchElementException();
        }
        if (i < 0 || i >= size()) {
            throw new IndexOutOfBoundsException();
        }
        elements[(start + i) % elements.length] = value;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public void clear() {
        this.size = 0;
        this.start = 0;
    }

    @Override
    public int compareTo(FixedSizeFIFOWorkList<E> other) {
        if (!(other instanceof FixedSizeFIFOWorkList<?>)) {
            throw new IllegalArgumentException();
        }
        FixedSizeFIFOWorkList<E> input = (FixedSizeFIFOWorkList<E>) other;
        Iterator<E> itr = this.iterator();
        Iterator<E> inputItr = input.iterator();

        //we start by comparing each element. If they differ we return E's compareTo method
        while (itr.hasNext() && inputItr.hasNext()) {
            E next = itr.next();
            E compare = inputItr.next();
            if(!next.equals(compare)) {
                return next.compareTo(compare);
            }
        }
        //if each element is equal we then check if they have the same size
        if(this.size() != input.size()) {
            return this.size() - input.size();
        }
        //if they have the same elements and are the same size, we have determined they are the same
        return 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        else if (!(obj instanceof FixedSizeFIFOWorkList<?>)) {
            return false;
        }
        else {
            FixedSizeFIFOWorkList<?> input = (FixedSizeFIFOWorkList<?>) obj;
            if(this.size != input.size()) {
                return false;
            }
            Iterator<?> inputItr = ((FixedSizeFIFOWorkList<?>) obj).iterator();
            Iterator<E> itr = this.iterator();
            while (itr.hasNext() && inputItr.hasNext()) {
                if(!itr.next().equals(inputItr.next())) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
