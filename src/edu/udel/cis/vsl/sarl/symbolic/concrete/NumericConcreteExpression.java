package edu.udel.cis.vsl.sarl.symbolic.concrete;

import edu.udel.cis.vsl.sarl.number.IF.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.number.IF.NumberIF;
import edu.udel.cis.vsl.sarl.number.IF.RationalNumberIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpression;

public class NumericConcreteExpression extends SymbolicExpression implements
		NumericConcreteExpressionIF {

	private NumberIF value;

	private static int classHashCode = NumericConcreteExpression.class
			.hashCode();

	NumericConcreteExpression(SymbolicTypeIF numericType, NumberIF value) {
		super(numericType);
		assert value != null;
		this.value = value;
	}

	public NumberIF value() {
		return value;
	}

	protected int intrinsicHashCode() {
		return classHashCode + value.hashCode();
	}

	protected boolean intrinsicEquals(SymbolicExpression that) {
		return that instanceof NumericConcreteExpression
				&& ((NumericConcreteExpression) that).value.equals(value);
	}

	public int signum() {
		return value.signum();
	}

	public String toString() {
		return value.toString();
	}

	public String atomString() {
		return value.atomString();
	}

	public boolean isZero() {
		return value.signum() == 0;
	}

	public boolean isOne() {
		return value.isOne();
	}

	public boolean isInteger() {
		return value instanceof IntegerNumberIF;
	}

	public boolean isReal() {
		return value instanceof RationalNumberIF;
	}

	public TreeExpressionIF argument(int index) {
		throw new IllegalArgumentException("0 arguments");
	}

	public SymbolicKind kind() {
		return SymbolicKind.CONCRETE_NUMBER;
	}

	public int numArguments() {
		return 0;
	}
}
