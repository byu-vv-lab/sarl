package edu.udel.cis.vsl.sarl.symbolic.tuple;

import java.util.Arrays;

import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTupleTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpression;

public class Tuple extends SymbolicExpression implements TreeExpressionIF {

	private TreeExpressionIF[] components;

	public Tuple(SymbolicTupleTypeIF tupleType, TreeExpressionIF[] components) {
		super(tupleType);
		this.components = components;
	}

	public SymbolicTupleTypeIF type() {
		return (SymbolicTupleTypeIF) super.type();
	}

	@Override
	protected boolean intrinsicEquals(SymbolicExpression expression) {
		if (expression instanceof Tuple) {
			Tuple that = (Tuple) expression;

			return type().equals(that.type())
					&& Arrays.equals(components, that.components);
		}
		return false;
	}

	@Override
	protected int intrinsicHashCode() {
		return type().hashCode() + Arrays.hashCode(components);
	}

	public String atomString() {
		String result = "<";

		for (int i = 0; i < components.length; i++) {
			TreeExpressionIF component = components[i];

			if (i > 0)
				result += ",";
			result += component;
		}
		result += ">";
		return result;
	}

	public String toString() {
		return atomString();
	}

	public TreeExpressionIF[] components() {
		return components;
	}

	public TreeExpressionIF argument(int index) {
		return components[index];
	}

	public SymbolicKind kind() {
		return SymbolicKind.CONCRETE_TUPLE;
	}

	public int numArguments() {
		return components.length;
	}

}
