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
