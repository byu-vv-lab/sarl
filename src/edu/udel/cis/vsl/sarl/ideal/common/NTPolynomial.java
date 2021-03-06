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
package edu.udel.cis.vsl.sarl.ideal.common;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Monic;
import edu.udel.cis.vsl.sarl.ideal.IF.Monomial;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;

/**
 * A non-trivial polynomial is the sum of at least 2 monomials with different
 * underlying monics, e.g., 1+x^2, x+y, or x+xy.
 * 
 * The set of monomials is represented as a map. A key in this map is a Monic.
 * The value associated to the Monic is a Monomial.
 * 
 * @author siegel
 * 
 */
public class NTPolynomial extends IdealExpression implements Polynomial {

	/**
	 * The factorization is extrinsic data: not used in hashCode or equals.
	 */
	private Monomial factorization;

	/**
	 * The degree of the polynomial, or -1 if the degree has not yet been
	 * computed.
	 */
	private int degree = -1;

	/**
	 * Constructs new NTPolynomial with given term map and factoriation. The
	 * term map must have at least 2 entries. The factorization must be a valid
	 * factorization of the polynomial represented by the termMap; this is not
	 * checked.
	 */
	protected NTPolynomial(SymbolicMap<Monic, Monomial> termMap,
			Monomial factorization) {
		super(SymbolicOperator.ADD, factorization.type(), termMap);
		assert termMap.size() >= 2;
		this.factorization = factorization;
	}

	@Override
	public SymbolicMap<Monic, Monomial> termMap(IdealFactory factory) {
		return termMap();
	}

	@SuppressWarnings("unchecked")
	public SymbolicMap<Monic, Monomial> termMap() {
		return (SymbolicMap<Monic, Monomial>) argument(0);
	}

	@Override
	public Monomial leadingTerm() {
		SymbolicMap<Monic, Monomial> map = termMap();

		if (map.isEmpty())
			return null;
		return map.getFirst();
	}

	@Override
	public Monomial factorization(IdealFactory factory) {
		return factorization;
	}

	@Override
	public Polynomial numerator(IdealFactory factory) {
		return this;
	}

	@Override
	public Polynomial denominator(IdealFactory factory) {
		return factory.one(type());
	}

	public StringBuffer toStringBuffer() {
		StringBuffer buffer = new StringBuffer();
		boolean first = true;

		for (SymbolicExpression expr : termMap()) {
			if (first)
				first = false;
			else
				buffer.append("+");
			buffer.append(expr.toString());
		}
		return buffer;
	}

	@Override
	public String toString() {
		return toStringBuffer().toString();
	}

	@Override
	public int degree() {
		if (degree < 0) {
			degree = 0;
			for (Monic expr : termMap().keys()) {
				int termDegree = expr.degree();

				if (termDegree > degree)
					degree = termDegree;
			}
		}
		return degree;
	}

	@Override
	public IdealKind idealKind() {
		return IdealKind.NTPolynomial;
	}

	@Override
	public Constant constantTerm(IdealFactory factory) {
		SymbolicType type = type();
		Constant constant = (Constant) termMap().get(factory.one(type));

		return constant == null ? factory.zero(type) : constant;
	}

}
