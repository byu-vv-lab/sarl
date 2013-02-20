package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Comparator;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSequence;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSet;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;

public class CommonCollectionFactory implements CollectionFactory {

	private ObjectFactory objectFactory;

	private SymbolicSet emptyHashSet, emptySortedSet;

	private SymbolicMap emptyHashMap, emptySortedMap;

	private SymbolicSequence emptySequence;

	private CollectionComparator comparator;

	private Comparator<SymbolicExpressionIF> expressionComparator;

	public CommonCollectionFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
		this.comparator = new CollectionComparator();
		emptyHashSet = (SymbolicSet) objectFactory
				.canonic(new PcollectionsSymbolicSet());
		emptyHashMap = (SymbolicMap) objectFactory
				.canonic(new PcollectionsSymbolicMap());
	}

	@Override
	public void setExpressionComparator(Comparator<SymbolicExpressionIF> c) {
		comparator.setExpressionComparator(c);
		this.expressionComparator = c;
	}

	@Override
	public void init() {
		assert expressionComparator != null;
		emptySortedMap = (SymbolicMap) objectFactory
				.canonic(new CljSortedSymbolicMap(expressionComparator));
		emptySequence = new PcollectionsSymbolicSequence();
		// etc.
	}

	@Override
	public Comparator<SymbolicCollection> comparator() {
		return comparator;
	}

	@Override
	public SymbolicSet emptyHashSet() {
		return emptyHashSet;
	}

	@Override
	public SymbolicSet emptySortedSet() {
		return emptySortedSet;
	}

	@Override
	public SymbolicSet singletonHashSet(SymbolicExpressionIF element) {
		return emptyHashSet.add(element);
	}

	@Override
	public SymbolicSet singletonSortedSet(SymbolicExpressionIF element) {
		return emptySortedSet.add(element);
	}

	@Override
	public SymbolicSequence sequence(
			Iterable<? extends SymbolicExpressionIF> elements) {
		return new PcollectionsSymbolicSequence(elements);
	}

	@Override
	public SymbolicSequence sequence(SymbolicExpressionIF[] elements) {
		return new PcollectionsSymbolicSequence(elements);
	}

	@Override
	public SymbolicSequence singletonSequence(SymbolicExpressionIF element) {
		return new PcollectionsSymbolicSequence(element);
	}

	@Override
	public SymbolicSequence emptySequence() {
		return emptySequence;
	}

	@Override
	public SymbolicMap emptySortedMap() {
		return emptySortedMap;
	}

	@Override
	public SymbolicMap emptyHashMap() {
		return emptyHashMap;
	}

	@Override
	public SymbolicMap singletonSortedMap(SymbolicExpressionIF key,
			SymbolicExpressionIF value) {
		return emptySortedMap.put(key, value);
	}

	@Override
	public SymbolicMap singletonHashMap(SymbolicExpressionIF key,
			SymbolicExpressionIF value) {
		return emptyHashMap.put(key, value);
	}

	@Override
	public SymbolicMap sortedMap(
			Map<SymbolicExpressionIF, SymbolicExpressionIF> javaMap) {
		return new CljSortedSymbolicMap(javaMap, expressionComparator);
	}

	@Override
	public SymbolicMap hashMap(
			Map<SymbolicExpressionIF, SymbolicExpressionIF> javaMap) {
		return new PcollectionsSymbolicMap(javaMap);
	}

	@Override
	public SymbolicSet emptySortedSet(
			Comparator<SymbolicExpressionIF> comparator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicSet singletonSortedSet(SymbolicExpressionIF element,
			Comparator<SymbolicExpressionIF> comparator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicMap emptySortedMap(
			Comparator<SymbolicExpressionIF> comparator) {
		return new CljSortedSymbolicMap(comparator);
	}

	@Override
	public SymbolicMap singletonSortedMap(
			Comparator<SymbolicExpressionIF> comparator,
			SymbolicExpressionIF key, SymbolicExpressionIF value) {
		SymbolicMap result = new CljSortedSymbolicMap(comparator);

		result = result.put(key, value);
		return result;
	}

	@Override
	public SymbolicMap sortedMap(Comparator<SymbolicExpressionIF> comparator,
			Map<SymbolicExpressionIF, SymbolicExpressionIF> javaMap) {
		return new CljSortedSymbolicMap(javaMap, comparator);
	}

}
