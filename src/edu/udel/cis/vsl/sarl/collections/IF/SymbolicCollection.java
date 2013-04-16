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
 * An immutable collection of symbolic expressions. Though immutable, these
 * collections support analogues of the usual mutation operations that return
 * new collections instead of modifying the collection. This is sometimes
 * refered to as a "persistent" collection.
 * 
 * @author siegel
 * 
 * @param <T>
 *            the subtype of SymbolicExpression to which each element of this
 *            collection must belong
 */
public interface SymbolicCollection<T extends SymbolicExpression> extends
		SymbolicObject, Iterable<T> {

	/**
	 * The various kinds of symbolic expression collections.
	 */
	public enum SymbolicCollectionKind {
		/**
		 * A basic collection that does not support any additional operations
		 * beyond those provided by this interface.
		 */
		BASIC,
		/**
		 * A set, i.e., a collection where each element can occur at most one
		 * time. A collection of this kind can be cast to
		 * {@link edu.udel.cis.vsl.sarl.collections.IF.SymbolicSet}. The set may
		 * be ordered or unordered.
		 */
		SET,
		/**
		 * A finite sequence of symbolic expressions. Can be cast to
		 * {@link edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence}.
		 */
		SEQUENCE,
		/**
		 * A collection of symbolic expressions which are "indexed" by symbolic
		 * expression keys. This is essentially a map from
		 * {@link SymbolicExpression} to {@link SymbolicExpression}, where the
		 * collection of values of the map are considered to be the elements of
		 * the collection. Can be cast to
		 * {@link edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap}.
		 */
		MAP
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

}
