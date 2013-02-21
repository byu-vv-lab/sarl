package edu.udel.cis.vsl.sarl.IF;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

/**
 * Interface for a binary operation on symbolic expressions.
 * 
 * TODO: combine with Multiplier
 * 
 * @author siegel
 * 
 */
public interface BinaryOperator {

	SymbolicExpression apply(SymbolicExpression arg0,
			SymbolicExpression arg1);

}
