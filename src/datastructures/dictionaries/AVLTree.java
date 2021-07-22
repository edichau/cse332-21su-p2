package datastructures.dictionaries;

import cse332.datastructures.trees.BinarySearchTree;
import datastructures.worklists.CircularArrayFIFOQueue;
import javafx.scene.Node;

/**
 * AVLTree must be a subclass of BinarySearchTree<E> and must use
 * inheritance and calls to superclass methods to avoid unnecessary
 * duplication or copying of functionality.
 *
 * 1. Create a subclass of BSTNode, perhaps named AVLNode.
 * 2. Override the insert method such that it creates AVLNode instances
 *    instead of BSTNode instances.
 * 3. Do NOT "replace" the children array in BSTNode with a new
 *    children array or left and right fields in AVLNode.  This will 
 *    instead mask the super-class fields (i.e., the resulting node 
 *    would actually have multiple copies of the node fields, with 
 *    code accessing one pair or the other depending on the type of 
 *    the references used to access the instance).  Such masking will 
 *    lead to highly perplexing and erroneous behavior. Instead, 
 *    continue using the existing BSTNode children array.
 * 4. If this class has redundant methods, your score will be heavily
 *    penalized.
 * 5. Cast children array to AVLNode whenever necessary in your
 *    AVLTree. This will result a lot of casts, so we recommend you make
 *    private methods that encapsulate those casts.
 * 6. Do NOT override the toString method. It is used for grading.
 */

public class AVLTree<K extends Comparable<? super K>, V> extends BinarySearchTree<K, V>  {

    private AVLNode current;
    @Override
    public V insert(K key, V value){
        if(key == null || value == null) {
            throw new IllegalArgumentException();
        }
        this.root = insertHelper(key, (AVLNode) this.root);
        V oldValue = current.value;
        current.value = value;
        return oldValue;
    }

    private AVLNode insertHelper(K key, AVLNode node){
        if(node == null) {
            current = new AVLNode(key, null);
            size++;
            return current;
        }

        int compareResult = key.compareTo(node.key);

        if(compareResult < 0) {
            node.children[0] = insertHelper(key, (AVLNode) node.children[0]);
        } else if (compareResult > 0){
            node.children[1] = insertHelper(key, (AVLNode) node.children[1]);
        } else {
            current = node;
            return node;
        }

        return balance(node);
    }

    private static final int ALLOWED_IMBALANCE = 1;

    private int getBalance(AVLNode node){
        return (node == null) ? 0 :
                getHeight(node.toAVL(0)) - getHeight(node.toAVL(1));
    }

    private AVLNode balance(AVLNode node){
        node.height = 1 + Math.max(getHeight(node.toAVL(0)), getHeight(node.toAVL(1)));

        int balVal = getBalance(node);
        if(balVal > ALLOWED_IMBALANCE) {
            int leftBalVal = getBalance(node.toAVL(0));
            if(leftBalVal < 0) {
                node.children[0] = rotateLeft(node.toAVL(0));
            }
            node = rotateRight(node);
        } else if(balVal < -ALLOWED_IMBALANCE) {
            int rightBalVal = getBalance(node.toAVL(1));
            if(rightBalVal > 0) {
                node.children[1] = rotateRight(node.toAVL(1));
            }
            node = rotateLeft(node);
        }

        return node;
    }

    public AVLNode rotateRight (AVLNode parent) {
        AVLNode temp = parent.toAVL(0);
        parent.children[0] = temp.toAVL(1);
        temp.children[1] = parent;
        // update heights
        parent.height = 1 + Math.max(getHeight(parent.toAVL(0)),
                getHeight(parent.toAVL(1)));
        temp.height = 1 + Math.max(getHeight(temp.toAVL(0)),
                getHeight(temp.toAVL(1)));

        return temp;
    }

    public AVLNode rotateLeft (AVLNode parent) {
        AVLNode temp = parent.toAVL(1);
        parent.children[1] = temp.toAVL(0);
        temp.children[0] = parent;
        // update heights
        parent.height = 1 + Math.max(getHeight(parent.toAVL(0)),
                getHeight(parent.toAVL(1)));
        temp.height = 1 + Math.max(getHeight(temp.toAVL(0)),
                getHeight(temp.toAVL(1)));

        return temp;
    }

//    private AVLNode rotateWithLeftChild(AVLNode k2){
//
//        AVLNode k1 = (AVLNode) k2.children[0];
//        k2.children[0] = k1.children[1];
//        k1.children[1] = k2;
//        k2.height = Math.max(getHeight(k2.toAVL(0)), getHeight(k2.toAVL(1))) + 1;
//        k1.height = Math.max(getHeight(k1.toAVL(0)), getHeight(k2)) + 1;
//        return k1;
//    }
//
//    private AVLNode rotateWithRightChild(AVLNode k2){
//
//        AVLNode k1 = (AVLNode) k2.children[1];
//        k2.children[1] = k1.children[0];
//        k1.children[0] = k2;
//        k2.height = Math.max(getHeight(k2.toAVL(1)), getHeight(k2.toAVL(0))) + 1;
//        k1.height = Math.max(getHeight(k1.toAVL(1)), getHeight(k2)) + 1;
//        return k1;
//    }
//
//    private AVLNode doubleWithLeftChild(AVLNode k3){
//        k3.children[0] = rotateWithRightChild(k3.toAVL(0));
//        return rotateWithLeftChild(k3);
//    }
//
//    private AVLNode doubleWithRightChild(AVLNode k3){
//        k3.children[0] = rotateWithLeftChild(k3.toAVL(1));
//        return rotateWithRightChild(k3);
//    }

    private int getHeight(AVLNode node){
        return node == null ? -1 : node.height;
    }

    public class AVLNode extends BSTNode implements Comparable<AVLNode> {

        public int height;

        public AVLNode(K key, V element) {
            super(key, element);
            this.height = 0;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public AVLNode toAVL (int childIndex) {
            return (AVLNode) this.children[childIndex];
        }

        @Override
        public int compareTo(AVLNode o) {
            return this.key.compareTo(o.key);
        }
    }

}
