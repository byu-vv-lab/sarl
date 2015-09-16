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

import java.util.Map.Entry;

import edu.udel.cis.vsl.sarl.IF.UnaryOperator;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.util.BinaryOperator;

/**
 * Map is interpreted as a collection of values. The keys are used for other
 * purposes. So, for example, ADD followed by map argument represents sum of
 * values in the map.
 * 
 * Examples of use:
 * 
 * Keys: monics, values: monomials. ADD applied to this represents a polynomial.
 * The polynomial allows fast lookup of coefficient for any given monomial
 * 
 * Keys:primitives, values: primitive-powers. MULTIPLY applied to this map
 * represents a monic.
 * 
 * Keys: polynomials, values: polynomial-powers. MULTIPLY applies to this map
 * represents a factorization of a polynomial.
 * 
 * Just need efficient ways to modify in immutable way!
 * 
 * @author siegel
 * 
 */
public interface SymbolicMap<K extends SymbolicExpression, V extends SymbolicExpression>
		extends SymbolicCollection<V> {

	/**
	 * Gets the value associated to the given key, or returns null if there is
	 * no entry for that key
	 * 
	 * @param key
	 *            the key
	 * @return value associated to key
	 */
	V get(K key);

	/**
	 * Returns the keys of the map.
	 * 
	 * @return the keys
	 */
	Iterable<K> keys();

	/**
	 * Same as elements().
	 * 
	 * @return the values of the map
	 */
	Iterable<V> values();

	/**
	 * Returns the key-value paris ("entries") of the map.
	 * 
	 * @return the entries
	 */
	Iterable<Entry<K, V>> entries();

	/**
	 * Is this map empty?
	 * 
	 * @return true iff this map has no entries
	 */
	boolean isEmpty();

	/**
	 * <p>
	 * Returns a symbolic map equivalent to the given one except that the entry
	 * for the given key is modified or created so to use the given value. An
	 * entry for the given key may or may not exist in the old map. Does not
	 * madify this map.
	 * </p>
	 * 
	 * @param key
	 *            a symbolic expression key
	 * @param value
	 *            a symbolic expression value to associate to that key
	 * @return a map based on the original map but with the given value
	 *         associated to the given key
	 */
	SymbolicMap<K, V> put(K key, V value);

	/**
	 * 
	 * <p>
	 * If this map is mutable, modifies this map by assigning <code>value</code>
	 * to <code>key</code>. Otherwise, behaves just as
	 * {@link #put(SymbolicExpression, SymbolicExpression)}.
	 * </p>
	 * 
	 * @param key
	 *            a symbolic expression key
	 * @param value
	 *            a symbolic expression value to associate to that key
	 * @return a map based on the original map but with the given value
	 *         associated to the given key; will be this map (modified) if this
	 *         map is mutable
	 */
	SymbolicMap<K, V> putMut(K key, V value);

	/**
	 * <p>
	 * Returns a map obtained by removing the entry with the given key, if there
	 * is one. If there is no entry with the given key, the map returned will be
	 * equal to this one.
	 * </p>
	 * 
	 * <p>
	 * If this map is mutable, this method will modify and return this map
	 * itself. Otherwise, a new map will be created if any modifications are
	 * needed.
	 * </p>
	 * 
	 * @param key
	 *            a symbolic expression key
	 * @return a map obtained by removing entry with given key or the original
	 *         map if such an entry is not present
	 */
	SymbolicMap<K, V> remove(K key);

	SymbolicMap<K, V> removeMut(K key);

	/**
	 * <p>
	 * Returns a map obtained by applying the given unary operator to the values
	 * of this map, without changing the keys. If the unary operator returns
	 * <code>null</code> on an element, that entry is removed from the map.
	 * </p>
	 * 
	 * <p>
	 * If this map is mutable, this method will modify and return this map
	 * itself. Otherwise, a new map will be created if any modifications are
	 * needed.
	 * </p>
	 * 
	 * @param operator
	 *            a unary operator on values
	 * @return a map obtained from the given one by applying operator to values
	 */
	SymbolicMap<K, V> apply(UnaryOperator<V> operator);

	SymbolicMap<K, V> applyMut(UnaryOperator<V> operator);

	/**
	 * <p>
	 * Combines that map with this one using the given binary operator. Iterates
	 * over the union of the key sets of the two maps. If a given key exists in
	 * only one map, the value associated to it in the new map is the same as
	 * the old value. If the key exists in two maps, the value is determined by
	 * applying the binary operator to the two old values. If the result of
	 * applying the binary operator is null, the element is removed from the
	 * map.
	 * </p>
	 * 
	 * <p>
	 * Examples:
	 * <ul>
	 * <li>adding polynomials: apply takes two monomials with same monic. adds
	 * coefficients. if coefficient is 0, returns null, else returns monomial
	 * with new coefficient and same monic</li>
	 * 
	 * <li>multiplying monics: apply takes two primitive powers with same
	 * primitive base. adds their exponents. Not possible to get null.</li>
	 * 
	 * <li>multiplying polynomial factorizations: like above</li>
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * If this map is mutable, this method will modify and return this map
	 * itself. Otherwise, a new map will be created if any modifications are
	 * needed. The given map will not be modified in any case (except for
	 * possible commits).
	 * </p>
	 * 
	 * @param operator
	 *            a binary operator which can be applied to the values in the
	 *            maps
	 * @param map
	 *            a symbolic map
	 * @return a map obtained by combining this map and the given map
	 */
	SymbolicMap<K, V> combine(BinaryOperator<V> operator, SymbolicMap<K, V> map);

	SymbolicMap<K, V> combineMut(BinaryOperator<V> operator,
			SymbolicMap<K, V> map);

}
