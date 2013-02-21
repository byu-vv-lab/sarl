package edu.udel.cis.vsl.sarl.IF;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

/**
 * Interface for a unary operator on symbolic expressions.
 * 
 * @author siegel
 * 
 */
public interface UnaryOperator {

	SymbolicExpression apply(SymbolicExpression arg);

}
