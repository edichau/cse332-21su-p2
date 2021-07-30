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
import java.util.Map;
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
public class ChainingHashTable<K, V> extends DeletelessDictionary<K,V> {
    private Supplier<Dictionary<K, V>> newChain;

    private ListFIFOQueue<Integer> primeList;
    private Dictionary<K,V>[] table;


    public ChainingHashTable(Supplier<Dictionary<K, V>> newChain) {
        super();
        this.newChain = newChain;
        this.primeList = new ListFIFOQueue<>();
        initializePrimeList();
        this.table = new Dictionary[11];
    }

    @SuppressWarnings("unchecked")
    @Override
    public V insert(K key, V value) {
        if (key == null ) { throw new IllegalArgumentException(); }
        if (value == null) {throw new IllegalArgumentException(); }
        if (needResize()) {
            resize();
        }

        int index = Math.abs(key.hashCode()) % table.length;
        if (table[index] == null) {
            Dictionary<K, V> dict = newChain.get();
            dict.insert(key, value);
            table[index] = dict;
            this.size++;
            return null;
        } else {
            V oldVal = (V) table[index].insert(key, value);
            if (oldVal == null) {
                this.size++;
            }
            return oldVal;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public V find(K key) {
        if(key == null) { return null; }
        int index = Math.abs(key.hashCode() % table.length);
        if (table[index] == null) {
            return null;
        } else {
            return (V) table[index].find(key);
        }
    }


    @Override
    public Iterator<Item<K, V>> iterator() { return new CHTIterator(); }


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
                dictItr= dictionaries.next().iterator();
            }
        }

        @Override
        public boolean hasNext() {
            if(dictItr == null) {
                return false;
            } else {
                return dictItr.hasNext() || dictionaries.hasWork();
            }
        }

        @Override
        public Item<K, V> next() {
            if (!dictItr.hasNext()) {
                if(dictionaries.hasWork()) {
                    dictItr = dictionaries.next().iterator();
                }else {
                    return null;
                }
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
        int tableSize = table.length;
        double lambda = (double) (this.size + 1) / tableSize;

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
        int newSize = getNewSize();
        Dictionary<K,V>[] newTable = new Dictionary[newSize];
        for (Dictionary<K,V> index : table) { //Iterate through hashTable to rehash
            if (index != null) { //Index is nonNull, rehash every key at index
                for (Item<K, V> item : index) {
                    int newIndex = Math.abs(item.key.hashCode()) % newSize;
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
