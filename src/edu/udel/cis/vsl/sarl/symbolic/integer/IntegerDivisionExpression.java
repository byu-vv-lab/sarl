package edu.udel.cis.vsl.sarl.symbolic.integer;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;

public class IntegerDivisionExpression extends CommonSymbolicExpression implements
		NumericPrimitive {

	// option-? gives Ö in Eclipse. What unicode is this?
	public final static String operator = "Ö";

	private TreeExpressionIF numerator;

	private TreeExpressionIF denominator;

	IntegerDivisionExpression(TreeExpressionIF numerator,
			TreeExpressionIF denominator) {
		super(numerator.type());
		assert denominator != null;
		assert numerator.type().operator() == SymbolicTypeKind.INTEGER;
		assert numerator.type().equals(denominator.type());
		this.numerator = numerator;
		this.denominator = denominator;
	}

	public TreeExpressionIF numerator() {
		return numerator;
	}

	public TreeExpressionIF denominator() {
		return denominator;
	}

	protected int intrinsicHashCode() {
		return IntegerDivisionExpression.class.hashCode()
				+ numerator.hashCode() + denominator.hashCode();
	}

	protected boolean intrinsicEquals(CommonSymbolicExpression expression) {
		if (expression instanceof IntegerDivisionExpression) {
			IntegerDivisionExpression that = (IntegerDivisionExpression) expression;

			return numerator.equals(that.numerator)
					&& denominator.equals(that.denominator);
		}
		return false;
	}

	public String toString() {
		return numerator.atomString() + operator + denominator.atomString();
	}

	public String atomString() {
		return "(" + toString() + ")";
	}

	public NumericPrimitiveKind numericPrimitiveKind() {
		return NumericPrimitiveKind.INT_DIV;
	}

	public TreeExpressionIF argument(int index) {
		switch (index) {
		case 0:
			return numerator;
		case 1:
			return denominator;
		default:
			throw new RuntimeException("numArguments=" + 2 + ", index=" + index);
		}
	}

	public SymbolicOperator operator() {
		return SymbolicOperator.INT_DIVIDE;
	}

	public int numArguments() {
		return 2;
	}

}
