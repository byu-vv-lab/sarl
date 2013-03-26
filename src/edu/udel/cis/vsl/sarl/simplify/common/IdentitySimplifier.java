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
package edu.udel.cis.vsl.sarl.simplify.common;

import edu.udel.cis.vsl.sarl.IF.Simplifier;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.Interval;

public class IdentitySimplifier implements Simplifier {

	private SymbolicUniverse universe;

	private BooleanExpression assumption;

	public IdentitySimplifier(SymbolicUniverse universe,
			BooleanExpression assumption) {
		this.universe = universe;
		this.assumption = assumption;
	}

	@Override
	public SymbolicUniverse universe() {
		return universe;
	}

	@Override
	public BooleanExpression newAssumption() {
		return assumption;
	}

	@Override
	public SymbolicExpression apply(SymbolicExpression expression) {
		return expression;
	}

	@Override
	public Interval assumptionAsInterval(SymbolicConstant symbolicConstant) {
		return null;
	}

}
