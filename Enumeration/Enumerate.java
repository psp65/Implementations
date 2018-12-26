package psp170230_lp4;

/**
 * Algorithm to find all the permutations.
 *
 * @author Tej Patel, Param Parikh
 */
public class Enumerate<T> {

    /**
     * Array for which we want to find enumerations.
     */
    T[] arr;

    /**
     * How many elements are to be permuted?
     */
    int k;

    /**
     * Stores count of all permutations.
     */
    int count;

    /**
     * Decides if the next element is consistent with constraints.
     */
    Approver<T> app;

    public static class Approver<T> {

        // Extend permutation by item?
        public boolean select(T item) {
            return true;
        }

        // Backtrack selected item
        public void unselect(T item) {
        }

        /* Visit a permutation or combination */
        public void visit(T[] array, int k) {
            for (int i = 0; i < k; i++) {
                System.out.print(array[i] + " ");
            }
            System.out.println();
        }
    }

    public Enumerate(T[] arr, int k, Approver<T> app) {
        this.arr = arr;
        this.k = k;
        this.count = 0;
        this.app = app;
    }

    public Enumerate(T[] arr, Approver<T> app) {
        this(arr, arr.length, app);
    }

    public Enumerate(T[] arr, int k) {
        this(arr, k, new Approver<>());
    }

    public Enumerate(T[] arr) {
        this(arr, arr.length, new Approver<>());
    }

    /**
     * Permutes the array with the condition that
     * last c elements are left to permute. Means
     * that first k-c elements are permuted.
     *
     * @param c int :- Elements from end that are to be permuted.
     */
    public void permute(int c) {
        if (c == 0) {
            visit(arr);
        } else {
            int d = k - c;
            permute(c - 1);
            for (int i = d + 1; i < arr.length; i++) {
                swap(d, i);
                permute(c - 1);
                swap(d, i);
            }
        }
    }

    public void visit(T[] array) {
        count++;
        app.visit(array, k);
    }

    void swap(int i, int j) {
        T tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    void reverse(int low, int high) {
        while (low < high) {
            swap(low, high);
            low++;
            high--;
        }
    }

    public static <T> Enumerate<T> permute(T[] arr, int k) {
        Enumerate<T> e = new Enumerate<>(arr, k);
        e.permute(k);
        return e;
    }
}