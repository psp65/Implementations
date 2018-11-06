package psp170230;

import java.util.Random;

/**
 * @author Param Parikh psp170230 
 */
public class SP9 {

    public static Random random = new Random();
    public static int numTrials = 100;
    private static final int T = 17;

    /**
     * Helper function for insertion sort
     * @param arr Array to be sorted
     */
    public static void insertionSort(int[] arr) {
        insertionSort(arr, 0, arr.length - 1);
    }

    /**
     * Function to perform insertion sort
     * @param arr Array to be sorted
     * @param p left index
     * @param r right index
     */
    private static void insertionSort(int[] arr, int p, int r) {
        for (int i = p + 1; i <= r; i++) {
            int key = arr[i];
            int j = i - 1;

            /* Move elements of arr[0..i-1], that are 
               greater than key, to one position ahead 
               of their current position */
            while (j >= p && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }

    /**
     * Helper function for mergesort take 1
     * @param arr Array to be sorted
     */
    public static void mergeSort1(int[] arr) {
        mergeSort1(arr, 0, arr.length - 1);
    }

    /**
     * MergeSort take1
     * @param arr Array to be sorted
     * @param p left index
     * @param r right index
     */
    private static void mergeSort1(int[] arr, int p, int r) {
        if (p >= r) {
            return;
        }

        int q = (p + r) / 2;
        mergeSort1(arr, p, q);
        mergeSort1(arr, q + 1, r);
        merge1(arr, p, q, r);
    }

    /**
     * Performs merging operation for given indexes on an array
     * @param arr  Array to be merged, First subarray [p...q], Second subarray [q+1...r]
     * @param p left index of first subarray
     * @param q last index of first subarray
     * @param r last index of second subarray
     */
    private static void merge1(int[] arr, int p, int q, int r) {
        int[] L = new int[q - p + 1];
        int[] R = new int[r - q];

        System.arraycopy(arr, p, L, 0, q - p + 1);
        System.arraycopy(arr, q + 1, R, 0, r - q);

        int i = 0, j = 0;
        for (int k = p; k <= r; k++) {
            if (j >= R.length || (i < L.length && L[i] <= R[j])) {
                arr[k] = L[i++];
            } else {
                arr[k] = R[j++];
            }
        }
    }

    /**
     * Helper function for mergesort take 2
     * @param arr Array to be sorted
     */
    public static void mergeSort2(int[] arr) {
        int[] brr = new int[arr.length];
        mergeSort2(arr, brr, 0, arr.length);
    }

    /**
     * MergeSort take2
     * @param arr Array to be sorted
     * @param brr Array for temporary usage
     * @param left leftmost index of array
     * @param n size of array
     */
    private static void mergeSort2(int[] arr, int[] brr, int left, int n) {
        if (n < T) { //if size is less than threshold
            insertionSort(arr, left, left + n - 1);
            return;
        }

        int Ln = n / 2;
        mergeSort2(arr, brr, left, Ln);
        mergeSort2(arr, brr, left + Ln, n - Ln);
        merge2(arr, brr, left, left + Ln - 1, left + n - 1);
    }

    /**
     * Performs merging operation for given indexes on an array
     * @param arr  Array to be merged, First subarray [p...q], Second subarray [q+1...r]
     * @param brr Array for temporary usage
     * @param p left index of first subarray
     * @param q last index of first subarray
     * @param r last index of second subarray
     */
    private static void merge2(int[] arr, int[] brr, int p, int q, int r) {
        System.arraycopy(arr, p, brr, p, r - p + 1);

        int i = p, j = q + 1;
        for (int k = p; k <= r; k++) {
            if (j > r || (i <= q && brr[i] <= brr[j])) {
                arr[k] = brr[i++];
            } else {
                arr[k] = brr[j++];
            }
        }
    }

    /**
     * Helper function for mergesort take 3
     * @param arr Array to be sorted
     */
    public static void mergeSort3(int[] arr) {
        int[] brr = new int[arr.length];
        System.arraycopy(arr, 0, brr, 0, arr.length);
        mergeSort3(arr, brr, 0, arr.length);
    }

    /**
     * MergeSort take3
     * @param arr Array to be sorted
     * @param brr Array for temporary usage
     * @param left leftmost index of array
     * @param n size of array
     */
    private static void mergeSort3(int[] arr, int[] brr, int left, int n) {
        if (n < T) { //if size is less than threshold
            insertionSort(arr, left, left + n - 1);
            return;
        }

        int Ln = n / 2;
        mergeSort3(brr, arr, left, Ln);
        mergeSort3(brr, arr, left + Ln, n - Ln);
        merge3(arr, brr, left, left + Ln - 1, left + n - 1);
    }

    /**
     * Performs merging operation for given indexes on an array
     * @param arr  Array to be merged, First subarray [p...q], Second subarray [q+1...r]
     * @param brr Array for temporary usage
     * @param p left index of first subarray
     * @param q last index of first subarray
     * @param r last index of second subarray
     */
    private static void merge3(int[] arr, int[] brr, int p, int q, int r) {
        int i = p, j = q + 1, k = p;
        while (i <= q && j <= r) {
            if (brr[i] <= brr[j]) {
                arr[k++] = brr[i++];
            } else {
                arr[k++] = brr[j++];
            }
        }

        while (i <= q) {
            arr[k++] = brr[i++];
        }

        while (j <= r) {
            arr[k++] = brr[j++];
        }
    }

    public static void main(String[] args) {
        int n = 1 << 23;
        int choice = 1 + random.nextInt(4);
        if (args.length > 0) {
            n = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            choice = Integer.parseInt(args[1]);
        }
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }
        
        Timer timer = new Timer();
        switch (choice) {
            case 1:
                Shuffle.shuffle(arr);
                numTrials = 1;
                insertionSort(arr);
                break;
            case 2:
                for (int i = 0; i < numTrials; i++) {
                    Shuffle.shuffle(arr);
                    mergeSort1(arr);
                }
                break;
            case 3:
                for (int i = 0; i < numTrials; i++) {
                    Shuffle.shuffle(arr);
                    mergeSort2(arr);
                }
                break;
            case 4:
                for (int i = 0; i < numTrials; i++) {
                    Shuffle.shuffle(arr);
                    mergeSort3(arr);
                }
                break;
        }
        timer.end();
        timer.scale(numTrials);
        System.out.println("Choice: " + choice + ", "+ "Elements: " + n + "\n" + timer);
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
                int j = random.nextInt(i);
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

