package psp170230_lp4;

import rbk.Graph;
import rbk.Graph.Edge;
import rbk.Graph.Factory;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Vertex;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Project Evaluation and Review Techniques is commonly abbreviated to PERT.
 * PERT is a method of analyzing the tasks involved in completing a given project,
 * especially the time needed to complete each task, and to identify the minimum
 * time needed to complete the total project.
 *
 * @author Tej Patel, Param Parikh
 */
public class PERT extends GraphAlgorithm<PERT.PERTVertex> {

    /**
     * Stores topological ordering of the DAG.
     */
    LinkedList<Vertex> topologicalOrder;

    /**
     * Is PERT applicable? If graph is not DAG then yes.
     */
    boolean isValidPertPossible;

    /**
     * Custom vertex used by PERT algorithm which modeles
     * a task in a project.
     */
    public static class PERTVertex implements Factory {
        /**
         * Earliest Completion time.
         */
        int ec;

        /**
         * Latest Completion time.
         */
        int lc;

        /**
         * Allowed slack (lc - sc).
         */
        int slack;

        /**
         * Duration of this task.
         */
        int duration;

        public PERTVertex(Vertex u) {
            ec = 0;
            lc = Integer.MAX_VALUE;
            slack = lc;
            duration = 0;
        }

        /**
         * Creates a PERT vertex from a Graph vertex.
         *
         * @param u Vertex :- Graph vertex
         * @return PERTVertex :- PERTVertex for Vertex u
         */
        @Override
        public PERTVertex make(Vertex u) {
            return new PERTVertex(u);
        }
    }


    public PERT(Graph g) {
        super(g, new PERTVertex(null));
        isValidPertPossible = true;

        // Introducing 2 Vertices s and t.
        // s is predecessor of all nodes.
        // t is successor of all nodes.
        for (int i = 1; i < g.size() - 1; i++) {
            g.addEdge(0, i, 0);
            g.addEdge(i, g.size() - 1, 0);
        }
        topologicalOrder = (LinkedList<Vertex>) DFS.topologicalOrder1(g);
    }

    /**
     * Finds the ec, lc and slack for all tasks utilizing
     * the topological ordering.
     */
    public void findPert() {

        // If graph is not a DAG return null
        if (topologicalOrder == null) {
            isValidPertPossible = false;
            return;
        }

        //LI - ec for all tasks before u is calculated
        for (Vertex u : topologicalOrder) {
            for (Edge e : g.incident(u)) {
                Vertex v = e.otherEnd(u);
                if (get(u).ec + get(v).duration > get(v).ec) {
                    get(v).ec = get(u).ec + get(v).duration;
                }
            }
        }

        int maxTime = criticalPath();

        for (Vertex u : g) {
            get(u).lc = maxTime;
        }

        Iterator<Vertex> descIt = topologicalOrder.descendingIterator();

        //LI - lc of all tasks after u is calculated
        while (descIt.hasNext()) {
            Vertex u = descIt.next();
            for (Edge e : g.incident(u)) {
                Vertex v = e.otherEnd(u);
                if (get(v).lc - get(v).duration < get(u).lc) {
                    get(u).lc = get(v).lc - get(v).duration;
                }
            }
            get(u).slack = get(u).lc - get(u).ec;
        }
    }

    /**
     * Sets duration of the vertex.
     *
     * @param u Vertex :- Graph vertex for which duraiton is to be set.
     * @param d int :- Duration amount.
     */
    public void setDuration(Vertex u, int d) {
        get(u).duration = d;
    }

    /**
     * Is pert possible?
     *
     * @return Boolean :- True if possible else false.
     */
    public boolean pert() {
        return isValidPertPossible;
    }

    /**
     * Gets ec of task.
     *
     * @param u Vertex :- Task of whose ec is needed.
     * @return int :- ec of task.
     */
    public int ec(Vertex u) {
        return get(u).ec;
    }

    /**
     * Gets lc of task.
     *
     * @param u Vertex :- Task of whose lc is needed.
     * @return int :- lc of task.
     */
    public int lc(Vertex u) {
        return get(u).lc;
    }

    /**
     * Gets slack of task.
     *
     * @param u Vertex :- Task of whose slack is needed.
     * @return int :- slack of task.
     */
    public int slack(Vertex u) {
        return get(u).slack;
    }

    /**
     * Gets the max earliest completion time of last tasks of project.
     *
     * @return int :- Time of completion of the project.
     */
    public int criticalPath() {
        return get(g.getVertex(g.size())).ec;
    }

    /**
     * Is the given task critical?
     *
     * @param u Vertex :- Task .
     * @return boolean :- True if the task is critical else false.
     */
    public boolean critical(Vertex u) {
        return get(u).slack == 0;
    }

    /**
     * Total number of critical tasks in project.
     *
     * @return int :- number of critical tasks.
     */
    public int numCritical() {
        int numCritical = 0;
        for (Vertex u : g) {
            if (get(u).slack == 0) {
                numCritical++;
            }
        }
        return numCritical;
    }

    /**
     * Run the algirithm on project represented as graph given duration of each task.
     *
     * @param g        Graph :- Project represented as Graph.
     * @param duration int[] :- Duraiton of all tasks.
     * @return PERT :- Pert object having all required information.
     */
    public static PERT pert(Graph g, int[] duration) {
        PERT pert = new PERT(g);
        for (Vertex u : g) {
            pert.setDuration(u, duration[u.getIndex()]);
        }
        pert.findPert();
        if (!pert.pert()) {
            return null;
        }
        return pert;
    }

}
