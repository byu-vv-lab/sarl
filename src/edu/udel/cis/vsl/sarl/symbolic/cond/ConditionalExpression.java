package edu.udel.cis.vsl.sarl.symbolic.cond;

import edu.udel.cis.vsl.sarl.expr.common.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;

public class ConditionalExpression extends CommonSymbolicExpression implements
		NumericPrimitive {

	private TreeExpressionIF predicate;

	private TreeExpressionIF trueValue;

	private TreeExpressionIF falseValue;

	ConditionalExpression(TreeExpressionIF predicate,
			TreeExpressionIF trueValue, TreeExpressionIF falseValue) {
		super(trueValue.type());
		assert type().equals(falseValue.type());
		assert predicate.type().isBoolean();
		this.predicate = predicate;
		this.trueValue = trueValue;
		this.falseValue = falseValue;
	}

	public TreeExpressionIF predicate() {
		return predicate;
	}

	public TreeExpressionIF trueValue() {
		return trueValue;
	}

	public TreeExpressionIF falseValue() {
		return falseValue;
	}

	@Override
	protected boolean intrinsicEquals(CommonSymbolicExpression expression) {
		if (expression instanceof ConditionalExpression) {
			ConditionalExpression that = (ConditionalExpression) expression;

			return predicate.equals(that.predicate)
					&& trueValue.equals(that.trueValue)
					&& falseValue.equals(that.falseValue);
		}
		return false;
	}

	@Override
	protected int intrinsicHashCode() {
		return predicate.hashCode() + trueValue.hashCode()
				+ falseValue.hashCode();
	}

	public String toString() {
		return "(" + predicate + " ? " + trueValue + " : " + falseValue + ")";
	}

	public String atomString() {
		return toString();
	}

	public NumericPrimitiveKind numericPrimitiveKind() {
		return NumericPrimitiveKind.COND;
	}

	public TreeExpressionIF argument(int index) {
		switch (index) {
		case 0:
			return predicate;
		case 1:
			return trueValue;
		case 2:
			return falseValue;
		default:
			throw new RuntimeException("numArguments=" + 3 + ", index=" + index);
		}
	}

	public SymbolicOperator operator() {
		return SymbolicOperator.COND;
	}

	public int numArguments() {
		return 3;
	}

}
