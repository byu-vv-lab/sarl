package edu.udel.cis.vsl.sarl.symbolic.array;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.symbolic.BooleanPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;

public class ArrayRead extends CommonSymbolicExpression implements NumericPrimitive,
		BooleanPrimitive, TreeExpressionIF {

	private TreeExpressionIF array;

	private TreeExpressionIF index;

	ArrayRead(TreeExpressionIF array, TreeExpressionIF index) {
		super(((SymbolicArrayTypeIF) array.type()).elementType());
		this.array = array;
		assert index != null;
		assert index.type().operator() == SymbolicTypeKind.INTEGER;
		this.index = index;
	}

	public TreeExpressionIF array() {
		return array;
	}

	public TreeExpressionIF index() {
		return index;
	}

	protected int intrinsicHashCode() {
		return ArrayRead.class.hashCode() + array.hashCode() + index.hashCode();
	}

	protected boolean intrinsicEquals(CommonSymbolicExpression expression) {
		if (expression instanceof ArrayRead) {
			return array.equals(((ArrayRead) expression).array)
					&& index.equals(((ArrayRead) expression).index);
		}
		return false;
	}

	public String toString() {
		return array.atomString() + "[" + index + "]";
	}

	public String atomString() {
		return toString();
	}

	public NumericPrimitiveKind numericPrimitiveKind() {
		return NumericPrimitiveKind.ARRAY_READ;
	}

	public BooleanPrimitiveKind booleanPrimitiveKind() {
		return BooleanPrimitiveKind.ARRAY_READ;
	}

	public TreeExpressionIF argument(int index) {
		switch (index) {
		case 0:
			return array;
		case 1:
			return this.index;
		default:
			throw new RuntimeException("numArguments=" + 2 + ", index=" + index);
		}
	}

	public SymbolicOperator operator() {
		return SymbolicOperator.ARRAY_READ;
	}

	public int numArguments() {
		return 2;
	}

}
