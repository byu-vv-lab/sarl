package edu.udel.cis.vsl.sarl.symbolic.tuple;

import edu.udel.cis.vsl.sarl.IF.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.expr.common.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;

public class TupleWrite extends CommonSymbolicExpression implements TreeExpressionIF {

	private TreeExpressionIF tuple;

	private NumericConcreteExpressionIF index;

	private TreeExpressionIF value;

	TupleWrite(TreeExpressionIF tuple, NumericConcreteExpressionIF index,
			TreeExpressionIF value) {
		super(tuple.type());
		assert tuple != null;
		assert index.signum() >= 0;
		assert value != null;
		this.tuple = tuple;
		this.index = index;
		this.value = value;
	}

	public TreeExpressionIF tuple() {
		return tuple;
	}

	public NumericConcreteExpressionIF index() {
		return index;
	}

	public TreeExpressionIF value() {
		return value;
	}

	public String toString() {
		return tuple + " WITH [" + index + "]:=" + value;
	}

	public String atomString() {
		return "(" + toString() + ")";
	}

	protected int intrinsicHashCode() {
		return TupleWrite.class.hashCode() + tuple.hashCode()
				+ value.hashCode() + index.hashCode();
	}

	protected boolean intrinsicEquals(CommonSymbolicExpression expression) {
		if (expression instanceof TupleWrite) {
			TupleWrite that = (TupleWrite) expression;

			return tuple.equals(that.tuple) && index.equals(that.index)
					&& value.equals(that.value);
		}
		return false;
	}

	public TreeExpressionIF argument(int index) {
		switch (index) {
		case 0:
			return tuple;
		case 1:
			return this.index;
		case 2:
			return value;
		default:
			throw new IllegalArgumentException("numArguments=3, index=" + index);
		}
	}

	public SymbolicOperator operator() {
		return SymbolicOperator.TUPLE_WRITE;
	}

	public int numArguments() {
		return 3;
	}
}
