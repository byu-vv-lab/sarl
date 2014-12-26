package edu.udel.cis.vsl.sarl.prove.common;

import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;

public class MultiProver implements TheoremProver {

	TheoremProver[] provers;

	public MultiProver(TheoremProver[] provers) {
		this.provers = provers;
		assert provers.length >= 1;
	}

	@Override
	public PreUniverse universe() {
		return provers[0].universe();
	}

	@Override
	public ValidityResult valid(BooleanExpression predicate) {
		ValidityResult result = null;

		for (TheoremProver prover : provers) {
			result = prover.valid(predicate);
			if (result.getResultType() != ResultType.MAYBE)
				break;
		}
		return result;
	}

	@Override
	public ValidityResult validOrModel(BooleanExpression predicate) {
		ValidityResult result = null;

		for (TheoremProver prover : provers) {
			result = prover.validOrModel(predicate);
			if (result.getResultType() != ResultType.MAYBE)
				break;
		}
		return result;
	}

}
