package psp170230;

import rbk.Graph;
import rbk.Graph.Edge;
import rbk.Graph.Factory;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Vertex;

import psp170230.BinaryHeap.Index;
import psp170230.BinaryHeap.IndexedHeap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Class to compute Minimum Spanning Tree of given Graph
 *
 * <p>
 * 4 Algorithms are implemented for comparison: 
 * 1) Kruskal's algorithm 
 * 2) Prim's algorithm which keeps track of edge weights 
 * 3) Prim's algorithm which keeps track of distance to vertices using Priority Queue 
 * 4) Prim's algorithm which keeps track of distance to vertices using Indexed Heaps
 * </p>
 *
 * @author Param Parikh, Tej Patel
 */
public class MST extends GraphAlgorithm<MST.MSTVertex> {

    /**
     * Stores name of the applied algorithm to compute MST
     */
    String algorithm;

    /**
     * Stores weight computed of MST
     */
    public long wmst;

    /**
     * Stores edges which are part of MST
     */
    List<Edge> mst;

    MST(Graph g) {
        super(g, new MSTVertex((Vertex) null));
    }

    /**
     * Class which keeps track of necessary information for each MST vertex
     */
    public static class MSTVertex implements Index, Comparable<MSTVertex>, Factory {

        /**
         * root of vertex in UNION/FIND
         */
        MSTVertex root;

        /**
         * predecessor vertex in MST. When edge e (u, v) is added to MST,
         * v.parent = u and v.d = w(e)
         */
        /**
         * Parent of current vertex
         */
        Vertex parent;

        /**
         * corresponding graph vertex
         */
        Vertex vertex;

        /**
         * index of vertex stored in priority queue
         */
        int index;

        /**
         * weight of edge (u,v) that belongs to MST. v.d = w(e)
         */
        int d;

        /**
         * rank of vertex in UNION/FIND
         */
        int rank;

        /**
         * when v.seen = true: edge e (u,v), where v.d = w(e) is added in MST
         */
        boolean seen;

        MSTVertex(Vertex u) {
            root = this; // vertex itself is its root node in the beginning            
            parent = null;
            vertex = u;
            d = Integer.MAX_VALUE;
            rank = 0;
            seen = false;
        }

        MSTVertex(MSTVertex u) {  // for prim2
            vertex = u.vertex;
        }

        @Override
        public MSTVertex make(Vertex u) {
            return new MSTVertex(u);
        }

        @Override
        public void putIndex(int index) {
            this.index = index;
        }

        @Override
        public int getIndex() {
            return index;
        }

        @Override
        public int compareTo(MSTVertex other) {
            if (other == null || this.d > other.d) {
                return 1;
            }

            if (this.d < other.d) {
                return -1;
            }

            return 0;
        }

        /**
         * find the root of vertex. point current vertex to root for immediate
         * access in future
         *
         * @return root of vertex
         */
        public MSTVertex find() {
            if (!vertex.equals(root.vertex)) { // recursive call while vertex's root is itself
                root = root.find(); // set direct pointer to its root
            }
            return root;
        }

        /**
         * union vertex 'rv' with current vertex
         *
         * @param rv vertex to union current vertex with
         */
        public void union(MSTVertex rv) {
            // set root of smaller tree as a child of root with bigger tree.
            // use rank to determine which tree is bigger
            if (rank > rv.rank) {
                rv.root = this;
            } else if (rank < rv.rank) {
                root = rv;
            } else { // if both tree has same rank, any one of two root vertices can become new root
                rank++;
                rv.root = this;
            }
        }

    }

    /**
     * Kruskal algorithm which exploits Union-Find method to compute MST
     *
     * @return Weight of Minimal Spanning Tree
     */
    public long kruskal() {
        algorithm = "Kruskal";
        Edge[] edgeArray = g.getEdgeArray();
        mst = new LinkedList<>();
        wmst = 0;

        Arrays.sort(edgeArray);
        for (Edge e : edgeArray) {
            MSTVertex ru = get(e.fromVertex()).find();
            MSTVertex rv = get(e.toVertex()).find();
            if (!ru.vertex.equals(rv.vertex)) {
                mst.add(e);
                ru.union(rv);
                wmst += e.getWeight();
            }
        }

        return wmst;
    }

    /**
     * Prim algorithm which uses IndexedHeap to efficiently compute MST
     *
     * @param s Source vertex of Graph
     * @return Weight of Minimal Spanning Tree
     */
    public long prim3(Vertex s) {
        algorithm = "indexed heaps";
        mst = new LinkedList<>();
        initialize(s);

        IndexedHeap<MSTVertex> q = new IndexedHeap<>(g.size());
        for (Vertex u : g) {
            q.add(get(u));
        }

        while (!q.isEmpty()) {
            MSTVertex u = q.poll(); //get vertex u representing edge e(v,u) with min weight (v.d)
            u.seen = true; // this vertex is processed
            wmst += u.d; // update MST weight

            for (Edge e : g.incident(u.vertex)) { // every edge e(u,v) outgoing of u
                Vertex v = e.otherEnd(u.vertex);
                //discard if v already processed or v already represented edge with less weight than e
                if (!get(v).seen && e.getWeight() < get(v).d) {
                    get(v).d = e.getWeight(); // update weight
                    get(v).parent = u.vertex; // update predecessor
                    q.decreaseKey(get(v)); // update priority queue
                }
            }
        }
        return wmst;
    }

    /**
     * Prim algorithm which exploits distance to Vertices as Priority
     *
     * @param s Source vertex of Graph
     * @return Weight of Minimal Spanning Tree
     */
    public long prim2(Vertex s) {
        algorithm = "PriorityQueue<Vertex>";
        mst = new LinkedList<>();
        initialize(s);

        PriorityQueue<MSTVertex> q = new PriorityQueue<>();
        q.add(get(s));

        while (!q.isEmpty()) {
            MSTVertex u = q.remove(); // get edge with min weight. vertex u represents edge e(v,u)
            if (!u.seen) {
                u.seen = true;
                wmst += u.d; // w(e)

                for (Edge e : g.incident(u.vertex)) { // edges (u,v) outgoing of u
                    Vertex v = e.otherEnd(u.vertex); // other end of edge (u,v)

                    // discard if v is already seen or v already represented edge e' with lesser weight then e
                    if (!get(v).seen && e.getWeight() < get(v).d) {
                        MSTVertex dupV = new MSTVertex(get(v)); // duplicate the old vertex
                        dupV.d = e.getWeight(); // set new lesser weight of edge
                        dupV.parent = u.vertex; // now new v represents new edge
                        get(v).seen = true; // make old vertex seen so that it is never used here again
                        put(dupV.vertex, dupV); // replace old vertex with new for future access
                        q.add(dupV); // add to priority queue
                    }
                }

            }
        }

        return wmst;
    }

    /**
     * Prim algorithm which exploits weight of Edges as Priority
     *
     * @param s Source vertex of Graph
     * @return Weight of Minimal Spanning Tree
     */
    public long prim1(Vertex s) {
        algorithm = "PriorityQueue<Edge>";
        mst = new LinkedList<>();
        initialize(s);
        get(s).seen = true;

        PriorityQueue<Edge> q = new PriorityQueue<>();

        //add every edge incident to source to queue
        for (Edge e : g.incident(s)) {
            q.add(e);
        }

        //while every edge is not processed
        while (!q.isEmpty()) {
            Edge e = q.remove(); // extract min weight edge e(u,v) with u.seen = true
            Vertex v = get(e.fromVertex()).seen ? e.toVertex() : e.fromVertex(); // get v from e(u,v)
            if (get(v).seen) {
                continue; // discard if other end is also processed
            }
            get(v).seen = true; // this vertex is processed now
            get(v).parent = e.otherEnd(v); // set its predecessor as u
            wmst += e.getWeight(); // update weight

            for (Edge e1 : g.incident(v)) { // for every outgoing edge e1(v, u)
                if (!get(e1.otherEnd(v)).seen) { // if u is not processed, meaning edge (v,u) is not processed, add to queue
                    q.add(e1);
                }
            }
        }
        return wmst;
    }

    /**
     * Initialization process for all prim algorithms
     *
     * @param s Source Vertex of the Graph
     */
    void initialize(Vertex s) {
        for (Vertex u : g) {
            get(u).seen = false;
            get(u).parent = null;
            get(u).d = Integer.MAX_VALUE;
        }
        wmst = 0;
        get(s).d = 0;
    }

    /**
     * Static method to run any algorithm implemented in this class
     *
     * @param g Graph
     * @param s Source
     * @param choice Algorithm choice
     * @return MSL class object
     */
    public static MST mst(Graph g, Vertex s, int choice) {
        MST m = new MST(g);
        switch (choice) {
            case 0:
                m.kruskal();
                break;
            case 1:
                m.prim1(s);
                break;
            case 2:
                m.prim2(s);
                break;
            default:
                m.prim3(s);
                break;
        }
        return m;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in;
        int choice = 0;  // Kruskal
        if (args.length == 0 || args[0].equals("-")) {
            in = new Scanner(System.in);
        } else {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        }

        if (args.length > 1) {
            choice = Integer.parseInt(args[1]);
        }

        Graph g = Graph.readGraph(in);
        Vertex s = g.getVertex(1);

        Timer timer = new Timer();
        MST m = mst(g, s, choice);
        System.out.println(m.algorithm + "\n" + m.wmst);
        System.out.println(timer.end());
    }

    /**
     * Time class to log execution times of various algorithms
     */
    public static class Timer {

        long startTime, endTime, elapsedTime, memAvailable, memUsed;

        public Timer() {
            startTime = System.currentTimeMillis();
        }

        public void start() {
            startTime = System.currentTimeMillis();
        }

        public Timer end() {
            endTime = System.currentTimeMillis();
            elapsedTime = endTime - startTime;
            memAvailable = Runtime.getRuntime().totalMemory();
            memUsed = memAvailable - Runtime.getRuntime().freeMemory();
            return this;
        }

        @Override
        public String toString() {
            return "Time: " + elapsedTime + " msec.\n" + "Memory: " + (memUsed / 1048576) + " MB / " + (memAvailable / 1048576) + " MB.";
        }

    }

}
