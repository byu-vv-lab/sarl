package edu.udel.cis.vsl.sarl.prove.cvc;

import edu.udel.cis.vsl.sarl.IF.config.ProverInfo;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class RobustCVC3TheoremProverFactory implements TheoremProverFactory {

	/**
	 * The symbolic universe used for managing symbolic expressions. Initialized
	 * by constructor and never changes.
	 */
	private PreUniverse universe;

	private ProverInfo prover;

	/**
	 * Constructs a CVC3 theorem prover factory with the given symbolic
	 * universe.
	 * 
	 * @param universe
	 */
	public RobustCVC3TheoremProverFactory(PreUniverse universe,
			ProverInfo prover) {
		this.universe = universe;
		this.prover = prover;
	}

	/**
	 * This is where the factory produces instances with a given context
	 * 
	 * @param context
	 * @return a new CVC3 theorem prover
	 */
	@Override
	public TheoremProver newProver(BooleanExpression context) {
		return new RobustCVC3TheoremProver(universe, context, prover);
	}
}
