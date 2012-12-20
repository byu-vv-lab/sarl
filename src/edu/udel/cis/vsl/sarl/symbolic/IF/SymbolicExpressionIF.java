package edu.udel.cis.vsl.sarl.symbolic.IF;

import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;

public interface SymbolicExpressionIF {

	/** Every expression in a universe has a unique id number */
	int id();

	String toString();

	/**
	 * A string representation appropriate for use in other expressions,
	 * typically by surrounding the normal string version with parentheses if
	 * necessary.
	 */
	String atomString();

	SymbolicTypeIF type();

}
