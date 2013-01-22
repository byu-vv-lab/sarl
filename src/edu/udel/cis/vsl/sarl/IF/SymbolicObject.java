package edu.udel.cis.vsl.sarl.IF;

/**
 * Any kind of symbolic object.
 * 
 * Also provides
 * <ul>
 * <li>useful <code>equals</code> and <code>hashCode</code> methods
 * <li>a comparator consistent with <code>equals</code>, so there is a total
 * order on symbolic objects</li>
 * <li>useful <code>toString</code> methods (provided by each implementing
 * class)</li>
 * </ul>
 * 
 * The kind determines the Java type of this object as follows:
 * <ul>
 * <li>SYMBOLIC_EXPRESSION: SymbolicExpressionIF</li>
 * <li>COLLECTION: SymbolicCollection</li>
 * <li>NUMBER: NumberObject</li>
 * <li>INTEGER: IntObject</li>
 * <li>BOOLEAN: BooleanObject</li>
 * <li>STRING: StringObject</li>
 * </ul>
 * 
 * @author siegel
 * 
 */
public interface SymbolicObject extends Comparable<SymbolicObject> {

	public enum SymbolicObjectKind {
		SYMBOLIC_EXPRESSION, COLLECTION, NUMBER, INTEGER, BOOLEAN, STRING
	}

	SymbolicObjectKind symbolicObjectKind();

	/**
	 * Every symbolic object has a unique ID number, returned by this method.
	 * 
	 * @return the ID number of this symbolic object
	 */
	long id();

	@Override
	boolean equals(Object o);

	@Override
	int hashCode();

	@Override
	String toString();

}
