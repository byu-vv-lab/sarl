package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Iterator;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SortedSymbolicSet;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;

/**
 * Partial implementation of the {@link SortedSymbolicSet} interface. Concrete
 * implementations can override methods here for greater efficiency.
 * 
 * @author siegel
 *
 */
public abstract class CommonSortedSet<T extends SymbolicExpression> extends
		CommonSymbolicCollection<T> implements SortedSymbolicSet<T> {

	public final static int classCode = SymbolicCollectionKind.SORTED_SET
			.hashCode();

	// Constructors...

	/**
	 * Constructs new sorted set.
	 */
	protected CommonSortedSet() {
		super(SymbolicCollectionKind.SORTED_SET);
	}

	// protected methods...

	/**
	 * {@inheritDoc}
	 * 
	 * This is a simple, generic algorithm to compare equality of two sorted
	 * sets. Recall that the precondition of this method is that this set and
	 * the other one have the same kind AND same size.
	 */
	@Override
	protected boolean collectionEquals(SymbolicCollection<T> o) {
		SortedSymbolicSet<T> that = (SortedSymbolicSet<T>) o;
		Iterator<T> iter1 = this.iterator();
		Iterator<T> iter2 = that.iterator();

		while (iter1.hasNext()) {
			if (!iter1.next().equals(iter2.next()))
				return false;
		}
		return true;
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * A simple, generic hash code function for sorted sets.
	 * 
	 */
	@Override
	protected int computeHashCode() {
		int result = classCode;

		for (T element : this)
			result = result ^ element.hashCode();
		return result;
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		StringBuffer result = new StringBuffer();
		boolean first = true;

		if (atomize)
			result.append("{");

		for (T element : this) {
			if (first)
				first = false;
			else
				result.append(",");
			result.append(element.toStringBuffer(false));
		}
		if (atomize)
			result.append("}");
		return result;
	}

	@Override
	public StringBuffer toStringBufferLong() {
		StringBuffer result = new StringBuffer("SortedSet");

		result.append(toStringBuffer(true));
		return result;
	}

}
