package edu.udel.cis.vsl.sarl.IF.collections;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

public interface SymbolicSequence extends SymbolicCollection {

	int size();

	/**
	 * The arguments of a sequence expression are always symbolic expressions.
	 */
	SymbolicExpression get(int index);

	/**
	 * Appends an element to the end of a sequence.
	 * 
	 * @param element
	 *            a symbolic expression
	 * @return a sequence identical to the given one except with the given
	 *         element added to the end
	 */
	SymbolicSequence add(SymbolicExpression element);

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
	SymbolicSequence set(int index, SymbolicExpression element);

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
	SymbolicSequence setExtend(int index, SymbolicExpression value,
			SymbolicExpression filler);

}
