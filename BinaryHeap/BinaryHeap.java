package psp170230;

import java.util.Comparator;
import java.util.Scanner;

/**
 * @author Param Parikh
 * @param <T>
 */
public class BinaryHeap<T extends Comparable<? super T>> {

    /**
     * Array to hold elements of Priority Queue
     */
    private T[] pq;

    /**
     * Function to compare two elements
     */
    private Comparator<T> comp;

    /**
     * Number of elements inside heap
     */
    private int size;

    /**
     * Constructor for building an empty priority queue using natural ordering
     * of T
     *
     * @param q Array in which Priority queue will be build
     */
    public BinaryHeap(T[] q) {
        // Use a lambda expression to create comparator from compareTo
        this(q, (T a, T b) -> a.compareTo(b));
    }

    /**
     * Constructor for building an empty priority queue with custom comparator
     *
     * @param q Array in which Priority queue will be build
     * @param c Comparator to be used while comparing two elements
     */
    public BinaryHeap(T[] q, Comparator<T> c) {
        pq = q;
        comp = c;
        size = 0;
    }

    /**
     * Adds an element in pq
     * <p>
     * Throws exception if pq is full </p>
     *
     * @param x Element to add in Priority Queue
     * @throws java.lang.Exception if pq is full
     */
    public void add(T x) throws Exception {
        if (size == pq.length) {
            throw new Exception("OVERFLOW");
        }

        pq[size] = x;
        percolateUp(size);
        size++;
    }

    /**
     * Adds an element in pq
     * <p>
     * Returns false if pq is full </p>
     *
     * @param x Element to add in Priority Queue
     * @return true if pq is not full, else false
     */
    public boolean offer(T x) {
        if (size == pq.length) {
            return false;
        }

        pq[size] = x;
        percolateUp(size);
        size++;
        return true;
    }

    /**
     * Remove element with highest priority
     * <p>
     * Throws exception if pq is empty </p>
     *
     * @return remove and returns element with highest priority in pq
     * @throws java.lang.Exception if pq is empty
     */
    public T remove() throws Exception {
        if (size == 0) {
            throw new Exception("PQ is empty.");
        }

        T x = pq[0];
        pq[0] = pq[size - 1];
        pq[size - 1] = null;
        size--;
        percolateDown(0);
        return x;
    }

    /**
     * Remove element with highest priority
     * <p>
     * Returns null if pq is empty </p>
     *
     * @return remove and returns element with highest priority in pq
     */
    public T poll() {
        if (size == 0) {
            return null;
        }

        T x = pq[0];
        pq[0] = pq[size - 1];
        pq[size - 1] = null;
        size--;
        percolateDown(0);
        return x;
    }

    /**
     * Return element with highest priority in pq
     * @return element with highest priority else null
     */
    public T peek() {
        return size == 0 ? null : pq[0];
    }

    /**
     * pq[i] may violate heap order with parent To ensure order constraint, node
     * is percolated up using swaps until correct location is found
     * 
     * @param i node from which percolate up operation has to be start
     */
    private void percolateUp(int i) {
        T x = pq[i];
        while ((i > 0) && (pq[parent(i)].compareTo(x) == 1)) {
            pq[i] = pq[parent(i)];
            i = parent(i);
        }
        pq[i] = x;
    }

    /**
     * pq[i] may violate heap order with children To ensure order constraint,
     * node is percolated down using swaps until correct location is found
     * 
     * @param i node from which percolate down operation has to be start
     */
    private void percolateDown(int i) {
        T x = pq[i];
        int l_child = 2 * i + 1;

        while (l_child <= size - 1) {
            if ((l_child < size - 1) && (pq[l_child].compareTo(pq[l_child + 1]) == 1)) {
                l_child++;
            }

            if (pq[l_child].compareTo(x) != -1) {
                break;
            }

            pq[i] = pq[l_child];
            i = l_child;
            l_child = 2 * i + 1;
        }
        pq[i] = x;
    }

    /**
     * Assign x to pq[i]. Indexed heap will override this method
     * 
     * @param i index to update pq
     * @param x element to replace previous one
     */
    void move(int i, T x) {
        pq[i] = x;
    }

    /**
     * Return parent of current node
     * 
     * @param i node
     * @return parent node
     */
    int parent(int i) {
        return (i - 1) / 2;
    }

    /**
     * Returns left child of node Right child can be calculated by adding 1 to
     * this
     * 
     * @param i node
     * @return left child node
     */
    int leftChild(int i) {
        return 2 * i + 1;
    }

    /**
     * Print PQ in order of priority (high to low priority)
     */
    void print() {
        System.out.print(size + ": ");
        for (T x : pq) {
            System.out.print(x + " ");
        }
        System.out.println();
    }

    /**
     * Driver to run code
     * 
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        int n;
        System.out.print("Give capacity: ");
        n = in.nextInt();
        Integer q[] = new Integer[n];
        BinaryHeap<Integer> pq = new BinaryHeap<>(q);

        whileloop:
        while (in.hasNext()) {
            int com = in.nextInt();
            switch (com) {
                case 1:  // add an element to Queue
                    System.out.print("Give Value: ");
                    n = in.nextInt();
                    pq.add(n);
                    pq.print();
                    break;
                case 2:  // offer something to Queue
                    System.out.print("Give Value: ");
                    n = in.nextInt();
                    pq.offer(n);
                    pq.print();
                    break;
                case 3: //Peek from Queue
                    System.out.println(pq.peek());
                    break;
                case 4: //Remove from Queue
                    System.out.println(pq.remove());
                    pq.print();
                    break;
                case 5: // poll from Queue
                    System.out.println(pq.poll());
                    pq.print();
                    break;
                default:  // Exit loop
                    break whileloop;
            }
        }
        in.close();
    }
}
