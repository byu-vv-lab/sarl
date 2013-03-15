package edu.udel.cis.vsl.sarl.IF.type;

import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

/**
 * A symbolic type represents the type of a symbolic expression.
 * 
 * Every symbolic type has a "kind", given by the enumerate type
 * SymbolicTypeKind.
 * 
 * If the kind is ARRAY, the object can be cast to SymbolicArrayType.
 * 
 * If the kind is FUNCTION, the object can be cast to SymbolicFunctionType.
 * 
 * If the kind is TUPLE, the object can be cast to SymbolicTupleType.
 * 
 * If the kind is UNION, the object can be cast to SymbolicUnionType.
 * 
 * @author siegel
 * 
 */
public interface SymbolicType extends SymbolicObject {

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

	/**
	 * Is this a Herbrand type? There are Herbrand variants of real and integer
	 * types. Operations on Herbrand expressions are treated as uninterpreted
	 * functions: no simplifications or transformations of any kind are
	 * performed.
	 * 
	 * @return true iff this type is a Herbrand type
	 */
	boolean isHerbrand();
	
	/**
	 * Is this an Ideal numeric type?  These are the mathematical integer
	 * and real types.
	 * 
	 * @return true iff this is the ideal real type or ideal integer type
	 */
	boolean isIdeal();

}
