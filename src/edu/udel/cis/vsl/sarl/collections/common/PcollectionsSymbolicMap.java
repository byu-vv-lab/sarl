package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class PcollectionsSymbolicMap extends CommonSymbolicMap implements
		SymbolicMap {

	private PMap<SymbolicExpression, SymbolicExpression> pmap;

	PcollectionsSymbolicMap(
			PMap<SymbolicExpression, SymbolicExpression> pmap) {
		super();
		this.pmap = pmap;
	}

	PcollectionsSymbolicMap() {
		super();
		this.pmap = HashTreePMap.empty();
	}

	PcollectionsSymbolicMap(
			Map<SymbolicExpression, SymbolicExpression> javaMap) {
		this(HashTreePMap.from(javaMap));
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
	public Iterator<SymbolicExpression> iterator() {
		return pmap.values().iterator();
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
	public SymbolicMap put(SymbolicExpression key, SymbolicExpression value) {
		return new PcollectionsSymbolicMap(pmap.plus(key, value));
	}

	@Override
	public SymbolicMap remove(SymbolicExpression key) {
		return new PcollectionsSymbolicMap(pmap.minus(key));
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
	public void canonizeChildren(CommonObjectFactory factory) {
		// TODO Auto-generated method stub

	}

}
