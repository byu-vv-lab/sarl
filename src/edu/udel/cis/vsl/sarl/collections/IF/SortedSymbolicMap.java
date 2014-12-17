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
package edu.udel.cis.vsl.sarl.collections.IF;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.UnaryOperator;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.util.BinaryOperator;

/**
 * A symbolic map in which the entries are ordered by keys. A comparator on keys
 * must be provided and is associated to this map.
 * 
 * @author siegel
 *
 * @param <K>
 *            the type of keys (a subtype of {@link SymbolicExpression}
 * @param <V>
 *            the type of the values (also a subtype of
 *            {@link SymbolicExpression}
 */
public interface SortedSymbolicMap<K extends SymbolicExpression, V extends SymbolicExpression>
		extends SymbolicMap<K, V> {

	/**
	 * If this map is sorted, returns the comparator used to sort the keys,
	 * otherwise returns null.
	 * 
	 * @return the key comparator or null
	 */
	Comparator<? super K> comparator();

	/**
	 * {@inheritDoc}
	 * 
	 * The map returned will be sorted.
	 */
	@Override
	SortedSymbolicMap<K, V> put(K key, V value);

	/**
	 * {@inheritDoc}
	 * 
	 * The map returned will be sorted.
	 */
	@Override
	SortedSymbolicMap<K, V> remove(K key);

	/**
	 * {@inheritDoc}
	 * 
	 * The map returned will be sorted.
	 */
	SortedSymbolicMap<K, V> apply(UnaryOperator<V> operator);

	/**
	 * {@inheritDoc}
	 * 
	 * The map returned will be sorted.
	 */
	SortedSymbolicMap<K, V> combine(BinaryOperator<V> operator,
			SymbolicMap<K, V> map);

}
