import java.io.*;

// computes the minimum spanning tree of a connected weighted graph
public class Prim {

    private static Edge[] edgeTo;        // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
    private static double[] distTo;      // distTo[v] = weight of shortest such edge
    private static boolean[] marked;     // marked[v] = true if v on tree, false otherwise
    //private static NaiveIndexMinPQ<Double> PQ;
    private static IndexMinPQ<Double> PQ;

    private static double MSTweight;     // weight of the minimum spanning tree

    // Prim's algorithm starting from vertex s
    private static void Prim(EdgeWeightedGraph G, int s) {

        edgeTo = new Edge[G.nodes()];
        distTo = new double[G.nodes()];
        marked = new boolean[G.nodes()];
        //PQ = new NaiveIndexMinPQ<Double>(G.nodes());
        PQ = new IndexMinPQ<Double>(G.nodes());

        // initialization
        for (int v = 0; v < G.nodes(); v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        distTo[s] = 0.0;
        MSTweight = 0.0;
        PQ.insert(s, distTo[s]);

        // main loop
        while (!PQ.isEmpty()) {
            int v = PQ.delMin();    // vertex with minimum distance from the tree
            MSTweight += distTo[v];
            scan(G, v);             // process edges adjacent to v
        }
    }

    // scan vertex v
    private static void scan(EdgeWeightedGraph G, int v) {
        marked[v] = true;
        for (Edge e : G.adj(v)) {
            int w = e.other(v); // process edge {v,w}
            if (marked[w]) {    // w is already on the tree
                continue;       // {v,w} is obsolete edge
            }
            if (e.weight() < distTo[w]) {
                distTo[w] = e.weight();
                edgeTo[w] = e;
                if (PQ.contains(w)) {
                    PQ.change(w, distTo[w]); // change w's key
                } else {
                    PQ.insert(w, distTo[w]); // insert w into PQ
                }
            }
        }
    }

    public static void main(String[] args) {

        In.init();
        int N = In.getInt();
        EdgeWeightedGraph G = new EdgeWeightedGraph(N + 1);
        int M = In.getInt();
        // read weighted graph
        for (int i = 0; i < M; i++) {
            int v = In.getInt();
            int w = In.getInt();
            double weight = In.getDouble();
            Edge e = new Edge(v, w, weight);
            G.addEdge(e);
        }

        long startTime = System.currentTimeMillis();
        Prim(G, 1);
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("total time = " + totalTime);
        System.out.println("MST weight = " + MSTweight);
    }
}
