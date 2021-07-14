package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.datastructures.trees.BinarySearchTree;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.Dictionary;
import cse332.interfaces.misc.SimpleIterator;
import cse332.interfaces.worklists.WorkList;
import datastructures.worklists.ArrayStack;
import datastructures.worklists.CircularArrayFIFOQueue;
import datastructures.worklists.ListFIFOQueue;

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
    private Node root;

    private class Node extends Item<K,V>  {
        public Node next;

        public Node(K key, V value) {
            super(key, value);
            next = null;
        }

        public Node(K key, V value, Node next) {
            super(key, value);
            this.next = next;
        }
    }

    public MoveToFrontList() {
        super();
        this.root = null;
        this.size = 0;
    }

    @Override
    public V insert(K key, V value) {
        if(key == null || value == null) {
            throw new IllegalArgumentException();
        }

        if(root == null) {
            //List is empty so we insert at root
            root = new Node(key, value, null);
            size++;
            return null;
        } else {
            //List is not empty so we search if key already exists
            V oldVal = find(key); //returns old val if previously in list. null ow.
            if (oldVal != null) {
                //key already exists and is now at root
                root.value = value;
            } else {
                //key doesnt exist. Add a new <key,value> that links to previous root
                Node newNode = new Node(key, value);
                newNode.next = root;
                root= newNode;
                size++;
            }
            return oldVal;
        }
    }

    @Override
    public V find(K key) {
        if(key == null) {
            throw new IllegalArgumentException();
        }
        Node prev = null;
        Node curr = this.root;

        //root is null so list is empty
        if(curr == null) {
            return null;
        }

        //else we iterate through list til key is found or end of list
        while(curr != null && !curr.key.equals(key)) {
            prev = curr;
            curr = curr.next;
        }
        if(curr == null) {
            //value doesnt exist
            return null;
        } else {
            //value is in list
            if(prev != null) {
                //value is in middle of list
                prev.next = curr.next;
                curr.next = root;
                root = curr;
            } else {
                //value is already at the root
                return root.value;
            }
            return curr.value;
        }
    }

    @Override
    public Iterator<Item<K, V>> iterator() { return new NodeIterator(); }


    private class NodeIterator extends SimpleIterator<Item<K, V>> {
        private Node current;

        public NodeIterator() {
            this.current = MoveToFrontList.this.root;
        }

        @Override
        public boolean hasNext() {
            return this.current != null;
        }

        @Override
        public Item<K, V> next() {
            Item<K,V> retVal = new Item<>(this.current.key, this.current.value);
            this.current= this.current.next;
            return retVal;
        }
    }

}
