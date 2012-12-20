package edu.udel.cis.vsl.sarl.symbolic.relation;

import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.cnf.BasicExpression;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpression;

// there is a need to store 0 to implement the SymbolicExpression interface GT
// add to interface GT0?

public class RelationalExpression extends SymbolicExpression implements
		BasicExpression {

	public enum RelationKind {
		GT0, EQ0, GTE0, NEQ0
	};

	private RelationKind relationKind;

	private TreeExpressionIF expression;

	private TreeExpressionIF zero;

	RelationalExpression(SymbolicTypeIF booleanType, RelationKind kind,
			TreeExpressionIF expression, TreeExpressionIF zero) {
		super(booleanType);
		assert kind != null;
		assert expression != null;
		assert zero != null;
		assert zero.type().equals(expression.type());
		this.relationKind = kind;
		this.expression = expression;
		this.zero = zero;
	}

	public RelationKind relationKind() {
		return relationKind;
	}

	public TreeExpressionIF expression() {
		return expression;
	}

	protected int intrinsicHashCode() {
		return RelationalExpression.class.hashCode() + expression.hashCode()
				+ relationKind.hashCode();
	}

	protected boolean intrinsicEquals(SymbolicExpression other) {
		if (other instanceof RelationalExpression) {
			RelationalExpression that = (RelationalExpression) other;

			return relationKind.equals(that.relationKind)
					&& expression.equals(that.expression);
		}
		return false;
	}

	public String toString() {
		String result = expression.toString();

		switch (relationKind) {
		case EQ0:
			result += " = 0";
			break;
		case NEQ0:
			result += " != 0";
			break;
		case GTE0:
			result += " >= 0";
			break;
		case GT0:
			result += " > 0 ";
			break;
		default:
			throw new IllegalArgumentException("Unknown relational operator: "
					+ relationKind);
		}
		return result;
	}

	public String atomString() {
		return toString();
	}

	public TreeExpressionIF argument(int index) {
		if (index < 0 || index > 1)
			throw new IllegalArgumentException("numArguments=" + 2 + ", index="
					+ index);
		switch (relationKind) {
		case EQ0:
		case NEQ0:
			return (index == 0 ? expression : zero);
		case GT0:
		case GTE0:
			return (index == 0 ? zero : expression);
		default:
			throw new IllegalArgumentException("Unknown relational operator: "
					+ relationKind);
		}
	}

	public SymbolicKind kind() {
		switch (relationKind) {
		case EQ0:
			return SymbolicKind.EQUALS;
		case NEQ0:
			return SymbolicKind.NEQ;
		case GT0:
			return SymbolicKind.LESS_THAN;
		case GTE0:
			return SymbolicKind.LESS_THAN_EQUALS;
		default:
			throw new IllegalArgumentException("Unknown relation kind: "
					+ relationKind);
		}
	}

	public int numArguments() {
		return 2;
	}

}
