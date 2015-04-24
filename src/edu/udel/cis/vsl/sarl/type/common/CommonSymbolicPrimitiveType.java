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

import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

/**
 * implementation for SymbolicPrimitiveType, which could be a boolean or a char.
 * 
 * @author mohammedalali
 */
public class CommonSymbolicPrimitiveType extends CommonSymbolicType {

	private final static int classCode = CommonSymbolicPrimitiveType.class
			.hashCode();

	private StringBuffer name;

	CommonSymbolicPrimitiveType(SymbolicTypeKind kind) {
		super(kind);
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		if (name == null) {
			if (isBoolean())
				name = new StringBuffer("boolean");
			else
				name = new StringBuffer(typeKind().toString());
		}
		return name;
	}

	@Override
	protected int computeHashCode() {
		return classCode ^ typeKind().hashCode();
	}

	/**
	 * @return true because the type of any CommonSymbolicType is TYPE, which is
	 *         the same as the type of CommonSymbolicPrimitiveType; TYPE.
	 */
	@Override
	protected boolean typeEquals(CommonSymbolicType that) {
		return true;
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
	}

	@Override
	public SymbolicType getPureType() {
		return this;
	}

	@Override
	protected void commitChildren() {
	}

}
