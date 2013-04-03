/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.prove.ideal;

import java.io.PrintStream;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.prove.TheoremProver;
import edu.udel.cis.vsl.sarl.IF.prove.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.prove.ValidityResult.ResultType;
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

	public ResultType valid(BooleanExpression assumption, BooleanExpression expr) {
		ResultType result = simpleProver.valid(assumption, expr);

		if (result == ResultType.MAYBE) {
			result = cvc3Prover.valid(assumption, expr);
		}
		return result;
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

	@Override
	public ValidityResult validOrModel(BooleanExpression assumption,
			BooleanExpression predicate) {
		ResultType result = simpleProver.valid(assumption, predicate);

		if (result == ResultType.YES) {
			return new ValidityResult(result, null);
		}
		return cvc3Prover.validOrModel(assumption, predicate);
	}

}
