package edu.udel.cis.vsl.sarl.symbolic.array;

import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpression;

public class ArrayLength extends SymbolicExpression implements
		NumericPrimitive, TreeExpressionIF {

	private TreeExpressionIF array;

	ArrayLength(TreeExpressionIF array, SymbolicTypeIF integerType) {
		super(integerType);
		this.array = array;
	}

	public TreeExpressionIF array() {
		return array;
	}

	@Override
	public String atomString() {
		return "length(" + array + ")";
	}

	@Override
	public SymbolicKind kind() {
		return SymbolicKind.LENGTH;
	}

	@Override
	public TreeExpressionIF argument(int index) {
		switch (index) {
		case 0:
			return array;
		default:
			throw new RuntimeException("numArguments=" + 1 + ", index=" + index);
		}
	}

	@Override
	public int numArguments() {
		return 1;
	}

	@Override
	public NumericPrimitiveKind numericPrimitiveKind() {
		return NumericPrimitiveKind.LENGTH;
	}

	@Override
	protected int intrinsicHashCode() {
		return ArrayRead.class.hashCode() + array.hashCode();
	}

	@Override
	protected boolean intrinsicEquals(SymbolicExpression that) {
		if (that instanceof ArrayLength) {
			return array.equals(((ArrayLength) that).array);
		}
		return false;
	}

}
