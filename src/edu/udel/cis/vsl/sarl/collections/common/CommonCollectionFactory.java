package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSequence;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSet;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;

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
		emptyHashSet = objectFactory
				.canonic(new PcollectionsSymbolicSet<SymbolicExpression>());
		emptyHashMap = objectFactory
				.canonic(new PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>());
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
				.canonic(new CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>(
						elementComparator));
		emptySequence = objectFactory
				.canonic(new PcollectionsSymbolicSequence<SymbolicExpression>());
		// etc.
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
	public <T extends SymbolicExpression> SymbolicSet<T> emptySortedSet() {
		return (SymbolicSet<T>) emptySortedSet;
	}

	@Override
	public <T extends SymbolicExpression> SymbolicSet<T> singletonHashSet(
			T element) {
		SymbolicSet<T> empty = emptyHashSet();

		return empty.add(element);
	}

	@Override
	public <T extends SymbolicExpression> SymbolicSet<T> singletonSortedSet(
			T element) {
		SymbolicSet<T> empty = emptySortedSet();

		return empty.add(element);
	}

	@Override
	public <T extends SymbolicExpression> SymbolicSequence<T> sequence(
			Iterable<? extends T> elements) {
		return new PcollectionsSymbolicSequence<T>(elements);
	}

	@Override
	public <T extends SymbolicExpression> SymbolicSequence<T> sequence(
			T[] elements) {
		return new PcollectionsSymbolicSequence<T>(elements);
	}

	@Override
	public <T extends SymbolicExpression> SymbolicSequence<T> singletonSequence(
			T element) {
		return new PcollectionsSymbolicSequence<T>(element);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends SymbolicExpression> SymbolicSequence<T> emptySequence() {
		return (SymbolicSequence<T>) emptySequence;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> emptySortedMap() {
		return (SymbolicMap<K, V>) emptySortedMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> emptyHashMap() {
		return (SymbolicMap<K, V>) emptyHashMap;
	}

	@Override
	public <K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> singletonSortedMap(
			K key, V value) {
		SymbolicMap<K, V> empty = emptySortedMap();

		return empty.put(key, value);
	}

	@Override
	public <K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> singletonHashMap(
			K key, V value) {
		SymbolicMap<K, V> empty = emptyHashMap();

		return empty.put(key, value);
	}

	@Override
	public <K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> sortedMap(
			Map<K, V> javaMap) {
		return new CljSortedSymbolicMap<K, V>(javaMap, elementComparator);
	}

	@Override
	public <K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> hashMap(
			Map<K, V> javaMap) {
		return new PcollectionsSymbolicMap<K, V>(javaMap);
	}

	@Override
	public <T extends SymbolicExpression> SymbolicSet<T> emptySortedSet(
			Comparator<? super T> comparator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends SymbolicExpression> SymbolicSet<T> singletonSortedSet(
			T element, Comparator<? super T> comparator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> emptySortedMap(
			Comparator<? super K> comparator) {
		return new CljSortedSymbolicMap<K, V>(comparator);
	}

	@Override
	public <K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> singletonSortedMap(
			Comparator<? super K> comparator, K key, V value) {
		SymbolicMap<K, V> result = new CljSortedSymbolicMap<K, V>(comparator);

		result = result.put(key, value);
		return result;
	}

	@Override
	public <K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> sortedMap(
			Comparator<? super K> comparator, Map<K, V> javaMap) {
		return new CljSortedSymbolicMap<K, V>(javaMap, comparator);
	}

}
