package edu.udel.cis.vsl.sarl.prove.IF;

import java.util.Map;

import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicConstantIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicUniverseIF;
import edu.udel.cis.vsl.sarl.util.TernaryResult.ResultType;

public interface TheoremProverIF {
	/**
	 * Get the symbolic universe associated with the theorem prover.
	 */
	SymbolicUniverseIF universe();

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
	ResultType valid(SymbolicExpressionIF assumption,
			SymbolicExpressionIF predicate);

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
	 * Find a model for a path condition if it is satisfiable. Returns null
	 * otherwise.
	 * 
	 * @param context
	 *            - the SymbolicExpressionIF path condition
	 * @return a map of SymbolicConstants to their SymbolicExpression values.
	 * @throws TheoremProverException
	 */
	Map<SymbolicConstantIF, SymbolicExpressionIF> findModel(
			SymbolicExpressionIF context) throws TheoremProverException;
}
