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

import edu.udel.cis.vsl.sarl.IF.type.SymbolicSetType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CommonSymbolicSetType extends CommonSymbolicType implements
		SymbolicSetType {

	private final static int classCode = CommonSymbolicSetType.class.hashCode();

	private SymbolicType elementType;

	/**
	 * Cache of the "pure" version of this type: the version that is recursively
	 * incomplete.
	 */
	private SymbolicSetType pureType = null;

	/**
	 * Creates new symbolic set type with given elementType. *
	 * 
	 * @param elementType
	 *            any non-null type
	 */
	CommonSymbolicSetType(SymbolicType elementType) {
		super(SymbolicTypeKind.SET);
		assert elementType != null;
		this.elementType = elementType;
	}

	/**
	 * Both this and that have kind SET.
	 */
	@Override
	protected boolean typeEquals(CommonSymbolicType that) {
		return elementType.equals(((CommonSymbolicSetType) that).elementType);
	}

	@Override
	protected int computeHashCode() {
		return classCode ^ elementType.hashCode();
	}

	@Override
	public SymbolicType elementType() {
		return elementType;
	}

	/**
	 * Nice human-readable representation of the set type. Example: <code>
	 * Set&lt;int&gt;* </code>
	 * 
	 */
	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		StringBuffer result = new StringBuffer();

		result.append("Set<");
		result.append(elementType.toStringBuffer(false));
		result.append(">");
		return result;
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		if (!elementType.isCanonic())
			elementType = factory.canonic(elementType);
		if (pureType != null && !pureType.isCanonic())
			pureType = factory.canonic(pureType);
	}

	public SymbolicSetType getPureType() {
		return pureType;
	}

	/**
	 * setting a new pureType to this ArrayType
	 * 
	 * @param pureType
	 */
	public void setPureType(SymbolicSetType pureType) {
		this.pureType = pureType;
	}

}
