/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.IF.SortedSymbolicMap;
import edu.udel.cis.vsl.sarl.collections.IF.SortedSymbolicSet;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSet;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;

/**
 * Implementation of CollectionFactory using simple array-based data structures
 * 
 * @author siegel
 * 
 */
public class CommonCollectionFactory implements CollectionFactory {

	private ObjectFactory objectFactory;

	private SymbolicSet<?> emptyHashSet, emptySortedSet;

	private SymbolicMap<?, ?> emptyHashMap, emptySortedMap;

	private SymbolicSequence<?> emptySequence;

	private CollectionComparator comparator;

	private Comparator<SymbolicExpression> elementComparator;

	public CommonCollectionFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
		this.comparator = new CollectionComparator();

		objectFactory.setCollectionComparator(comparator);
	}

	@Override
	public void setElementComparator(Comparator<SymbolicExpression> c) {
		comparator.setElementComparator(c);
		this.elementComparator = c;
	}

	@Override
	public void init() {
		assert elementComparator != null;
		emptySortedMap = objectFactory
				.canonic(new SimpleSortedMap<SymbolicExpression, SymbolicExpression>(
						elementComparator));
		emptySequence = objectFactory
				.canonic(new SimpleSequence<SymbolicExpression>());
		emptySortedSet = objectFactory
				.canonic(new SimpleSortedSet<SymbolicExpression>(
						elementComparator));
		emptyHashSet = emptySortedSet;
		emptyHashMap = emptySortedMap;
	}

	@Override
	public Comparator<SymbolicCollection<? extends SymbolicExpression>> comparator() {
		return comparator;
	}

	@Override
	public <T extends SymbolicExpression> SymbolicCollection<T> basicCollection(
			Collection<T> javaCollection) {
		return new BasicCollection<T>(javaCollection);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends SymbolicExpression> SymbolicSet<T> emptyHashSet() {
		return (SymbolicSet<T>) emptyHashSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends SymbolicExpression> SortedSymbolicSet<T> emptySortedSet() {
		return (SortedSymbolicSet<T>) emptySortedSet;
	}

	@Override
	public <T extends SymbolicExpression> SymbolicSet<T> singletonHashSet(
			T element) {
		SymbolicSet<T> empty = emptyHashSet();

		return empty.add(element);
	}

	@Override
	public <T extends SymbolicExpression> SortedSymbolicSet<T> singletonSortedSet(
			T element) {
		SortedSymbolicSet<T> empty = emptySortedSet();

		return empty.add(element);
	}

	@Override
	public <T extends SymbolicExpression> SymbolicSequence<T> sequence(
			Iterable<? extends T> elements) {
		return new SimpleSequence<T>(elements);
	}

	@Override
	public <T extends SymbolicExpression> SymbolicSequence<T> sequence(
			T[] elements) {
		return new SimpleSequence<T>(elements);
	}

	@Override
	public <T extends SymbolicExpression> SymbolicSequence<T> singletonSequence(
			T element) {
		return new SimpleSequence<T>(element);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends SymbolicExpression> SymbolicSequence<T> emptySequence() {
		return (SymbolicSequence<T>) emptySequence;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K extends SymbolicExpression, V extends SymbolicExpression> SortedSymbolicMap<K, V> emptySortedMap() {
		return (SortedSymbolicMap<K, V>) emptySortedMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> emptyHashMap() {
		return (SymbolicMap<K, V>) emptyHashMap;
	}

	@Override
	public <K extends SymbolicExpression, V extends SymbolicExpression> SortedSymbolicMap<K, V> singletonSortedMap(
			K key, V value) {
		SortedSymbolicMap<K, V> empty = emptySortedMap();

		return empty.put(key, value);
	}

	@Override
	public <K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> singletonHashMap(
			K key, V value) {
		SymbolicMap<K, V> empty = emptyHashMap();

		return empty.put(key, value);
	}

	@Override
	public <K extends SymbolicExpression, V extends SymbolicExpression> SortedSymbolicMap<K, V> sortedMap(
			Map<K, V> javaMap) {
		return new SimpleSortedMap<K, V>(javaMap, elementComparator);
	}

	@Override
	public <K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> hashMap(
			Map<K, V> javaMap) {
		return new SimpleSortedMap<K, V>(javaMap, elementComparator);
	}

	@Override
	public <T extends SymbolicExpression> SortedSymbolicSet<T> emptySortedSet(
			Comparator<? super T> comparator) {
		return new SimpleSortedSet<T>(comparator);
	}

	@Override
	public <T extends SymbolicExpression> SortedSymbolicSet<T> singletonSortedSet(
			T element, Comparator<? super T> comparator) {
		return emptySortedSet(comparator).add(element);
	}

	@Override
	public <K extends SymbolicExpression, V extends SymbolicExpression> SortedSymbolicMap<K, V> emptySortedMap(
			Comparator<? super K> comparator) {
		return new SimpleSortedMap<K, V>(comparator);
	}

	@Override
	public <K extends SymbolicExpression, V extends SymbolicExpression> SortedSymbolicMap<K, V> singletonSortedMap(
			Comparator<? super K> comparator, K key, V value) {
		SortedSymbolicMap<K, V> result = new SimpleSortedMap<K, V>(comparator);

		result = result.put(key, value);
		return result;
	}

	@Override
	public <K extends SymbolicExpression, V extends SymbolicExpression> SortedSymbolicMap<K, V> sortedMap(
			Comparator<? super K> comparator, Map<K, V> javaMap) {
		return new SimpleSortedMap<K, V>(javaMap, comparator);
	}

}
