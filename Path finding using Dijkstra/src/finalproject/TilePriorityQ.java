package finalproject;

import java.util.ArrayList;
import java.util.Arrays;


import finalproject.system.Tile;
import finalproject.tiles.DesertTile;

public class TilePriorityQ {
	// TODO level 3: Add fields that can help you implement this data type
	public ArrayList<Tile> heap;


	// TODO level 3: implement the constructor for the priority queue
	public TilePriorityQ (ArrayList<Tile> vertices) {
		this.heap = new ArrayList<>();
		this.heap.add(null);
		this.heap.addAll(vertices);
		// Build the heap using downheap
		for (int i = (heap.size() / 2); i >= 1; i--) {
			downHeap(i, heap.size() - 1);
		}
	}
	
	// TODO level 3: implement remove min as seen in class
	public Tile removeMin() {
		if (heap.size() <= 1) {
			return null;
		}

		Tile min = heap.get(1);
		Tile lastTile = heap.remove(heap.size() - 1);

		if (heap.size() > 1) {
			heap.set(1, lastTile);
			downHeap(1, heap.size() - 1);
		}

		return min;


	}
	
	// TODO level 3: implement updateKeys as described in the pdf
	public void updateKeys(Tile t, Tile newPred, double newEstimate) {
		for (int i = 1; i < heap.size(); i++) {
			if (heap.get(i).equals(t)) {
				heap.get(i).predecessor = newPred;
				heap.get(i).costEstimate = newEstimate;
				//reorder heap
				for (int j = (heap.size() / 2); j >= 1; j--) {
					downHeap(j, heap.size() - 1);
				}
				break;
			}
		}
	}




	//helper function
	//add



	//down heap
	private void downHeap(int startIndex, int maxIndex) {
		int i = startIndex;
		//less than left child
		while (2 * i <= maxIndex) {
			int child = 2 * i;

			if (child < maxIndex && heap.get(child + 1).costEstimate < heap.get(child).costEstimate) {

				child = child + 1;
			}
			//swap
			if (heap.get(child).costEstimate < heap.get(i).costEstimate) {
				Tile temp = heap.get(i);
				heap.set(i,heap.get(child));
				heap.set(child,temp);
				i = child;

			} else {
				break;
			}
		}
	}




	public void add(Tile key){
		int size = heap.size(); // number of elements in heap
		// assuming array has room for another element
		heap.add(key);
		int i = size;
		//  the following is sometimes called "upHeap"
		while ( i > 1 && heap.get(i).costEstimate < heap.get(i/2).costEstimate){
			Tile temp = heap.get(i);
			heap.set(i,heap.get(i/2));
			heap.set(i/2,temp);
			i = i/2;
		} }




}
