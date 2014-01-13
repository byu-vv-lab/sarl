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
package edu.udel.cis.vsl.sarl.reason.common;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.ModelResult;
import edu.udel.cis.vsl.sarl.IF.Reasoner;
import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.UnaryOperator;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;
import edu.udel.cis.vsl.sarl.simplify.IF.Simplifier;

/**
 * An implementation of Reasoner based on a given Simplifier and
 * TheoremProverFactory.
 * 
 * @author siegel
 * 
 */
public class CommonReasoner implements Reasoner {

	private TheoremProver prover = null;

	private Simplifier simplifier;

	private TheoremProverFactory factory;

	private HashMap<BooleanExpression, ValidityResult> validityCache = new HashMap<BooleanExpression, ValidityResult>();

	public CommonReasoner(Simplifier simplifier, TheoremProverFactory factory) {
		this.simplifier = simplifier;
		this.factory = factory;
	}

	public PreUniverse universe() {
		return simplifier.universe();
	}

	@Override
	public BooleanExpression getReducedContext() {
		return simplifier.getReducedContext();
	}

	@Override
	public BooleanExpression getFullContext() {
		return simplifier.getFullContext();
	}

	@Override
	public Interval assumptionAsInterval(SymbolicConstant symbolicConstant) {
		return simplifier.assumptionAsInterval(symbolicConstant);
	}

	@Override
	public SymbolicExpression simplify(SymbolicExpression expression) {
		if (expression == null)
			throw new SARLException("Argument to Reasoner.simplify is null.");
		return simplifier.apply(expression);
	}

	@Override
	public BooleanExpression simplify(BooleanExpression expression) {
		return (BooleanExpression) simplify((SymbolicExpression) expression);
	}

	@Override
	public NumericExpression simplify(NumericExpression expression) {
		return (NumericExpression) simplify((SymbolicExpression) expression);
	}

	@Override
	public ValidityResult valid(BooleanExpression predicate) {
		boolean showQuery = universe().getShowQueries();

		if (showQuery) {
			PrintStream out = universe().getOutputStream();
			int id = universe().numValidCalls();

			out.println("Query " + id + " assumption: "
					+ simplifier.getFullContext());
			out.println("Query " + id + " predicate:  " + predicate);
		}
		if (predicate == null)
			throw new SARLException("Argument to Reasoner.valid is null.");
		else {
			BooleanExpression simplifiedPredicate = (BooleanExpression) simplifier
					.apply(predicate);
			ValidityResult result;

			universe().incrementValidCount();
			if (simplifiedPredicate.isTrue())
				result = Prove.RESULT_YES;
			else if (simplifiedPredicate.isFalse())
				result = Prove.RESULT_NO;
			else {
				result = validityCache.get(simplifiedPredicate);
				if (result != null)
					return result;
				if (prover == null)
					prover = factory.newProver(getReducedContext());
				result = prover.valid(simplifiedPredicate);
				validityCache.put(predicate, result);
			}
			if (showQuery) {
				int id = universe().numValidCalls() - 1;
				PrintStream out = universe().getOutputStream();

				out.println("Query " + id + " result:     " + result);
			}
			return result;
		}
	}

	@Override
	public ValidityResult validOrModel(BooleanExpression predicate) {
		BooleanExpression simplifiedPredicate = (BooleanExpression) simplifier
				.apply(predicate);
		ValidityResult result;

		universe().incrementValidCount();
		if (simplifiedPredicate.isTrue())
			result = Prove.RESULT_YES;
		else {
			result = validityCache.get(simplifiedPredicate);
			if (result != null && result instanceof ModelResult)
				return result;
			if (prover == null)
				prover = factory.newProver(getReducedContext());
			result = prover.validOrModel(simplifiedPredicate);
			validityCache.put(predicate, result);
		}
		return result;
	}

	@Override
	public Map<SymbolicConstant, SymbolicExpression> substitutionMap() {
		return simplifier.substitutionMap();
	}

	@Override
	public boolean isValid(BooleanExpression predicate) {
		return valid(predicate).getResultType() == ResultType.YES;
	}

	@Override
	public Number extractNumber(NumericExpression expression) {
		NumericExpression simple = (NumericExpression) simplify(expression);

		return universe().extractNumber(simple);
	}

	@Override
	public UnaryOperator<SymbolicExpression> simplifier() {
		return new UnaryOperator<SymbolicExpression>() {
			@Override
			public SymbolicExpression apply(SymbolicExpression x) {
				return simplify(x);
			}
		};
	}

}
