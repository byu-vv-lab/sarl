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

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

/**
 * A symbolic set in which the elements are ordered according to some
 * {@link Comparator}.
 * 
 * @author Stephen F. Siegel
 *
 * @param <T>
 *            the type of elements in the set
 */
public interface SortedSymbolicSet<T extends SymbolicExpression> extends
		SymbolicSet<T> {

	/**
	 * Returns the comparator used for sorting the elements.
	 *
	 * @return the comparator
	 */
	Comparator<T> comparator();

	/**
	 * <p>
	 * Returns the set obtained by adding the given element to this set. If this
	 * already contains that element, the set returned will equal this.
	 * </p>
	 * 
	 * <p>
	 * If this set is mutable, this method will modify and return this set.
	 * Otherwise, if any modifications are needed, a new set will be created and
	 * returned.
	 * </p>
	 *
	 * @param element
	 *            any element of <code>T</code>
	 * @return set obtained by adding element to this
	 */
	@Override
	SortedSymbolicSet<T> add(T element);

	/**
	 * <p>
	 * Returns the set obtained by removing the given element from this set. If
	 * this does not contain the element, the set returned equals this.
	 * </p>
	 * 
	 * <p>
	 * If this set is mutable, this method will modify and return this set.
	 * Otherwise, if any modifications are needed, a new set will be created and
	 * returned.
	 * </p>
	 *
	 * @param element
	 *            any element of <code>T</code>
	 * @return this-{element}
	 */
	@Override
	SortedSymbolicSet<T> remove(T element);

	/**
	 * <p>
	 * Returns the set obtained by adding all of the elements in the given set
	 * to this set.
	 * </p>
	 *
	 * <p>
	 * If this set is mutable, this method may modify and return this set.
	 * Otherwise, if any modifications are needed, a new set will be created and
	 * returned.
	 * </p>
	 * 
	 * @param set
	 *            a set consisting of elements of <code>T</code>
	 * @return the set which is the union of this and <code>set</code>
	 */
	@Override
	SortedSymbolicSet<T> addAll(SymbolicSet<? extends T> set);

	/**
	 * <p>
	 * Returns the set which is the set difference of this set and the given
	 * set, i.e., the set consisting of all x in this such that x is not in
	 * <code>set</code>.
	 * </p>
	 * 
	 * <p>
	 * If this set is mutable, this method may modify and return this set.
	 * Otherwise, if any modifications are needed, a new set will be created and
	 * returned.
	 * </p>
	 *
	 * @param set
	 *            a set consisting of elements of <code>T</code>
	 * @return the set difference, set1-set2
	 */
	@Override
	SortedSymbolicSet<T> removeAll(SymbolicSet<? extends T> set);

	/**
	 * <p>
	 * Returns the set which is the intersection of this set with the given one.
	 * </p>
	 * 
	 * <p>
	 * If this set is mutable, this method may modify and return this set.
	 * Otherwise, if any modifications are needed, a new set will be created and
	 * returned.
	 * </p>
	 * 
	 * @param set
	 *            a set consisting of elements of <code>T</code>
	 * @return the intersection of the two sets
	 */
	@Override
	SortedSymbolicSet<T> keepOnly(SymbolicSet<? extends T> set);

}
