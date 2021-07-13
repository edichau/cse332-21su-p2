package datastructures.dictionaries;

import cse332.interfaces.trie.TrieMap;
import cse332.types.BString;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
        public Iterator<Entry<A, HashTrieNode>> iterator() {
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
        HashTrieNode currNode = (HashTrieNode) this.root;
        Iterator<A> keyItr = key.iterator();
        while(keyItr.hasNext()) {
            A singleChar = keyItr.next();
            if(!keyItr.hasNext()) { //last character in key so we want to put or replace the value in node
                if(!currNode.pointers.containsKey(singleChar)) { //current node doesnt contain char so we add with value
                    currNode.pointers.put(singleChar, new HashTrieNode(value));
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

        HashTrieNode currNode = (HashTrieNode) this.root;
        Iterator<A> keyItr = key.iterator();

        HashTrieNode removeNode = currNode; //remove char from this node
        A nodeCharRemove = null; //remove this char from pointers
        A currChar = null;

        if (keyItr.hasNext()) { //initialize removeNode and removeChar to first letter else key not in Trie
            currChar = keyItr.next();
            nodeCharRemove = currChar;
            if (!currNode.pointers.containsKey(currChar)) {
                return;
            } else {
                currNode = currNode.pointers.get(currChar);
            }
        }
        boolean updateCharRemove = false;

        while (keyItr.hasNext()) {
            currChar = keyItr.next();
            if (currNode.pointers.containsKey(currChar)) {
                if(updateCharRemove) {
                    nodeCharRemove = currChar;
                    removeNode = currNode;
                    updateCharRemove = false;
                }
                if (keyItr.hasNext()) { //not end of key
                    if (currNode.pointers.get(currChar).value != null) { //check if key contains prefix within it
                        updateCharRemove = true;
                    }
                }
                if ((currNode.pointers.size() - 1) > 0) { //this node is a char in other prefix
                    removeNode = currNode;
                    nodeCharRemove = currChar;
                }
                currNode = currNode.pointers.get(currChar);

            } else { //doesnt contain key
                return;
            }
        }

        if (currNode.pointers.get(currChar) != null) { //not a leaf so we dont want to remove entire prefix. We just remove value stored.
            currNode.pointers.get(currChar).value = null;
        } else { //node is a leaf
            removeNode.pointers.remove(nodeCharRemove);
        }
    }




        @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        ((HashTrieNode)this.root).pointers.clear();
    }
}
