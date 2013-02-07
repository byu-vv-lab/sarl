package edu.udel.cis.vsl.sarl.collections;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

import edu.udel.cis.vsl.sarl.IF.BinaryOperatorIF;
import edu.udel.cis.vsl.sarl.IF.UnaryOperatorIF;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.object.ObjectFactory;

public class PcollectionsSymbolicMap extends CommonSymbolicCollection implements
		SymbolicMap {

	private PMap<SymbolicExpressionIF, SymbolicExpressionIF> pmap;

	PcollectionsSymbolicMap(
			PMap<SymbolicExpressionIF, SymbolicExpressionIF> pmap) {
		super(SymbolicCollectionKind.MAP);
		this.pmap = pmap;
	}

	PcollectionsSymbolicMap() {
		super(SymbolicCollectionKind.MAP);
		this.pmap = HashTreePMap.empty();
	}

	PcollectionsSymbolicMap(
			Map<SymbolicExpressionIF, SymbolicExpressionIF> javaMap) {
		this(HashTreePMap.from(javaMap));
	}

	@Override
	public SymbolicExpressionIF get(SymbolicExpressionIF key) {
		return pmap.get(key);
	}

	@Override
	public Iterable<SymbolicExpressionIF> keys() {
		return pmap.keySet();
	}

	@Override
	public Iterable<SymbolicExpressionIF> values() {
		return pmap.values();
	}

	@Override
	public Iterable<Entry<SymbolicExpressionIF, SymbolicExpressionIF>> entries() {
		return pmap.entrySet();
	}

	@Override
	public Iterator<SymbolicExpressionIF> iterator() {
		return pmap.values().iterator();
	}

	/**
	 * Know that o is a MAP collection of same size as this one. TODO: THIS
	 * ASSUMES entries are ordered. I want ordered sets.
	 * 
	 */
	@Override
	protected int compareCollection(SymbolicCollection o) {
		PcollectionsSymbolicMap that = (PcollectionsSymbolicMap) o;
		Set<Entry<SymbolicExpressionIF, SymbolicExpressionIF>> entrySet = pmap
				.entrySet();
		int result;

		for (Entry<SymbolicExpressionIF, SymbolicExpressionIF> entry : entrySet) {
			SymbolicExpressionIF key = entry.getKey();
			SymbolicExpressionIF thisValue = entry.getValue();
			SymbolicExpressionIF thatValue = that.get(key);

			if (thisValue == null) {
				if (thatValue != null)
					return -1;
			} else {
				if (thatValue == null)
					return 1;
				result = thisValue.compareTo(thatValue);
				if (result != 0)
					return result;
			}
		}
		return 0;
	}

	@Override
	protected int computeHashCode() {
		return SymbolicCollectionKind.MAP.hashCode() ^ pmap.hashCode();
	}

	@Override
	public int size() {
		return pmap.size();
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection o) {
		PcollectionsSymbolicMap that = (PcollectionsSymbolicMap) o;

		return pmap.equals(that.pmap);
	}

	@Override
	public String toString() {
		return pmap.toString();
	}

	@Override
	public boolean isEmpty() {
		return pmap.isEmpty();
	}

	@Override
	public boolean isSorted() {
		return false;
	}

	@Override
	public SymbolicMap put(SymbolicExpressionIF key, SymbolicExpressionIF value) {
		return new PcollectionsSymbolicMap(pmap.plus(key, value));
	}

	@Override
	public SymbolicMap remove(SymbolicExpressionIF key) {
		return new PcollectionsSymbolicMap(pmap.minus(key));
	}

	@Override
	public SymbolicMap apply(UnaryOperatorIF operator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicMap combine(BinaryOperatorIF operator, SymbolicMap map) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Runs a few simple tests.
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {
		// TODO
	}

	@Override
	public void canonizeChildren(ObjectFactory factory) {
		// TODO Auto-generated method stub

	}

}
