// ID6 Group 6
// Dimoudis Georgios A.M.: 5212
// Chatzidimitriou Charilaos A.M.: 5387
// Chatziiordanis Omiros A.M.: 5388
import java.io.*;

// implementation of a FIFO queue
public class Queue<Item> {
    
    private Node head; // first node in the list
    private Node tail; // last node in the list
    
    private class Node {
        Item item;
        Node next;
		Node prev;
        Node(Item item, Node next, Node prev) {
            this.item = item;
            this.next = next;
			this.prev = prev;
        }
    }
    
    Queue() {
        head = null;
        tail = null;
    }
    
    public boolean isEmpty() {
        return head == null;
    }
    
    public void enqueue(Item item) {
        if (isEmpty()) {
            head = new Node(item, null,null);
            tail = head;
        } else {
            tail.next = new Node(item, null, null);
			Node temp= tail;
            tail = tail.next;
			tail.prev = temp;
        }
    }
    
    public Item dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue underflow");
        }
        Item item = head.item;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        return item;
    }
    
    public Item peekTail() {
		if (tail != null){
			System.out.println("item: " + tail.item);
			return tail.item;
		}
		return null;
    }

    public Item dequeueTail() {
        if (isEmpty()) {
            throw new RuntimeException("Queue underflow");
        }
        Item item = tail.item;
        tail = tail.prev;
        if (tail == null) {
            head = null;
        }
        return item;
    }

    public int size() {
        int count = 0;
        Node current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }
    

}
