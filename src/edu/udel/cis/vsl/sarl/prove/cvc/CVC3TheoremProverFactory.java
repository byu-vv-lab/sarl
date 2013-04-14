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
package edu.udel.cis.vsl.sarl.prove.cvc;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;
import edu.udel.cis.vsl.sarl.universe.IF.ExtendedUniverse;

/**
 * A factory for producing new instances of CVC3TheoremProver.
 * 
 * @author siegel
 * 
 */
public class CVC3TheoremProverFactory implements TheoremProverFactory {

	private ExtendedUniverse universe;

	public CVC3TheoremProverFactory(ExtendedUniverse universe) {
		this.universe = universe;
	}

	@Override
	public TheoremProver newProver(BooleanExpression context) {
		return new CVC3TheoremProver(universe, context);
	}
}
