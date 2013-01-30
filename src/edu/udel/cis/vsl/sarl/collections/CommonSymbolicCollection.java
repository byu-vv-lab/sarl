package edu.udel.cis.vsl.sarl.collections;

import edu.udel.cis.vsl.sarl.IF.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicObject;

public abstract class CommonSymbolicCollection extends CommonSymbolicObject
		implements SymbolicCollection {

	private SymbolicCollectionKind collectionKind;

	CommonSymbolicCollection(SymbolicCollectionKind kind) {
		super(SymbolicObjectKind.COLLECTION);
		this.collectionKind = kind;
	}

	@Override
	public SymbolicCollectionKind collectionKind() {
		return collectionKind;
	}

	/**
	 * Compares this to a collection of the same kind. Each implementing class
	 * should define this method. That class may assume that the given
	 * collection o has the same collection kind as this, i.e.,
	 * o.collectionKind() == this.collectionKind().
	 * 
	 * @param o
	 *            a symbolic collection of the same kind as this
	 * @return a negative int if this precedes o in the total order, 0 if the
	 *         collections are equals, a positive int if o comes first
	 */
	protected abstract int compareCollection(SymbolicCollection o);

	@Override
	protected int compareLocal(SymbolicObject o) {
		SymbolicCollection that = (SymbolicCollection) o;
		int result = collectionKind.compareTo(that.collectionKind());

		if (result != 0)
			return result;
		result = size() - that.size();
		if (result != 0)
			return result;
		return compareCollection((SymbolicCollection) o);
	}

	/**
	 * Tells whether the two collections (o and this) are equal, assuming o and
	 * this have the same kind.
	 * 
	 * @param o
	 * @return
	 */
	protected abstract boolean collectionEquals(SymbolicCollection o);

	@Override
	protected boolean intrinsicEquals(SymbolicObject o) {
		SymbolicCollection that = (SymbolicCollection) o;

		if (collectionKind != that.collectionKind())
			return false;
		if (size() != that.size())
			return false;
		return collectionEquals(that);
	}

}
