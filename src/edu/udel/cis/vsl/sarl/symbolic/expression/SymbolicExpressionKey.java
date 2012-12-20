package edu.udel.cis.vsl.sarl.symbolic.expression;

public class SymbolicExpressionKey<T extends SymbolicExpression> {

	private T expression;

	public SymbolicExpressionKey(T expression) {
		assert expression != null;
		this.expression = expression;
	}

	public boolean equals(Object object) {
		if (object instanceof SymbolicExpressionKey<?>) {
			return expression
					.intrinsicEquals(((SymbolicExpressionKey<?>) object).expression);
		}
		return false;
	}

	public int hashCode() {
		return expression.intrinsicHashCode();
	}
}
