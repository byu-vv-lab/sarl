package edu.udel.cis.vsl.sarl.collections;

import java.util.Map;

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

	public CommonCollectionFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
		emptyHashSet = (SymbolicSet) objectFactory
				.canonic(new PcollectionsSymbolicSet());
		emptyHashMap = (SymbolicMap) objectFactory
				.canonic(new PcollectionsSymbolicMap());
		emptySortedMap = (SymbolicMap) objectFactory
				.canonic(new CljSortedSymbolicMap());
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
		return new CljSortedSymbolicMap(javaMap);
	}

	@Override
	public SymbolicMap hashMap(
			Map<SymbolicExpressionIF, SymbolicExpressionIF> javaMap) {
		return new PcollectionsSymbolicMap(javaMap);
	}

}
