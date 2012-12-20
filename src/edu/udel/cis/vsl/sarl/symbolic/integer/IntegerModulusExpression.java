package edu.udel.cis.vsl.sarl.symbolic.integer;

import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpression;

public class IntegerModulusExpression extends SymbolicExpression implements
		NumericPrimitive {

	public final static String operator = "%";

	private TreeExpressionIF numerator;

	private TreeExpressionIF denominator;

	IntegerModulusExpression(TreeExpressionIF numerator,
			TreeExpressionIF denominator) {
		super(numerator.type());
		assert denominator != null;
		assert numerator.type().kind() == SymbolicTypeKind.INTEGER;
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
		return IntegerModulusExpression.class.hashCode() + numerator.hashCode()
				+ denominator.hashCode();
	}

	protected boolean intrinsicEquals(SymbolicExpression expression) {
		if (expression instanceof IntegerModulusExpression) {
			IntegerModulusExpression that = (IntegerModulusExpression) expression;

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
		return NumericPrimitiveKind.INT_MOD;
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

	public SymbolicKind kind() {
		return SymbolicKind.MODULO;
	}

	public int numArguments() {
		return 2;
	}

}
