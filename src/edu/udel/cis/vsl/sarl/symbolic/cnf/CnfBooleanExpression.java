package edu.udel.cis.vsl.sarl.symbolic.cnf;

import java.util.Arrays;

import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpression;

/**
 * A CnfBooleanExpression is the root of the canonical form for boolean
 * expressions. It represents the conjunction (and) of a set of clauses. Each of
 * those clauses is a disjunction (or).
 * 
 * @author siegel
 * 
 */
public class CnfBooleanExpression extends SymbolicExpression implements
		TreeExpressionIF {

	private static int classCode = CnfBooleanExpression.class.hashCode();

	private OrExpression[] clauses;

	CnfBooleanExpression(SymbolicTypeIF booleanType, OrExpression[] clauses) {
		super(booleanType);
		assert clauses != null;
		this.clauses = clauses;
	}

	protected int intrinsicHashCode() {
		return classCode + Arrays.hashCode(clauses);
	}

	protected boolean intrinsicEquals(SymbolicExpression expression) {
		return expression instanceof CnfBooleanExpression
				&& Arrays.equals(clauses,
						((CnfBooleanExpression) expression).clauses);
	}

	public int numClauses() {
		return clauses.length;
	}

	public OrExpression clause(int index) {
		return clauses[index];
	}

	public String toString() {
		int numClauses = clauses.length;

		if (numClauses == 0) {
			return "true";
		} else if (numClauses == 1) {
			return clauses[0].toString();
		} else {
			String result = "";

			for (int i = 0; i < clauses.length; i++) {
				if (i > 0)
					result += " && ";
				result += clauses[i].atomString();
			}
			return result;
		}
	}

	public String atomString() {
		int numClauses = clauses.length;

		if (numClauses == 0) {
			return "true";
		} else if (numClauses == 1) {
			return clauses[0].atomString();
		} else {
			String result = "(";

			for (int i = 0; i < clauses.length; i++) {
				if (i > 0)
					result += " && ";
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
		return SymbolicKind.AND;
	}

	public int numArguments() {
		return clauses.length;
	}

}
