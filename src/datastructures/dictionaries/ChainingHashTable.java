package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.datastructures.trees.BinarySearchTree;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.Dictionary;
import cse332.interfaces.misc.SimpleIterator;
import cse332.interfaces.worklists.WorkList;
import datastructures.worklists.ArrayStack;
import datastructures.worklists.ListFIFOQueue;

import java.util.Iterator;
import java.util.function.Supplier;

/**
 * 1. You must implement a generic chaining hashtable. You may not
 *    restrict the size of the input domain (i.e., it must accept 
 *    any key) or the number of inputs (i.e., it must grow as necessary).
 * 3. Your HashTable should rehash as appropriate (use load factor as
 *    shown in class!). 
 * 5. HashTable should be able to resize its capacity to prime numbers for more 
 *    than 200,000 elements. After more than 200,000 elements, it should 
 *    continue to resize using some other mechanism.
 * 6. We suggest you hard code some prime numbers. You can use this
 *    list: http://primes.utm.edu/lists/small/100000.txt 
 *    NOTE: Do NOT copy the whole list!
 * 7. When implementing your iterator, you should NOT copy every item to another
 *    dictionary/list and return that dictionary/list's iterator. 

 */
public class ChainingHashTable<K, V> extends DeletelessDictionary<K, V> {
    private Supplier<Dictionary<K, V>> newChain;

    private ListFIFOQueue<Integer> primeList;
    private int numItems;
    private Dictionary[] table;


    public ChainingHashTable(Supplier<Dictionary<K, V>> newChain) {
        this.newChain = newChain;
        this.primeList = new ListFIFOQueue<>();
        initializePrimeList();
        numItems = 0;
        this.table = new Dictionary[11];
    }

    @SuppressWarnings("unchecked")
    @Override
    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        if (needResize()) {
            resize();
        }

        int index = key.hashCode();
        if (table[index] == null) {
            Dictionary<K, V> dict = newChain.get();
            dict.insert(key, value);
            table[index] = dict;
            numItems++;
            return null;
        } else {
            V oldVal = (V) table[index].insert(key, value);
            if (oldVal == null) {
                numItems++;
            }
            return oldVal;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public V find(K key) {
        int index = key.hashCode();
        if (table[index] == null) {
            return null;
        } else {
            return (V) table[index].find(key);
        }
    }


    @Override
    public Iterator<Item<K, V>> iterator() { return new CHTIterator(); }

    @SuppressWarnings("unchecked")
    private class CHTIterator extends SimpleIterator<Item<K, V>> {
        private ListFIFOQueue<Dictionary<K,V>> dictionaries;
        Iterator<Item<K,V>> dictItr;


        public CHTIterator() {
            dictionaries = new ListFIFOQueue<>();
            //Create a list of non-null indexes
            for(int i=0; i < table.length; i++) {
                if(table[i] != null) {
                    dictionaries.add(table[i]);
                }
            }
            if(dictionaries.hasWork()) {
                dictItr = dictionaries.next().iterator();
            }
        }

        @Override
        public boolean hasNext() {
            return dictItr.hasNext();
        }

        @Override
        public Item<K, V> next() {
            if (!dictItr.hasNext()) {
                dictItr = dictionaries.next().iterator();
            }
            return dictItr.next();
        }
    }


    //initialize hard-coded prime list
    private void initializePrimeList() {
        primeList.add(41);
        primeList.add(83);
        primeList.add(227);
        primeList.add(509);
        primeList.add(1031);
        primeList.add(2213);
        primeList.add(5197);
        primeList.add(10009);
        primeList.add(20161);
        primeList.add(40013);
        primeList.add(80147);
        primeList.add(160159);
    }

    private boolean needResize() {
        int size = table.length;
        double lambda = (double) (numItems + 1) / size;

        return lambda > 2;
    }

    private int getNewSize() {
        if(primeList.hasWork()) {
            return primeList.next();
        } else {
            return (table.length * 2) + 1;
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Dictionary<K,V>[] newTable = new Dictionary[getNewSize()];
        for (Dictionary<K,V> index : table) { //Iterate through hashTable to rehash
            if (index != null) { //Index is nonNull, rehash every key at index
                for (Item<K, V> item : index) {
                    int newIndex = item.key.hashCode();
                    if (newTable[newIndex] == null) {
                        Dictionary<K, V> dict = newChain.get();
                        dict.insert(item.key, item.value);
                        newTable[newIndex] = dict;
                    } else {
                        newTable[newIndex].insert(item.key, item.value);
                    }
                }
            }
        }
        this.table = newTable;
    }
}
