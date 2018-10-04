package psp170230;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.NoSuchElementException;

/**  
 * @author Param Parikh
 * @param <T>
 */

public class SinglyLinkedList<T> implements Iterable<T> {

    /** Class Entry holds a single Singly node of the list */
    static class Entry<E> {
        E element;
        Entry<E> next;

        Entry(E x, Entry<E> nxt) {
            element = x;
            next = nxt;
        }
    }

    // Dummy header is used.  tail stores reference of tail element of list
    Entry<T> head, tail;
    int size;

    public SinglyLinkedList() {
        head = new Entry<>(null, null);
        tail = head;
        size = 0;
    }

    @Override
    public Iterator<T> iterator() { return new SLLIterator(); }

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
            if(!hasNext())
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
	    if(!ready) {
		throw new NoSuchElementException();
	    }
	    prev.next = cursor.next;
	    // Handle case when tail of a list is deleted
	    if(cursor == tail) {
		tail = prev;
	    }
	    cursor = prev;
	    ready = false;  // Calling remove again without calling next will result in exception thrown
	    size--;
	}
    }  // end of class SLLIterator

    // Add new elements to the end of the list
    public void add(T x) {
	add(new Entry<>(x, null));
    }
    
    protected void add(Entry<T> entry) {
        tail.next = entry;
	tail = tail.next;
	size++;
    }

    public void printList() {
	System.out.print(this.size + ": ");
	for(T item: this) {
	    System.out.print(item + " ");
	}
        System.out.println();
    }

    public static void main(String[] args) throws NoSuchElementException {
        int n = 10;
        if(args.length > 0) {
            n = Integer.parseInt(args[0]);
        }

        SinglyLinkedList<Integer> lst = new SinglyLinkedList<>();
        for(int i=1; i<=n; i++) {
            lst.add(Integer.valueOf(i));
        }
        lst.printList();

	Iterator<Integer> it = lst.iterator();
	Scanner in = new Scanner(System.in);
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
	    case 2:  // Remove element
		it.remove();
		lst.printList();
		break;
	    default:  // Exit loop
		 break whileloop;
	    }
	}
	lst.printList();
        in.close();
    }
}
