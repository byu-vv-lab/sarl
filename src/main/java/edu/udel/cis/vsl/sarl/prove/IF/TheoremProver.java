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

import edu.udel.cis.vsl.sarl.IF.ModelResult;
import edu.udel.cis.vsl.sarl.IF.TheoremProverException;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

/**
 * Provides an abstract interface for an automated theorem prover operating
 * under a fixed context (i.e., a boolean expression assumed to hold).
 * 
 * @author Stephen F. Siegel
 */
public interface TheoremProver {

	/**
	 * Get the symbolic universe associated with the theorem prover. This is the
	 * object used to create and manipulate symbolic expressions.
	 * 
	 * @return the symbolic universe associated to this theorem prover
	 */
	PreUniverse universe();

	/**
	 * <p>
	 * Attempts to determine whether the statement p(x)=>q(x) is valid, i.e., is
	 * a tautology. Here, p is the "context", q is the "predicate", and x stands
	 * for the set of all symbolic constants which occur in p or q.
	 * </p>
	 * 
	 * <p>
	 * A result of type YES implies forall x.(p(x)=>q(x)). A result of NO
	 * implies nsat(p)||exists x.(p(x)&&!q(x)). Nothing can be concluded from a
	 * result of MAYBE.
	 * </p>
	 * 
	 * <p>
	 * nsat(p) means p is not satisfiable, i.e., forall x.!p(x), or equivalently
	 * !exists x.p(x). Note that if p is not satisfiable then any of the three
	 * possible results could be returned.
	 * </p>
	 * 
	 * <p>
	 * Note that if the context is unsatisfiable then any of the three types
	 * could be returned.
	 * </p>
	 * 
	 * <p>
	 * Consider the case where the context p is "true". If valid(q) returns YES
	 * then forall x.q(x) (i.e., q is a tautology); if it returns NO then exists
	 * x.!q(x) (i.e., q is not a tautology). If valid(!q) returns YES then q is
	 * not satisfiable; if it returns NO then q is satisfiable.
	 * </p>
	 * 
	 * <p>
	 * Note that in the case NO is returned, there is no guarantee this method
	 * will attempt to find a model (and return an instance of
	 * {@link ModelResult}). If a model is required in the NO case, use method
	 * {@link #validOrModel} instead.
	 * </p>
	 * 
	 * 
	 * @param predicate
	 *            the boolean expression q(x)
	 * @throws TheoremProverException
	 *             if something goes wrong with the automated theorem prover
	 *             during this call
	 * @return a ValidityResult whose type must satisfy the constraints
	 *         described above
	 */
	ValidityResult valid(BooleanExpression predicate);

	/**
	 * Attempts to determine whether p(x)=>q(x) is valid, and, if not, also
	 * returns a model (counterexample). The specification is exactly the same
	 * as for {@link #valid}, except that a {@link ModelResult} is returned in
	 * the NO case. This provides a method to get the model. Note that the model
	 * may be null if there was some problem in obtaining or constructing the
	 * model.
	 * 
	 * If the model is non-null, it will be a map in which the key set consists
	 * of all the symbolic constants of non-function type that occur in the
	 * context or predicate. The value associated to a key will be a concrete
	 * symbolic expression.
	 * 
	 * @throws TheoremProverException
	 *             if something goes wrong with the automated theorem prover
	 *             during this call
	 * @return a validity result as specified above
	 */
	ValidityResult validOrModel(BooleanExpression predicate);

}
