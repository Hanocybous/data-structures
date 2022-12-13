import java.io.* ;

public class EdgeWeightedGraph
{
    private final int N;                     // number of nodes
    private int M;                           // number of edges
    private Collection<Edge>[] adj;  // adjacency lists

    // construct an edge-weighted graph with N nodes
    public EdgeWeightedGraph(int N)
    {
        this.N = N;
        this.M = 0;
        adj = (Collection<Edge>[]) new Collection[N];   // array of lists
        for (int i=0; i<N; i++)
            adj[i] = new Collection<Edge>();    // initialize lists to be empty
    }

    // return the number of nodes
    public int nodes()  
    {
        return N;
    }

    // return the number of edges
    public int edges()  
    {
        return M;
    }

    // add edge e
    public void addEdge(Edge e)
    {
        // add edge e to adjacency lists of both endpoints
        adj[e.from()].add(e);
        adj[e.to()].add(e);
        M++;
    }

    // edges {v,x} adjacent to node v
    public Iterable<Edge> adj(int v)
    {
        return adj[v];
    }

    public void printGraph()
    {
        System.out.println("adjacency lists; for each edge {u,v} with weight w it prints (v,w)");
        for (int v=0; v<N; v++)
        {
            System.out.print(v + " : ");
            for (Edge e : adj[v])
            {
                int w = e.other(v);
                System.out.print("(" + w + ", " + e.weight() + ") ");
            }
            System.out.println("");
        }
    }

    public static void main(String[] args)
    {
        In.init();
        int N = In.getInt();
        EdgeWeightedGraph G = new EdgeWeightedGraph(N);
        int M = In.getInt();
        for (int i=0; i<M; i++)
        {
            int v = In.getInt();
            int w = In.getInt();
            double weight = In.getDouble();
            Edge e = new Edge(v,w,weight);
            G.addEdge(e);
        }
        G.printGraph();
    }
}
