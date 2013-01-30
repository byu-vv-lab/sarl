package edu.udel.cis.vsl.sarl.symbolic.function;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.constant.SymbolicConstantExpression;

public class EvaluatedFunctionFactory {

	private Map<SymbolicExpressionKey<EvaluatedFunctionExpression>, EvaluatedFunctionExpression> evaluatedFunctionMap = new HashMap<SymbolicExpressionKey<EvaluatedFunctionExpression>, EvaluatedFunctionExpression>();

	private Map<SymbolicExpressionKey<LambdaExpression>, LambdaExpression> lambdaMap = new HashMap<SymbolicExpressionKey<LambdaExpression>, LambdaExpression>();

	/**
	 * Treats function as an uninterpreted function and returns a symbolic
	 * expression representing the application of function to the arguments.
	 */
	public EvaluatedFunctionExpression evaluatedFunction(
			TreeExpressionIF function, TreeExpressionIF[] arguments) {
		return CommonSymbolicExpression.flyweight(evaluatedFunctionMap,
				new EvaluatedFunctionExpression(function, arguments));
	}

	/**
	 * Returns a lambda expression, which is a function of one variable, whose
	 * value is defined by an expression which may refer to that variable.
	 */
	public LambdaExpression lambda(SymbolicFunctionTypeIF functionType,
			SymbolicConstantExpression variable, TreeExpressionIF expression) {
		return CommonSymbolicExpression.flyweight(lambdaMap, new LambdaExpression(
				functionType, variable, expression));
	}

}
