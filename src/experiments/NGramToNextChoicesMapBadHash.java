package experiments;

import cse332.datastructures.containers.Item;
import cse332.interfaces.misc.Dictionary;
import cse332.misc.LargeValueFirstItemComparator;
import cse332.types.AlphabeticString;
import p2.sorts.HeapSort;
import p2.sorts.TopKSort;
import p2.wordsuggestor.NGramToNextChoicesMap;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Supplier;

public class NGramToNextChoicesMapBadHash {
    private final Dictionary<NGramBadHash, Dictionary<AlphabeticString, Integer>> map;
    private final Supplier<Dictionary<AlphabeticString, Integer>> newInner;

    public NGramToNextChoicesMapBadHash(
            Supplier<Dictionary<NGramBadHash, Dictionary<AlphabeticString, Integer>>> newOuter,
            Supplier<Dictionary<AlphabeticString, Integer>> newInner) {
        this.map = newOuter.get();
        this.newInner = newInner;
    }

    /**
     * Increments the count of word after the particular NGram ngram.
     */
    public void seenWordAfterNGram(NGramBadHash ngram, String word) {
        Dictionary<AlphabeticString, Integer> counter = map.find(ngram);
        if (counter == null) {
            counter = newInner.get();
            map.insert(ngram, counter);
        }

        Integer prev = counter.find(new AlphabeticString(word));
        if (prev == null) {
            prev = 0;
        }
        counter.insert(new AlphabeticString(word), prev + 1);

    }

    /**
     * Returns an array of the DataCounts for this particular ngram. Order is
     * not specified.
     *
     * @param ngram
     *            the ngram we want the counts for
     * 
     * @return An array of all the Items for the requested ngram.
     */
    public Item<String, Integer>[] getCountsAfter(NGramBadHash ngram) {
        if (ngram == null) {
            return (Item<String, Integer>[]) new Item[0];
        }
        Dictionary<AlphabeticString, Integer> counter = map.find(ngram);
        Item<String, Integer>[] result = (Item<String, Integer>[]) new Item[counter != null
                ? counter.size() : 0];
        if (counter != null) {
            Iterator<Item<AlphabeticString, Integer>> it = counter.iterator();

            for (int i = 0; i < result.length; i++) {
                Item<AlphabeticString, Integer> item = it.next();
                result[i] = new Item<String, Integer>(item.key.toString(),
                        item.value);
            }
        }
        return result;

    }

    public String[] getWordsAfter(NGramBadHash ngram, int k) {
        Item<String, Integer>[] afterNGrams = getCountsAfter(ngram);

        Comparator<Item<String, Integer>> comp = new LargeValueFirstItemComparator<String, Integer>();
        if (k < 0) {
            HeapSort.sort(afterNGrams, comp);
        }
        else {
            // You must fix this line toward the end of the project
            TopKSort.sort(afterNGrams, k, comp.reversed());
            if(k > afterNGrams.length) {
                k = afterNGrams.length;
            }
            Item<String, Integer>[] passItem = new Item[k];

            for (int i = 0; i < k; i++) {
                passItem[i] = afterNGrams[k-i-1];
            }
            afterNGrams = passItem;
        }

        String[] nextWords = new String[k < 0 ? afterNGrams.length : k];
        for (int l = 0; l < afterNGrams.length && l < nextWords.length
                && afterNGrams[l] != null; l++) {
            nextWords[l] = afterNGrams[l].key;
        }
        return nextWords;
    }

    @Override
    public String toString() {
        return this.map.toString() + this.map.size(); }
}