package psp170230;

import rbk.BFSOO;
import rbk.Graph;
import rbk.Graph.Vertex;

import java.io.File;
import java.util.Scanner;

/**
 * Class to computer diameter of a graph. Graph should be Acyclic, Undirected
 * and Connected in order to generate the correct answer using this algorithm.
 *
 * @author Param Parikh
 */
public class DiameterOfTree {

    /**
     * PRE: Graph g should be Acyclic, Undirected and Connected.
     * In case of less than Two vertices, returned answer will be 0
     * 
     * @param g Tree for which diameter has to be found
     * @return Diameter of tree
     */
    public static int diameter(Graph g) {

        if (g == null || g.size() < 2) {
            return 0;
        }

        BFSOO bfsGraph;
        Vertex src;
        int diameter = 0;

        src = g.getVertexArray()[0]; // Setting first vertice as source for bfs
        bfsGraph = BFSOO.breadthFirstSearch(g, src);
        for (Vertex u : g) {
            if (bfsGraph.getDistance(u) > diameter) {
                diameter = bfsGraph.getDistance(u);
                src = u;
            }
        }

        bfsGraph = BFSOO.breadthFirstSearch(g, src); // Setting farthest element from previous source as new source for bfs
        diameter = 0;
        for (Vertex u : g) {
            if (bfsGraph.getDistance(u) > diameter) {
                diameter = bfsGraph.getDistance(u);
            }
        }

        return diameter;
    }

    public static void main(String[] args) throws Exception {
        String string = "10 9   1 2 1   2 4 1   2 5 1   4 8 1   1 3 1   3 6 1   3 7 1   7 9 1  7 10 1";
        Scanner in;
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);
        Graph g = Graph.readGraph(in);
        System.out.println(diameter(g));
    }

}