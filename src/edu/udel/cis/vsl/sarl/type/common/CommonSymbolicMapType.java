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

import java.util.List;

import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicMapType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;

public class CommonSymbolicMapType extends CommonSymbolicType implements
		SymbolicMapType {

	private final static int classCode = CommonSymbolicMapType.class.hashCode();

	private SymbolicType keyType;

	private SymbolicType valueType;

	private SymbolicTupleType entryType;

	/**
	 * Creates new symbolic set type with given elementType. *
	 * 
	 * @param elementType
	 *            any non-null type
	 */
	CommonSymbolicMapType(SymbolicType keyType, SymbolicType valueType) {
		super(SymbolicTypeKind.MAP);
		assert keyType != null;
		assert valueType != null;
		this.keyType = keyType;
		this.valueType = valueType;
	}

	/**
	 * Both this and that have kind SET.
	 */
	@Override
	protected boolean typeEquals(CommonSymbolicType that) {
		CommonSymbolicMapType mapType = (CommonSymbolicMapType) that;

		return keyType.equals(mapType.keyType)
				&& valueType.equals(mapType.valueType);
	}

	@Override
	protected int computeHashCode() {
		return classCode ^ keyType.hashCode() ^ valueType.hashCode();
	}

	@Override
	public SymbolicType keyType() {
		return keyType;
	}

	@Override
	public SymbolicType valueType() {
		return valueType;
	}

	/**
	 * Nice human-readable representation of the set type. Example: <code>
	 * Map&lt;int,double&gt;* </code>
	 * 
	 */
	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		StringBuffer result = new StringBuffer();

		result.append("Map<");
		result.append(keyType.toStringBuffer(false));
		result.append(',');
		result.append(valueType.toStringBuffer(false));
		result.append(">");
		return result;
	}

	@Override
	public void canonizeChildren(ObjectFactory factory) {
		super.canonizeChildren(factory);
		if (!keyType.isCanonic())
			keyType = factory.canonic(keyType);
		if (!valueType.isCanonic())
			valueType = factory.canonic(valueType);
		if (entryType != null && !entryType.isCanonic())
			entryType = factory.canonic(entryType);
	}

	public void setEntryType(SymbolicTupleType entryType) {
		assert isImmutable();
		this.entryType = entryType;
	}

	public SymbolicTupleType getEntryType() {
		assert isImmutable();
		return entryType;
	}

	@Override
	protected List<SymbolicObject> getChildren() {
		List<SymbolicObject> result = super.getChildren();

		result.add(keyType);
		result.add(valueType);
		if (entryType != null)
			result.add(entryType);
		return result;
	}

	@Override
	protected void nullifyFields() {
		super.nullifyFields();
		keyType = null;
		valueType = null;
		entryType = null;
	}

}
