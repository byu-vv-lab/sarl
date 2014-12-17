/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.github.krukow.clj_lang.PersistentTreeMap;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SortedSymbolicMap;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CljSortedMap<K extends SymbolicExpression, V extends SymbolicExpression>
		extends CommonSortedMap<K, V> {

	private PersistentTreeMap<K, V> pmap;

	Comparator<K> restrict(final Comparator<? super K> c) {
		return new Comparator<K>() {
			@Override
			public int compare(K o1, K o2) {
				return c.compare(o1, o2);
			}

		};
	}

	public CljSortedMap(PersistentTreeMap<K, V> pmap) {
		super();
		this.pmap = pmap;
	}

	public CljSortedMap(Comparator<? super K> comparator) {
		super();
		this.pmap = new PersistentTreeMap<K, V>(null, restrict(comparator));
	}

	CljSortedMap(Map<K, V> javaMap, Comparator<? super K> comparator) {
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
		int size = size();

		if (size != o.size())
			return false;
		if (size == 0)
			return true;
		else {
			SymbolicType thisType = getFirst().type(), thatType = o.getFirst()
					.type();

			if (thisType == null) {
				if (thatType != null)
					return false;
			} else {
				if (!thisType.equals(thatType))
					return false;
			}
			// now you know the two sets have the same type of elements,
			// since an expression collection holds elements of one type
			// TODO: make the type a field in a symbolic collection?
			if (o instanceof CljSortedMap)
				return pmap.equals(((CljSortedMap<?, ?>) o).pmap);
			return false;
		}
	}

	@Override
	public SortedSymbolicMap<K, V> put(K key, V value) {
		return new CljSortedMap<K, V>(pmap.assoc(key, value));
	}

	@Override
	public SortedSymbolicMap<K, V> remove(K key) {
		return new CljSortedMap<K, V>(pmap.without(key));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void canonizeChildren(CommonObjectFactory factory) {

		for (Entry<K, V> entry : entries()) {
			K key = entry.getKey();
			V value = entry.getValue();

			if (!key.isCanonic() || !value.isCanonic()) {
				if (key.isCanonic())
					pmap = pmap.assoc(key,
							(V) factory.canonic((SymbolicExpression) value));
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
