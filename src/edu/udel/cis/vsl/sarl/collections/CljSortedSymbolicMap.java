package edu.udel.cis.vsl.sarl.collections;

import java.util.Iterator;
import java.util.Map.Entry;

import com.trifork.clj_ds.PersistentTreeMap;

import edu.udel.cis.vsl.sarl.IF.BinaryOperatorIF;
import edu.udel.cis.vsl.sarl.IF.UnaryOperatorIF;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.object.ObjectFactory;

public class CljSortedSymbolicMap extends CommonSymbolicCollection implements
		SymbolicMap {

	private PersistentTreeMap<SymbolicExpressionIF, SymbolicExpressionIF> pmap;

	CljSortedSymbolicMap() {
		super(SymbolicCollectionKind.MAP);
		this.pmap = new PersistentTreeMap<SymbolicExpressionIF, SymbolicExpressionIF>();
	}

	CljSortedSymbolicMap(
			PersistentTreeMap<SymbolicExpressionIF, SymbolicExpressionIF> pmap) {
		super(SymbolicCollectionKind.MAP);
		this.pmap = pmap;
	}

	@Override
	public int size() {
		return pmap.size();
	}

	@Override
	public Iterator<SymbolicExpressionIF> iterator() {
		return pmap.vals();
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
	public boolean isEmpty() {
		return pmap.isEmpty();
	}

	@Override
	protected int compareCollection(SymbolicCollection o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection o) {
		// TODO Auto-generated method stub
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
	public SymbolicMap put(SymbolicExpressionIF key, SymbolicExpressionIF value) {
		return new CljSortedSymbolicMap(pmap.assoc(key, value));
	}

	@Override
	public SymbolicMap remove(SymbolicExpressionIF key) {
		return new CljSortedSymbolicMap(pmap.without(key));
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

	// public static void main(String[] args) {
	// CljSortedSymbolicMap map = new CljSortedSymbolicMap();
	// SymbolicTypeFactory typeFactory = new SymbolicTypeFactory();
	// SymbolicTypeIF t1 = typeFactory.integerType();
	//
	// // SymbolicExpressionIF e1 = new
	// // CommonSymbolicExpression(SymbolicOperator.ADD, t1);
	// // System.out.println("Empty map: "+map);
	// // map = map.put(key, value)
	// }

	@Override
	public void canonizeChildren(ObjectFactory factory) {
		// TODO Auto-generated method stub
		// need to construct whole new map replacing keys and values
		// with canonic representative if not already
	}
}
