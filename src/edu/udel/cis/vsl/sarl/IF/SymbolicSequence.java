package edu.udel.cis.vsl.sarl.IF;

public interface SymbolicSequence extends SymbolicCollection {

	int size();

	/**
	 * The arguments of a sequence expression are always symbolic expressions.
	 */
	SymbolicExpressionIF get(int index);

}
