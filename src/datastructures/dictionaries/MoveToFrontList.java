package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.Dictionary;
import datastructures.worklists.ArrayStack;

import java.util.Iterator;

/**
 * 1. The list is typically not sorted.
 * 2. Add new items to the front of the list.
 * 3. Whenever find is called on an item, move it to the front of the 
 *    list. This means you remove the node from its current position 
 *    and make it the first node in the list.
 * 4. You need to implement an iterator. The iterator SHOULD NOT move
 *    elements to the front.  The iterator should return elements in
 *    the order they are stored in the list, starting with the first
 *    element in the list. When implementing your iterator, you should 
 *    NOT copy every item to another dictionary/list and return that 
 *    dictionary/list's iterator. 
 */
public class MoveToFrontList<K, V> extends DeletelessDictionary<K, V> {
    private ArrayStack<Item<K, V>> stack;

    public MoveToFrontList() {
        super();
        this.stack = new ArrayStack<>();
    }

    @Override
    public V insert(K key, V value) {
        if(key == null || value == null) {
            throw new IllegalArgumentException();
        }

        V oldVal = find(key); //key is now at top of stack from searching. If key doesn't already exist value is null
        if(oldVal != null) {
            stack.peek().value = value;
        } else {
            stack.add(new Item<>(key,value));
        }
        return oldVal;
    }

    @Override
    public V find(K key) {
        if(key == null) {
            throw new IllegalArgumentException();
        }
        ArrayStack<Item<K,V>> temp = new ArrayStack<>();
        boolean foundItem = false;
        Item<K, V> value = new Item<>(key, null);

        //iterate through stack pushing values to temp to preserve order
        while(stack.hasWork() && !foundItem) {
            Item<K, V> next = stack.next();
            if(next.key.equals(key)) {
                foundItem = true;
                value = next;
            } else {
                temp.add(next);
            }
        }
        //push values back onto stack in the order they were removed starting with most recently removed
        while(temp.hasWork()) {
            stack.add(temp.next());
        }
        //if in list, we put key to front with new value
        if(value.value != null) {
            stack.add(value);
        }
        return value.value;
    }

    @Override
    public Iterator<Item<K, V>> iterator() {
        return null;
    }
}
