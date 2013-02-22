package edu.udel.cis.vsl.sarl.collections.common;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.object.common.CommonSymbolicObject;

public abstract class CommonSymbolicCollection<T extends SymbolicExpression>
		extends CommonSymbolicObject implements SymbolicCollection<T> {

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
	protected abstract boolean collectionEquals(SymbolicCollection<T> o);

	@Override
	protected boolean intrinsicEquals(SymbolicObject o) {
		@SuppressWarnings("unchecked")
		SymbolicCollection<T> that = (SymbolicCollection<T>) o;

		if (collectionKind != that.collectionKind())
			return false;
		if (size() != that.size())
			return false;
		return collectionEquals(that);
	}

	@Override
	public T getFirst() {
		return iterator().next();
	}

}
