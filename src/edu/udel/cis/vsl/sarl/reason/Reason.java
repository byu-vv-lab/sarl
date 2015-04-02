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
package edu.udel.cis.vsl.sarl.reason;

import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;
import edu.udel.cis.vsl.sarl.reason.IF.ReasonerFactory;
import edu.udel.cis.vsl.sarl.reason.common.ContextMinimizingReasonerFactory2;
import edu.udel.cis.vsl.sarl.simplify.IF.SimplifierFactory;

public class Reason {

	public static ReasonerFactory newReasonerFactory(PreUniverse universe,
			SimplifierFactory simplifierFactory,
			TheoremProverFactory proverFactory) {
		ReasonerFactory result;

		// result = new CommonReasonerFactory(simplifierFactory, proverFactory);

		result = new ContextMinimizingReasonerFactory2(universe, proverFactory,
				simplifierFactory);
		return result;
	}
}
