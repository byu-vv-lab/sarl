package edu.udel.cis.vsl.sarl.symbolic.tuple;

import edu.udel.cis.vsl.sarl.symbolic.BooleanPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.IF.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleTypeIF;

public class TupleRead extends CommonSymbolicExpression implements NumericPrimitive,
		BooleanPrimitive {

	private TreeExpressionIF tupleExpression;

	private NumericConcreteExpressionIF indexExpression;

	TupleRead(TreeExpressionIF tupleExpression,
			NumericConcreteExpressionIF indexExpression) {
		super(((SymbolicTupleTypeIF) tupleExpression.type())
				.fieldType(((IntegerNumberIF) indexExpression.value())
						.intValue()));
		this.tupleExpression = tupleExpression;
		this.indexExpression = indexExpression;
	}

	public NumericConcreteExpressionIF index() {
		return indexExpression;
	}

	public TreeExpressionIF tuple() {
		return tupleExpression;
	}

	public String toString() {
		return tupleExpression.atomString() + "." + indexExpression;
	}

	public String atomString() {
		return "(" + toString() + ")";
	}

	protected int intrinsicHashCode() {
		return TupleRead.class.hashCode() + tupleExpression.hashCode()
				+ indexExpression.hashCode();
	}

	protected boolean intrinsicEquals(CommonSymbolicExpression expression) {
		if (expression instanceof TupleRead) {
			return tupleExpression
					.equals(((TupleRead) expression).tupleExpression)
					&& indexExpression
							.equals(((TupleRead) expression).tupleExpression);
		}
		return false;
	}

	public NumericPrimitiveKind numericPrimitiveKind() {
		return NumericPrimitiveKind.TUPLE_READ;
	}

	public BooleanPrimitiveKind booleanPrimitiveKind() {
		return BooleanPrimitiveKind.TUPLE_READ;
	}

	public TreeExpressionIF argument(int index) {
		switch (index) {
		case 0:
			return tupleExpression;
		case 1:
			return indexExpression;
		default:
			throw new IllegalArgumentException("numArguments=" + 2 + ", index="
					+ index);
		}
	}

	public SymbolicOperator operator() {
		return SymbolicOperator.TUPLE_READ;
	}

	public int numArguments() {
		return 2;
	}
}
