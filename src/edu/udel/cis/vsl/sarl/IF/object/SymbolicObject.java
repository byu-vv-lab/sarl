package edu.udel.cis.vsl.sarl.IF.object;

/**
 * The root of the symbolic object type hierarchy. Represents any kind of
 * symbolic object: symbolic expressions, symbolic types, symbolic expression
 * collections, etc.
 * 
 * All symbolic objects provide reasonable hash code methods that are consistent
 * with equals.
 * 
 * 
 * The kind determines the Java type of this object as follows:
 * <ul>
 * <li>SYMBOLIC_EXPRESSION: SymbolicExpression</li>
 * <li>EXPRESSION_COLLECTION: SymbolicCollection</li>
 * <li>TYPE: SymbolicType</li>
 * <li>TYPE_SEQUENCE: SymbolicTypeSequence</li>
 * <li>NUMBER: NumberObject</li>
 * <li>INT: IntObject</li>
 * <li>BOOLEAN: BooleanObject</li>
 * <li>STRING: StringObject</li>
 * </ul>
 * 
 * @author siegel
 * 
 */
public interface SymbolicObject {

	public enum SymbolicObjectKind {
		BOOLEAN,
		EXPRESSION,
		EXPRESSION_COLLECTION,
		INT,
		NUMBER,
		STRING,
		TYPE,
		TYPE_SEQUENCE
	}

	/**
	 * What kindn of symbolic object is this? The kind determines the specific
	 * type to which this object can be safely cast. See the comments for this
	 * interface and the enumerated type SymbolicObjectKind for description.
	 * 
	 * @return the kind of this symbolic object.
	 */
	SymbolicObjectKind symbolicObjectKind();

	/**
	 * The equals method, included here to emphasize that the method provided by
	 * Object must be overridden!
	 * 
	 * @param o
	 *            any Java object
	 * @return true iff this represents the "same" symbolic object as o
	 */
	@Override
	boolean equals(Object o);

	/**
	 * The hash code method, included here to emphasize that the method provided
	 * by Object must be overridden!
	 * 
	 * @return the hash code of this symbolic object
	 */
	@Override
	int hashCode();

	/**
	 * Every symbolic object has a unique ID number, returned by this method.
	 * 
	 * @return the ID number of this symbolic object
	 */
	long id();

	/**
	 * Is this object the unique representative of its equivalence class (under
	 * "equals")?
	 * 
	 * @return true iff this object is canonic
	 */
	boolean isCanonic();

	/**
	 * Returns a string representation of this object, included here to
	 * emphasize that the method provided by Object must be overridden!
	 * 
	 * Same as toStringBuffer(false).toString();
	 * 
	 * @return a string representation of this object
	 */
	@Override
	String toString();

	/**
	 * Returns a string representation of this object as a StringBuffer. Use
	 * this instead of "toString()" for performance reasons if you are going to
	 * be building up big strings.
	 * 
	 * @param atomize
	 *            if true, place parentheses around the string if necessary in
	 *            order to include this as a term in a larger expression
	 */
	StringBuffer toStringBuffer(boolean atomize);

	/**
	 * Returns a detailed string representation of this object as a
	 * StringBuffer. It never needs to be atomized.
	 * 
	 */
	StringBuffer toStringBufferLong();

}
