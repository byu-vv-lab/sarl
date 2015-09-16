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
package edu.udel.cis.vsl.sarl.type.common;

import java.util.LinkedList;
import java.util.List;

import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.object.common.CommonSymbolicObject;
import edu.udel.cis.vsl.sarl.util.EmptyIterable;

/**
 * Partial implementation of {@link SymbolicType}.
 * 
 */
public abstract class CommonSymbolicType extends CommonSymbolicObject implements
		SymbolicType {

	protected final static Iterable<SymbolicObject> emptyIterable = new EmptyIterable<>();

	/**
	 * The kind of this type
	 */
	private SymbolicTypeKind kind;

	/**
	 * Cache of the "pure" version of this type: the version that is recursively
	 * incomplete. May be null.
	 */
	private SymbolicType pureType = null;

	/**
	 * Constructs new SymbolicType object with given kind and ID number -1.
	 */
	CommonSymbolicType(SymbolicTypeKind kind) {
		super(SymbolicObjectKind.TYPE);
		assert kind != null;
		this.kind = kind;
	}

	/**
	 * Is the given symbolic type equal to this one---assuming the given
	 * symbolic type is of the same kind as this one? Must be defined in any
	 * concrete subclass.
	 * 
	 * @param that
	 *            a symbolic type of the same kind as this one
	 * @return true iff they define the same type
	 */
	protected abstract boolean typeEquals(CommonSymbolicType that);

	@Override
	public boolean intrinsicEquals(SymbolicObject object) {
		if (this == object)
			return true;
		if (object instanceof CommonSymbolicType) {
			CommonSymbolicType that = (CommonSymbolicType) object;

			if (kind != that.kind)
				return false;

			return typeEquals(that);
		}
		return false;
	}

	@Override
	public SymbolicTypeKind typeKind() {
		return kind;
	}

	@Override
	public boolean isInteger() {
		return kind == SymbolicTypeKind.INTEGER;
	}

	@Override
	public boolean isBoolean() {
		return kind == SymbolicTypeKind.BOOLEAN;
	}

	@Override
	public boolean isReal() {
		return kind == SymbolicTypeKind.REAL;
	}

	@Override
	public boolean isNumeric() {
		return kind == SymbolicTypeKind.INTEGER
				|| kind == SymbolicTypeKind.REAL;
	}

	/**
	 * Note: returns false, since that is usually the case. This method must be
	 * overridden by any concrete class that actually is a Herbrand type.
	 */
	@Override
	public boolean isHerbrand() {
		return false;
	}

	/**
	 * Note: returns false, since that is usually the case. This method must be
	 * overridden by any concrete class that actually is an Ideal numeric type.
	 */
	@Override
	public boolean isIdeal() {
		return false;
	}

	/**
	 * For now, toStringBufferLong = toStringBuffer(false), for types.
	 */
	@Override
	public StringBuffer toStringBufferLong() {
		return toStringBuffer(false);
	}

	/**
	 * Returns the cached "pure" version of this type, i.e. the type obtained by
	 * making every array type incomplete recursively, so there are no symbolic
	 * expressions anywhere within the type.
	 * 
	 * @return pure version of this type of null if it has not yet been cached
	 */
	public SymbolicType getPureType() {
		assert isImmutable();
		return pureType;
	}

	/**
	 * setting a new pureType to this ArrayType
	 * 
	 * @param pureType
	 */
	public void setPureType(SymbolicType pureType) {
		assert isImmutable();
		this.pureType = pureType;
	}

	@Override
	public void canonizeChildren(ObjectFactory factory) {
		if (pureType != null && !pureType.isCanonic())
			pureType = factory.canonic(pureType);
	}

	@Override
	protected List<SymbolicObject> getChildren() {
		List<SymbolicObject> result = new LinkedList<>();

		if (pureType != null)
			result.add(pureType);
		return result;
	}

	@Override
	protected void nullifyFields() {
		pureType = null;
	}

	@Override
	public void uncommit() {
		if (pureType != null) {
			pureType.removeReferenceFrom(this);
			pureType = null;
		}
		super.uncommit();
	}

}
