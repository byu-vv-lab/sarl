package edu.udel.cis.vsl.sarl.prove;

import java.io.PrintWriter;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverseIF;
import edu.udel.cis.vsl.sarl.IF.TheoremProverIF;
import edu.udel.cis.vsl.sarl.prove.cvc.CVC3TheoremProverFactory;
import edu.udel.cis.vsl.sarl.prove.ideal.IdealCVC3HybridProver;

public class Prove {

	public static TheoremProverIF newIdealCVC3HybridProver(
			SymbolicUniverseIF universe, PrintWriter out,
			boolean showProverQueries) {
		return new IdealCVC3HybridProver(universe, out, showProverQueries);
	}

	public static TheoremProverIF newCVC3Prover(SymbolicUniverseIF universe,
			PrintWriter out, boolean showProverQueries) {
		return CVC3TheoremProverFactory.newCVC3TheoremProver(universe, out,
				showProverQueries);
	}
}
