package psp170230;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 * @author Param
 * @param <T>
 */
public class SinglyLinkedList_Extended<T> implements Iterable<T> {

    /**
     * Class Entry holds a single Singly node of the list
     */
    static class Entry<E> {

        E element;
        Entry<E> next;

        Entry(E x, Entry<E> nxt) {
            element = x;
            next = nxt;
        }
    } // end of clas Entry

    protected class SLLIterator implements Iterator<T> {

        Entry<T> cursor, prev;
        boolean ready;  // is item ready to be removed?

        SLLIterator() {
            cursor = head;
            prev = null;
            ready = false;
        }

        @Override
        public boolean hasNext() {
            return cursor.next != null;
        }

        @Override
        public T next() {
            if (!hasNext())
                throw new NoSuchElementException();

            prev = cursor;
            cursor = cursor.next;
            ready = true;
            return cursor.element;
        }

        // Removes the current element (retrieved by the most recent next())
        // Remove can be called only if next has been called and the element has not been removed
        @Override
        public void remove() {
            if (!ready) {
                throw new NoSuchElementException();
            }
            prev.next = cursor.next;
            // Handle case when tail of a list is deleted
            if (cursor == tail) {
                tail = prev;
            }
            cursor = prev;
            ready = false;  // Calling remove again without calling next will result in exception thrown
            size--;
        }
    }  // end of class SLLIterator

    Entry<T> head, tail; // Dummy head is used,  tail stores reference of tail element of list
    int size; // Total number of elements in linkedlist except Dummy head

    public SinglyLinkedList() {
        head = new Entry<>(null, null);
        tail = head;
        size = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new SLLIterator();
    }

   // Add new elements to the end of the list
    public void add(T x) {
        add(new Entry<>(x, null));
    }

    // Add given Entry at the end of the list
    private void add(Entry<T> entry) {
        tail.next = entry;
        tail = tail.next;
        size++;
    }

    // Add given element at given index
    public void add(int index, T x) {
        Entry<T> curr = getValidPreviousEntry(index);
        curr.next = new Entry<T>(x, curr.next);
        if (curr == tail) {
            tail = curr.next;
        }
        size++;
    }

    // Add elelemt in the front
    public void addFirst(T x) {
        Entry<T> first = new Entry<>(x, head.next);
        head.next = first;
        if (head == tail) {
            tail = first;
        }
        size++;
    }

    // Remove first occurance of given element from list and return it's index
    public int remove(T x) {
        int index = 0;
        Entry<T> curr = head.next;
        Entry<T> prev = head;

        while (curr != null) {
            if (curr.element.equals(x))
                break;

            prev = curr;
            curr = curr.next;
            index++;
        }

        if (index == size) {
            throw new NoSuchElementException();
        }

        prev.next = curr.next;
        if (curr == tail) {
            tail = prev;
        }
        size--;
        return index;
    }

    // Remove element at given index
    public void remove(int index) {
        Entry<T> curr = getValidPreviousEntry(index);
        curr.next = curr.next.next;
        if (curr.next == null) {
            tail = curr;
        }
        size--;
    }

    // Remove first Entry of list
    public void removeFirst() {
        if (head == tail) {
            return;
        }
        head.next = head.next.next;
        if (head.next == null) {
            tail = head;
        }
        size--;
    }

    public T get(int index) {
        return getValidPreviousEntry(index).next.element;
    }

    public void set(int index, T x) {
        getValidPreviousEntry(index).next.element = x;
    }

    // Helper to return node at previous from given index
    private Entry<T> getValidPreviousEntry(int index) {
        if (index < 0 || index >= size) {
            throw new NoSuchElementException();
        }

        Entry<T> curr = head;
        while (index-- > 0) {
            curr = curr.next;
        }
        return curr;
    }

    public void printList() {
        System.out.print(this.size + ": ");
        for (T item : this) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) throws NoSuchElementException{
        int n = 20;
        if(args.length > 0) {
            n = Integer.parseInt(args[0]);
        }

        SinglyLinkedList<Integer> lst = new SinglyLinkedList<>();
        for(int i=0; i<n; i++) {
            lst.add(i);
        }
        
        System.out.println("Command list can be found in ReadMe.txt file!");
        System.out.print("Initial list --> ");  lst.printList();

	Iterator<Integer> it = lst.iterator();
	Scanner in = new Scanner(System.in);
        int index, num;
	whileloop:
	while(in.hasNext()) {
	    int com = in.nextInt();
	    switch(com) {
	    case 1:  // Move to next element and print it
		if (it.hasNext()) {
		    System.out.println(it.next());
		} else {
		    break whileloop;
		}
		break;
	    case 2:  // Remove element from iterator
		it.remove();
		lst.printList();
		break;
            case 3:  // Adds a new Entry in the front of the LinkedList
		num = in.nextInt();
                lst.addFirst(num);
                lst.printList();
		break;
	    case 4:  // Removes First element of LinkedList
		lst.removeFirst();
		lst.printList();
		break;
            case 5: // Search for a particular element in LinkedList
                num = in.nextInt();
                System.out.println("Element " + num + " found firt at index " + lst.remove(Integer.valueOf(num)));
                lst.printList();
                break;
            case 6:  // gets entry at index
		index = in.nextInt();
                System.out.println(lst.get(index));
		break;
	    case 7:  // Set a new value at given index element
                index = in.nextInt();
                num = in.nextInt();
		lst.set(index, num);
		lst.printList();
		break;
            case 8: // Add a new element at given index 
                //Cannot add at index=size (ie.at the last of list) with this method
                index = in.nextInt();
                num = in.nextInt();
                lst.add(index, num);
                lst.printList();
                break;
            case 9: // Removes element at given index
                index = in.nextInt();
                lst.remove(index);
                lst.printList();
                break;
            case 10: // Print list
                lst.printList();
                break;
	    default:  // Exit loop
		 break whileloop;
	    }
	}
	System.out.print("Last state --> ");  lst.printList();
        in.close();
    }
}
