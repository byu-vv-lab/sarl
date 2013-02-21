package edu.udel.cis.vsl.sarl.prove.cvc;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverseIF;
import edu.udel.cis.vsl.sarl.IF.prove.TheoremProverIF;

public class CVC3TheoremProverFactory {
	public static TheoremProverIF newCVC3TheoremProver(
			SymbolicUniverseIF universe) {
		return new CVC3TheoremProver(universe);
	}
}
