package edu.udel.cis.vsl.sarl.IF.object;

/**
 * A symbolic object wrapping a single Java "int" value.
 * 
 * @author siegel
 * 
 */
public interface IntObject extends SymbolicObject, Comparable<IntObject> {

	/**
	 * Gets the int value.
	 * 
	 * @return the int value
	 */
	int getInt();

	/**
	 * Returns the minimum of this and that as an IntObject.
	 * 
	 * @param that
	 *            any IntObject
	 * @return the minimum of this and that
	 */
	IntObject minWith(IntObject that);

	/**
	 * Returns the maximum of this and that as an IntObject.
	 * 
	 * @param that
	 *            any IntObject
	 * @return the maximum of this and that
	 */
	IntObject maxWith(IntObject that);

	/**
	 * Returns the result of subtracting that from this, as an IntObject.
	 * 
	 * @param that
	 *            any IntObject
	 * @return this-that
	 */
	IntObject minus(IntObject that);

	/**
	 * Returns the result of addting this and that, as an IntObject.
	 * 
	 * @param that
	 *            any IntObject
	 * @return this+that
	 */
	IntObject plus(IntObject that);

	/**
	 * Returns -1 if the int value is negative, 0 if the int value is zero, or
	 * +1 if the int value is positive.
	 * 
	 * @return the signum of the int value
	 */
	int signum();

	/**
	 * Is the int value zero?
	 * 
	 * @return true iff the the int value is 0
	 */
	boolean isZero();

	/**
	 * Is the int value 1?
	 * 
	 * @return true iff the int value is 1
	 */
	boolean isOne();

	/**
	 * Is the int value positive?
	 * 
	 * @return true iff the int value is positive
	 */
	boolean isPositive();

	/**
	 * Is the int value negative?
	 * 
	 * @return true iff the int value is negative
	 */
	boolean isNegative();

}
