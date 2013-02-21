package edu.udel.cis.vsl.sarl.prove.ideal;

import java.io.PrintStream;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverseIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstantIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.prove.TernaryResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.prove.TheoremProverException;
import edu.udel.cis.vsl.sarl.IF.prove.TheoremProverIF;
import edu.udel.cis.vsl.sarl.prove.cvc.CVC3TheoremProverFactory;

/**
 * A hybrid prover for symbolic expressions in the ideal canonical form. It
 * first attempts to prove the query using the SimpleIdealProver. If this does
 * not yield a conclusive result, it then attempts the CVC3 prover (which is
 * much more expensive).
 */
public class IdealCVC3HybridProver implements TheoremProverIF {

	private SymbolicUniverseIF universe;

	private TheoremProverIF simpleProver, cvc3Prover;

	public IdealCVC3HybridProver(SymbolicUniverseIF universe) {
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

	public SymbolicUniverseIF universe() {
		return universe;
	}

	public ResultType valid(SymbolicExpressionIF assumption,
			SymbolicExpressionIF expr) {
		ResultType result = simpleProver.valid(assumption, expr);

		if (result == ResultType.MAYBE) {
			result = cvc3Prover.valid(assumption, expr);
		}
		return result;
	}

	public Map<SymbolicConstantIF, SymbolicExpressionIF> findModel(
			SymbolicExpressionIF context) throws TheoremProverException {
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
