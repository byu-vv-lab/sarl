package edu.udel.cis.vsl.sarl.symbolic.cnf;

import edu.udel.cis.vsl.sarl.IF.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.BooleanPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;

/**
 * A literal boolean expression is an expression of the form p or an expression
 * of the form !p, where p is a primitive expression.
 * 
 * @author siegel
 */
public class LiteralExpression extends CommonSymbolicExpression implements
		BasicExpression {

	private boolean not;

	private BooleanPrimitive primitive;

	LiteralExpression(SymbolicTypeIF booleanType, boolean not,
			BooleanPrimitive primitive) {
		super(booleanType);
		assert primitive != null;
		assert booleanType.equals(primitive.type());
		this.primitive = primitive;
		this.not = not;
	}

	public boolean not() {
		return not;
	}

	public BooleanPrimitive primitive() {
		return primitive;
	}

	protected int intrinsicHashCode() {
		return LiteralExpression.class.hashCode()
				+ (not ? -primitive.hashCode() : primitive.hashCode());
	}

	protected boolean intrinsicEquals(CommonSymbolicExpression expression) {
		if (expression instanceof LiteralExpression) {
			LiteralExpression that = (LiteralExpression) expression;

			return not == that.not && primitive.equals(that.primitive);
		}
		return false;
	}

	public String toString() {
		if (not) {
			return "!" + primitive.atomString();
		} else {
			return primitive.toString();
		}
	}

	public String atomString() {
		if (not) {
			return "(!" + primitive.atomString() + ")";
		} else {
			return primitive.atomString();
		}
	}

	/**
	 * From the point of view of the Tree interface, if "not" is "true", this is
	 * a tree with one child (primitive) and the operator is "not". If "not" is
	 * "false", this is exactly the same as the primitive itself, so this
	 * LiteralExpression become totally invisible.
	 */
	public TreeExpressionIF argument(int index) {
		if (not) {
			return primitive;
		} else {
			return primitive.argument(index);
		}
	}

	public SymbolicOperator operator() {
		if (not) {
			return SymbolicOperator.NOT;
		} else {
			return primitive.operator();
		}
	}

	public int numArguments() {
		if (not)
			return 1;
		else
			return primitive.numArguments();
	}

}
