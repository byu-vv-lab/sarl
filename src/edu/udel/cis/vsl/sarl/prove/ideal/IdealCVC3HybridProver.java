package edu.udel.cis.vsl.sarl.prove.ideal;

import java.io.PrintStream;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.prove.TernaryResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.prove.TheoremProverException;
import edu.udel.cis.vsl.sarl.IF.prove.TheoremProver;
import edu.udel.cis.vsl.sarl.prove.cvc.CVC3TheoremProverFactory;

/**
 * A hybrid prover for symbolic expressions in the ideal canonical form. It
 * first attempts to prove the query using the SimpleIdealProver. If this does
 * not yield a conclusive result, it then attempts the CVC3 prover (which is
 * much more expensive).
 */
public class IdealCVC3HybridProver implements TheoremProver {

	private SymbolicUniverse universe;

	private TheoremProver simpleProver, cvc3Prover;

	public IdealCVC3HybridProver(SymbolicUniverse universe) {
		simpleProver = new SimpleIdealProver(universe);
		cvc3Prover = CVC3TheoremProverFactory.newCVC3TheoremProver(universe);
		this.universe = universe;
	}

	public void close() {
		simpleProver.close();
		cvc3Prover.close();
	}

	public void reset() {
		simpleProver.reset();
		cvc3Prover.reset();
	}

	public SymbolicUniverse universe() {
		return universe;
	}

	public ResultType valid(SymbolicExpression assumption,
			SymbolicExpression expr) {
		ResultType result = simpleProver.valid(assumption, expr);

		if (result == ResultType.MAYBE) {
			result = cvc3Prover.valid(assumption, expr);
		}
		return result;
	}

	public Map<SymbolicConstant, SymbolicExpression> findModel(
			SymbolicExpression context) throws TheoremProverException {
		return cvc3Prover.findModel(context);
	}

	@Override
	public int numInternalValidCalls() {
		return cvc3Prover.numValidCalls();
	}

	@Override
	public int numValidCalls() {
		return simpleProver.numValidCalls();
	}

	@Override
	public void setOutput(PrintStream out) {
		simpleProver.setOutput(out);
		cvc3Prover.setOutput(out);
	}

}
