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
package edu.udel.cis.vsl.sarl.simplify;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.simplify.IF.RangeFactory;
import edu.udel.cis.vsl.sarl.simplify.IF.Simplifier;
import edu.udel.cis.vsl.sarl.simplify.IF.SimplifierFactory;
import edu.udel.cis.vsl.sarl.simplify.common.IdentitySimplifier;
import edu.udel.cis.vsl.sarl.simplify.common.IdentitySimplifierFactory;

/**
 * Entry point for module "simplify", providing static method to create basic
 * simplifiers, simplifier factories, and range factories.
 * 
 * @author siegel
 *
 */
public class Simplify {

	/**
	 * Creates a new "trivial" simplifier: given any expression this simplifiers
	 * just returns the expression.
	 * 
	 * @param universe
	 *            the pre-universe associated to the simplifier
	 * @param assumption
	 *            the boolean expression context, which is just not used
	 * @return the new trivial simplifier
	 */
	public static Simplifier identitySimplifier(PreUniverse universe,
			BooleanExpression assumption) {
		return new IdentitySimplifier(universe, assumption);
	}

	/**
	 * A factory for producing trivial simplifiers.
	 * 
	 * @param universe
	 *            the pre-universe to associate to the simplifier
	 * @return a new trivial simplifier factory
	 * @see #identitySimplifier(PreUniverse, BooleanExpression)
	 */
	public static SimplifierFactory newIdentitySimplifierFactory(
			PreUniverse universe) {
		return new IdentitySimplifierFactory(universe);
	}

	public static RangeFactory newIntervalUnionFactory() {
		// under construction
		return null;
	}

}
