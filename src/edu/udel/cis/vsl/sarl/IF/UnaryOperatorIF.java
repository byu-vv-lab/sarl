package edu.udel.cis.vsl.sarl.IF;

/**
 * Interface for a unary operator on symbolic expressions.
 * 
 * @author siegel
 * 
 */
public interface UnaryOperatorIF {

	SymbolicExpressionIF apply(SymbolicExpressionIF arg);

}
