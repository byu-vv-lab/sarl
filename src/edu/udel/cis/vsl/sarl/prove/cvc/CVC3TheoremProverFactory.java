package edu.udel.cis.vsl.sarl.prove.cvc;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.prove.TheoremProver;

public class CVC3TheoremProverFactory {
	public static TheoremProver newCVC3TheoremProver(
			SymbolicUniverse universe) {
		return new CVC3TheoremProver(universe);
	}
}
