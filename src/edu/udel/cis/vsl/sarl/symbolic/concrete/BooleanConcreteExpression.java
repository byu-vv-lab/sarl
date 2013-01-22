package edu.udel.cis.vsl.sarl.symbolic.concrete;

import edu.udel.cis.vsl.sarl.IF.BooleanConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;

public class BooleanConcreteExpression extends CommonSymbolicExpression implements
		BooleanConcreteExpressionIF {

	private Boolean value;

	BooleanConcreteExpression(SymbolicTypeIF booleanType, Boolean value) {
		super(booleanType);
		assert value != null;
		this.value = value;
	}

	public Boolean value() {
		return value;
	}

	protected int intrinsicHashCode() {
		return BooleanConcreteExpression.class.hashCode() + (value ? 1 : 0);
	}

	protected boolean intrinsicEquals(CommonSymbolicExpression that) {
		return that instanceof BooleanConcreteExpression
				&& ((BooleanConcreteExpression) that).value.equals(value);
	}

	public String toString() {
		return (value ? "true" : "false");
	}

	public String atomString() {
		return toString();
	}

	public TreeExpressionIF argument(int index) {
		throw new IllegalArgumentException("0 arguments");
	}

	public SymbolicOperator operator() {
		return SymbolicOperator.CONCRETE_BOOLEAN;
	}

	public int numArguments() {
		return 0;
	}

}
