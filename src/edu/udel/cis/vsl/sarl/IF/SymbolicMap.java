package edu.udel.cis.vsl.sarl.IF;

import java.util.Map.Entry;

/**
 * Map is interpreted as a collection of values. The keys are used for other
 * purposes. So, for example, ADD followed by map argument represents sum of
 * values in the map.
 * 
 * Examples of use:
 * 
 * Keys: monics, values: monomials. ADD applied to this represents a polynomial.
 * The polynomial allows fast lookup of coefficient for any given monomial
 * 
 * Keys:primitives, values: primitive-powers. MULTIPLY applied to this map
 * represents a monic.
 * 
 * Keys: polynomials, values: polynomial-powers. MULTIPLY applies to this map
 * represents a factorization of a polynomial.
 * 
 * Just need efficient ways to modify in immutable way!
 * 
 * @author siegel
 * 
 */
public interface SymbolicMap extends SymbolicCollection {

	/**
	 * Gets the value associated to the given key, or returns null if there is
	 * no entry for that key
	 * 
	 * @param key
	 *            the key
	 * @return value associated to key
	 */
	SymbolicExpressionIF get(SymbolicExpressionIF key);

	/**
	 * Returns the keys of the map.
	 * 
	 * @return the keys
	 */
	Iterable<SymbolicExpressionIF> keys();

	/**
	 * Same as elements().
	 * 
	 * @return the values of the map
	 */
	Iterable<SymbolicExpressionIF> values();

	/**
	 * Returns the key-value paris ("entries") of the map.
	 * 
	 * @return the entries
	 */
	Iterable<Entry<SymbolicExpressionIF, SymbolicExpressionIF>> entries();
}
