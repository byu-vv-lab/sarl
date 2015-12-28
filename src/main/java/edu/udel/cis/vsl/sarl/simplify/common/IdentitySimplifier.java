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
package edu.udel.cis.vsl.sarl.simplify.common;

import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.simplify.IF.Simplifier;
import edu.udel.cis.vsl.sarl.util.EmptyMap;

/**
 * A trivial implementation of {@link Simplifier} which does nothing: given an
 * expression, it returns the expression unchanged.
 * 
 * @author Stephen F. Siegel
 *
 */
public class IdentitySimplifier implements Simplifier {

	private PreUniverse universe;

	private BooleanExpression assumption;

	public IdentitySimplifier(PreUniverse universe, BooleanExpression assumption) {
		this.universe = universe;
		this.assumption = assumption;
	}

	@Override
	public PreUniverse universe() {
		return universe;
	}

	@Override
	public SymbolicExpression apply(SymbolicExpression expression) {
		return expression;
	}

	@Override
	public Interval assumptionAsInterval(SymbolicConstant symbolicConstant) {
		return null;
	}

	@Override
	public Map<SymbolicConstant, SymbolicExpression> substitutionMap() {
		return new EmptyMap<SymbolicConstant, SymbolicExpression>();
	}

	@Override
	public BooleanExpression getReducedContext() {
		return assumption;
	}

	@Override
	public BooleanExpression getFullContext() {
		return assumption;
	}

}
