package edu.udel.cis.vsl.sarl.prove.common;

import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;

public class ContextMinimizingProver implements TheoremProver {

	private PreUniverse universe;

	public ContextMinimizingProver(PreUniverse universe,
			BooleanExpression context) {

		// construct context reducer
		
	}

	@Override
	public PreUniverse universe() {
		return universe;
	}

	@Override
	public ValidityResult valid(BooleanExpression predicate) {
		// TODO Auto-generated method stub
		// reduce the context
		// use the underlying prover factory to get the prover
		// to do valid.  cache?
		// need to go back to cahcing level which is at beginning
		
		return null;
	}

	@Override
	public ValidityResult validOrModel(BooleanExpression predicate) {
		// TODO Auto-generated method stub
		return null;
	}

}
