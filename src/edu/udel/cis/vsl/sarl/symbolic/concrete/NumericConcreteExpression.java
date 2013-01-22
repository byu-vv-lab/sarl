package edu.udel.cis.vsl.sarl.symbolic.concrete;

import edu.udel.cis.vsl.sarl.IF.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.NumberIF;
import edu.udel.cis.vsl.sarl.IF.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.IF.RationalNumberIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;

public class NumericConcreteExpression extends CommonSymbolicExpression implements
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

	protected boolean intrinsicEquals(CommonSymbolicExpression that) {
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

	public SymbolicOperator operator() {
		return SymbolicOperator.CONCRETE_NUMBER;
	}

	public int numArguments() {
		return 0;
	}
}
