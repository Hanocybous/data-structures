import java.io.*;
import java.util.Random;

// minimum index priority queue implemented with a binary heap
public class IndexMinPQ<Key extends Comparable<Key>>
{
    private int N;           // number of items on priority queue
    private int   pq[];      // binary heap of items with respect to their keys
    private int   index[];   // index[j] = position of item j in pq : pq[index[j]]=index[pq[j]]=j
    private Key[] keys;      // key[j]   = priority of item j

    public IndexMinPQ(int maxN)
    {
        keys   = (Key []) new Comparable[maxN+1];
        pq     = new int[maxN+1];
        index  = new int[maxN+1];
        for (int i = 0; i <= maxN; i++) index[i]=-1;
    }
   
    //less
    private boolean less(int i, int j) {
    		return keys[pq[i]].compareTo(keys[pq[j]])<0; 
    }

    //naive index
    private void exch(int i, int j) {
        int t=pq[i]; pq[i]=pq[j]; pq[j]=t;
        index[pq[i]]=i;
        index[pq[j]]=j;
    }

    public boolean isEmpty() {
        return N == 0;
    }
    
    public int minItem() {
    	return pq[1];
    }
    
    public Key getKey(int j) {
        return keys[j];
    }

    public boolean contains(int j) {
        return index[j] != -1;
    }

    public void fixUp(int i) {
    	while (i>1 && less(i,i/2)) {
    		exch(i,i/2);
    		i = i/2;
    	}
    }

    private void fixDown(int k) {
    	int s;
        while (2*k < N+1) {
            s = 2*k;
            if (s<N && less(s+1,s)) s++;
            if (!less(s,k)) break;
            exch(k,s); k=s;
        }
    }

    public void insert(int i, Key key) {
        pq[++N] = i;
        index[i] = N;
        keys[i] = key;
        fixUp(N);
    }

    public void change(int j, Key key) {
    	Key oldKey = keys[j];
    	keys[j] = key;
    	int k = index[j];
    	if ( key.compareTo(oldKey) < 0 )
    	fixUp(k);
    	else
    	fixDown(k);
    }

    public int delMin() {
        int minKeyItem = this.minItem();
        exch(1, N--);
        fixDown(1);
        index[minKeyItem] = -1;
        return minKeyItem;
    }

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
