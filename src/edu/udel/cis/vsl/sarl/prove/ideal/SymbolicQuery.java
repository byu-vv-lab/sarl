package edu.udel.cis.vsl.sarl.prove.ideal;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;


/**
 * A query consists of an assumption p and a predicate q, both boolean-valued
 * expressions. Two Query objects are equal iff theirs assumptions are equal and
 * their predicates are equal.
 */
public class SymbolicQuery {

	private SymbolicExpressionIF assumption;

	private SymbolicExpressionIF predicate;

	/**
	 * Creates new query. Query is immutable.
	 */
	SymbolicQuery(SymbolicExpressionIF assumption, SymbolicExpressionIF predicate) {
		if (assumption == null)
			throw new NullPointerException("null assumption");
		if (predicate == null)
			throw new NullPointerException("null predicate");
		this.assumption = assumption;
		this.predicate = predicate;
	}

	public SymbolicExpressionIF assumption() {
		return assumption;
	}

	public SymbolicExpressionIF predicate() {
		return predicate;
	}

	public int hashCode() {
		return assumption.hashCode() + predicate.hashCode();
	}

	public boolean equals(Object object) {
		if (object instanceof SymbolicQuery) {
			SymbolicQuery that = (SymbolicQuery) object;

			return assumption.equals(that.assumption)
					&& predicate.equals(that.predicate);
		}
		return false;
	}
}
