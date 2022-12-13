import java.io.*;

public class LinearProbingHT<Key,Value> {

    private class Pair {
        Key key;
        Value value;
        
        Pair(Key key, Value value) {
            this.key = key;
            this.value = value;
        }
    }

    private int m;      // hash table size
    private Pair[] T;   // hash table
    private int n;      // number of distinct items inserted
    
    public int words() {
        return n;
    }
    private Value[] values;
    private Key[] keys;

    // hash function
    private int hash(Key key) {
        return ( (key.hashCode() & 0x7fffffff) % m);
    }

    // linear probing
    private int hashL(int k, int i)
    {
        return ( (k + i) % m );
    }
    
    // constructor: initialize empty hash table of size M
    LinearProbingHT(int M)
    {
        m = M;
        n = 0;
        T = new LinearProbingHT.Pair[m];
        values = (Value[]) new Object[m];
        keys = (Key[]) new Object[m];

    }

    public double loadFactor()
    {
        return (double) 100*n/m;
    }

    // insert key with associated value
    public void insert(Key key, Value value) {

        int i;
        for (i = hash(key); keys[i] != null; i = hashL(i, 1)) {
            if (keys[i].equals(key)) {
                values[i] = value;
                if (loadFactor() >= 80) resize(2*m);    
                else if (loadFactor() <= 40) resize(m/2);
                return;
            }
        }
        keys[i] = key;
        values[i] = value;
        n++;
    }

    // method to resize the hash table
    private void resize(int capacity) {
        LinearProbingHT<Key,Value> temp = new LinearProbingHT<Key,Value>(capacity);
        for (int i = 0; i < m; i++) {
            if (keys[i] != null) {
                temp.insert(keys[i], values[i]);
            }
        }
        keys = temp.keys;
        values = temp.values;
        m    = temp.m;
    }

    // return the value associated with key
    public Value contains(Key key)
    {
        int k = hash(key);
        while (keys[k] != null) {
            if (keys[k].equals(key)) {
                return values[k];
            }
            k = hashL(k, 1);
        }
        return null;
    }

    // print hash table 
    void print()
    {
        for (int j=0; j<m; j++) {
            if (T[j] == null) {
                System.out.println("T["+j+"]=");
                continue;
            }
            System.out.println("T["+j+"]=" + "(" + T[j].key + "," + T[j].value + ") ");
        }
    }

    public static void main(String[] args) {
        System.out.println("Test Hash Table with Linear Probing");

        int M = 3000; // initial hash table size
        LinearProbingHT T = new LinearProbingHT<String,Integer>(M);
        
        In.init();
        long startTime = System.currentTimeMillis();
        while (!In.empty()) {
            String s = In.getString();
            Integer count = (Integer) T.contains(s);
            if ( count != null ) {
                T.insert(s, count + 1);
            } else {
                T.insert(s, 1);
            }
        }
        //T.print();
        long endTime = System.currentTimeMillis();
        long chtTime = endTime - startTime;
        System.out.println("construction time = " + chtTime);
        System.out.println("load factor = " + T.loadFactor());
        
        System.out.println("number of words = " + T.words());
        
        System.out.println("contains 'and' " + T.contains("and") + " times");
        System.out.println("contains 'astonished' " + T.contains("astonished") + " times");
        System.out.println("contains 'boat' " + T.contains("boat") + " times");
        System.out.println("contains 'carol' " + T.contains("carol") + " times");
        System.out.println("contains 'city' " + T.contains("city") + " times");
        System.out.println("contains 'scrooge' " + T.contains("scrooge") + " times");
        System.out.println("contains 'the' " + T.contains("the") + " times");
        System.out.println("contains 'train' " + T.contains("train") + " times");
        System.out.println("contains 'wondered' " + T.contains("wondered") + " times");
        
        endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("total running time = " + totalTime); 
    }
}
