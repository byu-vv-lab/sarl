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

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;

/**
 * <p>
 * The root of the symbolic object type hierarchy. Represents any kind of
 * symbolic object: symbolic expressions, symbolic types, symbolic expression
 * collections, etc.
 * </p>
 * 
 * <p>
 * All symbolic objects provide reasonable hash code methods that are consistent
 * with equals.
 * </p>
 * 
 * <p>
 * The "kind" determines the Java type of this object as follows:
 * 
 * <table>
 * <tr>
 * <td>SYMBOLIC_EXPRESSION</td>
 * <td>{@link SymbolicExpression}</td>
 * </tr>
 * <tr>
 * <td>EXPRESSION_COLLECTION</td>
 * <td>{@link SymbolicCollection}</td>
 * </tr>
 * <tr>
 * <td>TYPE</td>
 * <td>{@link SymbolicType}</td>
 * </tr>
 * <tr>
 * <td>TYPE_SEQUENCE</td>
 * <td>{@link SymbolicTypeSequence}</td>
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
 * </p>
 * 
 * <p>
 * Structure: Each symbolic object has some number (possibly 0) of "children",
 * which are also symbolic objects. If v is a child of u, we say u is a "parent"
 * of v.
 * </p>
 * 
 * <p>
 * The child relation gives the set of all symbolic objects the structure of a
 * directed acyclic graph. The set of nodes reachable from u (including u) are
 * the "descendants" of u. Dually, the set of nodes that can reach u (including
 * u) are the "ancestors" of u.
 * </p>
 * 
 * <p>
 * Each symbolic object has a <strong>status</strong>, one of
 * <ol>
 * <li>FREE</li>
 * <li>OWNED</li>
 * <li>COMMITTED</li>
 * <li>CANONIC</li>
 * </ol>
 * Initially, the status is FREE and the object is not the child of any object.
 * A FREE object becomes OWNED if it is made the child of another object. A FREE
 * or OWNED object can be <strong>committed</strong>, at which point its status
 * becomes COMMITTED. A committed object can be <strong>canonized</strong> and,
 * at which point its status becomes CANONIC.
 * </p>
 * 
 * <p>
 * An object which is FREE or OWNED is <strong>mutable</strong>. An object which
 * is not mutable is <strong>immutable</strong>.
 * </p>
 * 
 * <p>
 * Some invariants:
 * <ul>
 * <li>All ancestors of a mutable object are also mutable.</li>
 * <li>A FREE object has no parent; an OWNED object has exactly one parent.</li>
 * <li>All descendants of an immutable object are also immutable.</li>
 * <li>All descendants of a CANONIC object are CANONIC.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * As the name implies, a mutable object may be modified by certain methods. A
 * mutable object can be the child of at most one symbolic object---its
 * "parent". The rationale is that a mutable object cannot be safely shared.
 * </p>
 * 
 * <p>
 * An immutable object cannot be modified in any way that could be distinguished
 * by the <code>equals</code> method. Methods that would otherwise modify the
 * object either throw an exception or instead return a new object --- see the
 * documentation for each method for details. An immutable object can be the
 * child of any number of other objects (as it can be safely shared). When an
 * object is committed, all of its descendants are committed too.
 * </p>
 * 
 * <p>
 * A CANONIC object is not only immutable, it is designated as the unique
 * representative of its equivalence class and permanently stored, a la the
 * Flyweight Pattern. The method {@link ObjectFactory#canonic(SymbolicObject)}
 * can be used to get the canonic representative of the equivalence class of any
 * symbolic object. If a canonic representative has not yet been designated for
 * that equivalence class, the given object is made the canonic representative
 * (and is committed and canonized in the proess). When an object is canonized,
 * so are all of its descendants.
 * </p>
 * 
 * <p>
 * General protocols: Methods may declare that certain arguments, if MUTABLE,
 * may be modified; here "modified" applies to any mutable descendants of those
 * arguments as well. The user needs to be aware that if that mutable argument
 * is a descendant of some other object, then modifications to it will also be
 * seen in the other object. The method contract should specify exactly how the
 * argument may be modified. Any method, at any time, may commit an object. If a
 * method wishes to take a mutable object o which has a non-null parent, and
 * make o a child of another object, it will first commit o. In general, the
 * object returned by a method should not depend on whether the arguments are
 * committed, at least in way that can be distinguished by "equals".
 * </p>
 * 
 * <p>
 * Example: setting the field of a tuple:
 * <code>tupleWrite(tuple, index, value)</code>. Argument <code>tuple</code> may
 * be modified if it is mutable. The new value will be the result of performing
 * the tuple-write to the old value. If <code>tuple</code> is immutable, it will
 * not be modified, and a new tuple value will be constructed and returned
 * instead. In either case, the object returned will be the result of performing
 * the tuple-write operation. No other argument will be modified (other than to
 * be committed or have components committed).
 * </p>
 *
 * <p>
 * Example: ideal mathematical addition: the first argument <code>x</code> may
 * be modified if it is mutable. If modified, it will be replaced with an object
 * representing the sum of the original value of <code>x</code> and
 * <code>y</code>. The second argument <code>y</code> will not be modified in
 * any case (other than to be committed or have components be committed). For
 * example, if x=sum{x1, x2, x3} and y=sum{y1, y2}, we might have x replaced
 * with sum{x1, x2, x3, y1, y2} with y1 and y2 committed.
 * </p>
 * 
 * <p>
 * Example: setting an element in a 2d-array. a=[a0=[1,2,3], a1=[4,5,6]].
 * Suppose you want to change element at position (1,0) from 4 to 9 and call the
 * resulting array a'. The usual way to do this is: let a1=read(a,1); let
 * a1'=write(a1,0,9), let a'=write(a,1,a1'). If a1 is mutable (which implies a
 * is mutable), then the write(a1,0,9) will modify and return a1, and the
 * write(a,1,a1') will effectively do nothing. In particular, no new objects are
 * created. If, on the other hand, a1 is immutable and a is mutable, this will
 * create a new object a1' that will replace a1 in a. If both a1 and a are
 * immutable, two new objects, a' and a1', will be created.
 * </p>
 * 
 * <p>
 * Future work: if there is demand, at some point we may provide a way to "free"
 * an OWNED object (basically making its parent invalid) so it can be re-used in
 * another expression without necessarily committing it. More generally: how
 * about introducing refCount: number of parents. increment and decrement.
 * </p>
 * 
 * @author Stephen F. Siegel
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

	/**
	 * Commits this symbolic object, making it immutable. If the object was
	 * mutable (status FREE or OWNED), its status becomes COMMITTED. If the
	 * object was immutable (status COMMITTED or CANONIC), this is a no-op.
	 * 
	 * @return this
	 * 
	 * @see #isImmutable()
	 */
	SymbolicObject commit();

	/**
	 * Tells whether this symbolic object is immutable, i.e., has status
	 * COMMITTED or CANONIC.
	 * 
	 * @return <code>true</code> iff this object is immutable
	 * 
	 * @see #commit()
	 */
	boolean isImmutable();

	/**
	 * Tells whether this symbolic object has status FREE, i.e., it mutable and
	 * is not the child of any object.
	 * 
	 * @return <code>true</code> iff this object is free
	 */
	boolean isFree();

	/**
	 * Makes this object a child of another object. This method should be
	 * invoked when making this object a child of another object. If the status
	 * is FREE, the status is changed to OWNED. If the status is OWNED,
	 * {@link #commit()} is invoked on this object. Otherwise it is a no-op.
	 */
	void makeChild();

	/**
	 * Declares this object to no longer be a child of one particular object. If
	 * this object is FREE, this is a no-op. If the object is OWNED, it becomes
	 * FREE. Otherwise, it is a no-op.
	 */
	void release();

}
