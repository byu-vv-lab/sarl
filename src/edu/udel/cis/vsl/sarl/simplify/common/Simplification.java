package edu.udel.cis.vsl.sarl.simplify.common;

import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

/**
 * Represents the result of a simplification operation. The original is some
 * expression. The result is the result of simplifying the expression and
 * placing it into canonical form. Success is true if some simplification
 * actually succeeded in making the expression simpler. Equality is completely
 * determined by the original expression, because there can be at most one
 * simplification operation for each original expression.
 */
public class Simplification {

	private SymbolicObject original;

	private SymbolicObject result;

	private boolean success;

	public Simplification(SymbolicObject original, SymbolicObject result,
			boolean success) {
		this.original = original;
		this.result = result;
		this.success = success;
	}

	public SymbolicObject original() {
		return original;
	}

	public SymbolicObject result() {
		return result;
	}

	public boolean success() {
		return success;
	}

	@Override
	public String toString() {
		return "[" + original + ", " + result + ", " + success + "]";
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Simplification) {
			Simplification that = (Simplification) object;

			return original.equals(that.original);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return original.hashCode();
	}

}
