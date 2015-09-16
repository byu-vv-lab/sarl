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
 * directed graph. The set of nodes reachable from u (including u) are the
 * "descendants" of u. Dually, the set of nodes that can reach u (including u)
 * are the "ancestors" of u. The graph may contain cycles.
 * </p>
 * 
 * <p>
 * Each symbolic object has a <strong>status</strong>, one of
 * <ol>
 * <li>MUTABLE</li>
 * <li>COMMITTED</li>
 * <li>CANONIC</li>
 * </ol>
 * Initially, the status is MUTABLE and the object is mutable, i.e., it can be
 * modified. A mutable object can be committed, at which point its status
 * becomes COMMITTED and it is immutable. A COMMITTED object can be uncommitted,
 * at which point its status again becomes MUTABLE and it is mutable. An object
 * can be <strong>canonized</strong> at which point its status becomes CANONIC
 * and it is immutable forever.
 * </p>
 * 
 * <p>
 * Some invariants:
 * <ul>
 * <li>All ancestors of a MUTABLE object are also MUTABLE.</li>
 * <li>All descendants of an immutable object are also immutable.</li>
 * <li>All descendants of a CANONIC object are CANONIC.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * An immutable object cannot be modified in any way that could be distinguished
 * by the <code>equals</code> method. Methods that would otherwise modify the
 * object either throw an exception or instead return a new object --- see the
 * documentation for each method for details.
 * </p>
 * 
 * <p>
 * A CANONIC object is not only immutable, it is designated as the unique
 * representative of its equivalence class and permanently stored, a la the
 * Flyweight Pattern. The method {@link ObjectFactory#canonic(SymbolicObject)}
 * can be used to get the canonic representative of the equivalence class of any
 * symbolic object. If a canonic representative has not yet been designated for
 * that equivalence class, the given object is made the canonic representative
 * (and is committed and canonized in the process). When an object is canonized,
 * so are all of its descendants.
 * </p>
 * 
 * <p>
 * This interface does not provide any methods to manually commit or uncommit.
 * Instead, these events occur automatically according to a certain protocol.
 * The protocol ensures that an object is mutable iff it is not shared. (An
 * object o is shared if there exist distinct objects a and b such that a can
 * reach o, b can reach o, a cannot reach b, and b cannot reach a.) This
 * protocol uses the <b>reference count</b> which is associated to each
 * non-canonic object.
 * </p>
 * 
 * <p>
 * The reference count of o is the number of objects (either SARL symbolic
 * objects or external objects that SARL does not know about) that contain a
 * direct reference to o. The user can declare external references to come into
 * existence or disappear by invoking methods {@link #incrementReferenceCount()}
 * and {@link #decrementReferenceCount()}, resp. SARL will automatically invoke
 * these methods for internal references, i.e., references that are created or
 * destroyed between two symbolic objects.
 * </p>
 * 
 * <p>
 * The reference count of o is initially 0. When
 * {@link #incrementReferenceCount()} is invoked, it increments the reference
 * count. If the new reference count is greater than 1, o and all of its
 * descendants are committed. When {@link #decrementReferenceCount()} is
 * invoked, it decrements the reference count. If the new reference count is 1
 * and the reference count of all ancestors of o is at most 1 then o is
 * uncommitted and the same policy is applied to all descendants of o. If, after
 * decrementing, the new reference count of o is 0, then in addition o is
 * "destroyed": all of its fields are set to null and
 * {@link #decrementReferenceCount()} is invoked on each child of o.
 * </p>
 * 
 * <p>
 * It is erroneous to invoke {@link #decrementReferenceCount()} when the
 * reference count is 0.
 * </p>
 * 
 * <p>
 * Note that {@link #decrementReferenceCount()} is the only event that can trigger
 * destruction.
 * </p>
 * 
 * <p>
 * Canonic objects are immutable forever and do not have a reference count.
 * Invoking any of the reference count methods on a canonic object results in
 * undefined behavior.
 * </p>
 * 
 * <p>
 * General protocols: many methods will come in two versions: immutable and
 * mutable. For example, the methods <code>add</code> and <code>addMut</code> in
 * the symbolic set interface. Each of these adds an element to a set. The first
 * version (immutable) never modifies the set; it instead creates and returns a
 * new set. The second version (mutable) may modify the set if the set is
 * MUTABLE; otherwise it will behave just like the immutable version.
 * </p>
 * 
 * <p>
 * Note that many methods may create new references between symbolic objects.
 * This results in invocations of the reference count increment/decrement
 * methods automatically. The user does not have to invoke these methods on such
 * internal references.
 * </p>
 * 
 * 
 * <p>
 * Example: setting the field of a tuple:
 * <code>tupleWriteMut(tuple, index, value)</code>. Argument <code>tuple</code>
 * may be modified if it is mutable. The new value will be the result of
 * performing the tuple-write to the old value. If <code>tuple</code> is
 * immutable, it will not be modified, and a new tuple value will be constructed
 * and returned instead. In either case, the object returned will be the result
 * of performing the tuple-write operation. No other argument will be modified
 * (other than to have its reference count/status changed).
 * </p>
 *
 * <p>
 * Example: ideal mathematical addition <code>addMut(x,y)</code>: the first
 * argument <code>x</code> may be modified if it is mutable. If modified, it
 * will be replaced with an object representing the sum of the original value of
 * <code>x</code> and <code>y</code>. The second argument <code>y</code> will
 * not be modified in any case (other than to have its reference count/status
 * changed). For example, if x=sum{x1, x2, x3} and y=sum{y1, y2}, we might have
 * x replaced with sum{x1, x2, x3, y1, y2} with y1 and y2 will have their
 * reference counts incremented.
 * </p>
 * 
 * <p>
 * Example: The user must be careful to inform SARL about the reference counts
 * from the user's state to SARL objects. Suppose <code>x</code>is a variable in
 * a Java code of type {@link SymbolicExpression}, and you wish to execute an
 * assignment of the form <code>x=expr;</code>. If in the pre-state
 * <code>x</code> is <code>null</code> the correct way to do this is:
 * 
 * <pre>
 * x = expr;
 * x.incrementReferenceCount();
 * </pre>
 * 
 * If the value of <code>x</code> in the pre-state is not <code>null</code> and
 * <code>x</code> is not used in <code>expr</code> then the correct way is:
 * 
 * <pre>
 * x.decrementReferenceCount();
 * x = expr;
 * x.incrementReferenceCount();
 * </pre>
 * 
 * If the value of <code>x</code> in the pre-state is not <code>null</code> and
 * <code>x</code> is used in <code>expr</code>, the above will not necessarily
 * work because the decrement of the reference count might cause the reference
 * count to be 0 and therefore <code>x</code> to be destroyed before it is used
 * in the next line. Instead, do something like:
 * 
 * <pre>
 * {
 * 	SymbolicExpression tmp = x;
 * 	x = universe.and(tmp, y);
 * 	if (x != tmp) {
 * 		x.incrementReferenceCount();
 * 		tmp.decrementReferenceCount();
 * 	}
 * }
 * </pre>
 * 
 * The patterns above are exactly the same for <code>andMut</code> (or
 * <code>addMut</code> or ...). Just for <code>andMut</code>, most of the time,
 * <code>x==tmp</code>, while for <code>and</code>, most of the time,
 * <code>x!=tmp</code>.
 * </p>
 * 
 * <p>
 * Example: setting an element in a 2d-array. a=[a0=[1,2,3], a1=[4,5,6]].
 * Suppose you want to change element at position (1,0) from 4 to 9 and call the
 * resulting array a'. The usual way to do this is: let a1=read(a,1); let
 * a1'=write(a1,0,9), let a'=writeMut(a,1,a1'). If a1 is mutable (which implies
 * a is mutable), then the write(a1,0,9) will modify and return a1, and the
 * write(a,1,a1') will effectively do nothing. In particular, no new objects are
 * created. If, on the other hand, a1 is immutable and a is mutable, this will
 * create a new object a1' that will replace a1 in a. If both a1 and a are
 * immutable, two new objects, a' and a1', will be created.
 * </p>
 * 
 * <p>
 * Example: an application like CIVL might have a method in Dycope to set the
 * value of a variable with index i:
 * 
 * <pre>
 * void setVariableValue(int i, SymbolicExpression expr) {
 * 	values[i].decrementRefCount();
 * 	expr.incrementRefCount();
 * 	values[i] = expr;
 * }
 * </pre>
 * 
 * This might be used in code like this:
 * 
 * <pre>
 * SymbolicExpression x = dyscope.getVariableValue(i);
 * SymbolicExpression y = ...;
 * dyscope.setVariableValue(i, universe.addMut(x,y));
 * </pre>
 * 
 * If a new value is created by the <code>addMut</code> (i.e., if x was shared),
 * the result will be that the reference count of the old value of x is
 * decremented, the reference count of the new expression is 1. The reference
 * count of y is unchanged.
 * 
 * If a new value is not created by addMut (i.e., it mutates x), then the
 * reference count is unchanged.
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
	 * Increments the reference count. This is used to declare that some other
	 * object has added a reference from it to this one.
	 */
	void incrementReferenceCount();

	/**
	 * Decrements the reference count. This is used to declare that some other
	 * object has removed a reference from it to this one.
	 * 
	 * <p>
	 * Preconditions: the reference count must be at least 1.
	 * </p>
	 */
	void decrementReferenceCount();

	/**
	 * Returns the current reference count of this object
	 * 
	 * @return the reference count
	 */
	int getReferenceCount();

	void addReferenceFrom(SymbolicObject object);

	void removeReferenceFrom(SymbolicObject object);

	/**
	 * Tells whether this symbolic object is immutable. The object is immutable
	 * iff it is canonic or one of its ancestors has reference count greater
	 * than 1.
	 * 
	 * @return <code>true</code> iff this object is immutable
	 * 
	 * @see #commit()
	 */
	boolean isImmutable();

}
