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
package edu.udel.cis.vsl.sarl.collections.IF;

import edu.udel.cis.vsl.sarl.IF.Transform;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

public interface SymbolicSequence<T extends SymbolicExpression> extends
		SymbolicCollection<T> {

	int size();

	/**
	 * The arguments of a sequence expression are always symbolic expressions.
	 */
	T get(int index);

	/**
	 * Appends an element to the end of a sequence.
	 * 
	 * @param element
	 *            a symbolic expression
	 * @return a sequence identical to the given one except with the given
	 *         element added to the end
	 */
	SymbolicSequence<T> add(T element);

	/**
	 * Sets the element at specified position. Sequence must have length at
	 * least index+1.
	 * 
	 * @param index
	 *            integer in range [0,n-1], where n is length of sequence
	 * @param element
	 *            a symbolic expression
	 * @return a sequence identical to old except that element in position index
	 *         now has value element
	 */
	SymbolicSequence<T> set(int index, T element);

	/**
	 * Removes the element at position index, shifting all subsequent elements
	 * down one.
	 * 
	 * @param index
	 *            integer in range [0,n-1], where n is length of sequence
	 * @return a sequence obtained from given one by removing the element and
	 *         shifting remaining element down one in index
	 */
	SymbolicSequence<T> remove(int index);

	/**
	 * If index is less than the original size s, same as set. Otherwise returns
	 * a sequence of length index+1, with the elements in positions s, s+1, ...,
	 * index-1 set to filler, and the element in position index set to value,
	 * and all other elements as in the original.
	 * 
	 * @param index
	 *            position to set
	 * @param value
	 *            new value for element at position
	 * @param filler
	 *            element to be inserted in newly created empty slots
	 * @return a new sequence, possibly extended with filler, in which element
	 *         at position index is value
	 */
	SymbolicSequence<T> setExtend(int index, T value, T filler);

	/**
	 * Returns the subsequence whose first element is the element at position
	 * start and last element is the element at position end-1. The length of
	 * the subsequence is therefore end-start.
	 * 
	 * @param start
	 *            index of first element
	 * @param end
	 *            one more than then index of last element
	 * @return the subsequence
	 */
	SymbolicSequence<T> subSequence(int start, int end);

	<U extends SymbolicExpression> SymbolicSequence<U> apply(
			Transform<T, U> transform);

}
