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
package edu.udel.cis.vsl.sarl.ideal.common;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Monic;
import edu.udel.cis.vsl.sarl.ideal.IF.Monomial;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;
import edu.udel.cis.vsl.sarl.ideal.IF.PrimitivePower;

/**
 * A non-trivial monic is the product of at least two primitive powers. The set
 * of primitive powers comprising this product is represented as a map.
 * 
 * A key in the map is primitive. The value associated to that key is a
 * PrimitivePower.
 * 
 * @author siegel
 * 
 */
public class NTMonic extends IdealExpression implements Monic {

	// private SymbolicMap<Monic, Monomial> polynomialMap = null;

	private int degree = -1;

	protected NTMonic(SymbolicType type,
			SymbolicMap<NumericPrimitive, PrimitivePower> factorMap) {
		super(SymbolicOperator.MULTIPLY, type, factorMap);
		assert factorMap.size() >= 2;
	}

	@Override
	public Constant monomialConstant(IdealFactory factory) {
		return factory.one(type());
	}

	@Override
	public Monic monic(IdealFactory factory) {
		return this;
	}

	@Override
	public SymbolicMap<Monic, Monomial> termMap(IdealFactory factory) {
		// if (polynomialMap == null)
		// polynomialMap = factory.singletonMap((Monic) this, (Monomial) this);
		return factory.singletonMap((Monic) this, (Monomial) this);
	}

	@Override
	public SymbolicMap<NumericPrimitive, PrimitivePower> monicFactors(
			IdealFactory factory) {
		return monicFactors();
	}

	@SuppressWarnings("unchecked")
	public SymbolicMap<NumericPrimitive, PrimitivePower> monicFactors() {
		return (SymbolicMap<NumericPrimitive, PrimitivePower>) argument(0);
	}

	@Override
	public Monomial factorization(IdealFactory factory) {
		return this;
	}

	@Override
	public Polynomial numerator(IdealFactory factory) {
		return this;
	}

	@Override
	public Polynomial denominator(IdealFactory factory) {
		return factory.one(type());
	}

	@Override
	public Monomial leadingTerm() {
		return this;
	}

	@Override
	public boolean isTrivialMonic() {
		return false;
	}

	@Override
	public Polynomial expand(IdealFactory factory) {
		Polynomial result = factory.one(type());

		for (PrimitivePower ppower : monicFactors())
			result = factory.multiply(result, ppower.expand(factory));
		return result;
	}

	public StringBuffer toStringBuffer() {
		StringBuffer buffer = new StringBuffer();

		for (SymbolicExpression expr : monicFactors())
			buffer.append(expr.atomString());
		return buffer;
	}

	// @Override
	// public String toString() {
	// return toStringBuffer().toString();
	// }

	@Override
	public IdealKind idealKind() {
		return IdealKind.NTMonic;
	}

	@Override
	public int degree() {
		if (degree < 0) {
			degree = 0;
			for (PrimitivePower expr : monicFactors())
				degree += expr.degree();
		}
		return degree;
	}

	@Override
	public Constant constantTerm(IdealFactory factory) {
		return factory.zero(type());
	}

}
