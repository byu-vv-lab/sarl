package edu.udel.cis.vsl.sarl.IF.type;

import edu.udel.cis.vsl.sarl.IF.object.StringObject;

/**
 * A tuple type is specified by (1) a name, and (2) an ordered, finite sequence
 * of component types. The domain of the tuple type is the Cartesian product of
 * the domains of the component types.
 * 
 * For two tuple types to be equal, their names must be equal, and their
 * sequences must be equal (have the same length and the corresponding types are
 * equal).
 * 
 * @author siegel
 * 
 */
public interface SymbolicTupleType extends SymbolicType {

	/**
	 * Returns the name of this tuple type, a non-null StringObject.
	 * 
	 * @return the name
	 */
	StringObject name();

	/**
	 * Returns the sequence of component types of this tuple object. May have
	 * length 0.
	 * 
	 * @return the component type sequence
	 */
	SymbolicTypeSequence sequence();
}
