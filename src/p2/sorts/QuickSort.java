package p2.sorts;

import cse332.exceptions.NotYetImplementedException;

import java.util.Comparator;
import java.util.Random;

public class QuickSort {
    public static <E extends Comparable<E>> void sort(E[] array) {
        QuickSort.sort(array, (x, y) -> x.compareTo(y));
    }

    public static <E> void sort(E[] array, Comparator<E> comparator) {
        quickSort(array, comparator, 0, array.length - 1);
    }

    private static <E> void quickSort(E[] arr, Comparator<E> comparator, int lower, int upper) {
        if(lower < upper) {
            int p = partition(arr, comparator, lower, upper);
            quickSort(arr,comparator,p+1,upper);
            quickSort(arr,comparator,lower,p-1);
        }
    }

    private static int getPivot(int low, int high) {
        Random rand = new Random();
        return rand.nextInt((high - low) + 1) + low;
    }

    private static <E> int partition(E[] arr, Comparator<E> comparator, int lower, int upper) {
        swap(arr, lower, getPivot(lower, upper));
        int border = lower + 1;
        for (int i = border; i<= upper; i++){
            if (comparator.compare(arr[i], arr[lower]) < 0){
                swap(arr, i, border++);
            }
        }
        swap(arr, lower, border - 1);
        return border - 1;
    }

    private static <E> void swap(E[] arr, int ind1, int ind2) {
        E temp = arr[ind1];
        arr[ind1] = arr[ind2];
        arr[ind2] = temp;
    }
}
