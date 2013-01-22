package edu.udel.cis.vsl.sarl.IF;

/**
 * Interface for a binary operation on symbolic expressions.
 * 
 * TODO: combine with Multiplier
 * 
 * @author siegel
 * 
 */
public interface BinaryOperatorIF {

	SymbolicExpressionIF apply(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1);

}
