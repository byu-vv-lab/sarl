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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;

/**
 * an implementation of {@link SymbolicUnionType}
 * 
 */
public class CommonSymbolicUnionType extends CommonSymbolicType implements
		SymbolicUnionType {

	private final static int classCode = CommonSymbolicUnionType.class
			.hashCode();
	/**
	 * sequence used to create the unionType
	 */
	private SymbolicTypeSequence sequence;

	/**
	 * holds the name of this unionType
	 */
	private StringObject name;

	private Map<SymbolicType, Integer> indexMap = new LinkedHashMap<SymbolicType, Integer>();

	/**
	 * The elements of the sequence must be unique, i.e., no repetitions.
	 * 
	 * @param name
	 * @param sequence
	 */
	CommonSymbolicUnionType(StringObject name, SymbolicTypeSequence sequence) {
		super(SymbolicTypeKind.UNION);
		assert sequence != null;

		int n = sequence.numTypes();

		for (int i = 0; i < n; i++) {
			SymbolicType type1 = sequence.getType(i);
			Integer index = indexMap.get(type1);

			if (index != null)
				throw new IllegalArgumentException("Component of union type "
						+ name + " occurred twice, at positions " + index
						+ " and " + i + ": " + type1);
			indexMap.put(type1, i);
			type1.addReferenceFrom(this);
		}
		assert name != null;
		this.name = name;
		this.sequence = sequence;
		sequence.addReferenceFrom(this);
	}

	@Override
	protected boolean typeEquals(CommonSymbolicType thatType) {
		CommonSymbolicUnionType that = (CommonSymbolicUnionType) thatType;

		return name.equals(that.name) && sequence.equals(that.sequence);
	}

	@Override
	protected int computeHashCode() {
		return classCode ^ name.hashCode() ^ sequence.hashCode();
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		StringBuffer result = new StringBuffer("Union[");

		result.append(name.toStringBuffer(false));
		result.append(",");
		result.append(sequence.toStringBuffer(false));
		result.append("]");
		return result;
	}

	@Override
	public StringObject name() {
		return name;
	}

	@Override
	public SymbolicTypeSequence sequence() {
		return sequence;
	}

	@Override
	public void canonizeChildren(ObjectFactory factory) {
		super.canonizeChildren(factory);
		if (!sequence.isCanonic())
			sequence = (CommonSymbolicTypeSequence) factory.canonic(sequence);
		if (!name.isCanonic())
			name = (StringObject) factory.canonic(name);

		Map<SymbolicType, Integer> newMap = new LinkedHashMap<>(indexMap.size());

		for (Entry<SymbolicType, Integer> entry : indexMap.entrySet()) {
			newMap.put(factory.canonic(entry.getKey()), entry.getValue());
		}
		indexMap = newMap;
	}

	@Override
	public Integer indexOfType(SymbolicType type) {
		return indexMap.get(type);
	}

	@Override
	protected List<SymbolicObject> getChildren() {
		List<SymbolicObject> result = super.getChildren();

		result.add(sequence);
		result.add(name);
		for (SymbolicType key : indexMap.keySet())
			result.add(key);
		return result;
	}

}
