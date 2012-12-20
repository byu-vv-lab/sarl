package edu.udel.cis.vsl.sarl.symbolic.cond;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpressionKey;

public class ConditionalExpressionFactory {

	private Map<SymbolicExpressionKey<ConditionalExpression>, ConditionalExpression> map = new HashMap<SymbolicExpressionKey<ConditionalExpression>, ConditionalExpression>();

	public ConditionalExpression conditionalExpression(
			TreeExpressionIF predicate, TreeExpressionIF trueValue,
			TreeExpressionIF falseValue) {
		return SymbolicExpression.flyweight(map, new ConditionalExpression(
				predicate, trueValue, falseValue));
	}

}
