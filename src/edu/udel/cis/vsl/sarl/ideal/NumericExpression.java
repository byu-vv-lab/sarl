package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;

public interface NumericExpression extends SymbolicExpressionIF {

	NumericExpression plus(IdealFactory factory, NumericExpression expr);

	NumericExpression times(IdealFactory factory, NumericExpression expr);

	NumericExpression negate(IdealFactory factory);

	NumericExpression invert(IdealFactory factory);

	boolean isZero();

	boolean isOne();

}
