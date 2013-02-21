package edu.udel.cis.vsl.sarl.prove.ideal;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;


/**
 * A query consists of an assumption p and a predicate q, both boolean-valued
 * expressions. Two Query objects are equal iff theirs assumptions are equal and
 * their predicates are equal.
 */
public class SymbolicQuery {

	private SymbolicExpression assumption;

	private SymbolicExpression predicate;

	/**
	 * Creates new query. Query is immutable.
	 */
	SymbolicQuery(SymbolicExpression assumption, SymbolicExpression predicate) {
		if (assumption == null)
			throw new NullPointerException("null assumption");
		if (predicate == null)
			throw new NullPointerException("null predicate");
		this.assumption = assumption;
		this.predicate = predicate;
	}

	public SymbolicExpression assumption() {
		return assumption;
	}

	public SymbolicExpression predicate() {
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
