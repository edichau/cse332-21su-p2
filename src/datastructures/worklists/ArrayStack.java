package datastructures.worklists;

import cse332.interfaces.worklists.LIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/LIFOWorkList.java
 * for method specifications.
 */
public class ArrayStack<E> extends LIFOWorkList<E> {
    int size;
    E[] workList;

    public ArrayStack() {
        this.size = 0;
        workList= (E[])new Object[10];
    }

    @Override
    public void add(E work) {
        if(size == workList.length) { //out of space in workList
            //create new array double the size and copy values of current workList into newWorkList
            //since we double the size every time we run out of space, our amortized time to add ends up being O(1)
            int newArrayLength= workList.length * 2;
            E[] newWorkList = (E[]) new Object[newArrayLength];
            for(int i = 0; i < size; i++) {
                newWorkList[i] = workList[i];
            }
            newWorkList[size] = work;
            this.workList = newWorkList;
        } else {
            workList[size] = work;
        }
        size++;
    }

    @Override
    public E peek() {
        if (!hasWork()) {
            throw new NoSuchElementException();
        }
        return workList[size - 1];
    }

    @Override
    public E next() {
        if (!hasWork()) {
            throw new NoSuchElementException();
        }
        size--;
        return workList[size];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        this.workList= (E[])new Object[10];
    }
}
