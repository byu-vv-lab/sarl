package edu.udel.cis.vsl.sarl.symbolic.array;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;

/**
 * An array lambda expression wraps a lambda expression with integer domain to
 * represent an array. The type must be a complete array type, T[N]. The lambda
 * expression must have type "function from int to T".
 */
public class ArrayLambdaExpression extends CommonSymbolicExpression implements
		TreeExpressionIF {

	private static int classHashCode = ArrayLambdaExpression.class.hashCode();

	private TreeExpressionIF function;

	ArrayLambdaExpression(SymbolicCompleteArrayTypeIF arrayType,
			TreeExpressionIF function) {
		super(arrayType);

		SymbolicFunctionTypeIF functionType = (SymbolicFunctionTypeIF) function
				.type();

		assert functionType.outputType().equals(arrayType.elementType());
		assert functionType.numInputs() == 1;
		assert functionType.inputType(0).isInteger();
		this.function = function;
	}

	public TreeExpressionIF function() {
		return function;
	}

	@Override
	public TreeExpressionIF argument(int index) {
		switch (index) {
		case 0:
			return function;
		default:
			throw new RuntimeException(
					"ArrayLambdaExpression has one argument: index=" + index);
		}
	}

	@Override
	public SymbolicOperator operator() {
		return SymbolicOperator.ARRAY_LAMBDA;
	}

	@Override
	public int numArguments() {
		return 1;
	}

	@Override
	public String atomString() {
		return function.atomString();
	}

	@Override
	public String toString() {
		return atomString();
	}

	@Override
	public SymbolicCompleteArrayTypeIF type() {
		return (SymbolicCompleteArrayTypeIF) super.type();
	}

	@Override
	protected boolean intrinsicEquals(CommonSymbolicExpression that) {
		return that instanceof ArrayLambdaExpression
				&& function.equals(((ArrayLambdaExpression) that).function);
	}

	@Override
	protected int intrinsicHashCode() {
		return classHashCode + function.hashCode();
	}

}
