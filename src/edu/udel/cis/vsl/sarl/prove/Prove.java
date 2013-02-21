package edu.udel.cis.vsl.sarl.prove;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverseIF;
import edu.udel.cis.vsl.sarl.IF.prove.TheoremProverIF;
import edu.udel.cis.vsl.sarl.prove.cvc.CVC3TheoremProverFactory;
import edu.udel.cis.vsl.sarl.prove.ideal.IdealCVC3HybridProver;

public class Prove {

	public static TheoremProverIF newIdealCVC3HybridProver(
			SymbolicUniverseIF universe) {
		return new IdealCVC3HybridProver(universe);
	}

	public static TheoremProverIF newCVC3Prover(SymbolicUniverseIF universe) {
		return CVC3TheoremProverFactory.newCVC3TheoremProver(universe);
	}
}
