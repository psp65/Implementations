package psp170230;

import java.util.Scanner;
import java.util.Stack;

/**
 * class to create and hold a Binary Search Tree (BST)
 *
 * @author Param Parikh
 * @param <T>
 */
public class BinarySearchTree<T extends Comparable<? super T>> {

    /**
     * class to hold value and reference to children nodes
     *
     * @param <T>
     */
    static class Entry<T> {

        /**
         * stores value for this node
         */
        T element;

        /**
         * reference to left child in BST
         */
        Entry<T> left;

        /**
         * reference to right child in BST
         */
        Entry<T> right;

        public Entry(T x, Entry<T> left, Entry<T> right) {
            this.element = x;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * root node of BST
     */
    Entry<T> root;

    /**
     * stack to keep track of previously encountered node while going down in
     * BST
     */
    Stack<Entry<T>> s;

    /**
     * holds number of elements inside BST 0 suggests tree is empty
     */
    int size;

    /**
     * constructor to build an empty BST
     */
    public BinarySearchTree() {
        root = null;
        size = 0;
    }

    /**
     * a utility function to check whether given element is exist or not
     * <p>
     * If element is found then, entry associated to it is returned Otherwise
     * node on which search is halted
     * </p>
     *
     * @param x element to find in BST
     * @return entry associated with matching element x otherwise the entry
     * where search halted
     */
    private Entry<T> find(T x) {
        s = new Stack<>();
        s.push(null);
        return find(root, x);
    }

    /**
     * a utility function to check whether given element is exist or not under
     * subtree of node t
     *
     * @param t Node under which we want to find element x
     * @param x element to find
     * @return entry associated with matching element x otherwise the entry
     * where search halted
     */
    private Entry<T> find(Entry<T> t, T x) {
        if (t == null) {
            return t;
        }

        while (true) {
            if (x.compareTo(t.element) < 0) {
                if (t.left == null) {
                    break;
                } else {
                    s.push(t);
                    t = t.left;
                }
            } else if (x.compareTo(t.element) > 0) {
                if (t.right == null) {
                    break;
                } else {
                    s.push(t);
                    t = t.right;
                }
            } else {
                break;
            }
        }
        return t;
    }

    /**
     * checks given element is part of BST or not
     *
     * @param x element to search inside tree
     * @return true if element exist in BST, otherwise false
     */
    public boolean contains(T x) {
        Entry<T> t = find(x);
        return !(t == null || x.compareTo(t.element) != 0);
    }

    /**
     * returns given element if it is part of BST or null
     *
     * @param x element to search inside tree
     * @return element if it exists in BST, otherwise null
     */
    public T get(T x) {
        Entry<T> t = find(x);
        return (t == null || x.compareTo(t.element) != 0) ? null : t.element;
    }

    /**
     * add a new element to tree, if it's not exist. If tree contains a node
     * with same key, replace it.
     *
     * @param x element to add in BST
     * @return true if x is a new element added to tree otherwise false
     */
    public boolean add(T x) {
        if (size == 0) {
            root = new Entry<>(x, null, null);
            size = 1;
            return true;
        }

        Entry<T> t = find(x);
        if (x.compareTo(t.element) < 0) {
            t.left = new Entry<>(x, null, null);
        } else if (x.compareTo(t.element) > 0) {
            t.right = new Entry<>(x, null, null);
        } else {
            return false;
        }

        size++;
        return true;
    }

    /**
     * remove and return x from tree it it exist
     *
     * @param x element to remove from BST
     * @return x if found, otherwise return null
     */
    public T remove(T x) {
        if (size == 0) {
            return null;
        }
        Entry<T> t = find(x);
        if (t.element != x) {
            return null;
        }

        T result = t.element;

        if (t.left == null || t.right == null) {
            bypass(t);
        } else {
            s.push(t);
            Entry<T> minRight = find(t.right, x);
            t.element = minRight.element;
            bypass(minRight);
        }
        size--;
        return result;
    }

    /**
     * Removing a node from BST can be interpreted as bypassing this node and
     * attach the parent to immediate predecessor/ successor of this node
     *
     * <p>
     * here left most on the right child is considered as successor and
     * bypassing will join parent to this node
     * </p>
     *
     * @param t node to remove (bypass) from BST
     */
    private void bypass(Entry<T> t) {
        Entry<T> parent = s.peek();
        Entry<T> child = t.left == null ? t.right : t.left;

        if (parent == null) {
            root = child;
        } else if (parent.left == t) {
            parent.left = child;
        } else {
            parent.right = child;
        }
    }

    /**
     * returns smallest element of BST
     *
     * @return smallest element of BST if BST is not empty else null
     */
    public T min() {
        if (size == 0) {
            return null;
        }

        Entry<T> node = root;
        while (node.left != null) {
            node = node.left;
        }

        return node.element;
    }

    /**
     * returns largest element of BST
     *
     * @return largest element of BST if BST is not empty else null
     */
    public T max() {
        if (size == 0) {
            return null;
        }

        Entry<T> node = root;
        while (node.right != null) {
            node = node.right;
        }

        return node.element;
    }

    /**
     * keeps track of current position inside array, which is supposed to be
     * filled
     */
    private int pos;

    /**
     * method to convert BST class into array, which stores entry.elements in ascending order
     * 
     * @return array of type comparable in ascending order of elements of BST
     */
    public Comparable[] toArray() {
        pos = 0;
        Comparable[] arr = new Comparable[size];
        AddNode(root, arr);
        return arr;
    }

    private void AddNode(Entry<T> current, Comparable[] arr) {
        if (current != null) {
            AddNode(current.left, arr);
            arr[pos++] = (Comparable) current.element;
            AddNode(current.right, arr);
        }
    }

    public static void main(String[] args) {
        BinarySearchTree<Integer> t = new BinarySearchTree<>();
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int x = in.nextInt();
            if (x > 0) {
                System.out.print("Add " + x + " : ");
                t.add(x);
                t.printTree();
            } else if (x < 0) {
                System.out.print("Remove " + x + " : ");
                t.remove(-x);
                t.printTree();
            } else {
                Comparable[] arr = t.toArray();
                System.out.print("Final: ");
                for (int i = 0; i < t.size; i++) {
                    System.out.print(arr[i] + " ");
                }
                System.out.println();
                return;
            }
        }
    }

    /**
     * Prints elements of tree in ascending order
     */
    public void printTree() {
        System.out.print("[" + size + "]");
        printTree(root);
        System.out.println();
    }

    /**
     * Inorder traversal of tree
     *
     * @param node root of the tree
     */
    void printTree(Entry<T> node) {
        if (node != null) {
            printTree(node.left);
            System.out.print(" " + node.element);
            printTree(node.right);
        }
    }

}
