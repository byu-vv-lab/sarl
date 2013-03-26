/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.IF.prove;

import java.io.PrintStream;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.prove.TernaryResult.ResultType;

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
	ResultType valid(BooleanExpression assumption, BooleanExpression predicate);

	/**
	 * Returns the total number of calls made to the method valid on this
	 * object.
	 */
	int numValidCalls();

	/**
	 * If this theorem prover uses another prover underneath the hood, this
	 * method returns the total number of calls to the valid method of that
	 * prover. Otherwise, returns 0.
	 */
	int numInternalValidCalls();

	/**
	 * Reset the theorem prover, delete all expressions and contexts.
	 */
	void reset();

	/**
	 * Close the theorem prover.
	 */
	void close();

	/**
	 * If you want to see the queries and results (e.g., for debugging), set
	 * this to the appropriate PrintStream. Setting to null turns off that
	 * output. It is null, be default.
	 * 
	 * @param out
	 *            PrintStream to which you want the output sent
	 */
	void setOutput(PrintStream out);

	/**
	 * Finds a model for a predicate if that predicate is satisfiable, else
	 * returns null. The model assigns a concrete value to each symbolic
	 * constant occurring in the predicate.
	 * 
	 * @param predicate
	 *            a boolean expression (e.g., the path condition)
	 * @return a map of SymbolicConstants to their (concrete) SymbolicExpression
	 *         values
	 * @throws TheoremProverException
	 *             if something goes wrong with the automated theorem prover
	 *             during this call
	 */
	Map<SymbolicConstant, SymbolicExpression> findModel(
			BooleanExpression predicate) throws TheoremProverException;
}
