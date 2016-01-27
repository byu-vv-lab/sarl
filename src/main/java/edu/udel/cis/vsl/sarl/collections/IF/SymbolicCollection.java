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

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

/**
 * <p>
 * An immutable collection of symbolic expressions. Though immutable, these
 * collections support analogs of the usual mutation operations that return new
 * collections instead of modifying the collection. This is sometimes referred
 * to as a "persistent" collection.
 * </p>
 * 
 * <p>
 * Equality: for two symbolic collections to be equal, they must have the same
 * {@link SymbolicCollectionKind}. Further requirements for each kind:
 * <ul>
 * <li>{@link SymbolicCollectionKind#BASIC}: the underlying collections must be equal,</li>
 * <li>{@link SymbolicCollectionKind#SORTED_SET} and {@link SymbolicCollectionKind#UNSORTED_SET}:
 * either both are sorted, or both are unsorted; furthermore,
 * they define the same set of symbolic expressions,</li>
 * <li>{@link SymbolicCollectionKind#SEQUENCE}: both define the same sequence of elements, i.e., they
 * have the same size and the same elements occur in the same order in both;</li>
 * <li>{@link SymbolicCollectionKind#SORTED_MAP} and {@link SymbolicCollectionKind#UNSORTED_MAP}:
 * either both are sorted, or both are unsorted; furthermore,
 * they define the same set of key-value pairs.</li>
 * </ul>
 * As can be seen from the above requirements, it is usually not advisable to
 * mix different sorts of symbolic collections in the same context, because
 * equality will not behave as you would expect. For example, two symbolic sets
 * which contain the same elements may be unequal, because one is sorted and the
 * other is not.
 * </p>
 * 
 * @author siegel
 * 
 * @param <T>
 *            the subtype of {@link SymbolicExpression} to which each element of
 *            this collection must belong
 */
public interface SymbolicCollection<T extends SymbolicExpression> extends
		SymbolicObject, Iterable<T> {

	/**
	 * The various kinds of symbolic expression collections.
	 */
	enum SymbolicCollectionKind {
		/**
		 * A basic collection that does not support any additional operations
		 * beyond those provided by this interface.
		 */
		BASIC,
		/**
		 * A sorted set, i.e., a collection where each element can occur at most
		 * one time, and those elements are ordered. A collection of this kind
		 * can be cast to {@link SortedSymbolicSet}.
		 */
		SORTED_SET,
		/**
		 * An unsorted set, i.e., a collection in which each element can occur
		 * at most once, just like a mathematical set. A collection of this kind
		 * can be cast to {@link SymbolicSet}.
		 */
		UNSORTED_SET,
		/**
		 * A finite sequence of symbolic expressions. Can be cast to
		 * {@link SymbolicSequence}.
		 */
		SEQUENCE,
		/**
		 * A collection of symbolic expressions which are "indexed" by symbolic
		 * expression keys, and those keys are ordered. This is essentially a
		 * map from {@link SymbolicExpression} to {@link SymbolicExpression},
		 * where the collection of values of the map are considered to be the
		 * elements of the collection. Can be cast to {@link SortedSymbolicMap}.
		 */
		SORTED_MAP,
		/**
		 * A collection of symbolic expressions which are "indexed" by symbolic
		 * expression keys. This is essentially a map from
		 * {@link SymbolicExpression} to {@link SymbolicExpression}, where the
		 * collection of values of the map are considered to be the elements of
		 * the collection. Can be cast to {@link SymbolicMap}.
		 */
		UNSORTED_MAP
	}

	/**
	 * Returns the number of elements in the collection.
	 * 
	 * @return the number of elements in the collection
	 */
	int size();

	/**
	 * Returns the kind of this collection.
	 * 
	 * @return the kind of this collection
	 */
	SymbolicCollectionKind collectionKind();

	/**
	 * Returns the "first" element of this collection or null if the collection
	 * is empty. For ordered collections, first means what you expect; for
	 * unordered collections it is some fixed element.
	 * 
	 * @return the first element.
	 */
	T getFirst();

	/**
	 * The hash code of a symbolic collection is specified as follows: for any
	 * kind other than a map, it is the bitwise exclusive or of the hash code of
	 * the kind and the hash codes of the elements of the collection. For a map,
	 * the hash code is the bitwise exclusive or of the hash codes of the kind
	 * and the hash codes of the values of the map. (This might change.)
	 */
	@Override
	int hashCode();

	/**
	 * The equals method is specified as follows: first, obj must be an instance
	 * of {@link SymbolicCollectionKind}. Second, the kinds of the two
	 * collections must be equal. For sets, the two sets must be equals as sets
	 * (using the "equals" method on the elements of the sets). For sequences,
	 * the two sequences must be equal as sequences. For maps, the two maps must
	 * be equal as maps: i.e., they each specify the same set of key-value
	 * pairs.
	 */
	@Override
	boolean equals(Object obj);

}
