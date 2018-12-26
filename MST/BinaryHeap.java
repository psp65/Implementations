package psp170230;

import java.util.NoSuchElementException;

/**
 * BinaryHeap class which has all the functionalities necessary for simulating a
 * Priority Queue.
 *
 * @author Param Parikh, Tej Patel
 *
 * @param <T> Any type which is Comparable
 */
public class BinaryHeap<T extends Comparable<? super T>> {

    /**
     * Array to hold elements of Priority Queue
     */
    Comparable[] pq;

    /**
     * Number of elements inside heap
     */
    private int size;

    /**
     * Constructor for building an empty priority queue using natural ordering
     * of T
     *
     * @param maxCapacity INitial capacity of PriorityQueue
     */
    public BinaryHeap(int maxCapacity) {
        pq = new Comparable[maxCapacity];
        size = 0;
    }

    /**
     * Adds an element in pq
     * <p>
     * Resize pq to double size, if pq is full before addition
     * </p>
     *
     * @param x Element to add in Priority Queue
     * @return true always
     */
    public boolean add(T x) {
        if (size == pq.length) {
            resize();
        }

        pq[size] = x;
        percolateUp(size);
        size++;
        return true;
    }

    /**
     * Adds an element in pq
     * <p>
     * Resize pq to double size, if pq is full before addition
     * </p>
     *
     * @param x Element to add in Priority Queue
     * @return true always
     */
    public boolean offer(T x) {
        return add(x);
    }

    /**
     * Remove element with highest priority
     * <p>
     * Throws exception if pq is empty
     * </p>
     *
     * @return remove and returns element with highest priority in pq
     */
    public T remove() throws NoSuchElementException {
        T result = poll();
        if (result == null) {
            throw new NoSuchElementException("Priority queue is empty");
        } else {
            return result;
        }
    }

    /**
     * Remove element with highest priority
     * <p>
     * Returns null if pq is empty
     * </p>
     *
     * @return remove and returns element with highest priority in pq
     */
    public T poll() {
        if (size == 0) {
            return null;
        }

        T x = (T) pq[0];
        pq[0] = pq[size - 1];
        size--;
        percolateDown(0);
        return x;
    }

    /**
     * Return element with highest priority in pq
     *
     * @return element with highest priority else null
     */
    public T min() {
        return peek();
    }

    /**
     * Return element with highest priority in pq
     *
     * @return element with highest priority else null
     */
    public T peek() {
        return size == 0 ? null : (T) pq[0];
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
     * pq[i] may violate heap order with parent To ensure order constraint, node
     * is percolated up using swaps until correct location is found
     *
     * @param i node from which percolate up operation has to be start
     */
    void percolateUp(int index) {
        Comparable x = pq[index];
        while ((index > 0) && compare(x, pq[parent(index)]) < 0) {
            move(index, pq[parent(index)]);
            index = parent(index);
        }
        move(index, x);
    }

    /**
     * pq[i] may violate heap order with children To ensure order constraint,
     * node is percolated down using swaps until correct location is found
     *
     * @param i node from which percolate down operation has to be start
     */
    void percolateDown(int index) {
        Comparable x = pq[index];
        int l_child = leftChild(index);

        while (l_child <= size - 1) {
            if ((l_child < size - 1) && compare(pq[l_child + 1], pq[l_child]) < 0) {
                l_child++;
            }

            if (compare(x, pq[l_child]) <= 0) {
                break;
            }

            move(index, pq[l_child]);
            index = l_child;
            l_child = leftChild(index);
        }

        move(index, x);
    }

    /**
     * Assign x to pq[dest]. Indexed heap will override this method
     *
     * @param dest index to update pq
     * @param x element to replace previous one
     */
    void move(int dest, Comparable x) {
        pq[dest] = x;
    }

    /**
     * Compares 2 element based on their comparator function
     *
     * @param a First Element
     * @param b Second Element
     * @return -1,0,1 based on comparison
     */
    int compare(Comparable a, Comparable b) {
        return ((T) a).compareTo((T) b);
    }

    /**
     * Create a heap from elements of array pq
     */
    void buildHeap() {
        for (int i = parent(size - 1); i >= 0; i--) {
            percolateDown(i);
        }
    }

    /**
     * Checks if pq is empty
     *
     * @return true if size is 0, otherwise false
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns number of elements in pq
     *
     * @return size of pq
     */
    public int size() {
        return size;
    }

    /**
     * Resize pq to double the current size
     */
    void resize() {
        Comparable[] spill = new Comparable[2 * pq.length];
        System.arraycopy(pq, 0, spill, 0, pq.length);
        pq = spill;
    }

    /**
     * Interface to keep track of Index of element inside the priority queue
     */
    public interface Index {

        /**
         * Set location of element on this index
         *
         * @param index location where element has to be set
         */
        public void putIndex(int index);

        /**
         * Get index of the element inside pq
         *
         * @return Index of the element in pq
         */
        public int getIndex();
    }

    /**
     * IndexedHeap class which has functionality of PriorityQueue and in
     * addition keeps track of location of each element inside PQ, such that
     * fetching of element can be done in constant time
     *
     * @param <T> Any type which is Comparable, Indexable and extends BinaryHeap
     * class
     */
    public static class IndexedHeap<T extends Index & Comparable<? super T>> extends BinaryHeap<T> {

        /**
         * Build a priority queue with a given initial capacity
         */
        IndexedHeap(int capacity) {
            super(capacity);
        }

        /**
         * Restore heap order property after the priority of x has decreased
         */
        void decreaseKey(T x) {
            pq[x.getIndex()] = x;
            percolateUp(x.getIndex());
        }

        /**
         * Assign x to pq[i] and set index of element x to i.
         *
         * @param i index to update pq
         * @param x element to replace previous one
         */
        @Override
        void move(int i, Comparable x) {
            super.move(i, x);
            ((T) x).putIndex(i);
        }
    }

}
