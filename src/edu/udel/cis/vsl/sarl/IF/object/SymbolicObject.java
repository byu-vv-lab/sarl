/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
 * 
 * <table>
 * <tr>
 * <td>SYMBOLIC_EXPRESSION</td>
 * <td>{@link edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression}</td>
 * </tr>
 * <tr>
 * <td>EXPRESSION_COLLECTION</td>
 * <td>{@link edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection}</td>
 * </tr>
 * <tr>
 * <td>TYPE</td>
 * <td>{@link edu.udel.cis.vsl.sarl.IF.type.SymbolicType}</td>
 * </tr>
 * <tr>
 * <td>TYPE_SEQUENCE</td>
 * <td>{@link edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence}</td>
 * </tr>
 * <tr>
 * <td>NUMBER</td>
 * <td>{@link NumberObject}</td>
 * </tr>
 * <tr>
 * <td>INT</td>
 * <td>{@link IntObject}</td>
 * </tr>
 * <tr>
 * <td>BOOLEAN</td>
 * <td>{@link BooleanObject}</td>
 * </tr>
 * <tr>
 * <td>STRING</td>
 * <td>{@link StringObject}</td>
 * </tr>
 * </table>
 * 
 * @author siegel
 * 
 */
public interface SymbolicObject {

	public enum SymbolicObjectKind {
		BOOLEAN, CHAR, EXPRESSION, EXPRESSION_COLLECTION, INT, NUMBER, STRING, TYPE, TYPE_SEQUENCE
	}

	/**
	 * Which kind of symbolic object is this? The kind determines the specific
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
	 * Every canonic symbolic object has a unique ID number, returned by this
	 * method. If this object is not canonic, the value returned is undefined.
	 * 
	 * @return the ID number of this canonic symbolic object
	 */
	int id();

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
	 * 
	 * @return a string representation of this object
	 */
	StringBuffer toStringBuffer(boolean atomize);

	/**
	 * Returns a detailed string representation of this object as a
	 * StringBuffer. It never needs to be atomized.
	 * 
	 * @return detailed string representation of this object
	 */
	StringBuffer toStringBufferLong();

}
