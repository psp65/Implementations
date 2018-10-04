package psp170230;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 * @author Param Parikh, Shivani Thakkar
 * @param <T>
 */

public class DoublyLinkedList<T> extends SinglyLinkedList<T> {
    
    /** Class Entry holds a single Doubly node of the list */
    static class Entry<E> extends SinglyLinkedList.Entry<E> {
	Entry<E> prev;
	Entry(E x, Entry<E> next, Entry<E> prev) {
	    super(x, next);
	    this.prev = prev;
	}
    }
    
    public DoublyLinkedList() {
        head = new Entry(null, null, null);
        tail = head;
        size = 0;
    }
    
    public Iterator<T> dllIterator() { return new DLLIterator(); }
    
    protected class DLLIterator extends SLLIterator {
        
        public boolean hasPrev() {
            return cursor != head && ((Entry<T>)cursor).prev != head;
        }
        
        public T prev() {
            if(!hasPrev())
                throw new NoSuchElementException();
            
            cursor = ((Entry<T>)cursor).prev;
            prev = ((Entry<T>)cursor).prev;
            ready = true;
            return cursor.element;
        }
        
        /** Adds a new Entry after cursor.
            Moves cursor to the newly created entry.
        */
        public void add(T x) {
            Entry<T> nextNode = (Entry<T>)cursor.next;
            cursor.next = new Entry(x, nextNode, (Entry<T>)cursor);

            if(cursor == tail)
                tail = cursor.next;
            else
                nextNode.prev = (Entry<T>)cursor.next;
            
            prev = cursor;
            cursor = cursor.next;
            ready = false;
            size++;
        } 
        
        @Override
        public void remove() {
            super.remove();
            if(cursor != tail)
                ((Entry<T>)cursor.next).prev = (Entry<T>)prev;
        }
    } //End of DLLIterator
    
    // Add new elements to the end of the list
    @Override
    public void add(T x) {
        add(new Entry<>(x, null, (Entry<T>) tail));
    }
    
    public void printReverseList() {
        System.out.print(this.size + ": ");
	Entry e = (Entry<T>)tail;
	
        while(e != head) {
          System.out.print(e.element + " ");
          e = e.prev;
        }
        System.out.println();
    }

    
    public static void main(String[] args) throws NoSuchElementException {
        int n = 10;
        if(args.length > 0) {
            n = Integer.parseInt(args[0]);
        }

        DoublyLinkedList<Integer> lst = new DoublyLinkedList<>();
        for(int i=1; i<=n; i++) {
            lst.add(Integer.valueOf(i));
        }
        lst.printList();
        
        DoublyLinkedList.DLLIterator it = (DoublyLinkedList.DLLIterator) lst.dllIterator();
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
            case 2: // Move to prev element and print it
                if (it.hasPrev()) {
                    System.out.println(it.prev());
                } else {
                    break whileloop;
                }
                break;
            case 3: // Add a new element after the cursor
                int x = in.nextInt();
                it.add(x);
                lst.printList();
                break;
	    case 4:  // Remove cursor element
		it.remove();
		lst.printList();
		break;
            case 5: // Print the list
                lst.printList();
                break;
            case 6:
                lst.printReverseList();
                break;
	    default: 
		 break whileloop;
	    }
	}
	lst.printList();
        in.close();
    }
}