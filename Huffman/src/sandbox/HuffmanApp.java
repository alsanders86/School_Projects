package sandbox;

import java.io.*;
import java.util.*;		// for stack

class Node {
	public int data;
	public char letter;
	public Node leftChild;
	public Node rightChild;
	public String code;
	
	public void setCode(String c) {
		this.code = c;
	}
	
	public void displayNode() {
		System.out.print("{" + data + "}");
	}
}

class Tree {
	public Node root;

	public Tree() {
		root = null;
	}
	
	public Node find(int key) {
		Node current = root;
		while(current.data != key)
		{
			if(key < current.data)
				current = current.leftChild;
			else
				current = current.rightChild;
			if(current == null)
				return null;
		}
		return current;
	}	// end find ----------------------------
	
	public void insert(int d, char l) {
		Node newNode = new Node();
		newNode.data = d;
		newNode.letter = l;
		if(root == null)
			root = newNode;
		else {
			Node current = root;
			Node parent;
			while(true) {
				parent = current;
				if(d < current.data) {		// go left?
					current = current.leftChild;
					if(current == null) {
						parent.leftChild = newNode;
						return;
					}
				}
				else {		// go right?
					current = current.rightChild;
					if(current == null) {
						parent.rightChild = newNode;
						return;
					}
				}
			}
		}
	}	// end insert ----------------------------
	
	public boolean delete(int key) {
		Node current = root;
		Node parent = root;
		boolean isLeftChild = true;
		
		while(current.data != key) { // search for node
			parent = current;
			if(key < current.data){
				isLeftChild = true;
				current = current.leftChild;
			}
			else {
				isLeftChild = false;
				current = current.rightChild;
			}
			if(current == null)
				return false;
		}	// end while
		// found node to delete
		
		// if no children, just delete
		if(current.leftChild == null & current.rightChild == null) {
			if(current == root)
				root = null;
			else if(isLeftChild)
				parent.leftChild = null;
			else
				parent.rightChild = null;
		}
		
		// if no right child, replace with left subtree
		else if(current.rightChild == null)
			if(current == root)
				root = current.leftChild;
			else if(isLeftChild)
				parent.leftChild = current.leftChild;
			else
				parent.rightChild = current.leftChild;
		
		// if no left child, replace with right subtree
		else if(current.leftChild == null)
			if(current == root)
				root = current.rightChild;
			else if(isLeftChild)
				parent.leftChild = current.rightChild;
			else
				parent.rightChild = current.rightChild;
		
		// two children, so replace with inorder successor
		else {
			// get successor of node to delete (current)
			Node successor = getSuccessor(current);
			
			// connect parent of current to successor instead
			if(current == root)
				root = successor;
			else if(isLeftChild)
				parent.leftChild = successor;
			else
				parent.rightChild = successor;
			
			// connect successor to current's left child
			successor.leftChild = current.leftChild;
		} // end else two children
		// successor cannot have a left child
		
		return true;
	} // end delete -------------------------------------
	
	private Node getSuccessor(Node delNode) {
		Node successorParent = delNode;
		Node successor = delNode;
		Node current = delNode.rightChild;
		while(current != null) {
			successorParent = successor;
			successor = current;
			current = current.leftChild;
		}
		
		if(successor != delNode.rightChild) {
			successorParent.leftChild = successor.rightChild;
			successor.rightChild = delNode.rightChild;
		}
		return successor;
	} // -----------------------------------------------
	
	public void traverse(int traverseType) {
		switch(traverseType) {
		case 1: System.out.print("\nPreorder: ");
		preOrder(root);
		break;
		case 2: System.out.print("\nInorder: ");
		inOrder(root);
		break;
		case 3: System.out.print("\nPostorder: ");
		postOrder(root);
		break;
		}
		
		System.out.println();
	}// -----------------------------------------------
	
	private void preOrder(Node localRoot) {
		if(localRoot != null) {
			//System.out.print(localRoot.data + " ");
			preOrder(localRoot.leftChild);
			preOrder(localRoot.rightChild);
		}
	} // ---------------------------------------------
	
	private void inOrder(Node localRoot) {
		if(localRoot != null) {
			inOrder(localRoot.leftChild);
			System.out.print(localRoot.data + " ");
			inOrder(localRoot.rightChild);
		}
	} // --------------------------------------------
	
	private void postOrder(Node localRoot) {
		if(localRoot != null) {
			postOrder(localRoot.leftChild);
			postOrder(localRoot.rightChild);
			System.out.print(localRoot.data + " ");
		}
	} // ------------------------------------------
	
	// Coding table creation ----------------------------------
	Node[] leafArray = new Node[7];
	int leafElems = 0;
	
	private boolean isLeaf(Node n) {
		return (n.leftChild == null && n.rightChild == null);
	}
	
	public void createCode(Node localRoot, String c) {
		if(isLeaf(localRoot)) {
			localRoot.code = c;
			leafArray[leafElems] = localRoot;
			leafElems++;
			//System.out.print(leafArray[leafElems-1].letter);
		}
		else {
			createCode(localRoot.leftChild, c + "0");
			createCode(localRoot.rightChild, c + "1");
		}
		sortLeaves();
		//showTable();
	}
	
	// sort the leaves for the table, alphabetical
	private void sortLeaves() {
		int out, in;
		for(out = leafElems-1; out>1; out--) 
			for(in = 0; in < out; in++)
				if(leafArray[in].letter > leafArray[in+1].letter)
					swapLeaves(in, in+1);
	}
	
	private void swapLeaves(int one, int two) {
		Node temp = leafArray[one];
		leafArray[one] = leafArray[two];
		leafArray[two] = temp;
	}
	
	public void showTable() {
		System.out.println("Letter\tFreq\tBinary");
		for(int i = 0; i < leafElems; i++) {
			System.out.println(Character.toString((char)leafArray[i].letter) + "\t" + leafArray[i].data + "\t" + leafArray[i].code);
		}
	}
	
	public Node[] getTable() {
		return leafArray;
	}
	
	
	
	public void displayTree()
	{
		Stack globalStack = new Stack();
		globalStack.push(root);
		int nBlanks = 32;
		boolean isRowEmpty = false;
		System.out.println("......................................................");
		
		while(isRowEmpty==false)
		{
			Stack localStack = new Stack();
			isRowEmpty = true;
			for(int j=0; j<nBlanks; j++)
				System.out.print(' ');
			while(globalStack.isEmpty()==false)
			{
				Node temp = (Node)globalStack.pop();
				if(temp != null)
				{
					System.out.print(temp.letter);
					System.out.print(temp.data);
					localStack.push(temp.leftChild);
					localStack.push(temp.rightChild);
					if(temp.leftChild != null ||
							temp.rightChild != null)
						isRowEmpty = false;
				}
				else
				{
					System.out.print("--");
					localStack.push(null);
					localStack.push(null);
				}
				for(int j=0; j<nBlanks*2-2; j++)
					System.out.print(' ');
			} // end while globalStack not empty
			System.out.println();
			nBlanks /= 2;
			while(localStack.isEmpty()==false)
				globalStack.push( localStack.pop() );
		} // end while isRowEmpty is false
		System.out.println("......................................................");
	} // end displayTree()
	// -------------------------------------------------------------

	public String decode(String c, Node r) {
		String result = "";
		c = c.replaceAll(" ", "");
		//System.out.print(c);
		Node current = r;
		preOrder(current);
		for(int i = 0; i < c.length(); i++) {
			// going left--0
			if(Character.getNumericValue(c.charAt(i)) == 0) {
				if(current.leftChild == null) {
					result += current.letter;
					current = r;
					i--;
				} else {
					current = current.leftChild;
					if(isLeaf(current)) {
						result += current.letter;
						current = r;
					}
				}
			}
			// going right -- 1
			else if (Character.getNumericValue(c.charAt(i)) == 1) {
				if(current.rightChild == null) {
					result += current.letter;
					current = r;
					i--;
				} else {
					current = current.rightChild;
					if(isLeaf(current)) {
						result += current.letter;
						current = r;
					}
				}
			}
		}
		return result;
	}
} // end class Tree
////////////////////////////////////////////////////////////////



// priority queue of nodes
class PriorityQ {
	private int maxSize;
	private Node[] queArray;
	public int nItems;
	
	public PriorityQ(int s) {
		maxSize = s;
		queArray = new Node[maxSize];
		nItems = 0;
	}
	
	public void insert(Node item) {
		int j;
		
		if(nItems == 0)
			queArray[nItems++] = item;
		else {
			for(j = nItems-1; j>=0; j--) {
				if(item.data > queArray[j].data) {
					queArray[j+1] = queArray[j];	// shift up if bigger
				}
				else
					break;		// do nothing if smaller
			}
			queArray[j+1] = item;
			nItems++;
		}
	}	// end insert
	
	public Node remove() {
		return queArray[--nItems];
	}
	
	public Node peekMin() {
		return queArray[nItems-1];
	}
	
	public boolean isEmpty() {
		return (nItems==0);
	}
	
	public boolean isFull() {
		return (nItems == maxSize);
	}
	
	public int getSize() {
		return maxSize;
	}
	
	public Node[] getArray() {
		return queArray;
	}
	
}	// end priority queue




public class HuffmanApp {
	public static void main(String[] args) throws IOException
	{
		File file = new File(args[0]);
		Scanner sc = new Scanner(file);
		String letters;
		PriorityQ pq = new PriorityQ(7);
		
		letters = sc.next();
		
		char[] c = letters.toCharArray();
		int[] freq = new int[26];
		int aFreq = 0;
		int bFreq = 0;
		int cFreq = 0;
		int dFreq = 0;
		int eFreq = 0;
		int fFreq = 0;
		int gFreq = 0;
		
		for(int i = 0; i < c.length; i++) {
			switch(c[i]) {
			case 65: aFreq++;
			break;
			case 66: bFreq++;
			break;
			case 67: cFreq++;
			break;
			case 68: dFreq++;
			break;
			case 69: eFreq++;
			break;
			case 70: fFreq++;
			break;
			case 71: gFreq++;
			break;
			}
		}
		
		// create letter nodes
		Node aNode = new Node();
		aNode.data = aFreq;
		aNode.letter = 'A';
		Node bNode = new Node();
		bNode.data = bFreq;
		bNode.letter = 'B';
		Node cNode = new Node();
		cNode.data = cFreq;
		cNode.letter = 'C';
		Node dNode = new Node();
		dNode.data = dFreq;
		dNode.letter = 'D';
		Node eNode = new Node();
		eNode.data = eFreq;
		eNode.letter = 'E';
		Node fNode = new Node();
		fNode.data = fFreq;
		fNode.letter = 'F';
		Node gNode = new Node();
		gNode.data = gFreq;
		gNode.letter = 'G';
		
		// put in priority queue
		pq.insert(aNode);
		pq.insert(bNode);
		pq.insert(cNode);
		pq.insert(dNode);
		pq.insert(eNode);
		pq.insert(fNode);
		pq.insert(gNode);
		
		Node localRoot = new Node();
			
		while(pq.nItems > 1) {
			Node var1 = pq.remove();
			Node var2 = pq.remove();
			
			int total = var1.data + var2.data;
			
			localRoot = new Node();
			localRoot.data = total;
			
			localRoot.leftChild = var1;
			localRoot.rightChild = var2;
			
			pq.insert(localRoot);
			
			//System.out.print(localRoot.data);
			
		}
		
		// create huffman tree with specified root
		Tree huffTree = new Tree();
		huffTree.root = localRoot;
		
		// print tree ********************************************************************************
		//huffTree.displayTree();		// menu option
		
		// print code table ***************************************************************************
		String code = "";
		huffTree.createCode(localRoot, code);
		//huffTree.showTable();	// menu option
		
		//create binary message **********************************************************************
		Node[] treeTable = huffTree.getTable();
		String bString = "";
		
		// create string
		for(int i = 0; i < c.length; i++) {
			if(c[i] >= 65 && c[i] <= 71) {
				for(int j = 0; j < treeTable.length - 1; j++) {
					if(treeTable[j].letter == (char)c[i])
						bString = bString.concat(treeTable[j].code);
				}
			}
		}
		
		bString = java.util.Arrays.toString(bString.split("(?<=\\G........)"));
		bString = bString.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(",", "");
		
		int charCount = 0;
		StringBuilder sb = new StringBuilder(bString);
		while((charCount = sb.indexOf(" ", charCount + 26)) != -1) {
			sb.replace(charCount, charCount + 1, "\n");
		}
		bString = sb.toString();
		//System.out.println(bString);	// menu option
		
		// recreate letters from code and huffman tree ****************************************************
		String backToLetters = huffTree.decode(bString, huffTree.root);
		//System.out.println(backToLetters);		// menu option
		
		
		boolean done = false;
		String userInput;
		
		Scanner scan = new Scanner(System.in);
		
		while(!done) {
			System.out.println("Hello! Please enter menu selection");
			System.out.println("a: Display Huffman Tree");
			System.out.println("b: Display Code Table");
			System.out.println("c: Binary Encoding of original file");
			System.out.println("d: Decoded message");
			System.out.println("stop: ends the program");
			
			userInput = scan.next();
			
			if(userInput.equalsIgnoreCase("stop"))
				done = true;
			else {
				if(userInput.equalsIgnoreCase("a")) {
					huffTree.displayTree();
				}
				else if(userInput.equalsIgnoreCase("b")) {
					huffTree.showTable();	
				}
				else if(userInput.equalsIgnoreCase("c")) {
					System.out.println(bString);
				}
				else if(userInput.equalsIgnoreCase("d")) {
					System.out.println(backToLetters);	
				}
				else
					System.out.println("Invalid input");
			}
		}
		
		
	}
}
