package experiments;

import cse332.datastructures.trees.BinarySearchTree;
import cse332.misc.WordReader;
import cse332.types.AlphabeticString;
import cse332.types.NGram;
import datastructures.dictionaries.AVLTree;
import datastructures.dictionaries.ChainingHashTable;
import datastructures.dictionaries.MoveToFrontList;
import datastructures.worklists.CircularArrayFIFOQueue;
import p2.clients.NGramTester;
import p2.wordsuggestor.NGramToNextChoicesMap;
import p2.wordsuggestor.ParseFBMessages;
import p2.wordsuggestor.WordSuggestor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Experiment4 {

    public static void main(String[] args) throws IOException {
        ChainingHashTable good = new ChainingHashTable(MoveToFrontList::new);
        ChainingHashTable bad = new ChainingHashTable(MoveToFrontList::new);

        long[] goodHashData = new long[10];
        long[] badHashData = new long[10];

        int index = 0;

        for (int i = 1000; i < 10000; i = i + 1000) {
            ArrayList<AlphabeticString> goodData = getGoodData(i);
            ArrayList<AlphabeticString> badData = getBadData(i);


            long goodStart = System.nanoTime();
            for (AlphabeticString d: goodData) {
                good.insert(d,d);
            }
            long goodEnd = System.nanoTime();
            long goodDest = goodEnd-goodStart;
            goodHashData[index] = goodDest;

            long badStart = System.nanoTime();
            for (AlphabeticString d: badData) {
                bad.insert(d,d);
            }
            long badEnd = System.nanoTime();
            long badDest = badEnd-badStart;
            badHashData[index] = badDest;

            index++;
            good = new ChainingHashTable(MoveToFrontList::new);
            bad = new ChainingHashTable(MoveToFrontList::new);
        }

        for (int i = 0; i < 10; i++) {
            System.out.println(goodHashData[i]);
        }
        System.out.println();
        for (int i = 0; i < 10; i++) {
            System.out.println(badHashData[i]);
        }

    }



    public static ArrayList<AlphabeticString> getBadData(int length) {
        WordReader reader = new WordReader("dictionary.txt");
        ArrayList<AlphabeticString> ret = new ArrayList<>();
        int numWords = 0;
        while (reader.hasNext() && numWords < length) {
            String word = reader.next();

            char[] letters = word.toCharArray();
            CircularArrayFIFOQueueBadHash insert = new CircularArrayFIFOQueueBadHash(letters.length);
            for (char l : letters) {
                insert.add(l);
            }
            AlphabeticString aString = new AlphabeticString(insert);
            ret.add(aString);
            numWords++;
        }
        return ret;
    }

    public static ArrayList<AlphabeticString> getGoodData(int length) {
        WordReader reader = new WordReader("dictionary.txt");
        ArrayList<AlphabeticString> ret = new ArrayList<>();
        int i = 0;
        while (reader.hasNext() && i < length) {
            String word = reader.next();

            char[] letters = word.toCharArray();
            CircularArrayFIFOQueue insert = new CircularArrayFIFOQueue(letters.length);
            for (char l : letters) {
                insert.add(l);
            }
            AlphabeticString aString = new AlphabeticString(insert);
            ret.add(aString);
            i++;
        }
        return ret;
    }


}


