package psp170230_lp4;

import rbk.Graph;
import rbk.Graph.Vertex;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Factory;

import java.util.List;
import java.util.LinkedList;

/**
 *
 * @author Tej Patel, Param Parikh
 */

enum Color {
    WHITE, GRAY, BLACK
}

public class DFS extends GraphAlgorithm<DFS.DFSVertex> {

    static LinkedList<Vertex> finishList = null;
    static boolean isCyclic = false;

    public static class DFSVertex implements Factory {

        int cno;
        Vertex parent;
        Color color;

        public DFSVertex(Vertex u) {
            parent = null;
        }

        @Override
        public DFSVertex make(Vertex u) {
            return new DFSVertex(u);
        }
    }

    public DFS(Graph g) {
        super(g, new DFSVertex(null));
    }

    public static DFS depthFirstSearch(Graph g) {
        DFS d = new DFS(g);
        d.dfs(g);
        return d;
    }

    private void dfs(Graph g) {
        finishList = new LinkedList<>();

        for (Vertex u : g) {
            get(u).parent = null;
            get(u).color = Color.WHITE;
        }

        for (Vertex u : g) {
            if (!isCyclic && get(u).color == Color.WHITE) {
                dfsVisit(u, g);
            }
        }
    }

    private void dfsVisit(Vertex u, Graph g) {
        if (isCyclic) {
            return;
        }

        get(u).color = Color.GRAY;
        for (Edge e : g.incident(u)) {
            Vertex v = e.otherEnd(u);
            if (get(v).color == Color.WHITE) {
                get(v).parent = u;
                dfsVisit(v, g);
            } else if (get(v).color == Color.GRAY) {
                isCyclic = true;
            }
        }
        get(u).color = Color.BLACK;
        finishList.addFirst(u);
    }

    // Member function to find topological order
    private List<Vertex> topologicalOrder1() {
        if (!g.isDirected()) {
            return null;
        }

        dfs(g);

        if (isCyclic) {
            return null;
        }
        return finishList;
    }

    // Find topological oder of a DAG using DFS. Returns null if g is not a DAG.
    public static List<Vertex> topologicalOrder1(Graph g) {
        return new DFS(g).topologicalOrder1();
    }

}
