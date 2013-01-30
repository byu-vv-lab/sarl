package edu.udel.cis.vsl.sarl.IF.collections;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;

public interface SymbolicSequence extends SymbolicCollection {

	int size();

	/**
	 * The arguments of a sequence expression are always symbolic expressions.
	 */
	SymbolicExpressionIF get(int index);

	/**
	 * Appends an element to the end of a sequence.
	 * 
	 * @param element
	 *            a symbolic expression
	 * @return a sequence identical to the given one except with the given
	 *         element added to the end
	 */
	SymbolicSequence add(SymbolicExpressionIF element);

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
	SymbolicSequence set(int index, SymbolicExpressionIF element);

	/**
	 * Removes the element at position index, shifting all subsequent elements
	 * down one.
	 * 
	 * @param index
	 *            integer in range [0,n-1], where n is length of sequence
	 * @return a sequence obtained from given one by removing the element and
	 *         shifting remaining element down one in index
	 */
	SymbolicSequence remove(int index);

}
