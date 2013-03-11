package edu.udel.cis.vsl.sarl.collections.IF;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

/**
 * A symbolic set represents a set of symbolic expressions. The elements do not
 * have any multiplicity or order, just as in a mathematical set.
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
	 * Is this a sorted map?
	 * 
	 * @return
	 */
	boolean isSorted();

	/**
	 * If the set is sorted, returns the compartor used for sorting, else
	 * returns null.
	 * 
	 * @return the comparator or null
	 */
	Comparator<T> comparator();

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
	SymbolicSet<T> add(T element);

	/**
	 * Returns the union of the two sets.
	 * 
	 * @param set0
	 *            a set of symbolic expressions
	 * @param set1
	 *            a set of symbolic expressions
	 * @return their union
	 */
	SymbolicSet<T> addAll(SymbolicSet<? extends T> set);

	// SymbolicSet<SymbolicExpression> expand();

	// SymbolicSet<SymbolicExpression> addAllAnyKind(SymbolicSet<?> set);

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
	SymbolicSet<T> remove(T element);

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
	SymbolicSet<T> removeAll(SymbolicSet<? extends T> set);

	/**
	 * Returns the intersections of the sets.
	 * 
	 * @param set1
	 *            a set of symbolic expressions
	 * @param set2
	 *            a set of symbolic expressions
	 * @return the intersection of the two sets
	 */
	SymbolicSet<T> keepOnly(SymbolicSet<? extends T> set);
}
