package experiments;

import cse332.datastructures.trees.BinarySearchTree;
import datastructures.dictionaries.AVLTree;

import p2.sorts.HeapSort;
import java.util.Random;

public class Ex2SearchTrees {

    public static void main(String[] args) {
        AVLTree<Integer, Integer> avlTree = new AVLTree<>();
        BinarySearchTree<Integer, Integer> binarySearchTree = new BinarySearchTree<>();

        int[] dataSize = {1000, 2000, 3000, 4000, 5000};
        Random r= new Random();
        long[] avlData = new long[5];
        long[] bstData = new long[5];

        for(int i=0; i< dataSize.length; i++) {
            Integer[] data = new Integer[dataSize[i]];
            for(int j=0; j<dataSize[i]; j++) {
                data[j] = r.nextInt();
            }
            HeapSort.sort(data);

            avlData[i] = getTime(data, avlTree);
            bstData[i] = getTime(data, binarySearchTree);

            avlTree = new AVLTree<>();
            binarySearchTree = new BinarySearchTree<>();
        }

        System.out.println("AVL");
        for (long time:avlData) {
            System.out.println(time);
        }
        System.out.println();
        System.out.println();
        System.out.println("BST");
        for (long time:bstData) {
            System.out.println(time);
        }
    }

    public static long getTime(Integer[] data, BinarySearchTree<Integer,Integer> tree) {
        long startTime = System.nanoTime();
        for(int i = 0; i<data.length; i++) {
            tree.insert(data[i], data[i]);
        }
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);

        return duration;
    }
}
