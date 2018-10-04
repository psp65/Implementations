package psp170230;

import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Param Parikh
 * @param <T>
 */
public class BoundedQueue<T> {

    private T[] queue; //Array to hold queue elements
    private int front, rear; //Pointer pointing to current ends
    final private int CAPACITY; //Max capacity of queue

    BoundedQueue(int capacity) {
        CAPACITY = capacity;
        queue = (T[]) new Object[capacity];
        front = rear = -1;
    }

    // add gicen element at the end, if capacity permits
    public boolean offer(T x) {
        if ((CAPACITY == 0) || (front == (rear + 1) % CAPACITY)) {
            return false;
        }

        if (front == -1) {
            front = 0;
        }
        rear = (rear + 1) % CAPACITY;
        queue[rear] = x;
        return true;
    }

    // removes and returns oldest element of queue, if available
    public T poll() {
        if (front == -1) {
            return null;
        }

        T x = queue[front];
        if (front == rear) {
            front = rear = -1;
        } else {
            front = (front + 1) % CAPACITY;
        }
        return x;
    }

    // return oldest element of queue, if available
    public T peek() {
        return front == -1 ? null : queue[front];
    }

    // returns number of elements present in queue
    public int size() {
        if (front == -1) {
            return 0;
        }
        return front <= rear ? (rear - front + 1) : (rear - front + 1 + CAPACITY);
    }

    public boolean isEmpty() {
        return front == -1;
    }

    // reset queue like an empty state
    public void clear() {
        front = rear = -1;
    }

    // fills the given array with queue elements
    // throws exeption in case of unsufficient memory of passed array
    public void toArray(T[] array) throws Exception {
        int Qsize = size();
        if (array.length < Qsize) {
            throw new Exception("Given memory is not enough to hold the entire queue.");
        }
        int Qindex = front, Aindex = 0;
        while (Qsize-- > 0) {
            array[Aindex++] = queue[Qindex++ % CAPACITY];
        }
    }

    public void printQueue() {
        System.out.print(size() + ": ");
        if (front == -1) {
            System.out.println("Queue is Empty.");
            return;
        }

        if (front <= rear) {
            for (int i = front; i <= rear; i++) {
                System.out.print(queue[i] + " ");
            }
        } else {
            for (int i = front; i < CAPACITY; i++) {
                System.out.print(queue[i] + " ");
            }
            for (int i = 0; i <= rear; i++) {
                System.out.print(queue[i] + " ");
            }
        }
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        System.out.print("Give Capacity: ");
        int n = in.nextInt();
        BoundedQueue<Integer> bq = new BoundedQueue<>(n);
        bq.printQueue();
        whileloop:
        while (in.hasNext()) {
            int com = in.nextInt();
            switch (com) {
                case 1:  // Offer something to Queue
                    n = in.nextInt();
                    if (bq.offer(Integer.valueOf(n))) {
                        System.out.print("Success --> ");
                    } else {
                        System.out.print("Fail --> ");
                    }
                    bq.printQueue();
                    break;
                case 2:  // Poll from Queue
                    System.out.println(bq.poll());
                    bq.printQueue();
                    break;
                case 3: // Peek from Queue
                    System.out.println(bq.peek());
                    break;
                case 4: //Clear queue
                    bq.clear();
                    System.out.println("Queue cleared.");
                    bq.printQueue();
                    break;
                case 5: // Fill user supplied array with the elements of the queue
                    Integer[] arrayToFill = new Integer[bq.CAPACITY];
                    bq.toArray(arrayToFill);
                    System.out.println("Copied Array: " + Arrays.toString(arrayToFill));
                    break;
                case 6: // Print total number of elements inside queue
                    System.out.println("Size: " + bq.size());
                    break;
                case 7: // Print Queue
                    bq.printQueue();
                    break;
                default:  // Exit loop
                    break whileloop;
            }
        }
        bq.printQueue();
        in.close();
    }
}
