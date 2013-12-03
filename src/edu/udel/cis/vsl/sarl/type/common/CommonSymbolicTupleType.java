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

import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

/**
 * an implementation of {@link SymbolicTupleType}
 * 
 * @author mohammedalali
 *
 */
public class CommonSymbolicTupleType extends CommonSymbolicType implements
		SymbolicTupleType {

	private final static int classCode = CommonSymbolicTupleType.class
			.hashCode();
	/**
	 * a SymbolicTypeSequenece to be used to create the TupleType
	 */
	private SymbolicTypeSequence sequence;

	/**
	 * a StringObject to hold the name of this TupleType
	 */
	private StringObject name;

	/**
	 * a SymbolicTupleType to hold the pureType of this TupleType
	 */
	private SymbolicTupleType pureType;

	CommonSymbolicTupleType(StringObject name, SymbolicTypeSequence sequence) {
		super(SymbolicTypeKind.TUPLE);
		assert name != null;
		assert sequence != null;
		this.name = name;
		this.sequence = sequence;
	}

	@Override
	protected boolean typeEquals(CommonSymbolicType thatType) {
		CommonSymbolicTupleType that = (CommonSymbolicTupleType) thatType;

		return name.equals(that.name) && sequence.equals(that.sequence);
	}

	@Override
	protected int computeHashCode() {
		return classCode ^ name.hashCode() ^ sequence.hashCode();
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		StringBuffer result = new StringBuffer(name.toStringBuffer(false));

		result.append(sequence.toStringBuffer(false));
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
	public void canonizeChildren(CommonObjectFactory factory) {
		if (!sequence.isCanonic())
			sequence = (SymbolicTypeSequence) factory.canonic(sequence);
		if (!name.isCanonic())
			name = (StringObject) factory.canonic(name);
	}

	/**
	 * returns the pureType of this TupleType,
	 * that is the same type without any length
	 * 
	 * Has to use setPureType(...) before calling this method
	 */
	public SymbolicTupleType getPureType() {
		return pureType;
	}

	/**
	 * used to set the pureType of this TupleType
	 * @param pureType
	 */
	public void setPureType(SymbolicTupleType pureType) {
		this.pureType = pureType;
	}

}
