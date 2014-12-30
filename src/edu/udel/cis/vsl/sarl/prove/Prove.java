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
package edu.udel.cis.vsl.sarl.prove;

import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.ModelResult;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.config.ProverInfo;
import edu.udel.cis.vsl.sarl.IF.config.SARLConfig;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;
import edu.udel.cis.vsl.sarl.prove.common.CommonModelResult;
import edu.udel.cis.vsl.sarl.prove.common.CommonValidityResult;
import edu.udel.cis.vsl.sarl.prove.common.MultiProverFactory;
import edu.udel.cis.vsl.sarl.prove.cvc.CVC3TheoremProverFactory;
import edu.udel.cis.vsl.sarl.prove.cvc.CVC4TheoremProverFactory;
import edu.udel.cis.vsl.sarl.prove.cvc.RobustCVCTheoremProverFactory;
import edu.udel.cis.vsl.sarl.prove.z3.Z3TheoremProverFactory;

/**
 * This is the entry point for module prove. It provides:
 * <ul>
 * <li>constants of type {@link ValidityResult} corresponding to the three
 * different kinds of validity results: {@link #RESULT_YES}, {@link #RESULT_NO},
 * and {@link #RESULT_MAYBE}</li>
 * <li>methods for producing new {@link TheoremProverFactory} instances.</li>
 * <li>various other methods dealing with prover results</li>
 * </ul>
 * 
 * @author siegel
 *
 */
public class Prove {

	/**
	 * A constant of type {@link ValidityResult} which has {@link ResultType}
	 * {@link ResultType.YES}.
	 */
	public final static ValidityResult RESULT_YES = new CommonValidityResult(
			ResultType.YES);

	/**
	 * A constant of type {@link ValidityResult} which has {@link ResultType}
	 * {@link ResultType.NO}.
	 */
	public final static ValidityResult RESULT_NO = new CommonValidityResult(
			ResultType.NO);

	/**
	 * A constant of type {@link ValidityResult} which has {@link ResultType}
	 * {@link ResultType.MAYBE}.
	 */
	public final static ValidityResult RESULT_MAYBE = new CommonValidityResult(
			ResultType.MAYBE);

	/**
	 * Constructs a new theorem prover factory based on the given configuration.
	 * A resulting prover resolves a query as follows: it starts by using the
	 * first external prover in the given config. If that result is
	 * inconclusive, it goes to the next, and so on.
	 * 
	 * @param universe
	 *            the symbolic universe used to manage and produce symbolic
	 *            expressions
	 * @param config
	 *            a SARL configuration object specifying some sequence of
	 *            theorem provers which are available
	 * @return a new theorem prover factory which may use all of the provers
	 *         specified in the config, in order, until a conclusive result is
	 *         reached or all provers have been exhausted
	 */
	public static TheoremProverFactory newMultiProverFactory(
			PreUniverse universe, SARLConfig config) {
		int numProvers = config.getNumProvers();
		TheoremProverFactory[] factories = new TheoremProverFactory[numProvers];
		int count = 0;

		for (ProverInfo prover : config.getProvers()) {
			factories[count] = newProverFactory(universe, prover);
			count++;
		}
		return new MultiProverFactory(universe, factories);
	}

	/**
	 * Constructs a new theorem prover factory based on a single underlying
	 * theorem prover.
	 * 
	 * @param universe
	 *            the symbolic universe used to produce and manipulate symbolic
	 *            expressions
	 * @param prover
	 *            a {@link ProverInfo} object providing information on the
	 *            specific underlying theorem prover which will be used
	 * @return the new theorem prover factory based on the given prover
	 */
	public static TheoremProverFactory newProverFactory(PreUniverse universe,
			ProverInfo prover) {
		switch (prover.getKind()) {
		case CVC3:
		case CVC4:
			return new RobustCVCTheoremProverFactory(universe, prover);
		case CVC3_API:
			return new CVC3TheoremProverFactory(universe, prover);
		case CVC4_API:
			return new CVC4TheoremProverFactory(universe, prover);
		case Z3_API:
			return new Z3TheoremProverFactory(universe, prover);
		case Z3:
			// not yet implemented
		default:
			throw new SARLInternalException("Unknown kind of theorem prover: "
					+ prover.getKind());
		}
	}

	/**
	 * Returns one of the constants {@link #RESULT_YES}, {@link #RESULT_NO},
	 * {@link #RESULT_MAYBE}, corresponding to the given type.
	 * 
	 * @param type
	 *            a non-null {@link ResultType}
	 * @return either {@link #RESULT_YES}, {@link #RESULT_NO}, or
	 *         {@link #RESULT_MAYBE}, depending on whether <code>type</code> is
	 *         {@link ResultType#YES}, {@link ResultType#NO}, or
	 *         {@link ResultType#MAYBE}, respectively.
	 */
	public static ValidityResult validityResult(ResultType type) {
		switch (type) {
		case YES:
			return RESULT_YES;
		case NO:
			return RESULT_NO;
		case MAYBE:
			return RESULT_MAYBE;
		default:
			throw new SARLInternalException("unreachable");
		}
	}

	/**
	 * Constructs a new {@link ModelResult} wrapping the given mapping from
	 * symbolic constants to symbolic expressions. The represents the case where
	 * a validity result is {@link ResultType#NO} and, in addition, a specific
	 * counter example has been found. The counterexample specifies a concrete
	 * value for each symbolic constant which was used in the query, in such a
	 * way that the queried predicate evaluates to <code>false</code> and the
	 * queried assumption evaluates to <code>true</code>.
	 * 
	 * @param model
	 *            mapping giving concrete value to each symbolic constant
	 *            occurring in the query
	 * @return new instance of {@link ModelResult} wrapping the given
	 *         <code>mode</code>.
	 */
	public static ModelResult modelResult(
			Map<SymbolicConstant, SymbolicExpression> model) {
		return new CommonModelResult(model);
	}

}
