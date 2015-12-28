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
package edu.udel.cis.vsl.sarl.ideal.IF;

import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;

/**
 * A polynomial: an expression which is the sum of monomials.
 * 
 * 
 * @author siegel
 * 
 */
public interface Polynomial extends RationalExpression {

	/**
	 * Returns the terms of this polynomial as a map from {@link Monic} to
	 * {@link Monomial}. The polynomial is sum of the monomials. A key in the
	 * map is a monomial m occurring in one of the terms. The value associated
	 * to m is a monomial c*m for some non-zero constant c.
	 * 
	 * @param factory
	 *            the ideal factory owning this polynomial
	 * 
	 * @return the terms of this polynomial as a map
	 */
	SymbolicMap<Monic, Monomial> termMap(IdealFactory factory);

	/**
	 * The leading term of this polynomial, or null if the polynomial is 0.
	 * 
	 * @return the leading term of this polynomial or null
	 */
	Monomial leadingTerm();

	/**
	 * The constant term of this polynomial, which may be 0.
	 * 
	 * @param factory
	 *            the ideal factory owning this polynomial
	 * 
	 * @return the constant term of this polynomial
	 */
	Constant constantTerm(IdealFactory factory);

	/**
	 * Returns a factorization of this polynomial expressed as a
	 * {@link Monomial} in which the "variables" are
	 * {@link edu.udel.cis.vsl.sarl.ideal.common.ReducedPolynomial}s as well as
	 * other standard
	 * {@link edu.udel.cis.vsl.sarl.ideal.common.NumericPrimitive}s, such as
	 * symbolic constants, etc.
	 * 
	 * @param factory
	 *            the ideal factory owning this polynomial
	 * 
	 * @return a factorization of this polynomial
	 */
	Monomial factorization(IdealFactory factory);

	/**
	 * Returns the degree of the polynomial, i.e., the maximum degree of a
	 * monomial term, or -1 if the polynomial is 0. A numeric primitive always
	 * has degree 1, even if it "wraps" a polynomial of higher degree.
	 * 
	 * @return the degree of the polynomial
	 */
	int degree();

}
