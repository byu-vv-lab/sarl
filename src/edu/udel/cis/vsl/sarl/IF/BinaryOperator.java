package edu.udel.cis.vsl.sarl.IF;

/**
 * A binary operator on a type T is an object which provides a method "apply"
 * which takes two elements of T and returns an element of T.
 * 
 * @author siegel
 * 
 */
public interface BinaryOperator<T> {

	/**
	 * Apply this binary operator to the given two elements of T.
	 * 
	 * @param x
	 *            an element of T
	 * @param y
	 *            an element of T
	 * @return the result of applying this binary operator to arg0 and arg1
	 */
	T apply(T x, T y);

}
