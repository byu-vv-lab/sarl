package edu.udel.cis.vsl.sarl.symbolic;

import edu.udel.cis.vsl.sarl.IF.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.SymbolicCollection;

public abstract class CommonSymbolicCollection extends CommonSymbolicObject implements
		SymbolicCollection {

	private SymbolicCollectionKind collectionKind;

	CommonSymbolicCollection(SymbolicCollectionKind kind) {
		super(SymbolicObjectKind.COLLECTION);
		this.collectionKind = kind;
	}

	@Override
	public SymbolicCollectionKind collectionKind() {
		return collectionKind;
	}

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
