package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Collection;
import java.util.Iterator;

import com.github.krukow.clj_ds.PersistentSet;
import com.github.krukow.clj_ds.Persistents;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSet;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CljHashSet<T extends SymbolicExpression> extends
		CommonUnsortedSet<T> {

	private PersistentSet<T> pset;

	CljHashSet(PersistentSet<T> pset) {
		super();
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
	public Iterator<T> iterator() {
		return pset.iterator();
	}

	@Override
	public boolean contains(T element) {
		return pset.contains(element);
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
		if (o instanceof CljHashSet<?>) {
			return pset.equals(((CljHashSet<T>) o).pset);
		} else {
			return super.collectionEquals(o);
		}
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
