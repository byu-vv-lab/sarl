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

import java.io.PrintStream;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.TheoremProverException;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.prove.common.CommonValidityResult;

/**
 * Provides an abstract interface for an automated theorem prover.
 */
public interface TheoremProver {
	/**
	 * Get the symbolic universe associated with the theorem prover.
	 */
	SymbolicUniverse universe();

	/**
	 * Attempts to determine whether the statement p(x)=>q(x) is a tautology.
	 * Here, p is the "assumption", q is the "predicate", and x stands for the
	 * set of all symbolic constants which occur in p or q.
	 * 
	 * A result of YES implies forall x.(p(x)=>q(x)). A result of NO implies
	 * nsat(p)||exists x.(p(x)&&!q(x)). Nothing can be concluded from a result
	 * of MAYBE.
	 * 
	 * nsat(p) means p is not satisfiable, i.e., forall x.!p(x), or equivalently
	 * !exists x.p(x). Note that if p is not satisfiable then any of the three
	 * possible results could be returned.
	 * 
	 * Consider a call to valid(true,q). If this returns YES then forall x.q(x)
	 * (i.e., q is a tautology). If it returns NO then exists x.!q(x) (i.e., q
	 * is not a tautology).
	 * 
	 * Consider a call to valid(true,!q). If this returns YES then q is not
	 * satisfiable. If it returns no, then q is satisfiable.
	 * 
	 */
	ValidityResult valid(BooleanExpression predicate);

	/**
	 * Attempts to determine whether p(x)=>q(x) is valid, and, if not, also
	 * returns a model (counter-example). The specification is exactly the same
	 * as for {@link valid}, except that a {@link ValidityResult} is returned.
	 * This provides a method {@link CommonValidityResult.getResultType} that
	 * returns the result type, but also a method
	 * {@link CommonValidityResult.getModel} that provides the model. That
	 * method will return null if the result type is YES or MAYBE. It may return
	 * null even if the result type is NO, either because the assumption is not
	 * satisfiable or a model could not be found for some reason.
	 * 
	 * If the model is non-null, it will be a map in which the key set consists
	 * of all the symbolic constants of non-function type that occur in the
	 * assumption or predicate. The value associated to a key will be a concrete
	 * symbolic expression.
	 * 
	 * @param predicate
	 *            the predicate q(x)
	 * @throws TheoremProverException
	 *             if something goes wrong with the automated theorem prover
	 *             during this call
	 * @return a validity result as specified above
	 */
	ValidityResult validOrModel(BooleanExpression predicate);

	/**
	 * If you want to see the queries and results (e.g., for debugging), set
	 * this to the appropriate PrintStream. Setting to null turns off that
	 * output. It is null, be default.
	 * 
	 * @param out
	 *            PrintStream to which you want the output sent
	 */
	void setOutput(PrintStream out);
}
