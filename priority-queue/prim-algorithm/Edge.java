public class Edge
{
    private final int v;          // first edge endpoint
    private final int w;          // second edge endpoint
    private final double weight;  // edge weight

    public Edge(int v, int w, double weight)
    {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public double weight()  // return edge weight
    {
        return weight;
    }

    public int from()  // return edge source
    {
        return v;
    }

    public int to()  // return edge target
    {
        return w;
    }
    
    public int other(int vertex) {
        if      (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new IllegalArgumentException("Illegal endpoint");
    }
}
