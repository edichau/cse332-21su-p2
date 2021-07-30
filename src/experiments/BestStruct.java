package experiments;

import cse332.datastructures.trees.BinarySearchTree;
import cse332.interfaces.misc.Dictionary;
import cse332.types.AlphabeticString;
import datastructures.dictionaries.AVLTree;
import datastructures.dictionaries.ChainingHashTable;
import datastructures.dictionaries.HashTrieMap;
import datastructures.dictionaries.MoveToFrontList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.function.Supplier;

public class BestStruct {

    public static void main(String[] args) {
        double hashTable = 0;
        double hashTrie = 0;
        double BST = 0;
        double AVL = 0;
        try {
            for (int i = 0; i < 1; i++) {
                double d = Test(new ChainingHashTable<>
                        (new Supplier<Dictionary<AlphabeticString, Integer>>() {
                            @Override
                            public Dictionary<AlphabeticString, Integer> get() {
                                return new MoveToFrontList<AlphabeticString, Integer>();
                            }
                        }));
                hashTable += d;
                d = Test(new HashTrieMap<>(AlphabeticString.class));
                hashTrie += d;
                d = Test(new BinarySearchTree<>());
                BST += d;
                d = Test(new AVLTree<>());
                AVL += d;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("HashTable: " + hashTable);
        System.out.println("HashTrieMap: " + hashTrie);
        System.out.println("BST: " + BST);
        System.out.println("AVL: " + AVL);

    }

    private static double Test(Dictionary<AlphabeticString, Integer> tester) throws FileNotFoundException {
        double totalTime = 0;
        double time2 = 0;
        File file = new File("alice.txt");
        Scanner read = new Scanner(file);
        while (read.hasNext()) {
            String line = read.nextLine();
            String[] regWords = line.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
            AlphabeticString[] words = new AlphabeticString[regWords.length];
            for (int i = 0; i < regWords.length; i++) {
                words[i] = new AlphabeticString(regWords[i]);
            }
            double start = System.currentTimeMillis();
            for (AlphabeticString word : words) {
                Integer n = tester.find(word);
                if (n == null) {
                    n = 1;
                }
                tester.insert(word, n);
            }
            double end = System.currentTimeMillis();
            totalTime += end - start;
            double start2 = System.currentTimeMillis();
            for (AlphabeticString word : words) {
                tester.find(word);
            }
            double end2 = System.currentTimeMillis();
            time2 += end2 - start2;

        }
        System.out.println(time2);
        return totalTime;
    }

}