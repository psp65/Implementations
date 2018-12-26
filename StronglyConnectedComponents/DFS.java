package psp170230;

/**
 *
 * @author Param Parikh
 */
import rbk.Graph;
import rbk.Graph.Vertex;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Factory;

import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;

public class DFS extends GraphAlgorithm<DFS.DFSVertex> {

    /**
     * Stores result of dfs in reverse order
     */
    static LinkedList<Vertex> finishList = null;

    /**
     * Stores names of each vertices in same group
     */
    static LinkedList<LinkedList<Vertex>> scc = null;

    /**
     * Total number of Strongly Connected Components in graph
     */
    static int num_scc;

    public static class DFSVertex implements Factory {

        int cno;
        boolean seen;

        public DFSVertex(Vertex u) {
        }

        protected void set_cno(int n) {
            cno = n;
        }

        @Override
        public DFSVertex make(Vertex u) {
            return new DFSVertex(u);
        }
    }

    public DFS(Graph g) {
        super(g, new DFSVertex(null));
        scc = new LinkedList<>();
    }

    /**
     * Function to run dfs on this graph
     */
    private void dfs() {
        dfs(g);
    }

    /**
     * Function to run dfs in order of Vertex appearing in list
     *
     * @param list List of vertices
     */
    private void dfs(List<Vertex> list) {
        Iterable<Vertex> itr = list;
        dfs(itr);
    }

    /**
     * Helper function to perform dfs on graph as vertices appears in itr
     *
     * @param itr Iterable Vertices
     */
    private void dfs(Iterable<Vertex> itr) {
        finishList = new LinkedList<>();
        num_scc = 0;
        scc = new LinkedList<>();

        for (Vertex u : itr) {
            get(u).seen = false;
        }

        for (Vertex u : itr) {
            if (get(u).seen == false) {
                LinkedList<Vertex> ll = new LinkedList<>();
                scc.add(ll);
                num_scc++;
                dfsVisit(u);
            }
        }
    }

    /**
     * Starting point of dfs from a vertex. Called by dfs method
     *
     * @param u Vertex from dfs starts
     */
    private void dfsVisit(Vertex u) {
        get(u).set_cno(num_scc);
        scc.getLast().add(u);
        get(u).seen = true;

        for (Edge e : g.outEdges(u)) {
            Vertex v = e.otherEnd(u);
            if (get(v).seen == false) {
                dfsVisit(v);
            }
        }
        finishList.addFirst(u);
    }

    /**
     * Helper that returns group number associated with vertex u
     *
     * @param u Query vertex
     * @return Group number of vertex
     */
    public int cno(Vertex u) {
        return get(u).cno;
    }

    /**
     * Find number of Strongly connected components of given graph Pre: Graph
     * should be Directed graph
     *
     * @param g Graph on which SCC has to be performed
     * @return DFS class with vertices in same component marked with same cno
     */
    public static DFS stronglyConnectedComponents(Graph g) {
        assert g.isDirected();

        DFS d = new DFS(g);
        d.dfs();
        List<Vertex> list = DFS.finishList;
        g.reverseGraph();
        d.dfs(list);
        g.reverseGraph();
        return d;
    }

    public static void main(String[] args) throws Exception {
        String string = "11 17  1 11 1  2 3 1  2 7 1  3 10 1  4 1 1  4 9 1  5 4 1  5 7 1  5 8 1  6 3 1  7 8 1  8 2 1  9 11 1  10 6 1  11 3 1  11 4 1  11 6 1 0";
        Scanner in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);

        Graph g = Graph.readDirectedGraph(in);
        g.printGraph(false);

        DFS scg = stronglyConnectedComponents(g);
        System.out.println("Number of strongly connected components: " + scg.num_scc + "\nu\tcno");
        for (Vertex u : g) {
            System.out.println(u + "\t" + scg.cno(u));
        }
        System.out.println("\nGroups:");
        System.out.println(scg.scc);
    }
}
