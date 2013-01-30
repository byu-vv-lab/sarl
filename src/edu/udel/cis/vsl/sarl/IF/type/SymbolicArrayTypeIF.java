package edu.udel.cis.vsl.sarl.IF.type;


/**
 * An array type T[]. The elements of this type are arrays of T. A subtype is
 * the complete array type, in which the extents are also specified.
 * 
 * @author siegel
 * 
 */
public interface SymbolicArrayTypeIF extends SymbolicTypeIF {

	/**
	 * The type of each element of the array, a non-null symbolic type.
	 * 
	 * @return the element type
	 */
	SymbolicTypeIF elementType();

	/**
	 * Is this a complete array type, i.e., is the extent specified? If true,
	 * this can be safely cast to SymbolicCompleteArrayTypeIF.
	 * 
	 * @return true iff this array type is complete
	 */
	boolean isComplete();

}
