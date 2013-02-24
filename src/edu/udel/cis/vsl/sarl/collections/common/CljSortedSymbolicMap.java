package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.trifork.clj_ds.PersistentTreeMap;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CljSortedSymbolicMap<K extends SymbolicExpression, V extends SymbolicExpression>
		extends CommonSymbolicMap<K, V> implements SymbolicMap<K, V> {

	private PersistentTreeMap<K, V> pmap;

	Comparator<K> restrict(final Comparator<? super K> c) {
		return new Comparator<K>() {
			@Override
			public int compare(K o1, K o2) {
				return c.compare(o1, o2);
			}

		};
	}

	CljSortedSymbolicMap(Comparator<? super K> comparator) {
		super();
		this.pmap = new PersistentTreeMap<K, V>(null, restrict(comparator));
	}

	CljSortedSymbolicMap(Map<K, V> javaMap, Comparator<? super K> comparator) {
		super();
		pmap = new PersistentTreeMap<K, V>(null, restrict(comparator));
		for (Entry<K, V> entry : javaMap.entrySet())
			pmap = pmap.assoc(entry.getKey(), entry.getValue());
	}

	@Override
	public int size() {
		return pmap.size();
	}

	@Override
	public Iterator<V> iterator() {
		return pmap.vals();
	}

	@Override
	public V get(K key) {
		return pmap.get(key);
	}

	@Override
	public Iterable<K> keys() {
		return pmap.keySet();
	}

	@Override
	public Iterable<V> values() {
		return pmap.values();
	}

	@Override
	public Iterable<Entry<K, V>> entries() {
		return pmap.entrySet();
	}

	@Override
	public boolean isEmpty() {
		return pmap.isEmpty();
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection<V> o) {
		if (o instanceof CljSortedSymbolicMap)
			return pmap.equals(((CljSortedSymbolicMap<?, ?>) o).pmap);
		return false;
	}

	@Override
	protected int computeHashCode() {
		return SymbolicCollectionKind.MAP.hashCode() ^ pmap.hashCode();
	}

	@Override
	public boolean isSorted() {
		return true;
	}

	@Override
	public SymbolicMap<K, V> put(K key, V value) {
		return new CljSortedSymbolicMap<K, V>(pmap.assoc(key, value),
				pmap.comparator());
	}

	@Override
	public SymbolicMap<K, V> remove(K key) {
		return new CljSortedSymbolicMap<K, V>(pmap.without(key),
				pmap.comparator());
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		// TODO Auto-generated method stub
		// need to construct whole new map replacing keys and values
		// with canonic representative if not already
	}

	@Override
	public Comparator<? super K> comparator() {
		return pmap.comparator();
	}
}
