package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;

public interface NumericExpression extends SymbolicExpressionIF {

	boolean isZero();

	boolean isOne();

}
