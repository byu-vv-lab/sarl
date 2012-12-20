package edu.udel.cis.vsl.sarl.symbolic.IF.tree;

import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicConstantIF;

/**
 * A symbolic constant expression simple wraps a symbolic constant in a symbolic
 * expression shell. It has no arguments.
 */
public interface SymbolicConstantExpressionIF extends
		TreeExpressionIF {

	SymbolicConstantIF symbolicConstant();
}
