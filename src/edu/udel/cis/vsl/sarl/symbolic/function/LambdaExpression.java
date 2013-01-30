package edu.udel.cis.vsl.sarl.symbolic.function;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.BooleanPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.constant.SymbolicConstantExpression;

/**
 * A lambda expression is a lambda expression in the sense of the lambda
 * calculus. It is an expression of the form lambda(x).f(x), where x is a bound
 * variable and f(x) is an expression that may involve x. The type of the lambda
 * expression if function from S to T, where S is the type of x, and T is the
 * type of f(x).
 */
public class LambdaExpression extends CommonSymbolicExpression implements
		NumericPrimitive, BooleanPrimitive {

	private static int classHashCode = LambdaExpression.class.hashCode();

	private SymbolicConstantExpression variable;

	private TreeExpressionIF expression;

	protected LambdaExpression(SymbolicFunctionTypeIF functionType,
			SymbolicConstantExpression variable, TreeExpressionIF expression) {
		super(functionType);
		assert functionType.numInputs() == 1;
		this.variable = variable;
		this.expression = expression;
	}

	public SymbolicConstantExpression variable() {
		return variable;
	}

	public TreeExpressionIF expression() {
		return expression;
	}

	@Override
	protected boolean intrinsicEquals(CommonSymbolicExpression that) {
		return that instanceof LambdaExpression
				&& variable.equals(((LambdaExpression) that).variable)
				&& expression.equals(((LambdaExpression) that).expression);
	}

	@Override
	protected int intrinsicHashCode() {
		return classHashCode + variable.hashCode() + expression.hashCode();
	}

	@Override
	public String toString() {
		return "lambda(" + variable() + ")." + expression().atomString();

	}

	@Override
	public String atomString() {
		return "(" + toString() + ")";
	}

	@Override
	public TreeExpressionIF argument(int index) {
		switch (index) {
		case 0:
			return variable;
		case 1:
			return expression;
		default:
			throw new RuntimeException("numArguments=" + 2 + ", index=" + index);
		}
	}

	@Override
	public SymbolicOperator operator() {
		return SymbolicOperator.LAMBDA;
	}

	@Override
	public int numArguments() {
		return 2;
	}

	@Override
	public NumericPrimitiveKind numericPrimitiveKind() {
		return NumericPrimitiveKind.LAMBDA;
	}

	@Override
	public BooleanPrimitiveKind booleanPrimitiveKind() {
		return BooleanPrimitiveKind.LAMBDA;
	}

	@Override
	public SymbolicFunctionTypeIF type() {
		return (SymbolicFunctionTypeIF) super.type();
	}

}
