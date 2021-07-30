package experiments;

import cse332.interfaces.worklists.FixedSizeFIFOWorkList;
import datastructures.worklists.CircularArrayFIFOQueue;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

public abstract class BStringBadHash<Alphabet extends Comparable<Alphabet>> implements Iterable<Alphabet>, Comparable<BStringBadHash<Alphabet>> {
    protected FixedSizeFIFOWorkList<Alphabet> str;

    public BStringBadHash(Alphabet[] str) {
        this.str = new CircularArrayFIFOQueueBadHash<Alphabet>(str.length);
        for (int i = 0; i < str.length; i++) {
            this.str.add(str[i]);
        }
    }

    public BStringBadHash(FixedSizeFIFOWorkList<Alphabet> q) {
        this.str = q;
    }

    @Override
    public final Iterator<Alphabet> iterator() {
        return this.str.iterator();
    }

    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>, X extends BStringBadHash<A>> Class<A> getLetterType(Class<X> clz) {
        try {
            return (Class<A>) clz.getMethod("getLetterType", (Class<?>[]) null)
                    .invoke(null, (Object[]) null);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException
                | SecurityException e) {
            e.printStackTrace();
            System.err.println(clz.getName() + " does not have a getLetterType() method");
            System.exit(1);

            /* This is not reachable... */
            return null;
        }

    }

    public int size() {
        return this.str.size();
    }

    public final boolean isEmpty() {
        return this.str.size() == 0;
    }

    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        for (Alphabet chr : this) {
            build.append(chr);
        }
        return build.toString();
    }

    protected static Character[] wrap(char[] arr) {
        Character[] out = new Character[arr.length];
        for (int i = 0; i < arr.length; i++) {
            out[i] = arr[i];
        }
        return out;
    }

    protected static Byte[] wrap(byte[] arr) {
        Byte[] out = new Byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            out[i] = arr[i];
        }
        return out;
    }

    @Override
    public int compareTo(BStringBadHash<Alphabet> other) {
        return this.str.compareTo(other.str);
    }

    @Override
    public int hashCode() {
        return this.str.hashCode();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        if (!(other instanceof BStringBadHash)) {
            return false;
        }
        return this.str.equals(((BStringBadHash<Alphabet>) other).str);
    }
}
