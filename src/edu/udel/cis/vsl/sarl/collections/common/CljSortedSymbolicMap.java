package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.trifork.clj_ds.PersistentTreeMap;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
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
		for (Entry<K, V> entry : entries()) {
			K key = entry.getKey();
			V value = entry.getValue();

			if (!key.isCanonic() || !value.isCanonic()) {
				if (key.isCanonic())
					pmap = pmap.assoc(key, factory.canonic(value));
				else {
					pmap = pmap.without(key);
					pmap = pmap.assoc(factory.canonic(key),
							factory.canonic(value));
				}
			}
		}
	}

	@Override
	public Comparator<? super K> comparator() {
		return pmap.comparator();
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		StringBuffer result = new StringBuffer("{");
		boolean first = true;

		for (Entry<K, V> entry : entries()) {
			if (first)
				first = false;
			else
				result.append(", ");
			result.append(entry.getKey().toStringBuffer(false));
			result.append("->");
			result.append(entry.getValue().toStringBuffer(false));
		}
		result.append("}");
		return result;
	}

	@Override
	public StringBuffer toStringBufferLong() {
		StringBuffer result = new StringBuffer("SortedMap");

		result.append(toStringBuffer(true));
		return result;
	}

}
