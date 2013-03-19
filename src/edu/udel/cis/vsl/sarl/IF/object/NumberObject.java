package edu.udel.cis.vsl.sarl.IF.object;

import edu.udel.cis.vsl.sarl.IF.number.Number;

/**
 * A symbolic object wrapping a single instance of
 * {@link edu.udel.cis.vsl.sarl.IF.number.Number}.
 * 
 * @author siegel
 * 
 */
public interface NumberObject extends SymbolicObject, Comparable<NumberObject> {

	/**
	 * Get the underlying Number.
	 * 
	 * @return the Number wrapped by this object
	 */
	Number getNumber();

	/**
	 * Returns either -1, 0, or +1, depending on whether the value of this
	 * expression is <0, 0, or >0 (respectively).
	 */
	int signum();

	/**
	 * Convenience method to determine whether this concrete expression is 0.
	 * Works for integer or real types.
	 */
	boolean isZero();

	/**
	 * Convenience method to determine whether this concrete expression is 1.
	 * Works for integer or real types.
	 */
	boolean isOne();

	/**
	 * Does the number have integer type? (Note: a rational number which is
	 * integer, e.g., "2/1", does not have integer type.)
	 * 
	 * @return true iff the number has integer type
	 */
	boolean isInteger();

	/**
	 * Does the number have real (not integer) type?
	 * 
	 * @return true iff the number is real
	 */
	boolean isReal();

}
