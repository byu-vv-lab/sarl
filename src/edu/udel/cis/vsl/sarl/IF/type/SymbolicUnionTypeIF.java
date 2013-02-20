package edu.udel.cis.vsl.sarl.IF.type;

import edu.udel.cis.vsl.sarl.IF.object.StringObject;

/**
 * A union type of a sequence of types t_i. The elements of the union type have
 * the form inject_i(x), where x is an element of type t_i.
 * 
 * @author siegel
 * 
 */
public interface SymbolicUnionTypeIF extends SymbolicTypeIF {

	/**
	 * The sequence of types comprising this union. This sequence must not
	 * contain duplicates.
	 * 
	 * @return the sequence of types comprising the union
	 */
	SymbolicTypeSequenceIF sequence();

	/**
	 * The name of this union type.
	 * 
	 * @return name of this union type
	 */
	StringObject name();

	/**
	 * If this type exists in the sequence of types comprising this union, this
	 * method will find the index of that type and return it; otherwise it
	 * returns null.
	 * 
	 * @param type
	 *            a symbolic type
	 * @return index of this type in the type sequence or null
	 */
	Integer indexOfType(SymbolicTypeIF type);

}
