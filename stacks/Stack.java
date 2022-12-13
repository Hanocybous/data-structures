/* ID6 Group 6
 * Georgios Dimoudis AM : 5212
 * Charilaos Chatzidimitriou AM : 5387
 * Omiros Chatziiordanis AM : 5388
 */
import java.util.NoSuchElementException;

public class Stack<I> {

    // Stack is implemented using a linked list
    private Node first = null;
    private int N = 0;

    // Node class
    private class Node {
        I item;
        Node next;
    }

    // Check if stack is empty
    public boolean isEmpty() {
        return first == null;
    }

    // Return the number of items in the stack
    public int size() {
        return N;
    }

    // Add item to the stack
    public void push(I item) {
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        N++;
    }

    // Remove item from the stack
    public I pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack underflow");
        }
        I item = first.item;
        first = first.next;
        N--;
        return item;
    }

    // Peek at the top of the stack
    public I peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack underflow");
        }
        return first.item;
    }

}
