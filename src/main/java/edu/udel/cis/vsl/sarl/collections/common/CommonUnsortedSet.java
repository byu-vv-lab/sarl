package edu.udel.cis.vsl.sarl.collections.common;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSet;

/**
 * Partial implementation of the {@link SymbolicSet} interface for an unsorted
 * set. Concrete implementations can override methods here for greater
 * efficiency.
 * 
 * @author siegel
 *
 */
public abstract class CommonUnsortedSet<T extends SymbolicExpression> extends
		CommonSymbolicCollection<T> implements SymbolicSet<T> {

	public final static int classCode = SymbolicCollectionKind.UNSORTED_SET
			.hashCode();

	// Constructors...

	/**
	 * Constructs new unsorted set.
	 */
	CommonUnsortedSet() {
		super(SymbolicCollectionKind.UNSORTED_SET);
	}

	// protected methods...

	@Override
	protected boolean collectionEquals(SymbolicCollection<T> o) {
		SymbolicSet<T> that = (SymbolicSet<T>) o;

		if (size() != that.size())
			return false;
		for (T element : this) {
			if (!that.contains(element))
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
		StringBuffer result = new StringBuffer("UnsortedSet");

		result.append(toStringBuffer(true));
		return result;
	}

}
