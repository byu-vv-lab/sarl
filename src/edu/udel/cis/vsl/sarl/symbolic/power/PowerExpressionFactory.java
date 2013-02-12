package edu.udel.cis.vsl.sarl.symbolic.power;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.expr.common.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;

public class PowerExpressionFactory {

	private Map<SymbolicExpressionKey<PowerExpression>, PowerExpression> map = new HashMap<SymbolicExpressionKey<PowerExpression>, PowerExpression>();

	public PowerExpression powerExpression(TreeExpressionIF base,
			NumericConcreteExpressionIF exponent) {
		PowerExpression expression = new PowerExpression(base, exponent);

		return CommonSymbolicExpression.flyweight(map, expression);
	}

}
