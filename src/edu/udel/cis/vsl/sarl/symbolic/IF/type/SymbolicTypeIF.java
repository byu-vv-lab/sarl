package edu.udel.cis.vsl.sarl.symbolic.IF.type;

/**
 * A symbolic type represents the type of a symbolic expression.
 * 
 * @author siegel
 * 
 */
public interface SymbolicTypeIF {

	/**
	 * The different kinds of types.
	 */
	public enum SymbolicTypeKind {
		BOOLEAN, INTEGER, REAL, ARRAY, TUPLE, FUNCTION
	};

	/**
	 * Returns the kind of the type.
	 */
	SymbolicTypeKind kind();

	/**
	 * Tells whether the type is a real or integer type
	 * 
	 * @return true if kind is REAL or INTEGER, false otherwise
	 */
	boolean isNumeric();

	/**
	 * Tells whether the type is an integer type.
	 * 
	 * @return true if kind is INTEGER, else false
	 */
	boolean isInteger();

	/**
	 * Tells whether the type is a real type.
	 * 
	 * @return true if kind is REAL, else false
	 */
	boolean isReal();

	/**
	 * Tells whether the type is the boolean type.
	 * 
	 * @return true if kind is BOOLEAN, else false
	 */
	boolean isBoolean();

}
