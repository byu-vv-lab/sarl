package edu.udel.cis.vsl.sarl.IF;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.Interval;

/**
 * A simplifier is an object for simplifying symbolic expressions. It is created
 * with a given "assumption", which is a boolean-valued symbolic expression. The
 * assumption itself can be simplified; the simplified version of the assumption
 * can then be obtained by the method newAssumption. Any other symbolic
 * expression can be simplified (subject to the assumption) using the method
 * simplify.
 * 
 * Example:
 * 
 * <pre>
 * assumption: N>=0 && N>=1
 * new assumption: N>=1
 * result of simplify (N>=1) : true
 * result of simplify (N>=2) : N>=2
 * result of simplify (N<0)  : false
 * </pre>
 * 
 * Etc.
 */
public interface Simplifier extends
		Transform<SymbolicExpression, SymbolicExpression> {

	/** Returns the symbolic universe associated to this simplifier */
	SymbolicUniverse universe();

	/** Returns the simplified assumption */
	SymbolicExpression newAssumption();

	// /**
	// * Returns the simplified version of the given expression (under the
	// * assumption).
	// apply does this now
	// */
	// SymbolicExpression simplify(SymbolicExpression expression);

	/**
	 * If the assumption can be represented as a simple interval constraint,
	 * i.e., an expression of the form A <= x <= B, where A and B are concrete
	 * numbers, x is the given symbolic constant, and <= could be < in either
	 * case, this returns the interval [A,B] (or (A,B], or, ...). Else returns
	 * null.
	 */
	Interval assumptionAsInterval(SymbolicConstant symbolicConstant);
}
