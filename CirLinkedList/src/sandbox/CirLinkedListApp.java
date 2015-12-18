package sandbox;

class Link {
	public int data;
	public Link next;
	
	// constructor
	public Link(int d) {
		d = data;
		next = null;
	}
	
	public String toString() {
		return String.valueOf(data);
	}
}

class CirLinkedList {
	private Link start;
	private Link current;
	private int count = 0;
	
	public CirLinkedList() {
		start = current = null;
	}
	
	public boolean isEmpty() {
		return (start == null);
	}
	
	// inserts item at the "end", similar to a queue
	public void insert(int d) {
		if (count == 0) {
			Link newLink = new Link(d);
			start = newLink;
			count++;
			current = start;
			current.next = start;	// current points to itself/start
		}
		else {
			Link newLink = new Link(d);
			current.next = newLink;		// previous link points to new link
			newLink.next = start;		// last inserted item points to the beginning of the circle
			count++;
			current = current.next;		// move current up one (next link)
		}
	}
	
}

public class CirLinkedListApp {
	public static void main(String[] args) {
		CirLinkedList myList = new CirLinkedList();
		
		
		myList.insert(1);
		myList.insert(2);
		myList.insert(3);
		myList.insert(4);
		myList.insert(5);
		
		
	}
}
