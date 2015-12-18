package sandbox;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

public class Josephus {
	
	public static void main(String[] args) {
		boolean done = false;
		
		while(!done) {
			String numbers = JOptionPane.showInputDialog(null, "Enter numbers and such \n", "Josephus", JOptionPane.PLAIN_MESSAGE);
			Scanner input = new Scanner(numbers);
			
			if(numbers.equalsIgnoreCase("stop")) {
				done = true;
			}
			else {
				int[] nums = new int[3];
				
				for (int i = 0; i < 3; i++) {
					nums[i] = input.nextInt();
				}
				
				int captiveNumber = nums[0];
				int runStep = nums[1];
				int startFrom = nums[2];
					
				CircularLinkedList list = new CircularLinkedList(startFrom, captiveNumber, runStep);
				Link lastOne = list.runGame();
				//System.out.println("The survivor: " + lastOne);
				
				JOptionPane.showMessageDialog(null, "The survivor is: " + lastOne, "Josephus", JOptionPane.PLAIN_MESSAGE);
				
			}
		}
		
	
		
	}

	public static class CircularLinkedList {
		private Link dynamicHead;
		private int runStep;
		
		public CircularLinkedList(int startFrom, int captiveNumber, int runStep) {
			this.runStep = runStep + 1;
			Link previous = null;
			Link head = null;
			
			for(int i = 1; i <= captiveNumber; i++) {
				Link tmpLink = new Link(i);
				if(previous == null) {
					head = tmpLink;
				}
				else {
					previous.next = tmpLink;
				}
				previous = tmpLink;
				
				if(i == startFrom) {
					dynamicHead = tmpLink;
				}
			}
			previous.next = head;
		}
		
		private Link suicide() {
			Link current = dynamicHead;
			Link previous = null;
			int testPos = runStep;
			
			while(--testPos >= 1) {
				previous = current;
				current = current.next;
			}
			if (previous == current.next)
				previous.next = null;
			else
				previous.next = current.next;
			
			dynamicHead = current.next;
			return current;
		}
		
		public Link runGame() {
			ArrayList killed = new ArrayList();
			while(dynamicHead != null && dynamicHead.next != null) {
				killed.add(suicide());
			}
			
			
			for (int i = 0; i < killed.size(); i++) {
				Link kill = (Link) killed.get(i);
				//System.out.println("Killing: " + kill);
			}
			
			return dynamicHead;
		}
	}
	
	private static class Link {
		private int number;
		public Link next;
		
		public Link(int number) {
			this.number = number;
		}
		
		public String toString() {
			return String.valueOf(number);
		}
	}
}
