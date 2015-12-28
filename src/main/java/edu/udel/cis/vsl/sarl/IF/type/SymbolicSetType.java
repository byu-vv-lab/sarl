package edu.udel.cis.vsl.sarl.IF.type;

/**
 * The type representing a set. This is a set in the mathematical sense. The set
 * type specified the type of the elements.
 * 
 * @author siegel
 * 
 */
public interface SymbolicSetType extends SymbolicType {

	/**
	 * The type of the elements of the set.
	 * 
	 * @return the element type
	 */
	SymbolicType elementType();
}
