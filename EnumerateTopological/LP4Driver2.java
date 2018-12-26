package psp170230_lp4;

import java.util.Scanner;

/**
 *
 * @author Param Parikh
 */
public class LP4Driver2 {

    public static void main(String[] args) throws Exception {
        boolean details = false;
        String graph = "11 12   2 4 1   2 5 1   3 5 1   3 6 1   4 7 1   5 7 1   5 8 1   6 8 1   6 9 1   7 10 1   8 10 1   9 10 1      0 3 2 3 2 1 3 2 4 1 0";
        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new java.io.File(args[0])) : new Scanner(graph);
        if (args.length > 1) {
            details = true;
        }
        rbk.Graph g = rbk.Graph.readDirectedGraph(in);
        System.out.println(EnumerateTopological.countTopologicalOrders(g));
    }
}
