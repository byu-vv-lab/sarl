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

/**
 * A Monomial is the product of a constant and a Monic.
 * 
 * @author siegel
 * 
 */
public interface Monomial extends Polynomial {

	Constant monomialConstant(IdealFactory factory);

	Monic monic(IdealFactory factory);

	/**
	 * If the monomial contains any primitives which are ReducedPolynomials,
	 * they are multiplied out (expanded) into polynomials involving only
	 * ordinary primitives.
	 * 
	 * @param factory
	 * @return equivalent polynomial with no ReducedPolynomial primitives in
	 *         terms
	 */
	Polynomial expand(IdealFactory factory);

}
