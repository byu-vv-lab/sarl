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

import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

/**
 * An implementation of {@link SymbolicObject}.
 * 
 * @author siegel
 * 
 */
public abstract class CommonSymbolicObject implements SymbolicObject {

	/**
	 * If true, toString() will return toStringBufferLong, not toStringBuffer
	 */
	private final static boolean debug = false;

	/**
	 * Set upon construction, used in equals for kind comparison
	 */
	private SymbolicObjectKind kind;

	/**
	 * Cached hashCode, set upon first run of hashCode()
	 */
	private int hashCode;

	/**
	 * Tells whether hashCode is set or not
	 */
	private boolean hashed = false;

	/**
	 * Unique id of the canonic symbolic object
	 */
	private int id = -1;

	/**
	 * The order of the object, used for comparisons
	 */
	private RationalNumber order;

	/**
	 * Instantiates object and sets this.kind
	 * @param kind
	 */
	protected CommonSymbolicObject(SymbolicObjectKind kind) {
		this.kind = kind;
	}

	/**
	 * set the order of the CommonSymbolicObject
	 * @param number
	 */
	public void setOrder(RationalNumber number) {
		order = number;
	}

	/**
	 * @return
	 * 		The order of the CommonSymbolicObject
	 */
	public RationalNumber getOrder() {
		return order;
	}

	public boolean isCanonic() {
		return id >= 0;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 */
	void setId(int id) {
		this.id = id;
	}
	
	public int id() {
		return id;
	}

	@Override
	public SymbolicObjectKind symbolicObjectKind() {
		return kind;
	}

	/**
	 * Computes the hash code to be returned by hashCode(). This is run the first time hashCode is run.
	 * The hash is cached for future calls to hashCode();
	 * @return hash code
	 */
	protected abstract int computeHashCode();

	@Override
	public int hashCode() {
		if (!hashed) {
			hashCode = computeHashCode();
			hashed = true;
		}
		return hashCode;
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
			if (id >= 0 && that.id >= 0) {
				return id == that.id;
			}
			if (hashed && that.hashed && hashCode != that.hashCode)
				return false;
			return intrinsicEquals(that);
		}
		return false;
	}

	/**
	 * Canonize the children of the ObjectFactory. Pull out the unique representatives of the objects.
	 * @param factory
	 */
	public abstract void canonizeChildren(CommonObjectFactory factory);

	/**
	 * Place parentheses around the string buffer.
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
