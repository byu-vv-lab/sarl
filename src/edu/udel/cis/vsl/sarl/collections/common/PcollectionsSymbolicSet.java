package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import org.pcollections.HashTreePSet;
import org.pcollections.PSet;

import edu.udel.cis.vsl.sarl.IF.Transform;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSet;
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
		int count = 0;
		Iterator<T> iter = pset.iterator();

		while (iter.hasNext()) {
			T t1 = iter.next();
			T t2 = factory.canonic(t1);

			if (t1 != t2) {
				PSet<T> newSet = HashTreePSet.empty();
				Iterator<T> iter2 = pset.iterator();

				for (int i = 0; i < count; i++)
					newSet = newSet.plus(iter2.next());
				newSet = newSet.plus(t2);
				while (iter.hasNext())
					newSet = newSet.plus(factory.canonic(iter.next()));
				pset = newSet;
			}
			count++;
		}
	}

	@Override
	public Comparator<T> comparator() {
		return null;
	}

	public <U extends SymbolicExpression> SymbolicSet<U> apply(
			Transform<T, U> transform) {
		int count = 0;
		Iterator<T> iter = pset.iterator();

		while (iter.hasNext()) {
			T t = iter.next();
			U u = transform.apply(t);

			if (t != u) {
				PSet<U> newSet = HashTreePSet.empty();
				Iterator<T> iter2 = pset.iterator();

				for (int i = 0; i < count; i++) {
					@SuppressWarnings("unchecked")
					U sameElement = (U) iter2.next();

					newSet = newSet.plus(sameElement);
				}
				newSet = newSet.plus(u);
				while (iter.hasNext())
					newSet = newSet.plus(transform.apply(iter.next()));
				return new PcollectionsSymbolicSet<U>(newSet);
			}
			count++;
		}
		{
			@SuppressWarnings("unchecked")
			SymbolicSet<U> result = (SymbolicSet<U>) this;

			return result;
		}
	}

}
