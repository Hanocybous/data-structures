// ID6 Group 6
// Dimoudis Georgios A.M.: 5212
// Chatzidimitriou Charilaos A.M.: 5387
// Chatziiordanis Omiros A.M.: 5388
import java.io.* ;

public class Graph {

    private final int N;                // number of vertices
    private int M;                      // number of edges
    private Collection<Integer>[] adj;  // adjacency lists
    private boolean[] marked;           // visited by BFS?
    private int parent[];               // parents in BFS forest
    
    public Graph(int N) {
        this.N = N;
        this.M = 0;
        adj = (Collection<Integer>[]) new Collection[N];   // array of references to collections
        for (int i = 0; i < N; i++) {
            adj[i] = new Collection<Integer>();   // initialize collections to be empty
        }
        marked = new boolean[N];
        parent = new int[N];
        
        for (int i = 0; i < N; i++) {
            marked[i] = false;
            parent[i] = -1; 
        }
    }

    // return the number of vertices
    public int nodes() 
    {
        return N;
    }

    // return the number of edges
    public int edges() 
    {
        return M;
    }

    // add edge {v,w}
    public void addEdge(int v, int w) {
        adj[v].insert(w);
        adj[w].insert(v);
        M++;
    }

    // neighbors of vertex v
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    public void printGraph() {
        System.out.println("adjacency lists");
        for (int v = 0; v < N; v++) {
            System.out.print(v + " : ");
            for (int w : adj(v)) {
                System.out.print(w + " ");
            }
            System.out.println("");
        }
    }

    // execute BFS from v and store the corresponding BFS tree
    void BFS(int v){
        Queue<Integer> Q = new Queue<>();
        marked[v] = true;
        Q.enqueue(v);
        while (!Q.isEmpty())
        {
            int k = Q.dequeue();
            for ( int i : adj(k) ) {
                if (marked[i] == false) {
                    marked[i] = true;
                    parent[i] = k;
                    Q.enqueue(i);
                }
            }
        }
    }

    // compute the connected components using BFS and store the corresponding BFS forest 
    public int components(){
		int count = 0;
        for (int i = 0; i < N; i++) {
            if (!marked[i]) {
                BFS(i);
            }
            if (parent[i] == -1) {
                count ++;
            }
        }
        return count;  
    }
    
    // test if s and t are in the same connected component
    public boolean connected(int s, int t) {
        int i = s;
        while (parent[i] != -1) {
            i = parent[i];
        }
        int j = t;
        while (parent[j] != -1) {
            j = parent[j];
        }
        return j == i;
    }
    
    public void printParent()
    {
        System.out.println("parent array:");
        for (int v=0; v<N; v++)
        {
            System.out.print("" + parent[v] + " ");
        }
        System.out.println("");
    }
    
    // return the path from v to w in the BFS tree that contains both vertices
    // return null if v and w are in different connected components 
    Queue<Integer> treePath(int v, int w) {
        Queue<Integer> leftQueue = new Queue<>();
        Queue<Integer> rightQueue = new Queue<>();
		boolean[] markedPath = new boolean[N];     //This says if a node has been inserted in the path
		boolean check = false;                    //This says if the nodes are actually connected
		int i = v;
		while(i != -1){
			leftQueue.enqueue(i);
			markedPath[i] = true;
			i = parent[i];
		}
		
		int j = w;
		while(j != -1 ){
			if(markedPath[j] == true){
				check = true;
				break;
			}
			rightQueue.enqueue(j);
			j = parent[j];
		}
		
		if (check == true){
			int temp = leftQueue.dequeueTail();
			while(temp != j){
				if (leftQueue.isEmpty()){
					break;
				}
				temp = leftQueue.dequeueTail();
			}
			leftQueue.enqueue(j);
			while (!rightQueue.isEmpty()){
				leftQueue.enqueue(rightQueue.dequeueTail());
			}
			return leftQueue;
		}
		
		return new Queue<>();
    }
    
    public void printQueue(Queue<Integer> Q)
    {
		if(Q.isEmpty()){
			System.out.println("No path");
		}else{
			while ( !Q.isEmpty() ) {
				int x = Q.dequeue();
				System.out.print(" " + x + " ");
			}
			System.out.println("");
		}
    }
        
    public static void main(String[] args) {
        In.init();
        int N = In.getInt();
        Graph G = new Graph(N);
        int M = In.getInt();
        for (int i = 0; i < M; i++) {
            int v = In.getInt();
            int w = In.getInt();
            G.addEdge(v, w);
        }
        //G.printGraph();

        long startTime = System.currentTimeMillis();
        int cc = G.components();
        System.out.println("Number of connected components = " + cc);
        //G.printParent();
        
        System.out.println("connected " + 0 + " and " + (N-1) + "? " + G.connected(0, N-1));
        System.out.println("connected " + 0 + " and " + N/2 + "? " + G.connected(0, N/2));
        System.out.println("connected " + N/2 + " and " + (N-1) + "? " + G.connected(N/2, N-1));
        
        System.out.println("Path from " + 0 + " to " + (N-1) + " = ");
        G.printQueue(G.treePath(0,N-1));
       
        System.out.println("Path from " + 0 + " to " + N/2 + " = ");
        G.printQueue(G.treePath(0,N/2));
       
        System.out.println("Path from " + N/2 + " to " + (N-1) + " = ");
        G.printQueue(G.treePath(N/2,N-1));
               
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("total time = " + totalTime);
    }
}
