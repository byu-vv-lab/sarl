package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Comparator;
import java.util.Iterator;

import com.github.krukow.clj_ds.PersistentSortedSet;
import com.github.krukow.clj_lang.PersistentTreeSet;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SortedSymbolicSet;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSet;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

/**
 * Implementation of {@link SymbolicSortedSet} based on the Clojure
 * {@link PersistentSortedSet} class.
 * 
 * @author siegel
 *
 * @param <T>
 */
public class CljSortedSet<T extends SymbolicExpression> extends
		CommonSortedSet<T> {

	/**
	 * The underlying Clojure set which this class wraps.
	 */
	private PersistentSortedSet<T> pset;

	private Comparator<T> restrict(final Comparator<? super T> c) {
		return new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return c.compare(o1, o2);
			}
		};
	}

	/**
	 * Instantiates new CljSortedSet wrapping the given Clojure pset.
	 * 
	 * @param pset
	 *            Clojure persistent set
	 */
	public CljSortedSet(PersistentSortedSet<T> pset) {
		super();
		this.pset = pset;
	}

	/**
	 * Creates new empty sorted set using given comparator.
	 * 
	 * @param comparator
	 *            element comparator
	 */
	public CljSortedSet(Comparator<? super T> comparator) {
		super();
		this.pset = PersistentTreeSet.create(restrict(comparator), null);
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
	public Comparator<T> comparator() {
		return pset.comparator();
	}

	@Override
	public SortedSymbolicSet<T> add(T element) {
		return new CljSortedSet<T>(pset.plus(element));
	}

	@Override
	public SortedSymbolicSet<T> addAll(SymbolicSet<? extends T> set) {
		PersistentSortedSet<T> result = pset;

		for (T element : set)
			result = result.plus(element);
		return new CljSortedSet<T>(result);
	}

	@Override
	public SortedSymbolicSet<T> remove(T element) {
		return new CljSortedSet<T>(pset.minus(element));
	}

	@Override
	public SortedSymbolicSet<T> removeAll(SymbolicSet<? extends T> set) {
		PersistentSortedSet<T> result = pset;

		for (T element : set)
			result = result.minus(element);
		return new CljSortedSet<T>(result);
	}

	@Override
	public SortedSymbolicSet<T> keepOnly(SymbolicSet<? extends T> set) {
		@SuppressWarnings("unchecked")
		SymbolicSet<T> theSet = (SymbolicSet<T>) set;
		PersistentSortedSet<T> result = pset;

		for (T element : pset)
			if (!theSet.contains(element))
				result = result.minus(element);
		return new CljSortedSet<T>(result);
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection<T> o) {
		SymbolicSet<T> that = (SymbolicSet<T>) o;

		if (that instanceof CljSortedSet<?>) {
			return pset.equals(((CljSortedSet<T>) o).pset);
		} else {
			return super.collectionEquals(o);
		}
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
