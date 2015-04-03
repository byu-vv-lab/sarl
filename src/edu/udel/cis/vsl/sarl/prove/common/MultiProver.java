package edu.udel.cis.vsl.sarl.prove.common;

import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;

/**
 * An implementation of {@link TheoremProver} which wraps a sequence of
 * underlying {@link TheoremProver}s. To determine validity of a formula, this
 * prover invokes the underlying provers in sequence, until a conclusive result
 * is obtained.
 * 
 * @author Stephen F. Siegel
 */
public class MultiProver implements TheoremProver {

	private PreUniverse universe;

	private TheoremProver[] provers;

	public MultiProver(PreUniverse universe, TheoremProver[] provers) {
		this.universe = universe;
		this.provers = provers;
	}

	@Override
	public PreUniverse universe() {
		return universe;
	}

	@Override
	public ValidityResult valid(BooleanExpression predicate) {
		for (TheoremProver prover : provers) {
			ValidityResult result = prover.valid(predicate);

			if (result.getResultType() != ResultType.MAYBE)
				return result;
		}
		return Prove.RESULT_MAYBE;
	}

	@Override
	public ValidityResult validOrModel(BooleanExpression predicate) {
		for (TheoremProver prover : provers) {
			ValidityResult result = prover.validOrModel(predicate);

			if (result.getResultType() != ResultType.MAYBE)
				return result;
		}
		return Prove.RESULT_MAYBE;
	}

}
