package edu.udel.cis.vsl.sarl.collections.IF;

import java.util.Comparator;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSequence;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSet;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

/**
 * A factory for producing persistent collections. A set is either "hash" or
 * "sorted". The operations on sets produce new sets of the same kind. Ditto for
 * maps; the sorted variety are sorted by key. Sequences are already ordered by
 * definition.
 * 
 * @author siegel
 * 
 */
public interface CollectionFactory {

	Comparator<SymbolicCollection> comparator();

	void setExpressionComparator(Comparator<SymbolicExpression> c);

	void init();

	/**
	 * Returns the empty hash set.
	 * 
	 * @return the empty hash set set
	 */
	SymbolicSet emptyHashSet();

	/**
	 * Returns the empty sorted set using default comparator.
	 * 
	 * @return the empty sorted set
	 */
	SymbolicSet emptySortedSet();

	/**
	 * Returns the empty sorted set.
	 * 
	 * @param comparator
	 *            Comparator used for sorting
	 * 
	 * @return the empty sorted set
	 */
	SymbolicSet emptySortedSet(Comparator<SymbolicExpression> comparator);

	/**
	 * Returns the singleton hash set containing the one element.
	 * 
	 * @param element
	 *            a symbolic expression
	 * @return the hash set consisting of that one element
	 */
	SymbolicSet singletonHashSet(SymbolicExpression element);

	/**
	 * Returns the singleton sorted set containing the one element.
	 * 
	 * @param element
	 *            a symbolic expression
	 * @return the sorted set consisting of the one element
	 */
	SymbolicSet singletonSortedSet(SymbolicExpression element);

	/**
	 * Returns the singleton sorted set containing the one element.
	 * 
	 * @param element
	 *            a symbolic expression
	 * @param comparator
	 *            used for sorting
	 * @return the set consisting of that one element
	 */
	SymbolicSet singletonSortedSet(SymbolicExpression element,
			Comparator<SymbolicExpression> comparator);

	/**
	 * Returns a SymbolicExpressionSequenceIF comprising the given sequence of
	 * elements.
	 * 
	 * @param elements
	 *            any object providing an iterator over SymbolicExpressionIF
	 * @return a single SymbolicExpressionSequenceIF which wraps the given list
	 *         of elements
	 */
	SymbolicSequence sequence(Iterable<? extends SymbolicExpression> elements);

	/**
	 * Returns a SymbolicExpressionSequenceIF comprising the sequence of
	 * elements specified as an array.
	 * 
	 * @param elements
	 *            any array of SymbolicExpressionIF
	 * @return a single SymbolicExpressionSequenceIF which wraps the given list
	 *         of elements
	 */
	SymbolicSequence sequence(SymbolicExpression[] elements);

	/**
	 * Returns the sequence of length 1 consisting of the given element.
	 * 
	 * @param element
	 * @return the sequence consisting of just the one element
	 */
	SymbolicSequence singletonSequence(SymbolicExpression element);

	/**
	 * Returns the empty sequence.
	 * 
	 * @return the empty sequence
	 */
	SymbolicSequence emptySequence();

	/**
	 * Returns an empty sorted symbolic map using default comparator on keys.
	 * 
	 * @return an empty sorted symbolic map
	 */
	SymbolicMap emptySortedMap();

	/**
	 * Returns an empty sorted symbolic map using given comparator on keys.
	 * 
	 * @return an empty sorted symbolic map
	 */
	SymbolicMap emptySortedMap(Comparator<SymbolicExpression> comparator);

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
	SymbolicMap singletonSortedMap(SymbolicExpression key,
			SymbolicExpression value);

	/**
	 * Returns the sorted map with one entry (key,value) and using the given
	 * comparator on keys.
	 * 
	 * @param key
	 *            the key for the entry
	 * @param value
	 *            the value for the entry
	 * @return the map with the one entry
	 */
	SymbolicMap singletonSortedMap(Comparator<SymbolicExpression> comparator,
			SymbolicExpression key, SymbolicExpression value);

	/**
	 * Returns the hash map with one entry (key,value).
	 * 
	 * @param key
	 *            the key for the entry
	 * @param value
	 *            the value for the entry
	 * @return the map with the one entry
	 */
	SymbolicMap singletonHashMap(SymbolicExpression key,
			SymbolicExpression value);

	/**
	 * Returns a sorted symbolic map based on the given Java Map. The Java map
	 * should not be modified after this method is invoked.
	 * 
	 * @param javaMap
	 * @return a symbolic map based on the given Java map
	 */
	SymbolicMap sortedMap(
			Map<SymbolicExpression, SymbolicExpression> javaMap);

	/**
	 * Returns a sorted symbolic map based on the given Java Map. The Java map
	 * should not be modified after this method is invoked.
	 * 
	 * @param javaMap
	 * @return a symbolic map based on the given Java map
	 */
	SymbolicMap sortedMap(Comparator<SymbolicExpression> comparator,
			Map<SymbolicExpression, SymbolicExpression> javaMap);

	/**
	 * Returns an (unsorted) hash symbolic map based on the given Java Map.
	 * 
	 * @param javaMap
	 * @return
	 */
	SymbolicMap hashMap(Map<SymbolicExpression, SymbolicExpression> javaMap);

}
