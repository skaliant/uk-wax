package de.skaliant.wax.util;

/**
 * Simple helper class for a pair of objects (such as a key value pair).
 *
 * @author Udo Kastilan
 */
public class Pair<A, B> {
	private A first = null;
	private B second = null;


	/**
	 * Creates an instance making use of type inference.
	 * 
	 * @param first
	 *          First object
	 * @param second
	 *          Second object
	 * @return Pair
	 */
	public static <A, B> Pair<A, B> create(A first, B second) {
		return new Pair<A, B>(first, second);
	}


	/**
	 * Creates an instance.
	 * 
	 * @param first
	 *          First object
	 * @param second
	 *          Second object
	 */
	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}


	/**
	 * Gets the first object.
	 * 
	 * @return
	 */
	public A getFirst() {
		return first;
	}


	/**
	 * Sets the first object.
	 * 
	 * @param first
	 */
	public void setFirst(A first) {
		this.first = first;
	}


	/**
	 * Gets the second object.
	 * 
	 * @return
	 */
	public B getSecond() {
		return second;
	}


	/**
	 * Sets the second object.
	 * 
	 * @param second
	 */
	public void setSecond(B second) {
		this.second = second;
	}
}
