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
 * A partial implementation of {@link SymbolicObject}.
 * 
 * State: MUTABLE, COMMITTED, HASHED, CANONIC, id
 * 
 * id: -999 = mutable, -998=committed, -997=hashed, >=0=canonic
 * 
 * @author siegel
 * 
 */
public abstract class CommonSymbolicObject implements SymbolicObject {

	private final static int MUTABLE = -999;

	private final static int COMMITTED = MUTABLE + 1;

	private final static int HASHED = COMMITTED + 1;

	/**
	 * If true, more detailed string representations of symbolic objects will be
	 * returned by the {@link #toString()} method.
	 */
	private final static boolean debug = false;

	/**
	 * What kind of the symbolic object is this? Set upon construction.
	 */
	private SymbolicObjectKind kind;

	/**
	 * Cached hashCode, set upon first run of {@link #hashCode()}.
	 */
	private int hashCode;

	private int state = MUTABLE;

	// /**
	// * An infinite-precision rational number associated to this object to
	// * facilitate comparisons. CURRENTLY NOT USED.
	// */
	// private RationalNumber order;

	/**
	 * Instantiates object and sets {@link #kind} as specified.
	 * 
	 * @param kind
	 *            the kind of symbolic object this is
	 */
	protected CommonSymbolicObject(SymbolicObjectKind kind) {
		this.kind = kind;
	}

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

	public boolean isCanonic() {
		return state >= 0;
	}

	public boolean isCommitted() {
		return state >= COMMITTED;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 */
	void setId(int id) {
		this.state = id;
	}

	public int id() {
		return state;
	}

	@Override
	public SymbolicObjectKind symbolicObjectKind() {
		return kind;
	}

	/**
	 * Computes the hash code to be returned by hashCode(). This is run the
	 * first time hashCode is run. The hash is cached for future calls to
	 * hashCode();
	 * 
	 * @return hash code
	 */
	protected abstract int computeHashCode();

	@Override
	public int hashCode() {
		if (state < HASHED) {
			hashCode = computeHashCode();
			if (state == COMMITTED)
				state = HASHED;
		}
		return hashCode;
	}

	protected abstract void commitChildren();

	@Override
	public SymbolicObject commit() {
		if (state == MUTABLE) {
			state = COMMITTED;
			commitChildren();
		}
		return this;
	}

	/**
	 * Is the given symbolic object equal to this one---assuming the given
	 * symbolic object is of the same kind as this one? Must be defined in any
	 * concrete subclass.
	 * 
	 * @param that
	 *            a symbolic object of the same kind as this one
	 * @return true iff they define the same type
	 */
	protected abstract boolean intrinsicEquals(SymbolicObject o);

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o instanceof CommonSymbolicObject) {
			CommonSymbolicObject that = (CommonSymbolicObject) o;

			if (kind != that.kind)
				return false;
			if (state >= 0 && that.state >= 0) {
				return state == that.state;
			}
			if (state >= HASHED && that.state >= HASHED
					&& hashCode != that.hashCode)
				return false;
			return intrinsicEquals(that);
		}
		return false;
	}

	/**
	 * Canonizes the children of this symbolic object. Replaces each child with
	 * the canonic version of that child.
	 * 
	 * @param factory
	 *            the object factory that is responsible for this symbolic
	 *            object
	 */
	public abstract void canonizeChildren(CommonObjectFactory factory);

	/**
	 * Places parentheses around the string buffer.
	 * 
	 * @param buffer
	 *            a string buffer
	 */
	protected void atomize(StringBuffer buffer) {
		buffer.insert(0, '(');
		buffer.append(')');
	}

	@Override
	public String toString() {
		if (debug)
			return toStringBufferLong().toString();
		else
			return toStringBuffer(false).toString();
	}

}
