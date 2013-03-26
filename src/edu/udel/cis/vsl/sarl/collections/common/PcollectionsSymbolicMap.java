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
package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class PcollectionsSymbolicMap<K extends SymbolicExpression, V extends SymbolicExpression>
		extends CommonSymbolicMap<K, V> implements SymbolicMap<K, V> {

	private PMap<K, V> pmap;

	PcollectionsSymbolicMap(PMap<K, V> pmap) {
		super();
		this.pmap = pmap;
	}

	PcollectionsSymbolicMap() {
		super();
		this.pmap = HashTreePMap.empty();
	}

	PcollectionsSymbolicMap(Map<K, V> javaMap) {
		this(HashTreePMap.from(javaMap));
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
	public Iterator<V> iterator() {
		return pmap.values().iterator();
	}

	@Override
	protected int computeHashCode() {
		return SymbolicCollectionKind.MAP.hashCode() ^ pmap.hashCode();
	}

	@Override
	public int size() {
		return pmap.size();
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection<V> o) {
		PcollectionsSymbolicMap<?, ?> that = (PcollectionsSymbolicMap<?, ?>) o;

		return pmap.equals(that.pmap);
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
	public boolean isEmpty() {
		return pmap.isEmpty();
	}

	@Override
	public boolean isSorted() {
		return false;
	}

	@Override
	public SymbolicMap<K, V> put(K key, V value) {
		return new PcollectionsSymbolicMap<K, V>(pmap.plus(key, value));
	}

	@Override
	public SymbolicMap<K, V> remove(K key) {
		return new PcollectionsSymbolicMap<K, V>(pmap.minus(key));
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

	@Override
	public Comparator<? super K> comparator() {
		return null;
	}

}
