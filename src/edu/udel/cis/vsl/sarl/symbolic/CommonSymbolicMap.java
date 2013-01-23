package edu.udel.cis.vsl.sarl.symbolic;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.pcollections.PMap;

import edu.udel.cis.vsl.sarl.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicMap;

public class CommonSymbolicMap extends CommonSymbolicCollection implements
		SymbolicMap {

	private PMap<SymbolicExpressionIF, SymbolicExpressionIF> pmap;

	CommonSymbolicMap(PMap<SymbolicExpressionIF, SymbolicExpressionIF> pmap) {
		super(SymbolicCollectionKind.MAP);
		this.pmap = pmap;
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
		CommonSymbolicMap that = (CommonSymbolicMap) o;
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
		CommonSymbolicMap that = (CommonSymbolicMap) o;

		return pmap.equals(that.pmap);
	}

	@Override
	public String toString() {
		return pmap.toString();
	}

}
