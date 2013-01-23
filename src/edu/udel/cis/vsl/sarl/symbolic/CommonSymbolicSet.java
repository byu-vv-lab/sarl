package edu.udel.cis.vsl.sarl.symbolic;

import java.util.Iterator;

import org.pcollections.PSet;

import edu.udel.cis.vsl.sarl.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicSet;

public class CommonSymbolicSet extends CommonSymbolicCollection implements
		SymbolicSet {

	// if you want a sorted set, keep a regular set and a vector
	// which supports insertion/deletion: PVector

	private PSet<SymbolicExpressionIF> pset;

	CommonSymbolicSet(PSet<SymbolicExpressionIF> pset) {
		super(SymbolicCollectionKind.SET);
		this.pset = pset;
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
	protected int compareCollection(SymbolicCollection o) {
		throw new UnsupportedOperationException("Can't do this until"
				+ "we find a persistent sorted set class.");
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection o) {
		return pset.equals(((CommonSymbolicSet) o).pset);
	}

	@Override
	protected int computeHashCode() {
		return SymbolicCollectionKind.SET.hashCode() ^ pset.hashCode();
	}

	@Override
	public String toString() {
		return pset.toString();
	}

}
