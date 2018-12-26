package psp170230;

import java.util.PriorityQueue;
import java.util.Random;

/**
 * Class that computes k largest elements from an array Class shows comparison
 * of 2 popular approach for this task, 1) Select approach 2) Binary HEap
 * approach.
 *
 * <p>
 * Clearly Select algorithm is significantly faster compared to Binary heap one
 * </p>
 *
 * @author Param Parikh
 */
public class K_LargestElements {

    /**
     * Stream to generate pseudo random numbers which can be treated as array
     * elements
     */
    private static final Random RAND = new Random();

    /**
     * Minimum number of required elements to perform Divide, Otherwise restrict
     * the branching and apply Insertion sort
     */
    private static final int T = 17;

    /**
     * Perform insertion sort on elements indexed from p to r in array arr
     *
     * @param arr input array
     * @param p starting index (included)
     * @param r ending index (included)
     */
    private static void insertionSort(int[] arr, int p, int r) {
        for (int i = p + 1; i <= r; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= p && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }

    /**
     * Swap elements in array at positions i and j as position parameters
     *
     * @param arr input array
     * @param i location 1
     * @param j location 2
     */
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * Partition array elements from index p to r around arr[r]. Returns the
     * index around which partition has done
     *
     * @param arr input array
     * @param p starting index (included)
     * @param r ending index (included)
     * @return index around which partition has been done
     */
    private static int partition(int[] arr, int p, int r) {

        int x = arr[r];
        int i = p - 1;

        for (int j = p; j <= r - 1; j++) {
            if (arr[j] <= x) {
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, r);
        return i + 1;
    }

    /**
     * Performs a random swap between two elements and calls Partition
     *
     * @param arr input array
     * @param p starting index (included)
     * @param r ending index (included)
     * @return index around which partition has been done by Partition method
     */
    private static int randomizedPartition(int[] arr, int p, int r) {
        int i = p + RAND.nextInt(r + 1 - p);
        swap(arr, i, r);
        return partition(arr, p, r);
    }

    /**
     *
     * Helper to select. Finds index of kth largest element inside the array
     *
     * @param arr input array
     * @param p starting index (included)
     * @param n ending index (included)
     * @param k find k largest elements from the range [p...r]
     * @return index at which kth largest element found
     */
    private static int select(int[] arr, int p, int n, int k) {
        if (n < T) {
            insertionSort(arr, p, p + n - 1);
            return p + n - k;
        }

        int q = randomizedPartition(arr, p, p + n - 1);
        int left = q - p;
        int right = n - left - 1;

        if (right >= k) {
            return select(arr, q + 1, right, k);
        }

        if (right + 1 == k) {
            return q;
        }

        return select(arr, p, left, k - right - 1);
    }

    /**
     * Select approach to get k largest elements from array. MMethod will modify
     * the input array and stores answer to last k elements ie. arr[length-k ...
     * length-1] of array
     *
     * @param arr input array
     * @param k find k largest elements
     */
    public static void select(int[] arr, int k) {
        assert k <= arr.length;
        select(arr, 0, arr.length, k);
    }

    /**
     * Binary heap approach to get k largest element from array. Method will
     * return a newly created array and input array stays unaffected after this
     * operation.
     *
     * @param arr input array
     * @param k find k largest elements
     * @return Object array containing k largest elements
     */
    public static Object[] BinaryHeap(int[] arr, int k) {
        assert k <= arr.length;
        PriorityQueue<Integer> pq = new PriorityQueue<>(k);

        for (int i = 0; i < k; i++) {
            pq.offer(arr[i]);
        }

        for (int i = k; i < arr.length; i++) {
            if (arr[i] > pq.peek()) {
                pq.remove();
                pq.offer(arr[i]);
            }
        }

        return pq.toArray();
    }

    public static void main(String[] args) {
        int n = 1 << 24; //16M elements by default
        int k = n >> 1; //Median
        int choice = 1; //Select Algorithm by default
        int numTrials = 10;

        if (args.length > 0) {
            n = Integer.parseInt(args[0]);
            k = n >> 1;
        }
        if (args.length > 1) {
            choice = Integer.parseInt(args[1]);
        }
        if (args.length > 2) {
            numTrials = Integer.parseInt(args[2]);
        }

        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i + 1;
        }

        Timer timer = new Timer();
        switch (choice) {
            case 1: //Using Select method to find k largest elements
                for (int i = 0; i < numTrials; i++) {
                    Shuffle.shuffle(arr);
                    select(arr, k);
                }
                break;

            case 2: //Using PriorityQueue tp find k largest elements
                for (int i = 0; i < numTrials; i++) {
                    Shuffle.shuffle(arr);
                    BinaryHeap(arr, k);
                }
                break;
        }
        timer.end();
        timer.scale(numTrials);
        System.out.println("Choice: " + choice + ", " + "Elements: " + n + "\n" + timer);
    }

    public static class Timer {

        long startTime, endTime, elapsedTime, memAvailable, memUsed;
        boolean ready;

        public Timer() {
            startTime = System.currentTimeMillis();
            ready = false;
        }

        public void start() {
            startTime = System.currentTimeMillis();
            ready = false;
        }

        public Timer end() {
            endTime = System.currentTimeMillis();
            elapsedTime = endTime - startTime;
            memAvailable = Runtime.getRuntime().totalMemory();
            memUsed = memAvailable - Runtime.getRuntime().freeMemory();
            ready = true;
            return this;
        }

        public long duration() {
            if (!ready) {
                end();
            }
            return elapsedTime;
        }

        public long memory() {
            if (!ready) {
                end();
            }
            return memUsed;
        }

        public void scale(int num) {
            elapsedTime /= num;
        }

        @Override
        public String toString() {
            if (!ready) {
                end();
            }
            return "Time: " + elapsedTime + " msec.\n" + "Memory: " + (memUsed / 1048576) + " MB / " + (memAvailable / 1048576) + " MB.";
        }
    }

    public static class Shuffle {

        public static void shuffle(int[] arr) {
            shuffle(arr, 0, arr.length - 1);
        }

        public static <T> void shuffle(T[] arr) {
            shuffle(arr, 0, arr.length - 1);
        }

        public static void shuffle(int[] arr, int from, int to) {
            int n = to - from + 1;
            for (int i = 1; i < n; i++) {
                int j = RAND.nextInt(i);
                swap(arr, i + from, j + from);
            }
        }

        public static <T> void shuffle(T[] arr, int from, int to) {
            int n = to - from + 1;
            Random random = new Random();
            for (int i = 1; i < n; i++) {
                int j = random.nextInt(i);
                swap(arr, i + from, j + from);
            }
        }

        static void swap(int[] arr, int x, int y) {
            int tmp = arr[x];
            arr[x] = arr[y];
            arr[y] = tmp;
        }

        static <T> void swap(T[] arr, int x, int y) {
            T tmp = arr[x];
            arr[x] = arr[y];
            arr[y] = tmp;
        }

        public static <T> void printArray(T[] arr, String message) {
            printArray(arr, 0, arr.length - 1, message);
        }

        public static <T> void printArray(T[] arr, int from, int to, String message) {
            System.out.print(message);
            for (int i = from; i <= to; i++) {
                System.out.print(" " + arr[i]);
            }
            System.out.println();
        }
    }

}
