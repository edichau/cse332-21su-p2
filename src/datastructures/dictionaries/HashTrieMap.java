package datastructures.dictionaries;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map.Entry;

import cse332.datastructures.containers.Item;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.SimpleIterator;
import cse332.types.BString;
import cse332.interfaces.trie.TrieMap;


/**
 * See cse332/interfaces/trie/TrieMap.java
 * and cse332/interfaces/misc/Dictionary.java
 * for method specifications.
 */
public class HashTrieMap<A extends Comparable<A>, K extends BString<A>, V> extends TrieMap<A, K, V> {


    public class HashTrieNode extends TrieNode<DeletelessDictionary<A, HashTrieNode>, HashTrieNode> {
        public HashTrieNode() {
            this.pointers = new ChainingHashTable<>(MoveToFrontList::new);
            this.value = null;
        }

        public HashTrieNode(V value) {
            this.pointers = new ChainingHashTable<>(MoveToFrontList::new);
            this.value = value;
        }

        @Override
        public Iterator<Entry<A,HashTrieMap<A,K,V>.HashTrieNode>> iterator() {
            return new HTNIterator();
        }

        private class HTNIterator extends SimpleIterator<Entry<A, HashTrieMap<A, K, V>.HashTrieNode>>{
            private Iterator<Item<A, HashTrieMap<A,K,V>.HashTrieNode>> pointerItr;

            public HTNIterator() {
                pointerItr = HashTrieNode.this.pointers.iterator();
            }

            @Override
            public boolean hasNext() {
               return pointerItr.hasNext();
            }

            @Override
            public Entry<A, HashTrieMap<A,K,V>.HashTrieNode> next() {
                Item<A, HashTrieMap<A,K,V>.HashTrieNode> next = pointerItr.next();
                AbstractMap.SimpleEntry<A,HashTrieMap<A,K,V>.HashTrieNode> val = new AbstractMap.SimpleEntry<>(next.key, next.value);
                return val;
            }
        }

    }

    public HashTrieMap(Class<K> KClass) {
        super(KClass);
        this.root = new HashTrieNode();
    }

    @Override
    @SuppressWarnings("unchecked")
    public V insert(K key, V value) {
        if(key==null || value==null) { throw new IllegalArgumentException(); }

        HashTrieNode currNode = (HashTrieNode) this.root;
        Iterator<A> keyItr = key.iterator();
        if(!keyItr.hasNext()) {
            V rootVal = currNode.value;
            currNode.value = value;
            return rootVal;
        }
        while(keyItr.hasNext()) {
            A singleChar = keyItr.next();
            if(!keyItr.hasNext()) { //last character in key so we want to put or replace the value in node
                if(currNode.pointers.find(singleChar) == null) { //current node doesnt contain char so we add with value
                    currNode.pointers.insert(singleChar, new HashTrieNode(value));
                    size++;
                    return null;
                } else { //replace value and return the previous value
                    V prevKey = currNode.pointers.find(singleChar).value;
                    currNode.pointers.insert(singleChar, new HashTrieNode(value));
                    return prevKey;
                }
            } else { //middle of key
                if(currNode.pointers.find(singleChar) == null) { //if there is no pointer to current char we add it
                    currNode.pointers.insert(singleChar, new HashTrieNode());
                }
                currNode = currNode.pointers.find(singleChar);
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V find(K key) {
        if(key == null) { throw new IllegalArgumentException(); }

        HashTrieNode currNode = (HashTrieNode) this.root;
        Iterator<A> keyItr = key.iterator();
        V val = null;
        if(!keyItr.hasNext()) { //key is empty string "" so we return the value of root
            val = currNode.value;
        }
        while(keyItr.hasNext()) {
            A singleChar = keyItr.next();
            if(currNode.pointers.find(singleChar) == null) { return null; } //partial key prefix not in map
            if(!keyItr.hasNext()) { //end of key
                val = currNode.pointers.find(singleChar).value;
            }
            currNode = currNode.pointers.find(singleChar);
        }
        return val;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean findPrefix(K key) {
        if(key == null) { throw new IllegalArgumentException(); }

        HashTrieNode currNode = (HashTrieNode) this.root;
        for (A singleChar : key) {
            if (currNode.pointers.find(singleChar) == null) {
                return false;
            }
            currNode = currNode.pointers.find(singleChar);
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(K key) {
        throw new UnsupportedOperationException();
    }


    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        ((HashTrieNode)this.root).pointers.clear();
    }


}