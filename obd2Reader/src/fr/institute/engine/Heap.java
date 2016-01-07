package fr.institute.engine;

import java.util.ArrayList;

public class Heap<T> {
	private ArrayList<T> heap;
	
	public Heap(){
		heap = new ArrayList<T>();
	}
	
	/**
	 * Add an element to the last index of the heap.
	 * @param element
	 */
	public void stack(T element){
		heap.add(element);
	}
	
	/**
	 * Remove the last element of the heap and return it.
	 * @return
	 */
	public T unStack(){
		T toReturn = heap.remove(heap.size()-1);
		System.out.println(toReturn);
		return(toReturn);
	}
	
	/**
	 * Get the last element of the heap without removing it.
	 * @return
	 */
	public T getTop(){
		return(heap.get(heap.size()-1));
	}
	
	public Boolean isEmpty(){
		return heap.isEmpty();
	}
}
