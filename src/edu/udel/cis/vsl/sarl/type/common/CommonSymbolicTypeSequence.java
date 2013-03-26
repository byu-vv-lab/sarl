/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.type.common;

import java.util.ArrayList;
import java.util.Iterator;

import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.object.common.CommonSymbolicObject;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CommonSymbolicTypeSequence extends CommonSymbolicObject implements
		SymbolicTypeSequence {

	private final static int classCode = CommonSymbolicTypeSequence.class
			.hashCode();

	private ArrayList<SymbolicType> elements;

	public CommonSymbolicTypeSequence(Iterable<? extends SymbolicType> types) {
		super(SymbolicObjectKind.TYPE_SEQUENCE);
		elements = new ArrayList<SymbolicType>();
		for (SymbolicType type : types) {
			elements.add(type);
		}
	}

	public CommonSymbolicTypeSequence(SymbolicType[] types) {
		super(SymbolicObjectKind.TYPE_SEQUENCE);
		elements = new ArrayList<SymbolicType>(types.length);
		for (SymbolicType type : types) {
			elements.add(type);
		}
	}

	@Override
	public Iterator<SymbolicType> iterator() {
		return elements.iterator();
	}

	@Override
	public int numTypes() {
		return elements.size();
	}

	@Override
	public SymbolicType getType(int index) {
		return elements.get(index);
	}

	@Override
	protected boolean intrinsicEquals(SymbolicObject object) {
		if (object instanceof CommonSymbolicTypeSequence) {
			return elements
					.equals(((CommonSymbolicTypeSequence) object).elements);
		}
		return false;
	}

	@Override
	public String toString() {
		// String result = "<";
		// int n = numTypes();
		//
		// for (int i = 0; i < n; i++) {
		// if (i > 0)
		// result += ",";
		// result += getType(i);
		// }
		// result += ">";
		// return result;
		return toStringBuffer(false).toString();
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		StringBuffer result = new StringBuffer("<");
		int n = numTypes();

		for (int i = 0; i < n; i++) {
			if (i > 0)
				result.append(",");
			result.append(getType(i).toStringBuffer(false));
		}
		result.append(">");
		return result;
	}

	@Override
	protected int computeHashCode() {
		return classCode ^ elements.hashCode();
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		int numElements = elements.size();

		for (int i = 0; i < numElements; i++) {
			SymbolicType type = elements.get(i);

			if (!type.isCanonic())
				elements.set(i, factory.canonic(type));
		}
	}

	@Override
	public StringBuffer toStringBufferLong() {
		return toStringBuffer(false);
	}

}
