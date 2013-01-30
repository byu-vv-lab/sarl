package edu.udel.cis.vsl.sarl.collections;

import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.BinaryOperatorIF;
import edu.udel.cis.vsl.sarl.IF.UnaryOperatorIF;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSequence;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSet;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;

public interface CollectionFactory {

	/**
	 * Returns the empty set.
	 * 
	 * @return the empty set
	 */
	SymbolicSet emptySet();

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
	SymbolicSet add(SymbolicSet set, SymbolicExpressionIF element);

	/**
	 * Returns the union of the two sets.
	 * 
	 * @param set0
	 *            a set of symbolic expressions
	 * @param set1
	 *            a set of symbolic expressions
	 * @return their union
	 */
	SymbolicSet union(SymbolicSet set0, SymbolicSet set1);

	/**
	 * Returns the singleton set containing the one element.
	 * 
	 * @param element
	 *            a symbolic expression
	 * @return the set consisting of that one element
	 */
	SymbolicSet singleton(SymbolicExpressionIF element);

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
	SymbolicSet remove(SymbolicSet set, SymbolicExpressionIF element);

	/**
	 * Returns the intersections of the sets.
	 * 
	 * @param set1
	 *            a set of symbolic expressions
	 * @param set2
	 *            a set of symbolic expressions
	 * @return the intersection of the two sets
	 */
	SymbolicSet intersection(SymbolicSet set1, SymbolicSet set2);

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
	SymbolicSet removeAll(SymbolicSet set1, SymbolicSet set2);

	/**
	 * Returns a SymbolicExpressionSequenceIF comprising the given sequence of
	 * elements.
	 * 
	 * @param elements
	 *            any object providing an iterator over SymbolicExpressionIF
	 * @return a single SymbolicExpressionSequenceIF which wraps the given list
	 *         of elements
	 */
	SymbolicSequence sequence(Iterable<? extends SymbolicExpressionIF> elements);

	/**
	 * Returns a SymbolicExpressionSequenceIF comprising the sequence of
	 * elements specified as an array.
	 * 
	 * @param elements
	 *            any array of SymbolicExpressionIF
	 * @return a single SymbolicExpressionSequenceIF which wraps the given list
	 *         of elements
	 */
	SymbolicSequence sequence(SymbolicExpressionIF[] elements);

	/**
	 * Returns the sequence of length 1 consisting of the given element.
	 * 
	 * @param element
	 * @return the sequence consisting of just the one element
	 */
	SymbolicSequence singletonSequence(SymbolicExpressionIF element);

	/**
	 * Returns the empty sequence.
	 * 
	 * @return the empty sequence
	 */
	SymbolicSequence emptySequence();

	/**
	 * Appends an element to the end of a sequence.
	 * 
	 * @param sequence
	 *            a symbolic sequence
	 * @param element
	 *            a symbolic expression
	 * @return a sequence identical to the given one except with the given
	 *         element added to the end
	 */
	SymbolicSequence add(SymbolicSequence sequence, SymbolicExpressionIF element);

	/**
	 * Sets the element at specified position. Sequence must have length at
	 * least index+1.
	 * 
	 * @param sequence
	 *            a symbolic sequence
	 * @param index
	 *            integer in range [0,n-1], where n is length of sequence
	 * @param element
	 *            a symbolic expression
	 * @return a sequence identical to old except that element in position index
	 *         now has value element
	 */
	SymbolicSequence set(SymbolicSequence sequence, int index,
			SymbolicExpressionIF element);

	/**
	 * Removes the element at position index, shifting all subsequent elements
	 * down one.
	 * 
	 * @param sequence
	 *            a symoblic sequence
	 * @param index
	 *            integer in range [0,n-1], where n is length of sequence
	 * @return a sequence obtained from given one by removing the element and
	 *         shifting remaining element down one in index
	 */
	SymbolicSequence remove(SymbolicSequence sequence, int index);

	/**
	 * Returns an empty sorted symbolic map.
	 * 
	 * @return an empty sorted symbolic map
	 */
	SymbolicMap emptySortedMap();

	/**
	 * Returns an empty hash symbolic map.
	 * 
	 * @return an empty hash symbolic map
	 */
	SymbolicMap emptyHashMap();

	/**
	 * Returns the sorted map with one entry (key,value).
	 * 
	 * @param key
	 *            the key for the entry
	 * @param value
	 *            the value for the entry
	 * @return the map with the one entry
	 */
	SymbolicMap singletonSortedMap(SymbolicExpressionIF key,
			SymbolicExpressionIF value);

	/**
	 * Returns the hash map with one entry (key,value).
	 * 
	 * @param key
	 *            the key for the entry
	 * @param value
	 *            the value for the entry
	 * @return the map with the one entry
	 */
	SymbolicMap singletonHashMap(SymbolicExpressionIF key,
			SymbolicExpressionIF value);

	/**
	 * Returns a symbolic map equivalent to the given one except that the entry
	 * for the given key is modified or created so to use the given value. An
	 * entry for the given key may or may not exist in the old map.
	 * 
	 * @param map
	 *            a symbolic map
	 * @param key
	 *            a symbolic expression key
	 * @param value
	 *            a symbolic expression value to associate to that key
	 * @return a map based on the original map but with the given value
	 *         associated to the given key
	 */
	SymbolicMap put(SymbolicMap map, SymbolicExpressionIF key,
			SymbolicExpressionIF value);

	/**
	 * Remove entry with given key (noop if key is not present in map).
	 * 
	 * @param map
	 *            a symbolic map
	 * @param key
	 *            a symbolic expression key
	 * @return a map obtained by removing entry with given key or the original
	 *         map if such an entry is not present
	 */
	SymbolicMap remove(SymbolicMap map, SymbolicExpressionIF key);

	/**
	 * Returns a sorted symbolic map based on the given Java Map. The Java map
	 * should not be modified after this method is invoked.
	 * 
	 * @param javaMap
	 * @return a symbolic map based on the given Java map
	 */
	SymbolicMap sortedMap(
			Map<SymbolicExpressionIF, SymbolicExpressionIF> javaMap);

	/**
	 * Returns an (unsorted) hash symbolic map based on the given Java Map.
	 * 
	 * @param javaMap
	 * @return
	 */
	SymbolicMap hashMap(Map<SymbolicExpressionIF, SymbolicExpressionIF> javaMap);

	/**
	 * Returns a map obtained by applying the given unary operator to the values
	 * of the given map, without changing the keys. If the unary operator
	 * returns null on an element, that entry is removed from the map.
	 * 
	 * @param map
	 *            a symbolic map
	 * @param operator
	 *            a unary operator on values
	 * @return a map obtained from the given one by applying operator to values
	 */
	SymbolicMap apply(SymbolicMap map, UnaryOperatorIF operator);

	/**
	 * Combines the two maps into a single map using the given binary operator.
	 * Iterates over the union of the key sets of the two maps. If a given key
	 * exists in only one map, the value associated to it in the new map is the
	 * same as the old value. If the key exists in two maps, the value is
	 * determined by applying the binary operator to the two old values. If the
	 * result of applying the binary operator is null, the element is removed
	 * from the map.
	 * 
	 * Examples:
	 * <ul>
	 * <li>adding polynomials: apply takes two monomials with same monic. adds
	 * coefficients. if coefficient is 0, returns null, else returns monomial
	 * with new coefficient and same monic</li>
	 * 
	 * <li>multiplying monics: apply takes two primitive powers with same
	 * primitive base. adds their exponents. Not possible to get null.</li>
	 * 
	 * <li>multiplying polynomial factorizations: like above</li>
	 * </ul>
	 * 
	 * Eventually would like efficient persistent implementation as in Clojure.
	 * 
	 * @param operator
	 *            a binary operator which can be applied to the values in the
	 *            maps
	 * @param map1
	 *            a symbolic map
	 * @param map2
	 *            a symbolic map
	 * @return a map obtained by combining the two given maps
	 */
	SymbolicMap combine(BinaryOperatorIF operator, SymbolicMap map1,
			SymbolicMap map2);

}
