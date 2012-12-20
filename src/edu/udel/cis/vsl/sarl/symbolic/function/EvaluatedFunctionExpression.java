package edu.udel.cis.vsl.sarl.symbolic.function;

import java.util.Arrays;

import edu.udel.cis.vsl.sarl.symbolic.BooleanPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicFunctionTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpression;

/**
 * An object of this class represents an expression of the form
 * f(x_{0},...,x_{n-1}), i.e., the application of a function f to a list of n
 * arguments.
 */
public class EvaluatedFunctionExpression extends SymbolicExpression implements
		NumericPrimitive, BooleanPrimitive {

	/** The function f */
	private TreeExpressionIF function;

	/** The list of arguments. */
	private TreeExpressionIF[] arguments;

	/**
	 * Constructs new evaluated function expression using the given given
	 * function and arguments array. The type of function must be an instance of
	 * SymbolicFunctionTypeIF. The type of this expression is the output type of
	 * the function. The array arguments may not be null, but it may have length
	 * 0. The types of the arguments must correspond (by equals) to the inputs
	 * types of the function.
	 */
	EvaluatedFunctionExpression(TreeExpressionIF function,
			TreeExpressionIF[] arguments) {
		super(((SymbolicFunctionTypeIF) function.type()).outputType());
		assert arguments != null;

		int numArgs = arguments.length;
		SymbolicFunctionTypeIF functionType = (SymbolicFunctionTypeIF) function
				.type();

		this.function = function;
		if (numArgs != functionType.numInputs()) {
			throw new IllegalArgumentException("Wrong number of arguments to "
					+ function + ": saw " + numArgs + ", expected "
					+ functionType.numInputs());
		}
		for (int i = 0; i < numArgs; i++) {
			SymbolicTypeIF argumentType = arguments[i].type();
			SymbolicTypeIF expectedType = functionType.inputType(i);

			if (!argumentType.equals(expectedType)) {
				throw new IllegalArgumentException(
						"Error in application of function " + function
								+ " argument " + i + ": expected type "
								+ expectedType + ", saw type " + argumentType);
			}
		}
		this.arguments = arguments;
	}

	/** Returns the function f. */
	public TreeExpressionIF function() {
		return function;
	}

	/** Returns the i-th argument, counting from 0. */
	public TreeExpressionIF functionArgument(int index) {
		return arguments[index];
	}

	/** Returns the array of arguments. Don't modify this array. */
	public TreeExpressionIF[] arguments() {
		return arguments;
	}

	public String atomString() {
		String result = function.atomString() + "(";
		int numArgs = arguments.length;

		for (int i = 0; i < numArgs; i++) {
			if (i > 0)
				result += ", ";
			result += arguments[i].toString();
		}
		result += ")";
		return result;
	}

	public String toString() {
		return atomString();
	}

	protected int intrinsicHashCode() {
		return function.hashCode() + Arrays.hashCode(arguments);
	}

	protected boolean intrinsicEquals(SymbolicExpression thatExpression) {
		if (thatExpression instanceof EvaluatedFunctionExpression) {
			EvaluatedFunctionExpression that = (EvaluatedFunctionExpression) thatExpression;

			return function.equals(that.function)
					&& Arrays.equals(arguments, that.arguments);
		}
		return false;
	}

	public NumericPrimitiveKind numericPrimitiveKind() {
		return NumericPrimitiveKind.APPLY;
	}

	public BooleanPrimitiveKind booleanPrimitiveKind() {
		return BooleanPrimitiveKind.APPLY;
	}

	public TreeExpressionIF argument(int index) {
		if (index == 0) {
			return function;
		} else {
			return arguments[index - 1];
		}
	}

	public SymbolicKind kind() {
		return SymbolicKind.APPLY;
	}

	public int numArguments() {
		return arguments.length + 1;
	}

}
