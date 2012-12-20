package edu.udel.cis.vsl.sarl.symbolic.array;

import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpression;

public class ArrayWrite extends SymbolicExpression implements TreeExpressionIF {

	private TreeExpressionIF array;

	private TreeExpressionIF index;

	private TreeExpressionIF value;

	ArrayWrite(TreeExpressionIF array, TreeExpressionIF index,
			TreeExpressionIF value) {
		super(array.type());
		this.array = array;
		this.index = index;
		this.value = value;
	}

	public TreeExpressionIF array() {
		return array;
	}

	public TreeExpressionIF index() {
		return index;
	}

	public TreeExpressionIF value() {
		return value;
	}

	protected boolean intrinsicEquals(SymbolicExpression expression) {
		if (expression instanceof ArrayWrite) {
			ArrayWrite that = (ArrayWrite) expression;

			return array.equals(that.array) && index.equals(that.index)
					&& value.equals(that.value);
		}
		return false;
	}

	protected int intrinsicHashCode() {
		return ArrayWrite.class.hashCode() + array.hashCode()
				+ index.hashCode() + value.hashCode();
	}

	public String toString() {
		String result = array.toString();

		result += " WITH [" + index + "] := " + value;
		return result;
	}

	public String atomString() {
		return "(" + toString() + ")";
	}

	public TreeExpressionIF argument(int index) {
		switch (index) {
		case 0:
			return array;
		case 1:
			return this.index;
		case 2:
			return value;
		default:
			throw new RuntimeException("numArguments=" + 3 + ", index=" + index);
		}
	}

	public SymbolicKind kind() {
		return SymbolicKind.ARRAY_WRITE;
	}

	public int numArguments() {
		return 3;
	}

}
