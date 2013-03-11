package edu.udel.cis.vsl.sarl.IF.type;

import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

/**
 * A finite, ordered sequence of SymbolicType.
 * 
 * @author siegel
 */
public interface SymbolicTypeSequence extends Iterable<SymbolicType>,
		SymbolicObject {

	/**
	 * Returns the number of types in this sequence.
	 * 
	 * @return the number of types
	 */
	int numTypes();

	/**
	 * Returns the index-th type in this sequence.
	 * 
	 * @param index
	 *            an integer in range [0,n-1], where n is the number of types in
	 *            this sequence
	 * @return the index-th type
	 */
	SymbolicType getType(int index);

}
