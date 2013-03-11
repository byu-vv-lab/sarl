package edu.udel.cis.vsl.sarl.IF;

/**
 * A Transform from type S to type T is an object that provides a method "apply"
 * which takes an element of S and returns an element of T.
 * 
 * @author siegel
 * 
 * @param <S>
 *            any Java type
 * @param <T>
 *            any Java type
 */
public interface Transform<S, T> {

	/**
	 * Apply this Transform to the element x of S.
	 * 
	 * @param x
	 *            an element of S
	 * @return the result of applying this Transform to x
	 * */
	T apply(S x);

}
