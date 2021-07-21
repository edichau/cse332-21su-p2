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

    @Override
    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }

        // copied from find in BinarySearchTree
        AVLNode prev = null;
        AVLNode current = (AVLNode) this.root;
        int maxLength;

        if(this.root == null) {
            maxLength = 2;
        } else {
            maxLength = ((AVLNode) this.root).getHeight() + 2;
        }

        CircularArrayFIFOQueue<AVLNode> path = new CircularArrayFIFOQueue<>(maxLength);
        CircularArrayFIFOQueue<Integer> childPath = new CircularArrayFIFOQueue<>(maxLength);

        int child = -1;

        while (current != null) {
            int direction = Integer.signum(key.compareTo(current.key));

            // We found the key!
            if (direction == 0) {
                break;
            } else {
                // direction + 1 = {0, 2} -> {0, 1}
                path.add(current);
                child = Integer.signum(direction + 1);
                childPath.add(child);
                prev = current;
                current = (AVLNode) current.children[child];
            }

        }

        //after the loop, the node we want to insert into is found

        //if the node we found already exists, then we dont have to create a new node
        if (current != null) {
            V oldValue = current.value;
            current.value = value;

            return oldValue;
        }

        // Create and insert the new node
        current = new AVLNode(key, value);
        path.add(current);
        this.size++;

        prev.children[child] = current;
        //TODO: update the height
        //TODO: Balance the tree
    }

    private int updateHeights(CircularArrayFIFOQueue<AVLNode> path) {

    }

    private void balance(CircularArrayFIFOQueue<AVLNode> path, CircularArrayFIFOQueue<Integer> childPath, int index) {

    }

    public class AVLNode extends BSTNode implements Comparable<AVLNode> {

        private int height;

        /**
         * Create a new data node.
         *
         * @param key   key with which the specified value is to be associated
         * @param value
         */
        public AVLNode(K key, V value) {
            super(key, value);
            this.height = 0;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getHeight() {
            return this.height;
        }

        @Override
        public int compareTo(AVLNode o) {
            return this.key.compareTo(o.key);
        }
    }
        //    public static boolean verifyAVL(AVLNode root) {
//        try {
//            verifyBST(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
//            verifyHeight(root);
//            verifyBalance(root);
//            return true;
//        } catch (IllegalStateException e) {
//            return false;
//        }
//    }
//
//    private static int verifyHeight(AVLNode root){
//        if (root == null){
//            return 0;
//        }
//        int left = verifyHeight(root.left);
//        int right = verifyHeight(root.right);
//        if (root.left != null && root.left.height != left - 1) {
//            throw new IllegalStateException();
//        }
//        if (root.right != null && root.right.height != right - 1) {
//            throw new IllegalStateException();
//        }
//        if (root.height != Math.max(verifyHeight(root.left), verifyHeight(root.right))) {
//            throw new IllegalStateException();
//        }
//        return Math.max(verifyHeight(root.left), verifyHeight(root.right)) + 1;
//    }
//
//    private static void verifyBST(AVLNode node, int min, int max) {
//        if (node == null) {
//            return;
//        }
//        if (node.left != null)
//            verifyBST(node.left, min, node.left.key);
//        if (node.right != null)
//            verifyBST(node.right, node.right.key, max);
//        if (node.right != null && (node.right.key < node.key || node.right.key > max))
//            throw new IllegalStateException();
//        if (node.left != null && (node.left.key < min || node.left.key > node.key))
//            throw new IllegalStateException();
//    }
//
//    private static void verifyBalance(AVLNode node){
//        if(node == null || (node.left == null && node.right == null)){
//            return;
//        }
//        if ((node.right == null && (node.left.height > 1 || node.height > 1)) || (node.left == null && (node.right.height > 1 || node.height > 1)) ||
//                (node.left != null && node.right != null && Math.abs(node.left.height - node.right.height) > 1 )) {
//            throw new IllegalStateException();
//        }
//        verifyBalance(node.left);
//        verifyBalance(node.right);
//    }
}
