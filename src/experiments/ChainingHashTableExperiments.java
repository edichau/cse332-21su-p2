package experiments;

import cse332.datastructures.trees.BinarySearchTree;
import datastructures.dictionaries.AVLTree;
import datastructures.dictionaries.ChainingHashTable;
import datastructures.dictionaries.MoveToFrontList;

import java.util.*;

public class ChainingHashTableExperiments {

    public static void main(String[] args) {
        ChainingHashTable<Integer, Integer> avlChain = new ChainingHashTable<Integer, Integer>(AVLTree::new);
        ChainingHashTable<Integer, Integer> bstChain = new ChainingHashTable<Integer, Integer>(BinarySearchTree::new);
        ChainingHashTable<Integer, Integer> MTFListChain = new ChainingHashTable<Integer, Integer>(MoveToFrontList::new);

        int[] dataSize = {1000};
        int[] rangeMax= {2000,3000,4000};

        Map<String, Map<String, String>> avl = new HashMap<>();
        Map<String, Map<String, String>> bst = new HashMap<>();
        Map<String, Map<String, String>> mtf = new HashMap<>();

        Map<String, Map<String, String>> repeatAvl = new HashMap<>();
        Map<String, Map<String, String>> repeatBst = new HashMap<>();
        Map<String, Map<String, String>> repeatMtf = new HashMap<>();


        for(int size: dataSize) {
            for(int range: rangeMax) {
                Integer[] repeat = getRepeat(size, range);
                Integer[] noRepeat = getNoRepeat(size, range);

                avl.put(String.valueOf(range), getTime(noRepeat, avlChain, new HashMap<>()));
                bst.put(String.valueOf(range), getTime(noRepeat, bstChain, new HashMap<>()));
                mtf.put(String.valueOf(range), getTime(noRepeat, MTFListChain, new HashMap<>()));

                repeatAvl.put(String.valueOf(range), getTime(repeat, avlChain, new HashMap<>()));
                repeatBst.put(String.valueOf(range), getTime(repeat, bstChain, new HashMap<>()));
                repeatMtf.put(String.valueOf(range), getTime(repeat, MTFListChain, new HashMap<>()));

            }
        }
        System.out.println("AVL");
        System.out.println(avl);
        System.out.println("repeat" + repeatAvl);
        System.out.println("BST");
        System.out.println(bst);
        System.out.println("repeat"+ repeatBst);
        System.out.println("MTF");
        System.out.println(mtf);
        System.out.println("repeat" + repeatMtf);


    }

    public static Integer[] getRepeat(int size, int range) {

        Integer[] repeatData = new Integer[size];
        Random r = new Random();

        for(int i=0; i < size; i++) {
            repeatData[i] = (Integer) r.nextInt(range);
        }

        return repeatData;
    }

    public static Integer[] getNoRepeat(int size, int range) {
        Set<Integer> repeatData = new HashSet<Integer>();
        Random r = new Random();

        while(repeatData.size() < size) {
            repeatData.add((Integer) r.nextInt(range));
        }

        Iterator<Integer> itr = repeatData.iterator();
        Integer[] arr = new Integer[size];
        int i = 0;
        while(itr.hasNext()) {
            arr[i] = itr.next();
            i++;
        }
        return arr;
    }

    public static Map<String, String> getTime(Integer[] data, ChainingHashTable<Integer,Integer> hashMap, Map<String, String> insert) {
        ChainingHashTable<Integer, Integer> map = hashMap;
        long startTime = System.nanoTime();
        for(int i = 0; i<data.length; i++) {
            map.insert(data[i], data[i]);
        }
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        Map<String, Integer> stats = map.getHeapStats();
        insert.put("time", Double.toString(duration));
        insert.put("average collisions:", Integer.toString(stats.get("average")));
        insert.put("max heap collision:", Integer.toString(stats.get("max")));
        insert.put("min heap collision:", Integer.toString(stats.get("min")));

        return insert;

    }


}


