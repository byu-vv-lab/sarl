package edu.udel.cis.vsl.sarl.expr.ideal;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

/**
 * Represents the result of a simplification operation. The original is some
 * expression. The result is the result of simplifying the expression and
 * placing it into canonical form. Success is true if some simplification
 * actually succeeded in making the expression simpler. Equality is completely
 * determined by the original expression, because there can be at most one
 * simplification operation for each original expression.
 */
public class Simplification {

	private SymbolicExpression original;

	private SymbolicExpression result;

	private boolean success;

	public Simplification(SymbolicExpression original,
			SymbolicExpression result, boolean success) {
		this.original = original;
		this.result = result;
		this.success = success;
	}

	public SymbolicExpression original() {
		return original;
	}

	public SymbolicExpression result() {
		return result;
	}

	public boolean success() {
		return success;
	}

	public String toString() {
		return "[" + original + ", " + result + ", " + success + "]";
	}

	public boolean equals(Object object) {
		if (object instanceof Simplification) {
			Simplification that = (Simplification) object;

			return original.equals(that.original);
		}
		return false;
	}

	public int hashCode() {
		return original.hashCode();
	}

}
