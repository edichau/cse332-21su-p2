package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.PriorityWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/PriorityWorkList.java
 * for method specifications.
 */
public class MinFourHeapComparable<E extends Comparable<E>> extends PriorityWorkList<E> {
    /* Do not change the name of this field; the tests rely on it to work correctly. */
    private E[] data;
    private int size;

    @SuppressWarnings("unchecked")
    public MinFourHeapComparable() {
        this.data = (E[]) new Comparable[20];
        this.size = 0;
    }

    @Override
    public boolean hasWork() {
        return this.size() > 0;
    }

    @Override
    public void add(E work) {
        // resizes the array if theres more data
        if (this.size == this.data.length) {
            resize();
        }

        // puts in the new work element and percolates it up to find its place
        this.data[this.size++] = work;
        this.percolateUp();
    }

    @Override
    public E peek() {
        if (!this.hasWork()) {
            throw new NoSuchElementException();
        }
        return this.data[0];
    }

    @Override
    public E next() {
        if (!this.hasWork()) {
            throw new NoSuchElementException();
        }

        E minElem = this.data[0];

        // puts the last element as the first, then percolates down to find its place
        this.data[0] = this.data[--this.size];
        percolateDown(0);

        return minElem;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        this.data = (E[]) new Comparable[20];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        E[] copy = (E[]) new Comparable[this.data.length * 2];
        if (this.size >= 0) System.arraycopy(this.data, 0, copy, 0, this.size);
        this.data = copy;
    }

    private void percolateUp()  {
        for (int i = size-1; i > 0; i--) {
            E child = data[i];
            E parent = data [(i-1)/4];

            // swaps parent and child if parent is larger than child
            if (child.compareTo(parent) < 0) {
                data[(i-1)/4] = child;
                data[i] = parent;
            }
        }
    }

    private void percolateDown(int index) {
        int minChildIndex = index * 4 + 1;

        if (minChildIndex >= this.size) {
            return;
        }

        // finds the smallest child of all the children of a parent
        int childIndex = index * 4 + 2;
        while (childIndex <= index * 4 + 4 && childIndex < this.size) {
            if (this.data[childIndex].compareTo(this.data[minChildIndex]) < 0) {
                minChildIndex = childIndex;
            }
            childIndex++;
        }

        // swaps the parent and the smallest child when the parent is greater
        if (this.data[index].compareTo(this.data[minChildIndex]) > 0) {
            E tmp = this.data[index];
            this.data[index] = this.data[minChildIndex];
            this.data[minChildIndex] = tmp;
            percolateDown(minChildIndex);
        }
    }
}
