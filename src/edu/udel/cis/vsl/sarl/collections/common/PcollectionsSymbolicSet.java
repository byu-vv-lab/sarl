package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import org.pcollections.HashTreePSet;
import org.pcollections.PSet;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSet;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class PcollectionsSymbolicSet<T extends SymbolicExpression> extends
		CommonSymbolicCollection<T> implements SymbolicSet<T> {

	private PSet<T> pset;

	PcollectionsSymbolicSet(PSet<T> pset) {
		super(SymbolicCollectionKind.SET);
		this.pset = pset;
	}

	PcollectionsSymbolicSet() {
		super(SymbolicCollectionKind.SET);
		this.pset = HashTreePSet.empty();
	}

	PcollectionsSymbolicSet(Collection<T> elements) {
		this(HashTreePSet.from(elements));
	}

	@Override
	public int size() {
		return pset.size();
	}

	@Override
	public Iterator<T> iterator() {
		return pset.iterator();
	}

	@Override
	public boolean contains(T element) {
		return pset.contains(element);
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection<T> o) {
		return pset.equals(((PcollectionsSymbolicSet<T>) o).pset);
	}

	@Override
	protected int computeHashCode() {
		return SymbolicCollectionKind.SET.hashCode() ^ pset.hashCode();
	}

	@Override
	public String toString() {
		return pset.toString();
	}

	@Override
	public boolean isSorted() {
		return false;
	}

	// /**
	// * I say the cast is safe because instances are immutable.
	// * Therefore, if A is a subtype of B, then "immutable-set-of-A"
	// * is a subtype of "immutable-set-of-B".
	// */
	// @SuppressWarnings("unchecked")
	// @Override
	// public SymbolicSet<SymbolicExpression> expand() {
	// return (SymbolicSet<SymbolicExpression>) this;
	// }

	@Override
	public SymbolicSet<T> add(T element) {
		return new PcollectionsSymbolicSet<T>(pset.plus(element));
	}

	@Override
	public SymbolicSet<T> addAll(SymbolicSet<? extends T> set) {
		return new PcollectionsSymbolicSet<T>(pset.plusAll(pset));
	}

	@Override
	public SymbolicSet<T> remove(T element) {
		return new PcollectionsSymbolicSet<T>(pset.minus(element));
	}

	@Override
	public SymbolicSet<T> removeAll(SymbolicSet<? extends T> set) {
		return new PcollectionsSymbolicSet<T>(
				pset.minusAll(((PcollectionsSymbolicSet<?>) set).pset));
	}

	@Override
	public SymbolicSet<T> keepOnly(SymbolicSet<? extends T> set) {
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		// TODO Auto-generated method stub

	}

	@Override
	public Comparator<T> comparator() {
		return null;
	}

	// @Override
	// public SymbolicSet<SymbolicExpression> addAllAnyKind(SymbolicSet<?> set)
	// {
	// return addAll(set);
	// // TODO Auto-generated method stub
	// return null;
	// }

}
