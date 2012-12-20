package edu.udel.cis.vsl.sarl.symbolic.ideal.simplify;

import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.ideal.IdealExpression;

/**
 * Represents the result of a simplification operation. The original is some
 * tree expression. The result is the result of simplifying the tree expression
 * and placing it into canonical form. Success is true if some simplification
 * acutally succeeded in making the expression simpler. Equality is completely
 * determined by the original expression, because there can be at most one
 * simplification operation for each original expression.
 */
public class Simplification {

	private TreeExpressionIF original;

	private IdealExpression result;

	private boolean success;

	public Simplification(TreeExpressionIF original, IdealExpression result,
			boolean success) {
		this.original = original;
		this.result = result;
		this.success = success;
	}

	public TreeExpressionIF original() {
		return original;
	}

	public IdealExpression result() {
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
