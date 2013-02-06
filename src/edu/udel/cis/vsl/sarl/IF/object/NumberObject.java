package edu.udel.cis.vsl.sarl.IF.object;

import edu.udel.cis.vsl.sarl.IF.number.NumberIF;

public interface NumberObject extends SymbolicObject {

	NumberIF getNumber();

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
	
	boolean isInteger();
	
	boolean isReal();

}
