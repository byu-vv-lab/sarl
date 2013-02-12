package edu.udel.cis.vsl.sarl.expr.IF;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;

public interface NumericExpression extends SymbolicExpressionIF {

	boolean isZero();

	boolean isOne();

}
