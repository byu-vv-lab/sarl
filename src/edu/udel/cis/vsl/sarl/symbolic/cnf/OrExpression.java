package edu.udel.cis.vsl.sarl.symbolic.cnf;

import java.util.Arrays;

import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpression;

public class OrExpression extends SymbolicExpression implements
		TreeExpressionIF {

	private BasicExpression[] clauses;

	OrExpression(SymbolicTypeIF booleanType, BasicExpression[] clauses) {
		super(booleanType);
		assert clauses != null;
		this.clauses = clauses;
	}

	protected int intrinsicHashCode() {
		return OrExpression.class.hashCode() + Arrays.hashCode(clauses);
	}

	protected boolean intrinsicEquals(SymbolicExpression expression) {
		return expression instanceof OrExpression
				&& Arrays.equals(clauses, ((OrExpression) expression).clauses);
	}

	public int numClauses() {
		return clauses.length;
	}

	public BasicExpression clause(int index) {
		return clauses[index];
	}

	public String toString() {
		int numClauses = clauses.length;

		if (numClauses == 0) {
			return "false";
		} else if (numClauses == 1) {
			return clauses[0].toString();
		} else {
			String result = "";

			for (int i = 0; i < clauses.length; i++) {
				if (i > 0)
					result += " || ";
				result += clauses[i].atomString();
			}
			return result;
		}
	}

	public String atomString() {
		int numClauses = clauses.length;

		if (numClauses == 0) {
			return "false";
		} else if (numClauses == 1) {
			return clauses[0].atomString();
		} else {
			String result = "(";

			for (int i = 0; i < clauses.length; i++) {
				if (i > 0)
					result += " || ";
				result += clauses[i].atomString();
			}
			result += ")";
			return result;
		}
	}

	public TreeExpressionIF argument(int index) {
		return clauses[index];
	}

	public SymbolicKind kind() {
		return SymbolicKind.OR;
	}

	public int numArguments() {
		return clauses.length;
	}
}
