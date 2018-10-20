package psp170230;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * A HashSet which utilizes first level Cuckoo hashing mechanism and Spill-over
 * memory area to prevent the on-the-fly rebuild mechanism.
 *
 * The elements in spill area will be added to master table at the time of
 * resize, if possible.
 *
 * @author Param Parikh
 * @param <T> Key to store in Set
 */
public class CuckooHashing<T> {

    /**
     * Desired current elements by capacity ratio
     */
    private final float LOAD_FACTOR = 0.75f;

    /**
     * Multiply the previous capacity by this constant when LOAD_FACTOR is
     * reached
     */
    private final int MULTIPLIER = 2;

    /**
     * A safe guard just to prevent memory leakage
     */
    private final int MAX_CAPACITY = 1 << 30;

    /**
     * Number of hash functions in Cuckoo mechanism
     */
    private final int HASH_FUNCTIONS = 4;

    /**
     * Each hash function will utilize this many coefficients to generate a
     * final hash value
     */
    private final int NUMBER_OF_COEFF = 2;

    /**
     * The spill area for elements which are unable to accommodate in master
     * table
     */
    private final ArrayList<LinkedList<T>> spill;

    /**
     * Seed to generate random numbers, which along with coefficients generates
     * a hash function
     */
    private final Random rand;

    /**
     * Master table to store elements
     */
    private T[][] master;

    /**
     * Coefficients of hash function
     */
    private int[][] coeffs;

    /**
     * An utility array to store all the computed locations of a particular
     * element inside each master tables
     *
     * A call to Contains( ) will write this array
     */
    private int[] positions;

    /**
     * Location of table, inside which element is located. Combined use of this
     * and positions[foundAt] will fetch the element from it's location
     *
     * -1, if element is not present
     */
    private int foundAt;

    /**
     * True is element exists in Master tables, else False
     */
    private boolean inMaster;

    /**
     * Initial capacity of the master table
     */
    private int capacity = 1 << 4;

    /**
     * Total number of elements in Set
     */
    private int size = 0;

    /**
     * Initializing space for master table, spill area, coefficients for hash
     * functions
     */
    public CuckooHashing() {
        master = (T[][]) new Object[HASH_FUNCTIONS][];
        for (int i = 0; i < HASH_FUNCTIONS; i++) {
            master[i] = (T[]) new Object[capacity];
        }

        spill = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            spill.add(new LinkedList<>());
        }

        rand = new Random();
        setCoeffs();
    }

    /**
     * Utility function to increment the size of Master table, when set reaches
     * to it's LOAD_FACTOR
     *
     * After increment of master and re-adding previously existing elements,
     * this will try to accommodate existing elements from Spill area to master
     * table
     */
    private void resize() throws Exception {
        int old_capacity = capacity;
        capacity *= MULTIPLIER;

        if (capacity > MAX_CAPACITY) {
            throw new Exception("...MAXIMUM MEMORY LIMIT REACHED...");
        }

        T[][] temp = master;
        master = (T[][]) new Object[HASH_FUNCTIONS][];
        for (int i = 0; i < HASH_FUNCTIONS; i++) {
            master[i] = (T[]) new Object[capacity];
        }

        for (int table = 0; table < HASH_FUNCTIONS; table++) {
            for (int key = 0; key < old_capacity; key++) {
                if (temp[table][key] != null) {
                    add(temp[table][key]);
                }
            }
        }

        LinkedList<T> old;
        for (int i = 0; i < 32; i++) {
            if (spill.get(i).size() > 0) {
                old = new LinkedList<>();
                for (T x : spill.get(i)) {
                    if (!addToMaster(x)) {
                        old.add(x);
                    }
                }
                spill.set(i, old);
            }
        }
    }

    /**
     * Setting up coefficients for hash functions from a randomly generated seed
     */
    private void setCoeffs() {
        coeffs = new int[HASH_FUNCTIONS][NUMBER_OF_COEFF];

        for (int i = 0; i < HASH_FUNCTIONS; i++) {
            for (int j = 0; j < NUMBER_OF_COEFF; j++) {
                coeffs[i][j] = rand.nextInt();
            }
        }
    }

    /**
     * Function to find element's location in Master table
     *
     * Hashes the element and stores it's possible location in positions[] which
     * later used by other methods to find the precise location of the element
     *
     * @param x Element to hash
     */
    private void hashMaster(T x) {
        positions = new int[HASH_FUNCTIONS];
        int key = x.hashCode();

        //TODO This function should be extended in such way that it can handle more than 2 number of operations on coeffs, in order to genetrate the final hash value
        for (int i = 0; i < HASH_FUNCTIONS; i++) {
            int calculated = key * coeffs[i][0];
            calculated += coeffs[i][1];
            calculated %= capacity;
            positions[i] = Math.abs(calculated);
        }
    }

    /**
     * Function to find element's location in Spill area
     *
     * Hashes the element and store it's location in positions[0]
     *
     * Hashing function used for spill over memory ares is number of set bits in
     * element's hashCode representation
     *
     * @param x Element to hash
     */
    private void hashSpill(T x) {
        positions = new int[1];
        int key = x.hashCode();
        positions[0] = Integer.bitCount(key);
    }

    /**
     * Checks whether given element already exists in set or not
     *
     * @param x Element to check for
     * @return True if element present in set, otherwise false
     */
    public boolean contains(T x) {
        hashMaster(x);
        for (int i = 0; i < HASH_FUNCTIONS; i++) {
            if (master[i][positions[i]] != null && master[i][positions[i]].equals(x)) {
                foundAt = i;
                inMaster = true;
                return true;
            }
        }

        inMaster = false;
        hashSpill(x);
        if ((foundAt = spill.get(positions[0]).indexOf(x)) > 0) {
            return true;
        }

        foundAt = -1;
        return false;
    }

    /**
     * Function to add element in master table
     *
     * @param x Element to add in Master table
     * @return True if addition is successful, otherwise false
     */
    private boolean addToMaster(T x) {
        hashMaster(x);
        for (int i = 0; i < HASH_FUNCTIONS; i++) {
            if (master[i][positions[i]] == null) {
                master[i][positions[i]] = x;
                return true;
            }
        }
        return false;
    }

    /**
     * Function to add element in spill memory
     *
     * @param x Element to add in spill memory
     */
    private void addToSpill(T x) {
        hashSpill(x);
        spill.get(positions[0]).add(x);
    }

    /**
     * Add given element in set, it it's not there.
     *
     * After adding the new element, if the set reaches to it's LOAD_FACTOR,
     * resize() is called in order to maintain the LOAD_FACTOR
     *
     * @param x Element to add in set
     * @return true if element is added, otherwise false
     * @throws java.lang.Exception
     */
    public boolean add(T x) throws Exception {
        if (contains(x)) {
            return false;
        }

        if (!addToMaster(x)) {
            addToSpill(x);
        }

        size++;
        if (size >= HASH_FUNCTIONS * capacity * LOAD_FACTOR) {
            resize();
        }
        return true;
    }

    /**
     * Remove given element from the set, if it exists
     *
     * @param x Element to remove from set
     * @return true if element was present, otherwise false
     */
    public boolean remove(T x) {
        if (!contains(x)) {
            return false;
        }

        if (inMaster) {
            master[foundAt][positions[foundAt]] = null;
        } else {
            spill.get(positions[0]).remove(foundAt);
        }

        size--;
        return true;
    }

    /**
     * Function to check whether set is having any element inside
     *
     * @return true is set is empty, otherwise false
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Function to check total number of elements inside set
     *
     * @return number of elements in set
     */
    public int size() {
        return size;
    }

}
