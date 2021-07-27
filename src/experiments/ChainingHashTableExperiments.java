package experiments;

import cse332.datastructures.trees.BinarySearchTree;
import datastructures.dictionaries.AVLTree;
import datastructures.dictionaries.ChainingHashTable;
import datastructures.dictionaries.MoveToFrontList;
import p2.sorts.HeapSort;

import java.util.*;

public class ChainingHashTableExperiments {

    public static void main(String[] args) {
        ChainingHashTable<Integer, Integer> avlChain = new ChainingHashTable<Integer, Integer>(AVLTree::new);
        ChainingHashTable<Integer, Integer> bstChain = new ChainingHashTable<Integer, Integer>(BinarySearchTree::new);
        ChainingHashTable<Integer, Integer> MTFListChain = new ChainingHashTable<Integer, Integer>(MoveToFrontList::new);

        int[] dataSize = {100000, 200000, 300000, 400000, 500000, 600000, 700000, 800000};
        Random r= new Random();
        long[] avlData = new long[8];
        long[] bstData = new long[8];
        long[] mtfData = new long[8];

        for(int i=0; i< dataSize.length; i++) {
            Integer[] data = new Integer[dataSize[i]];

            for(int j=0; j<dataSize[i]; j++) {
                data[j] = Math.abs(r.nextInt());
            }

            avlData[i] = getTime(data, avlChain);
            bstData[i] = getTime(data, bstChain);
            mtfData[i] = getTime(data, MTFListChain);

            avlChain = new ChainingHashTable<Integer, Integer>(AVLTree::new);
            bstChain = new ChainingHashTable<Integer, Integer>(BinarySearchTree::new);
            MTFListChain = new ChainingHashTable<Integer, Integer>(MoveToFrontList::new);
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

        System.out.println();
        System.out.println();
        System.out.println("MTF");
        for (long time:mtfData) {
            System.out.println(time);
        }

    }



    public static long getTime(Integer[] data, ChainingHashTable<Integer,Integer> dictionary) {

        long startTime = System.nanoTime();
        for(int i = 0; i<data.length; i++) {
            dictionary.insert(data[i], data[i]);
        }
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);

        return duration;
    }


}


