/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;

public class SingletonMap<K, V> implements Map<K, V> {

	private K theKey;

	private V theValue;

	public SingletonMap(K key, V value) {
		assert key != null;
		theKey = key;
		theValue = value;
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean containsKey(Object key) {
		return theKey.equals(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return theValue == null ? (value == null) : theValue.equals(value);
	}

	@Override
	public V get(Object key) {
		return theKey.equals(key) ? theValue : null;
	}

	@Override
	public V put(K key, V value) {
		throw new SARLInternalException("Map is immutable");
	}

	@Override
	public V remove(Object key) {
		throw new SARLInternalException("Map is immutable");
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new SARLInternalException("Map is immutable");
	}

	@Override
	public void clear() {
		throw new SARLInternalException("Map is immutable");
	}

	@Override
	public Set<K> keySet() {
		return new SingletonSet<K>(theKey);
	}

	@Override
	public Collection<V> values() {
		return new SingletonSet<V>(theValue);
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return new SingletonSet<Entry<K, V>>(new Entry<K, V>() {
			@Override
			public K getKey() {
				return theKey;
			}

			@Override
			public V getValue() {
				return theValue;
			}

			@Override
			public V setValue(V value) {
				throw new SARLInternalException("Map is immutable");
			}
		});
	}

	@Override
	public String toString() {
		return "{" + theKey + "=" + theValue + "}";
	}

	@Override
	public int hashCode() {
		return theKey.hashCode() + theValue.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o instanceof Map<?, ?>) {
			Map<?, ?> that = (Map<?, ?>) o;

			if (that.size() != 1)
				return false;
			else {
				Entry<?, ?> thatEntry = that.entrySet().iterator().next();

				return theKey.equals(thatEntry.getKey())
						&& theValue.equals(thatEntry.getValue());
			}
		}
		return false;
	}

}
