package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.github.krukow.clj_ds.PersistentMap;
import com.github.krukow.clj_ds.Persistents;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class Clj4HashSymbolicMap<K extends SymbolicExpression, V extends SymbolicExpression>
		extends CommonSymbolicMap<K, V> implements SymbolicMap<K, V> {

	private PersistentMap<K, V> pmap;

	public Clj4HashSymbolicMap(PersistentMap<K, V> pmap) {
		super();
		this.pmap = pmap;
	}

	public Clj4HashSymbolicMap() {
		this(Persistents.<K, V> hashMap());
	}

	Clj4HashSymbolicMap(Map<K, V> javaMap) {
		this(Persistents.hashMap(javaMap));
	}

	@Override
	public int size() {
		return pmap.size();
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
		StringBuffer result = new StringBuffer("UnsortedMap");

		result.append(toStringBuffer(true));
		return result;
	}

	@Override
	public Iterator<V> iterator() {
		return pmap.values().iterator();
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

	/**
	 * Returns false, since this is a hash map, not a sorted map.
	 */
	@Override
	public boolean isSorted() {
		return false;
	}

	/**
	 * Returns null, since this is a hash map, not a sorted map.
	 */
	@Override
	public Comparator<? super K> comparator() {
		return null;
	}

	@Override
	public SymbolicMap<K, V> put(K key, V value) {
		return new Clj4HashSymbolicMap<K, V>(pmap.plus(key, value));
	}

	@Override
	public SymbolicMap<K, V> remove(K key) {
		return new Clj4HashSymbolicMap<K, V>(pmap.minus(key));
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection<V> o) {
		Clj4HashSymbolicMap<?, ?> that = (Clj4HashSymbolicMap<?, ?>) o;

		return pmap.equals(that.pmap);
	}

	@Override
	protected int computeHashCode() {
		return SymbolicCollectionKind.MAP.hashCode() ^ pmap.hashCode();
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		for (Entry<K, V> entry : entries()) {
			K key = entry.getKey();
			V value = entry.getValue();

			if (!key.isCanonic() || !value.isCanonic()) {
				if (key.isCanonic())
					pmap = pmap.plus(key, factory.canonic(value));
				else {
					pmap = pmap.minus(key);
					pmap = pmap.plus(factory.canonic(key),
							factory.canonic(value));
				}
			}
		}
	}

}
