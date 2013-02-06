package edu.udel.cis.vsl.sarl.IF.type;

import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

/**
 * A symbolic type represents the type of a symbolic expression.
 * 
 * Every symbolic type has a "kind", given by the enumerate type
 * SymbolicTypeKind.
 * 
 * If the kind is ARRAY, the object can be cast to SymbolicArrayTypeIF.
 * 
 * If the kind is FUNCTION, the object can be cast to SymbolicFunctionTypeIF.
 * 
 * If the kind is TUPLE, the object can be cast to SymbolicTupleTypeIF.
 * 
 * If the kind is UNION, the object can be cast to SymbolicUnionTypeIF.
 * 
 * @author siegel
 * 
 */
public interface SymbolicTypeIF extends SymbolicObject {

	/**
	 * The different kinds of types.
	 */
	public enum SymbolicTypeKind {
		ARRAY, BOOLEAN, FUNCTION, INTEGER, REAL, TUPLE, UNION
	};

	/**
	 * Tells whether the type is the boolean type.
	 * 
	 * @return true if kind is BOOLEAN, else false
	 */
	boolean isBoolean();

	/**
	 * Tells whether the type is an integer type.
	 * 
	 * @return true if kind is INTEGER, else false
	 */
	boolean isInteger();

	/**
	 * Tells whether the type is a real or integer type
	 * 
	 * @return true if kind is REAL or INTEGER, false otherwise
	 */
	boolean isNumeric();

	/**
	 * Tells whether the type is a real type.
	 * 
	 * @return true if kind is REAL, else false
	 */
	boolean isReal();

	/**
	 * Returns the kind of the type.
	 */
	SymbolicTypeKind typeKind();

}
