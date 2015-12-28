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
 * <p>
 * A symbolic set represents a set of symbolic expressions. The elements do not
 * have any multiplicity, just as in a mathematical set.
 * </p>
 * 
 * <p>
 * A set may be sorted or unsorted. A sorted set is an instance of
 * {@link SortedSymbolicSet} and an unsorted set is not. A sorted set uses a
 * {@link Comparator} to order the elements of the set from least to greatest.
 * Note that a sorted set and an unsorted set can never be equal (even if they
 * contain the same elements).
 * </p>
 * 
 * @author siegel
 * 
 */
public interface SymbolicSet<T extends SymbolicExpression> extends
		SymbolicCollection<T> {

	/**
	 * Does this set contain the element?
	 * 
	 * @param element
	 *            a symbolic expression
	 * @return true iff this set contains the element
	 */
	boolean contains(T element);

	/**
	 * Returns the set obtained by adding the given element to this set. If this
	 * already contains that element, the set returned will equal this.
	 * 
	 * @param element
	 *            any element of <code>T</code>
	 * @return set obtained by adding element to this
	 */
	SymbolicSet<T> add(T element);

	/**
	 * Returns the set obtained by removing the given element from this set. If
	 * this does not contain the element, the set returned equals this.
	 * 
	 * @param element
	 *            any element of <code>T</code>
	 * @return this-{element}
	 */
	SymbolicSet<T> remove(T element);

	/**
	 * Returns the set obtained by adding all of the elements in the given set
	 * to this set.
	 * 
	 * @param set
	 *            a set consisting of elements of <code>T</code>
	 * @return the set which is the union of this and <code>set</code>
	 */
	SymbolicSet<T> addAll(SymbolicSet<? extends T> set);

	/**
	 * Returns the set which is the set difference of this set and the given
	 * set, i.e., the set consisting of all x in this such that x is not in
	 * <code>set</code>.
	 * 
	 * @param set
	 *            a set consisting of elements of <code>T</code>
	 * @return the set difference, set1-set2
	 */
	SymbolicSet<T> removeAll(SymbolicSet<? extends T> set);

	/**
	 * Returns the set which is the intersection of this set with the given one.
	 * 
	 * @param set
	 *            a set consisting of elements of <code>T</code>
	 * @return the intersection of the two sets
	 */
	SymbolicSet<T> keepOnly(SymbolicSet<? extends T> set);

}
