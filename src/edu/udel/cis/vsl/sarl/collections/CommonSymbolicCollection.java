package edu.udel.cis.vsl.sarl.collections;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.object.CommonSymbolicObject;

public abstract class CommonSymbolicCollection extends CommonSymbolicObject
		implements SymbolicCollection {

	private SymbolicCollectionKind collectionKind;

	CommonSymbolicCollection(SymbolicCollectionKind kind) {
		super(SymbolicObjectKind.EXPRESSION_COLLECTION);
		this.collectionKind = kind;
	}

	@Override
	public SymbolicCollectionKind collectionKind() {
		return collectionKind;
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
