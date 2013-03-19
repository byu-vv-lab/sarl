package edu.udel.cis.vsl.sarl.collections.IF;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

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

	Comparator<SymbolicCollection<? extends SymbolicExpression>> comparator();

	void setElementComparator(Comparator<SymbolicExpression> c);

	void init();

	<T extends SymbolicExpression> SymbolicCollection<T> basicCollection(
			Collection<T> javaCollection);

	/**
	 * Returns the empty hash set.
	 * 
	 * @return the empty hash set set
	 */
	<T extends SymbolicExpression> SymbolicSet<T> emptyHashSet();

	/**
	 * Returns the empty sorted set using default comparator.
	 * 
	 * @return the empty sorted set
	 */
	<T extends SymbolicExpression> SymbolicSet<T> emptySortedSet();

	/**
	 * Returns the empty sorted set.
	 * 
	 * @param comparator
	 *            Comparator used for sorting
	 * 
	 * @return the empty sorted set
	 */
	<T extends SymbolicExpression> SymbolicSet<T> emptySortedSet(
			Comparator<? super T> comparator);

	/**
	 * Returns the singleton hash set containing the one element.
	 * 
	 * @param element
	 *            a symbolic expression
	 * @return the hash set consisting of that one element
	 */
	<T extends SymbolicExpression> SymbolicSet<T> singletonHashSet(T element);

	/**
	 * Returns the singleton sorted set containing the one element.
	 * 
	 * @param element
	 *            a symbolic expression
	 * @return the sorted set consisting of the one element
	 */
	<T extends SymbolicExpression> SymbolicSet<T> singletonSortedSet(T element);

	/**
	 * Returns the singleton sorted set containing the one element.
	 * 
	 * @param element
	 *            a symbolic expression
	 * @param comparator
	 *            used for sorting
	 * @return the set consisting of that one element
	 */
	<T extends SymbolicExpression> SymbolicSet<T> singletonSortedSet(T element,
			Comparator<? super T> comparator);

	/**
	 * Returns a SymbolicExpressionSequenceIF comprising the given sequence of
	 * elements.
	 * 
	 * @param elements
	 *            any object providing an iterator over SymbolicExpressionIF
	 * @return a single SymbolicExpressionSequenceIF which wraps the given list
	 *         of elements
	 */
	<T extends SymbolicExpression> SymbolicSequence<T> sequence(
			Iterable<? extends T> elements);

	/**
	 * Returns a SymbolicExpressionSequenceIF comprising the sequence of
	 * elements specified as an array.
	 * 
	 * @param elements
	 *            any array of SymbolicExpressionIF
	 * @return a single SymbolicExpressionSequenceIF which wraps the given list
	 *         of elements
	 */
	<T extends SymbolicExpression> SymbolicSequence<T> sequence(T[] elements);

	/**
	 * Returns the sequence of length 1 consisting of the given element.
	 * 
	 * @param element
	 * @return the sequence consisting of just the one element
	 */
	<T extends SymbolicExpression> SymbolicSequence<T> singletonSequence(
			T element);

	/**
	 * Returns the empty sequence.
	 * 
	 * @return the empty sequence
	 */
	<T extends SymbolicExpression> SymbolicSequence<T> emptySequence();

	/**
	 * Returns an empty sorted symbolic map using default comparator on keys.
	 * 
	 * @return an empty sorted symbolic map
	 */
	<K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> emptySortedMap();

	/**
	 * Returns an empty sorted symbolic map using given comparator on keys.
	 * 
	 * @return an empty sorted symbolic map
	 */
	<K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> emptySortedMap(
			Comparator<? super K> comparator);

	/**
	 * Returns an empty hash symbolic map.
	 * 
	 * @return an empty hash symbolic map
	 */
	<K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> emptyHashMap();

	/**
	 * Returns the sorted map with one entry (key,value).
	 * 
	 * @param key
	 *            the key for the entry
	 * @param value
	 *            the value for the entry
	 * @return the map with the one entry
	 */
	<K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> singletonSortedMap(
			K key, V value);

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
	<K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> singletonSortedMap(
			Comparator<? super K> comparator, K key, V value);

	/**
	 * Returns the hash map with one entry (key,value).
	 * 
	 * @param key
	 *            the key for the entry
	 * @param value
	 *            the value for the entry
	 * @return the map with the one entry
	 */
	<K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> singletonHashMap(
			K key, V value);

	/**
	 * Returns a sorted symbolic map based on the given Java Map. The Java map
	 * should not be modified after this method is invoked.
	 * 
	 * @param javaMap
	 * @return a symbolic map based on the given Java map
	 */
	<K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> sortedMap(
			Map<K, V> javaMap);

	/**
	 * Returns a sorted symbolic map based on the given Java Map. The Java map
	 * should not be modified after this method is invoked.
	 * 
	 * @param javaMap
	 * @return a sorted symbolic map based on the given Java map
	 */
	<K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> sortedMap(
			Comparator<? super K> comparator, Map<K, V> javaMap);

	/**
	 * Returns an (unsorted) hash symbolic map based on the given Java Map.
	 * 
	 * @param javaMap
	 *            a Java {@link java.util.Map}
	 * @return an (unsorted) hash symbolic map based on the javaMap
	 */
	<K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> hashMap(
			Map<K, V> javaMap);

}
