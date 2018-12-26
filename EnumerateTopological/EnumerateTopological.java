package psp170230_lp4;

import rbk.Graph;
import rbk.Graph.Edge;
import rbk.Graph.Factory;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Vertex;

import java.util.ArrayDeque;

/**
 * Enumerates all the topological ordering of the graph.
 *
 * @author Tej Patel, Param Parikh
 */
public class EnumerateTopological extends GraphAlgorithm<EnumerateTopological.EnumVertex> {

    /**
     * Set to true to print array in visit.
     */
    boolean print;

    /**
     * Number of permutations or combinations visited.
     */
    long count;

    /**
     * Selects vertex next in ordering.
     */
    Selector sel;

    /**
     * Stores current ordering.
     */
    ArrayDeque<Vertex> stack;

    public EnumerateTopological(Graph g) {
        super(g, new EnumVertex());
        print = false;
        count = 0;
        sel = new Selector();
        stack = new ArrayDeque<>();

        for (Vertex v : g) {
            get(v).indegree = v.inDegree();
            get(v).visited = false;
        }
    }

    /**
     * Custom vertex used by the algorithm.
     */
    static class EnumVertex implements Factory {

        /**
         * True if the vertex is present in current ordering.
         */
        boolean visited;

        /**
         * Total incoming edges on this vertex.
         */
        int indegree;

        EnumVertex() {
            visited = false;
            indegree = 0;
        }

        @Override
        public EnumVertex make(Vertex u) {
            return new EnumVertex();
        }
    }

    /**
     * Selector checks if the next vertex is okay to
     * be chosen and will not violate constraints
     * imposed by a topological ordering.
     */
    class Selector extends Enumerate.Approver<Vertex> {

        /**
         * Checks of the vertex can be next in current ordering.
         *
         * @param u Vertex :- Vertex to be selected.
         * @return boolean :- True if vertex is approved else false.
         */
        @Override
        public boolean select(Vertex u) {
            if (!get(u).visited && get(u).indegree == 0) {
                get(u).visited = true;
                stack.push(u);

                for (Edge e : g.incident(u)) {
                    get(e.otherEnd(u)).indegree--;
                }

                return true;
            }
            return false;
        }

        /**
         * Removes vertex from current ordering.
         *
         * @param u Vertex :- Vertex to be removed.
         */
        @Override
        public void unselect(Vertex u) {
            get(u).visited = false;
            stack.pop();
            for (Edge e : g.incident(u)) {
                get(e.otherEnd(u)).indegree++;
            }
        }

        /**
         * Permutation is found, so update count or print it.
         *
         * @param arr Vertex[] :- Permutation.
         * @param k   int :- Total elements permuted.
         */
        @Override
        public void visit(Vertex[] arr, int k) {
            count++;
            if (print) {
                for (int i = arr.length - 1; i >= 0; i--) {
                    System.out.print(arr[i] + " ");
                }
                System.out.println();
            }

        }
    }

    /**
     * Finds all the topological sort of the graph.
     *
     * @param flag boolean :- If true, prints all the orderings.
     * @return int :- Total number of permutations.
     */
    public long enumerateTopological(boolean flag) {
        print = flag;
        enumTopHelper();
        return count;
    }

    /**
     * Backtracking algorithm to find the orderings.
     */
    private void enumTopHelper() {
        boolean flag = false;

        for (Vertex u : g) {
            if (sel.select(u)) {
                enumTopHelper();
                sel.unselect(u);
                flag = true;
            }
        }

        if (!flag) {
            sel.visit(stack.toArray(new Vertex[0]), stack.size());
        }

    }

    /**
     * Counts total topologicals orderings possible.
     *
     * @param g Graph :- Graph of which ordering is required.
     * @return long :- Total orderings.
     */
    public static long countTopologicalOrders(Graph g) {
        return new EnumerateTopological(g).enumerateTopological(false);
    }

    /**
     * Counts and prints all topological orders of given graph.
     *
     * @param g Graph :- Graph of which ordering is required.
     * @return long :- Total orderings.
     */
    public static long enumerateTopologicalOrders(Graph g) {
        return new EnumerateTopological(g).enumerateTopological(true);
    }
}
