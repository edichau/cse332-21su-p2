package datastructures.dictionaries;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cse332.exceptions.NotYetImplementedException;
import cse332.types.BString;
import cse332.interfaces.trie.TrieMap;

/**
 * See cse332/interfaces/trie/TrieMap.java
 * and cse332/interfaces/misc/Dictionary.java
 * for method specifications.
 */
public class HashTrieMap<A extends Comparable<A>, K extends BString<A>, V> extends TrieMap<A, K, V> {
    public class HashTrieNode extends TrieNode<Map<A, HashTrieNode>, HashTrieNode> {
        public HashTrieNode() {
            this(null);
        }

        public HashTrieNode(V value) {
            this.pointers = new HashMap<A, HashTrieNode>();
            this.value = value;
        }

        @Override
        public Iterator<Entry<A, HashTrieMap<A, K, V>.HashTrieNode>> iterator() {
            return pointers.entrySet().iterator();
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
        while(keyItr.hasNext()) {
            A singleChar = keyItr.next();
            if(!keyItr.hasNext()) { //last character in key so we want to put or replace the value in node
                if(!currNode.pointers.containsKey(singleChar)) { //current node doesnt contain char so we add with value
                    currNode.pointers.put(singleChar, new HashTrieNode(value));
                    size++;
                    return null;
                } else { //replace value and return the previous value
                    V prevKey = currNode.pointers.get(singleChar).value;
                    currNode.pointers.get(singleChar).value = value;
                    return prevKey;
                }
            } else { //middle of key
                if(!currNode.pointers.containsKey(singleChar)) { //if there is no pointer to current char we add it
                    currNode.pointers.put(singleChar, new HashTrieNode());
                }
                currNode = currNode.pointers.get(singleChar);
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
            if(!currNode.pointers.containsKey(singleChar)) { return null; } //partial key prefix not in map
            if(!keyItr.hasNext()) { //end of key
                val = currNode.pointers.get(singleChar).value;
            }
            currNode = currNode.pointers.get(singleChar);
        }
        return val;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean findPrefix(K key) {
        if(key == null) { throw new IllegalArgumentException(); }

        HashTrieNode currNode = (HashTrieNode) this.root;
        for (A singleChar : key) {
            if (!currNode.pointers.containsKey(singleChar)) {
                return false;
            }
            currNode = currNode.pointers.get(singleChar);
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if(this.root == null) {
            return;
        }

        HashTrieNode currNode = (HashTrieNode) this.root;

        Iterator<A> keyItr = key.iterator();

        HashTrieNode removeNode = currNode; //remove char from this node
        A nodeCharRemove = null; //remove this char from pointers

        while(keyItr.hasNext()) {
            A nextChar = keyItr.next();
            if(nodeCharRemove == null) {
                nodeCharRemove = nextChar;
            }
            if(currNode.pointers.size() == 0) {
                return;
            } else if(currNode.pointers.containsKey(nextChar)) {
                if(currNode.value != null || (currNode.pointers.size() -1) > 0) {
                    removeNode = currNode;
                    nodeCharRemove = nextChar;
                }
            } else {
                return;
            }
            currNode = currNode.pointers.get(nextChar);
        }

        if(currNode.pointers.size() > 0) {
            if(currNode != root) {
                currNode.value = null;
            }
        } else {
            removeNode.pointers.remove(nodeCharRemove);
        }
        size--;
    }




    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        ((HashTrieNode)this.root).pointers.clear();
    }
}