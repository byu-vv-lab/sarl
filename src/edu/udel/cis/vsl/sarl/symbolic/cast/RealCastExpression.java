package edu.udel.cis.vsl.sarl.symbolic.cast;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;

public class RealCastExpression extends CommonSymbolicExpression implements
		NumericPrimitive {

	private NumericPrimitive integerExpression;

	public RealCastExpression(SymbolicTypeIF realType,
			NumericPrimitive integerExpression) {
		super(realType);
		this.integerExpression = integerExpression;
	}

	public NumericPrimitive integerExpression() {
		return integerExpression;
	}

	@Override
	protected boolean intrinsicEquals(CommonSymbolicExpression that) {
		return that instanceof RealCastExpression
				&& integerExpression
						.equals(((RealCastExpression) that).integerExpression);
	}

	@Override
	protected int intrinsicHashCode() {
		return RealCastExpression.class.hashCode()
				+ integerExpression.hashCode();
	}

	public String toString() {
		return "(real)" + integerExpression.atomString();
	}

	public String atomString() {
		return toString();
	}

	public NumericPrimitiveKind numericPrimitiveKind() {
		return NumericPrimitiveKind.CAST;
	}

	public TreeExpressionIF argument(int index) {
		switch (index) {
		case 0:
			return integerExpression;
		default:
			throw new RuntimeException("numArguments=" + 1 + ", index=" + index);
		}
	}

	public SymbolicOperator operator() {
		return SymbolicOperator.CAST;
	}

	public int numArguments() {
		return 1;
	}

}
