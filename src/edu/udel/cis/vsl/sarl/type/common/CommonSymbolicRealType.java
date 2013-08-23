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

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CommonSymbolicRealType extends CommonSymbolicType implements
		SymbolicRealType {

	private final static int classCode = CommonSymbolicRealType.class
			.hashCode();

	private RealKind realKind;

	private StringBuffer name;

	public CommonSymbolicRealType(RealKind kind) {
		super(SymbolicTypeKind.REAL);
		this.realKind = kind;
	}

	@Override
	public RealKind realKind() {
		return realKind;
	}

	@Override
	protected boolean typeEquals(CommonSymbolicType that) {
		return realKind == ((CommonSymbolicRealType) that).realKind;
	}

	@Override
	protected int computeHashCode() {
		return classCode ^ realKind.hashCode();
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		if (name == null) {
			String shortName;

			switch (realKind) {
			case IDEAL:
				shortName = "real";
				break;
			case HERBRAND:
				shortName = "hreal";
				break;
			case FLOAT:
				shortName = "float";
				break;
			default:
				throw new SARLInternalException("unreachable");
			}
			name = new StringBuffer(shortName);
		}
		return name;
	}

	@Override
	public boolean isHerbrand() {
		return realKind == RealKind.HERBRAND;
	}

	@Override
	public boolean isIdeal() {
		return realKind == RealKind.IDEAL;
	}

	@Override
	public SymbolicType getPureType() {
		return this;
	}
}
