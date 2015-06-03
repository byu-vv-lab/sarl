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
	 * Indicates an object is <strong>free</strong>: mutable and the child of
	 * any object.
	 */
	private final static int FREE = -1000;

	/**
	 * Indicates an object is <strong>owned</strong>: mutable but is the child
	 * of exactly one other object (its parent).
	 */
	private final static int OWNED = FREE + 1;

	/**
	 * Indicates an object is <strong>committed</strong> but not yet hashed. The
	 * object is immutable but its hash code has not been computed and cached,
	 * and it is not canonic.
	 */
	private final static int COMMITTED = OWNED + 1;

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
	private int status = FREE;

	// /**
	// * An infinite-precision rational number associated to this object to
	// * facilitate comparisons. CURRENTLY NOT USED.
	// */
	// private RationalNumber order;

	// Constructors:

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
	 * Commits the children of this object.
	 */
	protected abstract void commitChildren();

	/**
	 * Computes the hash code to be returned by method {@link #hashCode()}. If
	 * this object is immutable, the result returned will be cached in
	 * {@link #hashCode}.
	 * 
	 * @return the hash code
	 */
	protected abstract int computeHashCode();

	/**
	 * Is the given symbolic object equal to this one---assuming the given
	 * symbolic object is of the same kind as this one? Must be defined in any
	 * concrete subclass.
	 * 
	 * Preconditions: this != o, o.symbolicObjectKind() ==
	 * this.symbolicObjectKind
	 * 
	 * @param that
	 *            a symbolic object of the same kind as this one
	 * @return true iff they define the same type
	 */
	protected abstract boolean intrinsicEquals(SymbolicObject o);

	/**
	 * Canonizes the children of this symbolic object. Replaces each child with
	 * the canonic version of that child.
	 * 
	 * @param factory
	 *            the object factory that is responsible for this symbolic
	 *            object
	 */
	public abstract void canonizeChildren(CommonObjectFactory factory);

	// Package-private methods:

	/**
	 * Sets the canonic id of this object.
	 * 
	 * @param id
	 *            the number to be the canonic ID; must be nonnegative
	 */
	void setId(int id) {
		this.status = id;
	}

	// Protected methods:

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

	// Public methods specified in Object:

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

	// Public methods specified in SymbolicObject:

	@Override
	public boolean isFree() {
		return status <= FREE;
	}

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

	@Override
	public SymbolicObject commit() {
		if (status < COMMITTED) {
			status = COMMITTED;
			commitChildren();
		}
		return this;
	}

	@Override
	public void makeChild() {
		if (status == FREE) {
			status = OWNED;
		} else if (status == OWNED) {
			commit();
		}
	}

	@Override
	public void release() {
		if (status == OWNED) {
			status = FREE;
		}
	}

	// Future work...

	// /**
	// * Sets the {@link #order} field to the specified number.
	// *
	// * @param number
	// * an infinite precision rational number
	// */
	// public void setOrder(RationalNumber number) {
	// order = number;
	// }

	// /**
	// * @return the rational number {@link #order}.
	// */
	// public RationalNumber getOrder() {
	// return order;
	// }

}
