package edu.udel.cis.vsl.sarl.symbolic.power;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.symbolic.IF.tree.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpressionKey;

public class PowerExpressionFactory {

	private Map<SymbolicExpressionKey<PowerExpression>, PowerExpression> map = new HashMap<SymbolicExpressionKey<PowerExpression>, PowerExpression>();

	public PowerExpression powerExpression(TreeExpressionIF base,
			NumericConcreteExpressionIF exponent) {
		PowerExpression expression = new PowerExpression(base, exponent);

		return SymbolicExpression.flyweight(map, expression);
	}

}
