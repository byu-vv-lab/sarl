package edu.udel.cis.vsl.sarl.symbolic.array;

import java.util.Arrays;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayTypeIF;
import edu.udel.cis.vsl.sarl.expr.common.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;

public class ArrayExpression extends CommonSymbolicExpression implements
		TreeExpressionIF {

	private TreeExpressionIF origin;

	private TreeExpressionIF[] elements;

	private ArrayFactory arrayFactory;

	private TreeExpressionIF treeExpression = null;

	protected ArrayExpression(TreeExpressionIF origin,
			TreeExpressionIF[] elements, ArrayFactory arrayFactory) {
		super(origin.type());
		assert elements != null;
		assert origin.type() instanceof SymbolicArrayTypeIF;
		this.origin = origin;
		this.elements = elements;
		this.arrayFactory = arrayFactory;
	}

	public TreeExpressionIF[] elements() {
		return elements;
	}

	public TreeExpressionIF origin() {
		return origin;
	}

	@Override
	protected boolean intrinsicEquals(CommonSymbolicExpression expression) {
		if (expression instanceof ArrayExpression) {
			ArrayExpression that = (ArrayExpression) expression;

			return origin.equals(that.origin) && elements.equals(that.elements);
		}
		return false;
	}

	@Override
	protected int intrinsicHashCode() {
		return origin.hashCode() + Arrays.hashCode(elements);
	}

	private void computeTreeExpression() {
		if (treeExpression != null)
			return;
		treeExpression = origin;
		int length = elements.length;
		for (int i = 0; i < length; i++) {
			TreeExpressionIF element = elements[i];

			if (element != null) {
				treeExpression = arrayFactory.arrayWrite(treeExpression, i,
						element);
			}
		}
	}

	@Override
	public TreeExpressionIF argument(int index) {
		computeTreeExpression();
		return treeExpression.argument(index);
	}

	@Override
	public SymbolicOperator operator() {
		computeTreeExpression();
		return treeExpression.operator();
	}

	@Override
	public int numArguments() {
		computeTreeExpression();
		return treeExpression.numArguments();
	}

	@Override
	public String atomString() {
		return toString();
	}

	@Override
	public String toString() {
		String result = origin.atomString();
		boolean first = true;

		result += "{";
		for (TreeExpressionIF element : elements) {
			if (first)
				first = false;
			else
				result += ",";
			result += element;
		}
		result += "}";
		return result;
	}

	@Override
	public SymbolicArrayTypeIF type() {
		return (SymbolicArrayTypeIF) super.type();
	}

}
