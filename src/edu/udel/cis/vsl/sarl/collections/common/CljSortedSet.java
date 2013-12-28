package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Comparator;
import java.util.Iterator;

import com.github.krukow.clj_ds.PersistentSortedSet;
import com.github.krukow.clj_lang.PersistentTreeSet;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSet;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CljSortedSet<T extends SymbolicExpression> extends
		CommonSymbolicCollection<T> implements SymbolicSet<T> {

	private final static int classCode = CljSortedSet.class.hashCode();

	private PersistentSortedSet<T> pset;

	private Comparator<T> restrict(final Comparator<? super T> c) {
		return new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return c.compare(o1, o2);
			}
		};
	}

	public CljSortedSet(PersistentSortedSet<T> pset) {
		super(SymbolicCollectionKind.SET);
		this.pset = pset;
	}

	public CljSortedSet(Comparator<? super T> comparator) {
		super(SymbolicCollectionKind.SET);
		this.pset = PersistentTreeSet.create(restrict(comparator), null);
	}

	@Override
	public int size() {
		return pset.size();
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

	@Override
	public Iterator<T> iterator() {
		return pset.iterator();
	}

	@Override
	public boolean contains(T element) {
		return pset.contains(element);
	}

	@Override
	public boolean isSorted() {
		return true;
	}

	@Override
	public Comparator<T> comparator() {
		return pset.comparator();
	}

	@Override
	public SymbolicSet<T> add(T element) {
		return new CljSortedSet<T>(pset.plus(element));
	}

	@Override
	public SymbolicSet<T> addAll(SymbolicSet<? extends T> set) {
		PersistentSortedSet<T> result = pset;

		for (T element : set)
			result = result.plus(element);
		return new CljSortedSet<T>(result);
	}

	@Override
	public SymbolicSet<T> remove(T element) {
		return new CljSortedSet<T>(pset.minus(element));
	}

	@Override
	public SymbolicSet<T> removeAll(SymbolicSet<? extends T> set) {
		PersistentSortedSet<T> result = pset;

		for (T element : set)
			result = result.minus(element);
		return new CljSortedSet<T>(result);
	}

	@Override
	public SymbolicSet<T> keepOnly(SymbolicSet<? extends T> set) {
		@SuppressWarnings("unchecked")
		SymbolicSet<T> theSet = (SymbolicSet<T>) set;
		PersistentSortedSet<T> result = pset;

		for (T element : pset)
			if (!theSet.contains(element))
				result = result.minus(element);

		return new CljSortedSet<T>(result);
	}

	/**
	 * Sorted set comes first, then unsorted. This is sorted, so if other is
	 * unsorted, this comes first.
	 */
	@Override
	protected boolean collectionEquals(SymbolicCollection<T> o) {
		SymbolicSet<T> that = (SymbolicSet<T>) o;

		return that.isSorted() && pset.equals(((CljSortedSet<T>) o).pset);
	}

	/**
	 * Sorted get -.
	 */
	@Override
	protected int computeHashCode() {
		return classCode ^ pset.hashCode();
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		Iterator<T> iter = pset.iterator();
		PersistentSortedSet<T> newSet = PersistentTreeSet.create(comparator(),
				null);

		while (iter.hasNext()) {
			T t1 = iter.next();
			T t2 = factory.canonic(t1);

			newSet = newSet.plus(t2);
		}
		pset = newSet;
	}

}
