package experiments;

import cse332.datastructures.trees.BinarySearchTree;
import cse332.types.AlphabeticString;
import cse332.types.NGram;
import datastructures.dictionaries.AVLTree;
import datastructures.dictionaries.ChainingHashTable;
import datastructures.dictionaries.MoveToFrontList;
import datastructures.worklists.CircularArrayFIFOQueue;
import p2.clients.NGramTester;
import p2.wordsuggestor.NGramToNextChoicesMap;
import p2.wordsuggestor.WordSuggestor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Experiment4 {

    public static void main(String[] args) throws IOException {

        long[] goodHashData = new long[5];
        long[] badHashData = new long[5];

        int index= 0;
        for(int i = 1000; i >= 1; i+= 200) {

            long startTime = System.nanoTime();
            goodHash(i);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);

            long startTimeBadHash = System.nanoTime();
            badHash(i);
            long endTimeBadHash = System.nanoTime();
            long durationBadHash = (endTimeBadHash - startTimeBadHash);

            goodHashData[index] = duration;
            badHashData[index] = durationBadHash;
            index++;
        }

        for(int i = 0; i < 5; i++) {
            System.out.println(goodHashData[i]);
            System.out.println(badHashData[i]);
            System.out.println();
        }

    }

    public static boolean goodHash(int n) throws IOException {
        WordSuggestor suggestions = new WordSuggestor("dictionary.txt", n, -1,
                NGramTester.hashtableConstructor(MoveToFrontList::new),
                NGramTester.hashtableConstructor(MoveToFrontList::new));

        return true;
    }

    public static boolean badHash(int n) throws IOException {
        WordSuggestorBadHash suggestions = new WordSuggestorBadHash("dictionary.txt", n, -1,
                NGramTester.hashtableConstructor(MoveToFrontList::new),
                NGramTester.hashtableConstructor(MoveToFrontList::new));

        return true;
    }



}


