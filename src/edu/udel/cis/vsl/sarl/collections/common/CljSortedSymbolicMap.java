package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.trifork.clj_ds.PersistentTreeMap;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CljSortedSymbolicMap extends CommonSymbolicMap implements
		SymbolicMap {

	private PersistentTreeMap<SymbolicExpression, SymbolicExpression> pmap;

	CljSortedSymbolicMap(Comparator<SymbolicExpression> comparator) {
		super();
		this.pmap = new PersistentTreeMap<SymbolicExpression, SymbolicExpression>(
				null, comparator);
	}

	// CljSortedSymbolicMap() {
	// super();
	// this.pmap = new PersistentTreeMap<SymbolicExpressionIF,
	// SymbolicExpressionIF>();
	// }

	// CljSortedSymbolicMap(
	// PersistentTreeMap<SymbolicExpressionIF, SymbolicExpressionIF> pmap) {
	// super();
	// this.pmap = pmap;
	// }

	CljSortedSymbolicMap(
			Map<SymbolicExpression, SymbolicExpression> javaMap,
			Comparator<SymbolicExpression> comparator) {
		super();
		pmap = new PersistentTreeMap<SymbolicExpression, SymbolicExpression>(
				null, comparator);
		for (Entry<SymbolicExpression, SymbolicExpression> entry : javaMap
				.entrySet())
			pmap = pmap.assoc(entry.getKey(), entry.getValue());
	}

	// CljSortedSymbolicMap(Map<SymbolicExpressionIF, SymbolicExpressionIF>
	// javaMap) {
	// super();
	// pmap = new PersistentTreeMap<SymbolicExpressionIF,
	// SymbolicExpressionIF>();
	// for (Entry<SymbolicExpressionIF, SymbolicExpressionIF> entry : javaMap
	// .entrySet())
	// pmap = pmap.assoc(entry.getKey(), entry.getValue());
	// }

	@Override
	public int size() {
		return pmap.size();
	}

	@Override
	public Iterator<SymbolicExpression> iterator() {
		return pmap.vals();
	}

	@Override
	public SymbolicExpression get(SymbolicExpression key) {
		return pmap.get(key);
	}

	@Override
	public Iterable<SymbolicExpression> keys() {
		return pmap.keySet();
	}

	@Override
	public Iterable<SymbolicExpression> values() {
		return pmap.values();
	}

	@Override
	public Iterable<Entry<SymbolicExpression, SymbolicExpression>> entries() {
		return pmap.entrySet();
	}

	@Override
	public boolean isEmpty() {
		return pmap.isEmpty();
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection o) {
		if (o instanceof CljSortedSymbolicMap)
			return pmap.equals(((CljSortedSymbolicMap) o).pmap);
		return false;
	}

	@Override
	protected int computeHashCode() {
		return SymbolicCollectionKind.MAP.hashCode() ^ pmap.hashCode();
	}

	@Override
	public boolean isSorted() {
		return true;
	}

	@Override
	public SymbolicMap put(SymbolicExpression key, SymbolicExpression value) {
		return new CljSortedSymbolicMap(pmap.assoc(key, value),
				pmap.comparator());
	}

	@Override
	public SymbolicMap remove(SymbolicExpression key) {
		return new CljSortedSymbolicMap(pmap.without(key), pmap.comparator());
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		// TODO Auto-generated method stub
		// need to construct whole new map replacing keys and values
		// with canonic representative if not already
	}
}
