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

import static edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection.SymbolicCollectionKind.SORTED_MAP;
import static edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection.SymbolicCollectionKind.UNSORTED_MAP;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import edu.udel.cis.vsl.sarl.IF.UnaryOperator;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.util.BinaryOperator;

/**
 * Partial implementation of a {@link SymbolicMap}, either sorted or unsorted.
 * Note that the methods defined here work correctly whether or not this map is
 * mutable. That is because they operate on the maps only through the methods
 * provided in the {@link SymbolicMap} interface, and those methods work
 * correctly in either case.
 * 
 * @author siegel
 *
 * @param <K>
 *            type for the keys in the map
 * @param <V>
 *            type for the values in the map
 */
public abstract class CommonSymbolicMap<K extends SymbolicExpression, V extends SymbolicExpression>
		extends CommonSymbolicCollection<V> implements SymbolicMap<K, V> {

	public CommonSymbolicMap(SymbolicCollectionKind kind) {
		super(kind);
		assert kind == SORTED_MAP || kind == UNSORTED_MAP;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Implementation note: the first change results in a new map, while the
	 * iteration takes place over the original map, so there is no conflict.
	 * </p>
	 * 
	 * @param operator
	 *            a unary operator to be applied to the values of this map
	 * @return a map which results from apply the given operator to the values
	 *         of this map, with entries resulting in null removed
	 */
	public SymbolicMap<K, V> apply(UnaryOperator<V> operator) {
		SymbolicMap<K, V> result = this;

		for (Entry<K, V> entry : this.entries()) {
			K key = entry.getKey();
			V value = entry.getValue();
			V newValue = operator.apply(value);

			if (newValue == null)
				result = result.remove(key);
			else if (value != newValue)
				result = result.put(key, newValue);
		}
		return result;
	}

	@Override
	public SymbolicMap<K, V> applyMut(UnaryOperator<V> operator) {
		if (isImmutable())
			return apply(operator);

		List<K> removeList = new LinkedList<>();

		for (Entry<K, V> entry : this.entries()) {
			K key = entry.getKey();
			V value = entry.getValue();
			V newValue = operator.apply(value);

			if (newValue == null)
				removeList.add(key);
			else if (value != newValue) {
				value.removeReferenceFrom(this);
				newValue.addReferenceFrom(this);
				entry.setValue(newValue);
			}
		}
		for (K key : removeList)
			remove(key);
		return this;
	}

	@Override
	protected int computeHashCode() {
		int result = this.collectionKind().hashCode();

		for (V value : this) {
			result = result ^ value.hashCode();
		}
		return result;
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection<V> o) {
		@SuppressWarnings("unchecked")
		SymbolicMap<K, V> that = (SymbolicMap<K, V>) o;

		for (Entry<K, V> entry : entries()) {
			K key1 = entry.getKey();
			V value1 = entry.getValue();
			V value2 = that.get(key1);

			if (!value1.equals(value2))
				return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * In this implementation, you iterate over one map while modifying the
	 * other one.
	 */
	@Override
	public SymbolicMap<K, V> combine(BinaryOperator<V> operator,
			SymbolicMap<K, V> map) {
		SymbolicMap<K, V> result, map2;

		// optimization: iterate over the shorter map
		if (this.size() < map.size()) {
			result = map;
			map2 = this;
		} else {
			result = this;
			map2 = map;
		}
		for (Entry<K, V> entry : map2.entries()) {
			K key = entry.getKey();
			V value2 = entry.getValue();
			V value1 = result.get(key);

			if (value1 == null)
				result = result.put(key, value2);
			else {
				V newValue = operator.apply(value1, value2);

				if (newValue == null)
					result = result.remove(key);
				else if (newValue != value1)
					result = result.put(key, newValue);
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Note: in this implementation, you iterate over the entries in
	 * <code>map</code> while modifying <code>this</code>. This is fine as long
	 * as <code>map</code> and <code>this</code> are not the same object. If
	 * they are the same object, then that object should be committed, in which
	 * case this method will invoke
	 * {@link #combine(BinaryOperator, SymbolicMap)}.
	 */
	@Override
	public SymbolicMap<K, V> combineMut(BinaryOperator<V> operator,
			SymbolicMap<K, V> map) {
		if (isImmutable())
			return combine(operator, map);

		assert map != this;
		for (Entry<K, V> entry : map.entries()) {
			K key = entry.getKey();
			V value2 = entry.getValue();
			V value1 = get(key);

			assert value2 != null;
			if (value1 == null)
				putMut(key, value2);
			else {
				V newValue = operator.apply(value1, value2);

				// if operator mutates, it may have modified and returned
				// value1.
				if (newValue == null)
					removeMut(key);
				else if (newValue != value1)
					putMut(key, newValue);
			}
		}
		return this;
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
		StringBuffer result = new StringBuffer("Map");

		result.append(toStringBuffer(true));
		return result;
	}

	@Override
	public String toString() {
		return toStringBuffer(false).toString();
	}

}
