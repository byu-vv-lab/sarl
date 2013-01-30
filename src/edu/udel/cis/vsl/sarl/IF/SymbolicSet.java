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

	/**
	 * Is this a sorted map?
	 * 
	 * @return
	 */
	boolean isSorted();

	/**
	 * Returns the collection obtained by adding the given element to the given
	 * collection. If the given collection already contains that element, the
	 * collection returned will equal the given one.
	 * 
	 * @param set
	 *            any collection of symbolic expressions
	 * @param element
	 *            any symbolic expression
	 * @return collection obtained by adding element
	 */
	SymbolicSet add(SymbolicExpressionIF element);

	/**
	 * Returns the union of the two sets.
	 * 
	 * @param set0
	 *            a set of symbolic expressions
	 * @param set1
	 *            a set of symbolic expressions
	 * @return their union
	 */
	SymbolicSet addAll(SymbolicSet set);

	/**
	 * Returns the set obtained by removing the given element from a set. If the
	 * given set does not contain the element, the set returns equals the given
	 * one.
	 * 
	 * @param set
	 *            a set of symbolic expressions
	 * @param element
	 *            a symbolic expressions
	 * @return set-{element}
	 */
	SymbolicSet remove(SymbolicExpressionIF element);

	/**
	 * Returns the set set1-set2, i.e., the set consisting of all x in set1 such
	 * that x is not in set2.
	 * 
	 * @param set1
	 *            a set of symbolic expressions
	 * @param set2
	 *            a set of symbolic expressions
	 * @return the set difference, set1-set2
	 */
	SymbolicSet removeAll(SymbolicSet set);

	/**
	 * Returns the intersections of the sets.
	 * 
	 * @param set1
	 *            a set of symbolic expressions
	 * @param set2
	 *            a set of symbolic expressions
	 * @return the intersection of the two sets
	 */
	SymbolicSet keepOnly(SymbolicSet set);
}
