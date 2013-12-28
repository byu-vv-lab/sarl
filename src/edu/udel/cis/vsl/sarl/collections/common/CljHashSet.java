package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import com.github.krukow.clj_ds.PersistentSet;
import com.github.krukow.clj_ds.Persistents;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSet;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CljHashSet<T extends SymbolicExpression> extends
		CommonSymbolicCollection<T> implements SymbolicSet<T> {

	private final static int classCode = CljHashSet.class.hashCode();

	private PersistentSet<T> pset;

	CljHashSet(PersistentSet<T> pset) {
		super(SymbolicCollectionKind.SET);
		this.pset = pset;
	}

	CljHashSet() {
		this(Persistents.<T> hashSet());
	}

	CljHashSet(Collection<T> elements) {
		this(Persistents.hashSet(elements));
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
		StringBuffer result = new StringBuffer("UnsortedSet");

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

	/**
	 * Returns false because this is a hash set, not a sorted set.
	 */
	@Override
	public boolean isSorted() {
		return false;
	}

	/**
	 * Returns null because this is a hash set, not a sorted set, so doesn't use
	 * a comparator.
	 */
	@Override
	public Comparator<T> comparator() {
		return null;
	}

	@Override
	public SymbolicSet<T> add(T element) {
		return new CljHashSet<T>(pset.plus(element));
	}

	@Override
	public SymbolicSet<T> addAll(SymbolicSet<? extends T> set) {
		PersistentSet<T> result = pset;

		for (T element : set)
			result = result.plus(element);
		return new CljHashSet<T>(result);
	}

	@Override
	public SymbolicSet<T> remove(T element) {
		return new CljHashSet<T>(pset.minus(element));
	}

	@Override
	public SymbolicSet<T> removeAll(SymbolicSet<? extends T> set) {
		PersistentSet<T> result = pset;

		for (T element : set)
			result = result.minus(element);
		return new CljHashSet<T>(result);
	}

	@Override
	public SymbolicSet<T> keepOnly(SymbolicSet<? extends T> set) {
		@SuppressWarnings("unchecked")
		SymbolicSet<T> theSet = (SymbolicSet<T>) set;
		PersistentSet<T> result = pset;

		for (T element : pset)
			if (!theSet.contains(element))
				result = result.minus(element);

		return new CljHashSet<T>(result);
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection<T> o) {
		SymbolicSet<T> that = (SymbolicSet<T>) o;

		return !that.isSorted() && pset.equals(((CljHashSet<T>) o).pset);
	}

	@Override
	protected int computeHashCode() {
		return classCode ^ pset.hashCode();
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		Iterator<T> iter = pset.iterator();
		PersistentSet<T> newSet = Persistents.hashSet();

		while (iter.hasNext()) {
			T t1 = iter.next();
			T t2 = factory.canonic(t1);

			newSet = newSet.plus(t2);
		}
		pset = newSet;
	}

}
