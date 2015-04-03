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
package edu.udel.cis.vsl.sarl.prove.IF;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;

/**
 * A factory for producing instances of {@link TheoremProver}. Each instance of
 * {@link TheoremProver} contains a specific, fixed "context". The context is a
 * boolean symbolic expression. It is the assumption that is used for all
 * queries asked of that theorem prover. In symbolic execution, the context is
 * typically the "path condition". Hence a distinct theorem prover is generated
 * for each path condition.
 * 
 * @author Stephen F. Siegel
 */
public interface TheoremProverFactory {

	/**
	 * Returns a new instance of {@link TheoremProver} with the given context.
	 * 
	 * @param context
	 *            boolean expression assumed to hold
	 * @return a theorem prover operating under the given context
	 */
	TheoremProver newProver(BooleanExpression context);

}
