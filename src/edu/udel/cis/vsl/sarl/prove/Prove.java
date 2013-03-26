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
package edu.udel.cis.vsl.sarl.prove;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.prove.TheoremProver;
import edu.udel.cis.vsl.sarl.prove.cvc.CVC3TheoremProverFactory;
import edu.udel.cis.vsl.sarl.prove.ideal.IdealCVC3HybridProver;

public class Prove {

	public static TheoremProver newIdealCVC3HybridProver(
			SymbolicUniverse universe) {
		return new IdealCVC3HybridProver(universe);
	}

	public static TheoremProver newCVC3Prover(SymbolicUniverse universe) {
		return CVC3TheoremProverFactory.newCVC3TheoremProver(universe);
	}
}
