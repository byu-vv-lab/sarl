package edu.udel.cis.vsl.sarl.symbolic;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;

public interface NumericExpression extends SymbolicExpressionIF {

	boolean isZero();

	boolean isOne();
	
	int compareNumeric(NumericExpression that);

}
