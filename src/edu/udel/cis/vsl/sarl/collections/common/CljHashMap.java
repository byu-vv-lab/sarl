package edu.udel.cis.vsl.sarl.collections.common;

import static edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection.SymbolicCollectionKind.UNSORTED_MAP;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.github.krukow.clj_ds.PersistentMap;
import com.github.krukow.clj_ds.Persistents;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CljHashMap<K extends SymbolicExpression, V extends SymbolicExpression>
		extends CommonSymbolicMap<K, V> {

	private PersistentMap<K, V> pmap;

	public CljHashMap(PersistentMap<K, V> pmap) {
		super(UNSORTED_MAP);
		this.pmap = pmap;
	}

	public CljHashMap() {
		this(Persistents.<K, V> hashMap());
	}

	CljHashMap(Map<K, V> javaMap) {
		this(Persistents.hashMap(javaMap));
	}

	@Override
	public int size() {
		return pmap.size();
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

	@Override
	public SymbolicMap<K, V> put(K key, V value) {
		return new CljHashMap<K, V>(pmap.plus(key, value));
	}

	@Override
	public SymbolicMap<K, V> remove(K key) {
		return new CljHashMap<K, V>(pmap.minus(key));
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection<V> o) {
		if (o instanceof CljHashMap<?, ?>) {
			CljHashMap<?, ?> that = (CljHashMap<?, ?>) o;

			return pmap.equals(that.pmap);
		} else {
			return super.collectionEquals(o);
		}
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
