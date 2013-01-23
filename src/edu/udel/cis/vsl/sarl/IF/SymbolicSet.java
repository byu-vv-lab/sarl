package edu.udel.cis.vsl.sarl.IF;

/**
 * A symbolic set represents a set of symbolic expressions. The elements do not
 * have any multiplicity or order, just as in a mathematical set.
 * 
 * @author siegel
 * 
 */
public interface SymbolicSet extends SymbolicCollection {

	/**
	 * Does this set contain the element?
	 * 
	 * @param element
	 *            a symbolic expression
	 * @return true iff this set contains the element
	 */
	boolean contains(SymbolicExpressionIF element);

}
