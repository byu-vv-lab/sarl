package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SortedSymbolicSet;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSet;

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
	 * <p>
	 * This method can be used to help implement the
	 * {@link #keepOnly(SymbolicSet)} method. Given a sorted symbolic set
	 * (sorted using the same comparator as this), it returns the subset of this
	 * set consisting of those elements which are also in the given one (i.e.,
	 * the intersection). The result is returned as a list, with the elements
	 * appearing in order.
	 * </p>
	 * 
	 * @param set
	 *            a sorted symbolic set using same comparator as this
	 * @return list of elements in the intersection of this and the given set,
	 *         in order
	 */
	protected List<T> keepOnly_helper(SortedSymbolicSet<? extends T> set) {
		boolean immutable = this.isImmutable();
		Comparator<T> comparator = this.comparator();
		LinkedList<T> merged = new LinkedList<>();
		Iterator<T> iter1 = this.iterator();
		Iterator<? extends T> iter2 = set.iterator();
		T x1 = iter1.hasNext() ? iter1.next() : null;
		T x2 = iter2.hasNext() ? iter2.next() : null;

		if (immutable) {
			// this does not call commit because the way this
			// method will be used, every element of the new
			// set will have addChild() invoked on it in
			// construction
			while (x1 != null && x2 != null) {
				int compare = comparator.compare(x1, x2);

				if (compare == 0) {
					merged.add(x1);
					x1 = iter1.hasNext() ? iter1.next() : null;
					x2 = iter2.hasNext() ? iter2.next() : null;
				} else if (compare < 0) {
					x1 = iter1.hasNext() ? iter1.next() : null;
				} else {
					x2 = iter2.hasNext() ? iter2.next() : null;
				}
			}
		} else {
			// same as above but release elements of this set
			// that are not being kept ...
			while (x1 != null && x2 != null) {
				int compare = comparator.compare(x1, x2);

				if (compare == 0) {
					merged.add(x1);
					x1 = iter1.hasNext() ? iter1.next() : null;
					x2 = iter2.hasNext() ? iter2.next() : null;
				} else if (compare < 0) {
					x1.release(); // x1 is being removed from this
					x1 = iter1.hasNext() ? iter1.next() : null;
				} else {
					x2 = iter2.hasNext() ? iter2.next() : null;
				}
			}
			if (x1 != null)
				x1.release();
			while (iter1.hasNext())
				iter1.next().release();
		}
		return merged;
	}

	/**
	 * This method can be used to help implement {@link #addAll(SymbolicSet)}.
	 * Given any kind of sorted set, it returns the elements of the union of
	 * this set and the given one, sorted, as a list. It also commits all the
	 * elements in the given <code>set</code> that are not in this one, since
	 * they will be shared between the new set and <code>set</code>.
	 * 
	 * @param set
	 *            a sorted symbolic set using the same comparator as this
	 * @return the sorted union of this and set as a list
	 */
	protected List<T> addAll_helper(SortedSymbolicSet<? extends T> set) {
		Comparator<T> comparator = this.comparator();
		LinkedList<T> merged = new LinkedList<>();
		Iterator<T> iter1 = this.iterator();
		Iterator<? extends T> iter2 = set.iterator();
		T x1 = iter1.hasNext() ? iter1.next() : null;
		T x2 = iter2.hasNext() ? iter2.next() : null;

		if (this.isImmutable()) {
			// no need to commit since resulting list will be used
			// in constructor
			while (x1 != null && x2 != null) {
				int compare = comparator.compare(x1, x2);

				if (compare == 0) {
					// x1.commit();
					merged.add(x1);
					x1 = iter1.hasNext() ? iter1.next() : null;
					x2 = iter2.hasNext() ? iter2.next() : null;
				} else if (compare < 0) {
					// x1.commit();
					merged.add(x1);
					x1 = iter1.hasNext() ? iter1.next() : null;
				} else {
					// x2.commit();
					merged.add(x2);
					x2 = iter2.hasNext() ? iter2.next() : null;
				}
			}
			if (x1 != null) {
				// x1.commit();
				merged.add(x1);
				while (iter1.hasNext()) {
					x1 = iter1.next();
					// x1.commit();
					merged.add(x1);
				}
			} else if (x2 != null) {
				// x2.commit();
				merged.add(x2);
				while (iter2.hasNext()) {
					x2 = iter2.next();
					// x2.commit();
					merged.add(x2);
				}
			}
		} else { // mutable
			while (x1 != null && x2 != null) {
				int compare = comparator.compare(x1, x2);

				if (compare == 0) {
					merged.add(x1);
					x1 = iter1.hasNext() ? iter1.next() : null;
					x2 = iter2.hasNext() ? iter2.next() : null;
				} else if (compare < 0) {
					merged.add(x1);
					x1 = iter1.hasNext() ? iter1.next() : null;
				} else {
					x2.commit(); // since it will now appear in two sets
					merged.add(x2);
					x2 = iter2.hasNext() ? iter2.next() : null;
				}
			}
			if (x1 != null) {
				merged.add(x1);
				while (iter1.hasNext()) {
					x1 = iter1.next();
					merged.add(x1);
				}
			} else if (x2 != null) {
				x2.commit();
				merged.add(x2); // since it will now appear in two sets
				while (iter2.hasNext()) {
					x2 = iter2.next();
					x2.commit();
					merged.add(x2);
				}
			}
		}
		return merged;
	}

	/**
	 * This method can be used to help implement {@link #removeAll(SymbolicSet)}
	 * . Given any sorted set, this returns a list of the elements of this set
	 * that are not in the given set. The list is ordered. Both sets must use
	 * the same comparator.
	 * 
	 * @param set
	 *            an ordered set
	 * @return ordered list of elements of this set not in the other one
	 */
	protected List<T> removeAll_helper(SortedSymbolicSet<? extends T> set) {
		Comparator<T> comparator = this.comparator();
		LinkedList<T> merged = new LinkedList<>();
		Iterator<T> iter1 = this.iterator();
		Iterator<? extends T> iter2 = set.iterator();
		T x1 = iter1.hasNext() ? iter1.next() : null;
		T x2 = iter2.hasNext() ? iter2.next() : null;

		if (this.isImmutable()) {
			// no need to commit since resulting list will be used
			// in constructor...
			while (x1 != null && x2 != null) {
				int compare = comparator.compare(x1, x2);

				if (compare == 0) {
					x1 = iter1.hasNext() ? iter1.next() : null;
					x2 = iter2.hasNext() ? iter2.next() : null;
				} else if (compare < 0) {
					// x1.commit();
					merged.add(x1);
					x1 = iter1.hasNext() ? iter1.next() : null;
				} else {
					x2 = iter2.hasNext() ? iter2.next() : null;
				}
			}
			if (x1 != null) {
				// x1.commit();
				merged.add(x1);
				while (iter1.hasNext()) {
					x1 = iter1.next();
					// x1.commit();
					merged.add(x1);
				}
			}
		} else { // mutable
			while (x1 != null && x2 != null) {
				int compare = comparator.compare(x1, x2);

				if (compare == 0) {
					x1.release(); // removing x1
					x1 = iter1.hasNext() ? iter1.next() : null;
					x2 = iter2.hasNext() ? iter2.next() : null;
				} else if (compare < 0) {
					merged.add(x1);
					x1 = iter1.hasNext() ? iter1.next() : null;
				} else {
					x2 = iter2.hasNext() ? iter2.next() : null;
				}
			}
			if (x1 != null) {
				merged.add(x1);
				while (iter1.hasNext()) {
					x1 = iter1.next();
					merged.add(x1);
				}
			}
		}
		return merged;
	}

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
