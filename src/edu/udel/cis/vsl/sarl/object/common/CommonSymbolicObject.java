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
package edu.udel.cis.vsl.sarl.object.common;

import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.util.EmptyIterable;

/**
 * A partial implementation of {@link SymbolicObject}. Does not maintain the
 * children; it is up to each concrete class to figure out how to do that for
 * itself.
 * 
 * @author Stephen F. Siegel
 */
public abstract class CommonSymbolicObject implements SymbolicObject {

	// Constants (static fields):

	/**
	 * Indicates an object is <strong>MUTABLE</strong>. In particular it and all
	 * of its ancestors each has at most one parent.
	 */
	private final static int MUTABLE = -1000;

	/**
	 * Indicates an object is <strong>committed</strong> but not yet hashed. The
	 * object is currently immutable but its hash code has not been computed and
	 * cached, and it is not canonic.
	 */
	private final static int COMMITTED = MUTABLE + 1;

	/**
	 * Indicates that an object is <strong>committed</strong> and hashed. The
	 * cached hash code is stored in instance field {@link #hashCode}.
	 */
	private final static int HASHED = COMMITTED + 1;

	/**
	 * If true, more detailed string representations of symbolic objects will be
	 * returned by the {@link #toString()} method.
	 */
	private final static boolean debug = false;

	protected final static Iterable<SymbolicObject> emptyIterable = new EmptyIterable<>();

	// Instance fields:

	/**
	 * What kind of the symbolic object is this? Set upon construction.
	 */
	private SymbolicObjectKind kind;

	/**
	 * Cached hashCode, set upon first run of {@link #hashCode()}.
	 */
	private int hashCode;

	/**
	 * If this object is canonic, {@link #status} will be a nonnegative integer
	 * which is the canonic ID number of this object. Otherwise, it will be one
	 * of {@link #FREE}, {@link #OWNED}, {@link #COMMITTED}, {@link #HASHED}:
	 * all of which are negative.
	 */
	private int status = MUTABLE;

	/**
	 * The total number of references from mutable objects to this object. An
	 * external reference (i.e., a reference from any object which is not a
	 * {@link SymbolicObject}), is considered mutable and is included in this
	 * total. If one object contains 2 references to this, that contributes 2 to
	 * this total.
	 * 
	 * This will be set to 0 and not used if this object is made canonic.
	 */
	private int mutableReferenceCount = 0;

	/**
	 * The number of references from committed symbolic objects which contain a
	 * reference to this symbolic object. Set to -1 and not used if this object
	 * is made canonic.
	 */
	private int committedReferenceCount = 0;

	/**
	 * Instantiates object and sets {@link #kind} as specified.
	 * 
	 * @param kind
	 *            the kind of symbolic object this is
	 */
	protected CommonSymbolicObject(SymbolicObjectKind kind) {
		this.kind = kind;
	}

	// Abstract methods:

	/**
	 * Returns the "children" of this symbolic object. These are other symbolic
	 * objects, such as the arguments in a symbolic expression, the members of a
	 * symbolic collection etc. A child can never be <code>null</code>.
	 * 
	 * @return the children of this object
	 */
	protected abstract Iterable<? extends SymbolicObject> getChildren();

	/**
	 * Computes the hash code to be returned by method {@link #hashCode()}. If
	 * this object is immutable, the result returned will be cached in
	 * {@link #hashCode}.
	 * 
	 * @return the hash code of this
	 */
	protected abstract int computeHashCode();

	/**
	 * <p>
	 * Is the given symbolic object equal to this one---assuming the given
	 * symbolic object is of the same kind as this one? Must be defined in any
	 * concrete subclass.
	 * </p>
	 * 
	 * <p>
	 * Preconditions: this != o, o.symbolicObjectKind() ==
	 * this.symbolicObjectKind
	 * </p>
	 * 
	 * @param that
	 *            a symbolic object of the same kind as this one
	 * @return true iff they define the same type
	 */
	protected abstract boolean intrinsicEquals(SymbolicObject o);

	/**
	 * Sets fields to <code>null</code> in preparation for destruction. It is
	 * especially important to do this for the children fields. Might help the
	 * garbage collector.
	 */
	protected abstract void nullifyFields();

	/**
	 * Replaces all children with their canonic representatives.
	 * 
	 * @param factory
	 *            the object factory responsible for this symbolic object
	 */
	protected abstract void canonizeChildren(ObjectFactory factory);

	// Private methods...

	/**
	 * <p>
	 * The method is invoked on a node u when a parent of u changes its status
	 * from COMMITTED to MUTABLE. It updates u's
	 * {@link #committedReferenceCount} and {@link #mutableReferenceCount}
	 * fields. It then determines if u's status should be changed to MUTABLE. If
	 * so, it changes the status and then invokes
	 * {@link #changeCommittedParentToMutable()} on each child of u.
	 * </p>
	 * 
	 * <p>
	 * Preconditions: this object is committed, but not canonic. The
	 * {@link #committedReferenceCount} is at least 1.
	 * </p>
	 */
	private void changeCommittedParentToMutable() {
		assert status >= COMMITTED && status < 0; // committed and not canonic
		assert committedReferenceCount >= 1;
		committedReferenceCount--;
		mutableReferenceCount++;
		if (committedReferenceCount == 0 && mutableReferenceCount == 1) {
			uncommit();
		}
	}

	/**
	 * <p>
	 * This method is invoked on a node u when a parent of u changes its status
	 * from MUTABLE to COMMITTED. It updates u's
	 * {@link #committedReferenceCount} and {@link #mutableReferenceCount}
	 * fields. If u is currently MUTABLE, u is committed.
	 * </p>
	 * 
	 * <p>
	 * Preconditions: The {@link #mutableReferenceCount} is at least 1. This is
	 * not canonic.
	 * </p>
	 */
	private void changeMutableParentToCommitted() {
		assert mutableReferenceCount >= 1;
		assert status < 0; // not canonic
		committedReferenceCount++;
		mutableReferenceCount--;
		if (status <= MUTABLE) {
			commit();
		}
	}

	/**
	 * Increments the committed reference count. If this object is not already
	 * committed, commits it.
	 * 
	 * <p>
	 * Preconditions: This object is not canonic.
	 * </p>
	 */
	private void incrementCommittedReferenceCount() {
		assert status < 0; // not canonic
		committedReferenceCount++;
		if (status < COMMITTED)
			commit();
	}

	/**
	 * <p>
	 * Decrements {@link #committedReferenceCount}. Used to declare that a
	 * committed symbolic object is removing a reference from it to this object.
	 * Note that since the parent is assumed to be committed, this object must
	 * also be committed (in the pre-state).
	 * </p>
	 * 
	 * <p>
	 * If, after decrementing, {@link #committedReferenceCount} is 0 and
	 * {@link #mutableReferenceCount} is 1, then the status of this object will
	 * be changed to MUTABLE, and the descendants will be updated as needed. If
	 * both counts become 0, this object is destroyed.
	 * </p>
	 * 
	 * <p>
	 * Preconditions: {@link #committedReferenceCount} is at least 1. In
	 * particular, this object is committed in the pre-state. This object is not
	 * canonic.
	 * </p>
	 */
	private void decrementCommittedReferenceCount() {
		assert status < 0; // not canonic
		assert committedReferenceCount >= 1;
		assert status >= COMMITTED;
		committedReferenceCount--;
		if (committedReferenceCount == 0) {
			if (mutableReferenceCount == 1) {
				uncommit();
			} else if (mutableReferenceCount == 0) {
				destroy();
			}
		}
	}

	/**
	 * <p>
	 * Destroys this node. Iterates over all children and invokes
	 * {@link #decrementReferenceCount()}, then invokes {@link #nullifyFields()}
	 * </p>
	 * 
	 * <p>
	 * Pre-conditions: status is MUTABLE, {@link #mutableReferenceCount} and
	 * {@link #committedReferenceCount} are both 0.
	 * </p>
	 */
	private void destroy() {
		assert status <= MUTABLE;
		assert mutableReferenceCount == 0;
		assert committedReferenceCount == 0;
		for (SymbolicObject child : getChildren()) {
			child.decrementReferenceCount();
		}
		nullifyFields();
	}

	// Package-private methods...

	/**
	 * Sets the canonic id of this object.
	 * 
	 * @param id
	 *            the number to be the canonic ID; must be nonnegative
	 */
	void setId(int id) {
		this.status = id;
	}

	// Protected methods...
	//
	// /**
	// * Prepares this object for the transition from immutable to mutable. This
	// * may require nullifying references to extrinsic data. For example: the
	// * negation of a boolean expression. Concrete classes may override this to
	// * handle their own data. Note that when this method is called, the object
	// * will still be committed. Therefore if nullifying references to objects,
	// * use the method {@link #decrementCommittedReferenceCount()}.
	// */
	// protected void makeMutable() {
	// }

	/**
	 * Places parentheses around the given string buffer.
	 * 
	 * @param buffer
	 *            a string buffer
	 */
	protected void atomize(StringBuffer buffer) {
		buffer.insert(0, '(');
		buffer.append(')');
	}

	/**
	 * <p>
	 * Changes status from MUTABLE to COMMITTED. Carries out modifications to
	 * descendants as needed.
	 * </p>
	 * 
	 * <p>
	 * This method is protected because subclasses may wish to override it if
	 * their fields need to be modified to reflect the status change.
	 * </p>
	 * 
	 * <p>
	 * Preconditions: this node is MUTABLE
	 * </p>
	 */
	protected void commit() {
		assert status == MUTABLE;
		for (SymbolicObject child : getChildren()) {
			CommonSymbolicObject theChild = (CommonSymbolicObject) child;

			if (theChild.status < 0) { // leave canonic children alone
				theChild.changeMutableParentToCommitted();
			}
		}
	}

	/**
	 * Makes this committed node MUTABLE. Carries out modifications to
	 * descendants as needed.
	 * 
	 * <p>
	 * This method is protected because subclasses may wish to override it if
	 * their fields need to be modified to reflect the status change.
	 * </p>
	 * 
	 * <p>
	 * Precondition: this node is committed.
	 * </p>
	 */
	protected void uncommit() {
		assert status < 0 && status >= COMMITTED;
		status = MUTABLE;
		for (SymbolicObject child : getChildren()) {
			CommonSymbolicObject theChild = (CommonSymbolicObject) child;

			if (theChild.status < 0) { // leave canonic children alone
				theChild.changeCommittedParentToMutable();
			}
		}
	}

	// Public methods specified in Object...

	@Override
	public int hashCode() {
		if (status < HASHED) {
			hashCode = computeHashCode();
			if (status == COMMITTED)
				status = HASHED;
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o instanceof CommonSymbolicObject) {
			CommonSymbolicObject that = (CommonSymbolicObject) o;

			if (kind != that.kind)
				return false;
			if (status >= 0 && that.status >= 0) {
				// if both are canonic, they are equal iff they have save id
				// iff they are ==
				return status == that.status;
			}
			if (status >= HASHED && that.status >= HASHED
					&& hashCode != that.hashCode)
				return false;
			return intrinsicEquals(that);
		}
		return false;
	}

	@Override
	public String toString() {
		if (debug)
			return toStringBufferLong().toString();
		else
			return toStringBuffer(false).toString();
	}

	// Public methods specified in SymbolicObject...

	@Override
	public boolean isCanonic() {
		return status >= 0;
	}

	@Override
	public boolean isImmutable() {
		return status >= COMMITTED;
	}

	@Override
	public int id() {
		return status;
	}

	@Override
	public SymbolicObjectKind symbolicObjectKind() {
		return kind;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * This method increments {@link #mutableReferenceCount}. All external
	 * references are counted as mutable. This method should only be called when
	 * the source of the new reference (which is not identified) is a mutable
	 * symbolic object, or is not an instance of {@link SymbolicObject}. When
	 * the source is a committed symbolic object, use
	 * {@link #incrementCommittedReferenceCount()} instead.
	 * </p>
	 * 
	 * <p>
	 * If, in the pre-state, this object is mutable, then it must be the case
	 * that {@link #committedReferenceCount} is 0. If it is mutable and after
	 * incrementing, {@link #mutableReferenceCount} is 2, then {@link #commit()}
	 * is invoked, changing the status of this object and its descendants to
	 * COMMITTED.
	 * </p>
	 * 
	 */
	@Override
	public void incrementReferenceCount() {
		if (status >= 0) // this is a no-op on canonic objects
			return;
		mutableReferenceCount++;
		if (status == MUTABLE && mutableReferenceCount == 2)
			commit();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * This method is used to declare that a reference from a mutable object to
	 * this object is going away. Any object which is not an instance of
	 * {@link SymbolicObject} is considered "mutable" for this purpose.
	 * </p>
	 * 
	 * <p>
	 * If after decrementing, {@link #mutableReferenceCount} is 1 and
	 * {@link #committedReferenceCount} is 0, then the status must be COMMITTED;
	 * this method will change the status to MUTABLE and invoke {@link
	 * decrementCommittedReferenceCount()} on each child. If both reference
	 * counts are 0, this method will destroy this object by invoking
	 * {@link #destroy()}.
	 * </p>
	 */
	@Override
	public void decrementReferenceCount() {
		if (status >= 0)
			return; // no-op on a canonic object
		assert mutableReferenceCount >= 1;
		mutableReferenceCount--;
		if (committedReferenceCount == 0) {
			if (mutableReferenceCount == 1) {
				assert status >= COMMITTED;
				uncommit();
			} else if (mutableReferenceCount == 0) {
				destroy();
			}
		}
	}

	@Override
	public int getReferenceCount() {
		return mutableReferenceCount + committedReferenceCount;
	}

	@Override
	public void addReferenceFrom(SymbolicObject object) {
		if (status < 0) { // do nothing if this is canonic
			if (object.isImmutable())
				incrementCommittedReferenceCount();
			else
				incrementReferenceCount();
		}
	}

	@Override
	public void removeReferenceFrom(SymbolicObject object) {
		if (status < 0) { // do nothing if this is canonic
			if (object.isImmutable())
				decrementCommittedReferenceCount();
			else
				decrementReferenceCount();
		}
	}

}
