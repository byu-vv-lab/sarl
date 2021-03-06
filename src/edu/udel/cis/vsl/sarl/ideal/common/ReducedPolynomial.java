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

import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Monic;
import edu.udel.cis.vsl.sarl.ideal.IF.Monomial;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;

/**
 * <p>
 * A reduced polynomial is a polynomial satisfying: (1) if the type is real then
 * the leading coefficient of the polynomial is 1, (2) if the type is integer
 * then the leading coefficient is positive and the GCD of the absolute values
 * of the coefficients is 1, and (3) there is no known nontrivial factorization
 * of the polynomial.
 * </p>
 * 
 * <p>
 * A reduced polynomial is treated as a primitive, i.e., it may be used as a
 * "variable" in polynomial expressions. This is to express factorizations: a
 * factorization is a monomial in which the "variables" are reduced polynomials.
 * </p>
 * 
 * @author siegel
 * 
 */
// TODO: problem: this object will appear "equal" to a Polynomial with
// the same term map. They should be unequal.
public class ReducedPolynomial extends NumericPrimitive {

	public ReducedPolynomial(SymbolicType type,
			SymbolicMap<Monic, Monomial> termMap) {
		super(SymbolicOperator.ADD, type, termMap);
	}

	@SuppressWarnings("unchecked")
	public SymbolicMap<Monic, Monomial> termMap() {
		return (SymbolicMap<Monic, Monomial>) argument(0);
	}

	/**
	 * Expands this reduced polynomial by re-building a polynomial from the
	 * terms of this one. The result is a polynomial which is not an instance of
	 * {@link ReducedPolynomial}.
	 * 
	 * @return a polynomial equivalent to this one but which is not a
	 *         {@link ReducedPolynomial}.
	 */
	@Override
	public Polynomial expand(IdealFactory factory) {
		return factory.polynomial(termMap(), this);
	}

}
