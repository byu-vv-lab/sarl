package edu.udel.cis.vsl.sarl.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class EmptyMap<K, V> implements Map<K, V> {

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public boolean containsKey(Object key) {
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		return false;
	}

	@Override
	public V get(Object key) {
		return null;
	}

	@Override
	public V put(K key, V value) {
		throw new UnsupportedOperationException("This is an immutable map");
	}

	@Override
	public V remove(Object key) {
		throw new UnsupportedOperationException("This is an immutable map");
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException("This is an immutable map");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("This is an immutable map");
	}

	@Override
	public Set<K> keySet() {
		return new EmptySet<K>();
	}

	@Override
	public Collection<V> values() {
		return new EmptySet<V>();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return new EmptySet<Entry<K, V>>();
	}
}
