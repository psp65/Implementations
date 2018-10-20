import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

/**
 * Randomized Skip List Implementation. Rebuild is implemented to convert a Skip
 * List to a perfect possible structure to achieve expected O(logn) search.
 * 
 * @author Axat Chaudhari, Jaiminee Kataria, Param Parikh, Tej Patel
 *
 * @param <T>
 */
public class SkipList<T extends Comparable<? super T>> {

    /**
     * Maximum possible levels of skip list.
     */
    static final int POSSIBLE_LEVELS = 33;

    private static class Entry<T> {
        /**
         * Element that is to be stored.
         */
        private T element;

        /**
         * Array of next pointers.
         */
        private Entry<T>[] next;

        /**
         * Pointer of previous node.
         */
        private Entry<T> prev;

        /**
         * At index i, stores count of elements between current element and next[i].
         */
        private int[] span;

        @SuppressWarnings("unchecked")
        public Entry(T x, int lev) {
            element = x;
            next = new Entry[lev];
            span = new int[lev];
        }

        @SuppressWarnings("unchecked")
        public void setNext(int lev) {
            next = new Entry[lev];
            span = new int[lev];
        }
    }

    /**
     * Head and Tail pointers of Skip List.
     */
    private Entry<T> head, tail;

    /**
     * Array to store the trace back of elements required to find a given element.
     * Helpful for updating next pointers and span at various levels.
     */
    private Entry<T>[] last;

    /**
     * Current size and maxLevel of SkipList.
     */
    private int size, maxLevel;

    /**
     * Generate random levels at each add.
     */
    private Random rand;

    /**
     * Keeps track for no of nodes traveled at a particular level which helps in
     * updating span when a new node is added.
     */
    private int[] traveled;

    @SuppressWarnings("unchecked")
    public SkipList() {
        /**
         * Initializing head and tail as a dummy nodes with maximum possible levels
         */
        head = new Entry<>(null, POSSIBLE_LEVELS);
        tail = new Entry<>(null, POSSIBLE_LEVELS);
        last = new Entry[POSSIBLE_LEVELS];
        traveled = new int[POSSIBLE_LEVELS];

        /**
         * Initializing skip list
         */
        for (int i = 0; i < POSSIBLE_LEVELS; i++) {
            head.next[i] = tail;
            head.span[i] = 1;
            traveled[i] = 0;
            last[i] = head;
            tail.prev = head;
        }
        size = 0;
        maxLevel = 1;
        rand = new Random();
    }

    /**
     * Starting from maximum level, searches for the minimum possible element before
     * x. Each time an element less than x is found, go to that element else search
     * from lower levels. last and traveled are updated accordingly.
     * 
     * @param x Element to be found
     */
    private void find(T x) {
        Entry<T> p = head;
        for (int i = maxLevel - 1; i >= 0; i--) {
            traveled[i] = 0;

            while (p.next[i].element != null && p.next[i].element.compareTo(x) < 0) {
                traveled[i] += p.span[i];
                p = p.next[i];
            }
            last[i] = p;
        }
    }

    /**
     * Fast way to generate random number. Traditional coin flip may be slow when it
     * keeps on generating heads. Sets level to the generated random number.
     * 
     * @return Generated level
     */
    private int chooseLevel() {
        int lev = 1 + Integer.numberOfTrailingZeros(rand.nextInt());

        if (lev > maxLevel) {
            maxLevel = lev;
        }

        return lev;
    }

    /**
     * Adds element to a skip list at appropriate location so that the list remains
     * sorted.
     * 
     * @param x Element to be added
     * @return True if element is added, False if element is already present
     */
    public boolean add(T x) {
        int level = chooseLevel();
        if (contains(x))
            return false;

        for (int i = maxLevel; i < POSSIBLE_LEVELS; i++)
            head.span[i]++;

        // Counter keeps track of span or distance traveled at previous level to
        // the current element and uses that to update current level span.
        int counter = last[0].span[0];
        Entry<T> entry = new Entry<>(x, level);
        for (int i = 0; i < level; i++) {
            if (i > 0)
                counter += traveled[i - 1];
            entry.next[i] = last[i].next[i]; // Sets pointers

            // If current level obstructs previous level then span is updated by
            // calculating total span - span to reach current element
            entry.span[i] = last[i].span[i] + 1 - counter;
            last[i].span[i] = counter;
            last[i].next[i] = entry;
        }

        // Increment span of elements that are above level of currently added
        // element
        for (int i = maxLevel - 1; i >= level; i--) {
            last[i].span[i]++;
        }
        entry.next[0].prev = entry;
        size++;
        return true;
    }

    /**
     * Find smallest element that is greater or equal to x
     * 
     * @param x Element of which ceiling is to be found
     * @return Ceiling of x
     */
    public T ceiling(T x) {
        find(x);
        return last[0].next[0].element;
    }

    /**
     * Does list contain x?
     * 
     * @param x Element to be searched
     * @return True if element found else False
     */
    public boolean contains(T x) {
        find(x);
        return last[0].next[0] != tail && last[0].next[0].element.compareTo(x) == 0;
    }

    /**
     * Return first element of list
     * 
     * @return First Element
     */
    public T first() {
        return head.next[0] != tail ? head.next[0].element : null;
    }

    /**
     * Find largest element that is less than or equal to x
     * 
     * @param x Element of which floor is to be found
     * @return Floor of x
     */
    public T floor(T x) {
        if (contains(x)) {
            return last[0].next[0].element;
        }
        return last[0].element;

    }

    /**
     * Return element at index n of list. First element is at index 0.
     * 
     * @param Index
     * @return Element at index n or null if index is inappropriate
     */
    public T get(int n) {
        return getLog(n);
    }

    /**
     * O(n) algorithm for getting element
     * 
     * @param n Index
     * @return Element at given index
     */
    private T getLinear(int n) {
        if (n < 0 || n > size() - 1) {
            throw new NoSuchElementException();
        }
        Entry<T> p = head;
        for (int i = -1; i < n; i++) {
            p = p.next[0];
        }
        return p.element;
    }

    /**
     * O(logn) expected algorithm for getting element
     * 
     * @param n Index
     * @return Element at given index
     */
    private T getLog(int n) {
        if (n >= size())
            return null;
        int c = -1;
        Entry<T> p = head;
        for (int l = maxLevel - 1; l >= 0; l--) {
            while (p.span[l] + c <= n) {
                c += p.span[l];
                p = p.next[l];
            }
            if (c == n) {
                break;
            }
        }
        return p.element;
    }

    /**
     * Is the list empty?
     * 
     * @return True if list is empty else False
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Iterate through the elements of list in sorted order
     * 
     * @return Iterator of Skip List
     */
    public Iterator<T> iterator() {
        return new SkipListIterator();
    }

    /**
     * Implementation of iterator
     * 
     * @author Tej
     *
     */
    protected class SkipListIterator implements Iterator<T> {
        Entry<T> cursor;

        SkipListIterator() {
            cursor = head;
        }

        @Override
        public boolean hasNext() {
            return cursor.next[0] != tail;
        }

        @Override
        public T next() {
            cursor = cursor.next[0];
            return cursor.element;
        }
    }

    /**
     * Return last element of list
     * 
     * @return Last element is present else null
     */
    public T last() {
        return tail.prev != head ? tail.prev.element : null;
    }

    /**
     * Rebuilds the skiplist into a perfect possible structure.
     */
    public void rebuild() {
        if (size() == 0) {
            return;
        }

        // maximum length required given the size
        int maxLen = (int) (Math.log((double) size()) / Math.log(2));

        Entry<T> p = head;
        // Filling out first level
        int index = 0;
        while (p.next[0] != tail) {
            p = p.next[0];
            Entry<T> next = p.next[0];
            // Reset the next[] and span[] of current node to perfect level.
            p.setNext(getlevel(index + 1));
            p.next[0] = next;
            p.span[0] = 1;
            index++;
        }

        // Filling out rest of levels
        for (int l = 1; l <= maxLen; l++) {
            Entry<T> prev = head;
            Entry<T> cur = head;
            int span = 0;
            while (cur != tail) {
                if (cur != head && l <= cur.span.length - 1) {
                    prev.next[l] = cur;
                    prev.span[l] = span;
                    prev = cur;
                    span = 0;
                }
                span += cur.span[l - 1];
                cur = cur.next[l - 1];
            }
            prev.next[l] = tail;
            prev.span[l] = span;
        }

        // Setting span for all levels above max level to maintain consistency
        for (int i = maxLen + 1; i < POSSIBLE_LEVELS; i++) {
            head.span[i] = size() + 1;
        }

        this.maxLevel = maxLen + 1;
    }

    /**
     * Gets level given the position of the node
     * 
     * @param ind Index at which perfect level is to be determined
     * @return Perfect level
     */
    private int getlevel(int ind) {
        int level = 1;
        while (((ind & 1) != 1) && ind != 0) {
            ind = ind >> 1;
            level++;
        }
        return level;
    }

    /**
     * Removes x from skiplist.
     * 
     * @param x Element to be removed
     * @return Element removed or null if the element is not present
     */
    public T remove(T x) {
        if (!contains(x)) {
            return null;
        }

        Entry<T> ent = last[0].next[0];
        ent.next[0].prev = last[0];
        for (int i = 0; i < ent.span.length; i++) {
            last[i].next[i] = ent.next[i];

            // Span updated.
            last[i].span[i] += (ent.span[i] - 1);
        }

        // Decrease span for elements that are above the level of removed
        // element
        int lIndex = ent.span.length;
        while (lIndex <= maxLevel) {
            for (int j = lIndex; j < last[lIndex].span.length; j++) {
                last[lIndex].span[j]--;
            }
            lIndex = last[lIndex].span.length;
        }

        size--;
        return ent.element;
    }

    /**
     * Return the number of elements in the list
     * 
     * @return Integer representing size of skiplist
     */
    public int size() {
        return size;
    }

    /**
     * Converts skiplist into a string
     */
    public String toString() {
        StringBuilder s = new StringBuilder("[" + size + "]");
        Iterator<T> i = iterator();
        while (i.hasNext()) {
            s.append(" " + i.next());
        }
        s.append("\n");
        return s.toString();
    }
}