package edu.udel.cis.vsl.sarl.symbolic.power;

import edu.udel.cis.vsl.sarl.symbolic.IF.tree.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpression;

public class PowerExpression extends SymbolicExpression implements
		TreeExpressionIF {

	private TreeExpressionIF base;

	/** Exponent must be non-negative concrete integer. */
	private NumericConcreteExpressionIF exponent;

	PowerExpression(TreeExpressionIF base, NumericConcreteExpressionIF exponent) {
		super(base.type());

		assert type().isNumeric();
		assert exponent.type().isInteger();
		assert exponent.value().signum() >= 0;
		this.base = base;
		this.exponent = exponent;
	}

	@Override
	protected boolean intrinsicEquals(SymbolicExpression expression) {
		if (expression instanceof PowerExpression) {
			PowerExpression that = (PowerExpression) expression;

			return base.equals(that.base) && exponent.equals(that.exponent);
		}
		return false;
	}

	@Override
	protected int intrinsicHashCode() {

		assert base != null;
		assert exponent != null;
		assert PowerExpression.class != null;

		return PowerExpression.class.hashCode() + base.hashCode()
				+ exponent.hashCode();
	}

	public String toString() {
		if (exponent.isOne()) {
			return base.toString();
		} else {
			return base.atomString() + "^" + exponent.atomString();
		}
	}

	public String atomString() {
		if (exponent.isOne()) {
			return base.atomString();
		} else {
			return toString();
		}
	}

	public SymbolicKind kind() {
		return SymbolicKind.POWER;
	}

	public int numArguments() {
		return 2;
	}

	public TreeExpressionIF argument(int index) {
		switch (index) {
		case 0:
			return base;
		case 1:
			return exponent;
		default:
			throw new RuntimeException("numArguments=" + 2 + ", index=" + index);
		}
	}

	public NumericConcreteExpressionIF exponent() {
		return exponent;
	}

	public TreeExpressionIF base() {
		return base;
	}

}
