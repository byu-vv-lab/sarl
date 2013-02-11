package edu.udel.cis.vsl.sarl.collections;

import java.util.Collection;
import java.util.Iterator;

import org.pcollections.HashTreePSet;
import org.pcollections.PSet;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSet;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.object.ObjectFactory;

public class PcollectionsSymbolicSet extends CommonSymbolicCollection implements
		SymbolicSet {

	private PSet<SymbolicExpressionIF> pset;

	PcollectionsSymbolicSet(PSet<SymbolicExpressionIF> pset) {
		super(SymbolicCollectionKind.SET);
		this.pset = pset;
	}

	PcollectionsSymbolicSet() {
		super(SymbolicCollectionKind.SET);
		this.pset = HashTreePSet.empty();
	}

	PcollectionsSymbolicSet(Collection<SymbolicExpressionIF> elements) {
		this(HashTreePSet.from(elements));
	}

	@Override
	public int size() {
		return pset.size();
	}

	@Override
	public Iterator<SymbolicExpressionIF> iterator() {
		return pset.iterator();
	}

	@Override
	public boolean contains(SymbolicExpressionIF element) {
		return pset.contains(element);
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection o) {
		return pset.equals(((PcollectionsSymbolicSet) o).pset);
	}

	@Override
	protected int computeHashCode() {
		return SymbolicCollectionKind.SET.hashCode() ^ pset.hashCode();
	}

	@Override
	public String toString() {
		return pset.toString();
	}

	@Override
	public boolean isSorted() {
		return false;
	}

	@Override
	public SymbolicSet add(SymbolicExpressionIF element) {
		return new PcollectionsSymbolicSet(pset.plus(element));
	}

	@Override
	public SymbolicSet addAll(SymbolicSet set) {
		return new PcollectionsSymbolicSet(pset.plusAll(pset));
	}

	@Override
	public SymbolicSet remove(SymbolicExpressionIF element) {
		return new PcollectionsSymbolicSet(pset.minus(element));
	}

	@Override
	public SymbolicSet removeAll(SymbolicSet set) {
		return new PcollectionsSymbolicSet(
				pset.minusAll(((PcollectionsSymbolicSet) set).pset));
	}

	@Override
	public SymbolicSet keepOnly(SymbolicSet set) {
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public void canonizeChildren(ObjectFactory factory) {
		// TODO Auto-generated method stub

	}

}
