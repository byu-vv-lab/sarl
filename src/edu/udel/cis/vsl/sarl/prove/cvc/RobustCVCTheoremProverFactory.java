package edu.udel.cis.vsl.sarl.prove.cvc;

import edu.udel.cis.vsl.sarl.IF.config.ProverInfo;
import edu.udel.cis.vsl.sarl.IF.config.ProverInfo.ProverKind;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

/**
 * Factory for producing new instances of {@link RobustCVCTheoremProver}.
 * 
 * @author siegel
 *
 */
public class RobustCVCTheoremProverFactory implements TheoremProverFactory {

	/**
	 * The symbolic universe used for managing symbolic expressions. Initialized
	 * by constructor and never changes.
	 */
	private PreUniverse universe;

	/**
	 * Information object for underlying prover, which must have
	 * {@link ProverKind} either {@link ProverKind#CVC3} or
	 * {@link ProverKind#CVC4}
	 */
	private ProverInfo prover;

	/**
	 * Constructs new CVC theorem prover factory with the given symbolic
	 * universe.
	 * 
	 * @param universe
	 *            symbolic universe used to manage symbolic expressions
	 * @param prover
	 *            information object for underlying prover, which must have
	 *            {@link ProverKind} either {@link ProverKind#CVC3} or
	 *            {@link ProverKind#CVC4}
	 */
	public RobustCVCTheoremProverFactory(PreUniverse universe, ProverInfo prover) {
		this.universe = universe;
		this.prover = prover;
	}

	@Override
	public TheoremProver newProver(BooleanExpression context) {
		return new RobustCVCTheoremProver(universe, context, prover);
	}
}
