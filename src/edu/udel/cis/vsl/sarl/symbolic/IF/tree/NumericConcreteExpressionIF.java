package edu.udel.cis.vsl.sarl.symbolic.IF.tree;

import edu.udel.cis.vsl.sarl.number.IF.NumberIF;

/**
 * A numeric concrete expression essentially "wraps" an instance of NumberIF. A
 * number of methods are provided for "convenience" but the same information can
 * also be obtained from the underlying NumberIF object.
 */
public interface NumericConcreteExpressionIF extends ConcreteExpressionIF {

	/**
	 * The instance of NumberIF wrapped by this expression.
	 */
	NumberIF value();

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
	 * Tells whether the type of this expression is integer.
	 */
	boolean isInteger();

	/**
	 * Tells whether the type of this expression is real.
	 */
	boolean isReal();
}
