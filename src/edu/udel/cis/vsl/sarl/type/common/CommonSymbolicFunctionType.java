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

import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CommonSymbolicFunctionType extends CommonSymbolicType implements
		SymbolicFunctionType {

	private final static int classCode = CommonSymbolicFunctionType.class
			.hashCode();

	private SymbolicTypeSequence inputTypes;

	private SymbolicType outputType;

	CommonSymbolicFunctionType(SymbolicTypeSequence inputTypes,
			SymbolicType outputType) {
		super(SymbolicTypeKind.FUNCTION);
		assert inputTypes != null;
		assert outputType != null;
		this.inputTypes = inputTypes;
		this.outputType = outputType;
	}

	@Override
	protected boolean typeEquals(CommonSymbolicType thatType) {
		CommonSymbolicFunctionType that = (CommonSymbolicFunctionType) thatType;

		return that.outputType.equals(outputType)
				&& that.inputTypes.equals(inputTypes);
	}

	@Override
	protected int computeHashCode() {
		return classCode ^ inputTypes.hashCode() ^ outputType.hashCode();
	}

	@Override
	public SymbolicType outputType() {
		return outputType;
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		StringBuffer result = inputTypes.toStringBuffer(true);

		result.append("->");
		result.append(outputType.toStringBuffer(true));
		if (atomize)
			atomize(result);
		return result;
	}

	@Override
	public SymbolicTypeSequence inputTypes() {
		return inputTypes;
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		if (!inputTypes.isCanonic())
			inputTypes = (SymbolicTypeSequence) factory.canonic(inputTypes);
		if (!outputType.isCanonic())
			outputType = factory.canonic(outputType);
	}

}
