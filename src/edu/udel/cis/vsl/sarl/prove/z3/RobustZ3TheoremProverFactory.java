package edu.udel.cis.vsl.sarl.prove.z3;

import edu.udel.cis.vsl.sarl.IF.config.ProverInfo;
import edu.udel.cis.vsl.sarl.IF.config.ProverInfo.ProverKind;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;
import edu.udel.cis.vsl.sarl.prove.cvc.RobustCVCTheoremProver;

/**
 * Factory for producing new instances of {@link RobustCVCTheoremProver}.
 * 
 * @author Stephen F. Siegel
 */
public class RobustZ3TheoremProverFactory implements TheoremProverFactory {

	/**
	 * The symbolic universe used for managing symbolic expressions. Initialized
	 * by constructor and never changes.
	 */
	private PreUniverse universe;

	/**
	 * Information object for underlying prover, which must have
	 * {@link ProverKind} {@link ProverKind#Z3}.
	 */
	private ProverInfo prover;

	/**
	 * Constructs new Z3 theorem prover factory with the given symbolic
	 * universe.
	 * 
	 * @param universe
	 *            symbolic universe used to manage symbolic expressions
	 * @param prover
	 *            information object for underlying prover, which must have
	 *            {@link ProverKind} {@link ProverKind#Z3}
	 */
	public RobustZ3TheoremProverFactory(PreUniverse universe, ProverInfo prover) {
		this.universe = universe;
		this.prover = prover;
	}

	@Override
	public TheoremProver newProver(BooleanExpression context) {
		return new RobustZ3TheoremProver(universe, context, prover);
	}
}
