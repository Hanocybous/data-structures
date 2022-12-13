import java.io.*;
import java.util.Random;

// minimum index priority queue implemented with a binary heap
public class IndexMinPQ<Key extends Comparable<Key>>
{
    private int N;           // number of items on priority queue
    private int   pq[];      // binary heap of items with respect to their keys
    private int   index[];   // index[j] = position of item j in pq : pq[index[j]]=index[pq[j]]=j
    private Key[] keys;      // key[j]   = priority of item j
    private int small;

    public IndexMinPQ(int maxN)
    {
        keys   = (Key []) new Comparable[maxN+1];
        pq     = new int[maxN+1];
        index  = new int[maxN+1];
        for (int i = 0; i <= maxN; i++) index[i]=-1;
    }

    private boolean more(int i, int j) {
    	return keys[pq[i]].compareTo(keys[pq[j]])>0; 
    }
    
    private void exch(int i, int j) {
    	int temp = pq[i];
    	pq[i] = pq[j];
    	pq[j] = temp;
    	
    	index[pq[i]] = i;
    	index[pq[j]] = j;
    	
    }
    
    // check if priority queue is empty
    public boolean isEmpty() {
        return N == 0;
    }
    
    public Key min() {
    	int temp = 0;
    	Key tempkey = getKey(index[0]);
    	for (int i=1; i<N-1; i++) {
    		if (more(temp,i+1)){
    			temp = i;
    			tempkey = getKey(index[i]);
    		}
    	}
    	return tempkey;
    }
    
    // check if priority queue contains item j
    public boolean contains(int j) {
        return index[j] != -1;
    }
    
    // return the key of item j
    public Key getKey(int j) {
        return keys[j];
    }
    
    public void fixUp(int i) {
    	while (i>1 && more(i/2,i)) {
    		exch(i/2,i);
    		i = i/2;
    	}
    }

    // fix down
    public void fixDown(int i) {
    	while (2*i<=N) {
    		int j = 2*i;
    		if (j<N && more(j,j+1)) j++;
    		if (!more(i,j)) break;
    		exch(i,j);
    		i = j;
    	}
    }

    // insert item j with a key
    public void insert(int j, Key key) {
        index[j] = ++N;
        pq[N] = j;
        keys[j] = key;
        
        fixUp(j+1);
    }

    // delete and return item with minimum key
    public int delMin() {
        int min = pq[1];
        exch(1, N--);
        fixDown(1);
        index[min] = -1;
        keys[pq[N+1]] = null;
        pq[N+1] = -1;
        return min;
    }

    // return item with minimum key
    public int minItem() {
        return pq[1];
    }

    // print pq, key and index arrays
    public void printPQ()
    {
        for (int j=1; j<=N; j++)
            System.out.println("pq["+j+"]= " + pq[j] + ", key= " + keys[pq[j]]);

        for (int j=0; j<N; j++)
            System.out.println("index["+j+"]= " + index[j]);
    }

    public static void main(String[] args) {
        System.out.println("Test Index Min Priority Queue");

        int N = 20;
        System.out.println("Number of items to be inserted = " + N);

        long startTime = System.currentTimeMillis();
        IndexMinPQ<Integer> PQ = new IndexMinPQ<Integer>(N);
        Random rand = new Random(0);

        for (int i=0; i<N; i++) {
            int key = rand.nextInt(N*N); // assign random keys
            System.out.println("insert item " + i + " key " + key);
            PQ.insert(i,key);
        }
        PQ.printPQ();
        System.out.println(19/2);
        
        
        
        while ( !PQ.isEmpty() ) {
            int key = PQ.getKey(PQ.minItem());
            int k = PQ.delMin();
            System.out.println("delMin item " + k + " key " + key);
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("total time = " + totalTime);
    }
}
