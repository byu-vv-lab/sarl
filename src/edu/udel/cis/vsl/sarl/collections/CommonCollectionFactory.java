package edu.udel.cis.vsl.sarl.collections;

import java.util.Comparator;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSequence;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSet;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.object.ObjectFactory;

public class CommonCollectionFactory implements CollectionFactory {

	private ObjectFactory objectFactory;

	private SymbolicSet emptyHashSet, emptySortedSet;

	private SymbolicMap emptyHashMap, emptySortedMap;

	private SymbolicSequence emptySequence;

	private Comparator<SymbolicCollection> collectionComparator;

	private Comparator<SymbolicExpressionIF> expressionComparator;

	public CommonCollectionFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
		this.collectionComparator = new CollectionComparator();
		emptyHashSet = (SymbolicSet) objectFactory
				.canonic(new PcollectionsSymbolicSet());
		emptyHashMap = (SymbolicMap) objectFactory
				.canonic(new PcollectionsSymbolicMap());
	}

	public void init() {
		expressionComparator = objectFactory.comparator()
				.expressionComparator();
		emptySortedMap = (SymbolicMap) objectFactory
				.canonic(new CljSortedSymbolicMap(expressionComparator));
	}

	public Comparator<SymbolicCollection> collectionComparator() {
		return collectionComparator;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicSequence sequence(SymbolicExpressionIF[] elements) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicSequence singletonSequence(SymbolicExpressionIF element) {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public CollectionComparator newCollectionComparator() {
		return new CollectionComparator();
	}

}
