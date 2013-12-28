/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import org.pcollections.HashTreePSet;
import org.pcollections.PSet;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSet;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class PcollectionsHashSet<T extends SymbolicExpression> extends
		CommonSymbolicCollection<T> implements SymbolicSet<T> {

	private PSet<T> pset;

	PcollectionsHashSet(PSet<T> pset) {
		super(SymbolicCollectionKind.SET);
		this.pset = pset;
	}

	PcollectionsHashSet() {
		super(SymbolicCollectionKind.SET);
		this.pset = HashTreePSet.empty();
	}

	PcollectionsHashSet(Collection<T> elements) {
		this(HashTreePSet.from(elements));
	}

	@Override
	public int size() {
		return pset.size();
	}

	@Override
	public Iterator<T> iterator() {
		return pset.iterator();
	}

	@Override
	public boolean contains(T element) {
		return pset.contains(element);
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection<T> o) {
		return pset.equals(((PcollectionsHashSet<T>) o).pset);
	}

	@Override
	protected int computeHashCode() {
		return SymbolicCollectionKind.SET.hashCode() ^ pset.hashCode();
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		StringBuffer result = new StringBuffer();
		boolean first = true;

		if (atomize)
			result.append("{");

		for (T element : this) {
			if (first)
				first = false;
			else
				result.append(",");
			result.append(element.toStringBuffer(false));
		}
		if (atomize)
			result.append("}");
		return result;
	}

	@Override
	public StringBuffer toStringBufferLong() {
		StringBuffer result = new StringBuffer("UnsortedSet");

		result.append(toStringBuffer(true));
		return result;
	}

	@Override
	public boolean isSorted() {
		return false;
	}

	@Override
	public SymbolicSet<T> add(T element) {
		return new PcollectionsHashSet<T>(pset.plus(element));
	}

	@SuppressWarnings("unchecked")
	@Override
	public SymbolicSet<T> addAll(SymbolicSet<? extends T> set) {
		return new PcollectionsHashSet<T>(
				pset.plusAll(((PcollectionsHashSet<T>) set).pset));
	}

	@Override
	public SymbolicSet<T> remove(T element) {
		return new PcollectionsHashSet<T>(pset.minus(element));
	}

	@Override
	public SymbolicSet<T> removeAll(SymbolicSet<? extends T> set) {
		return new PcollectionsHashSet<T>(
				pset.minusAll(((PcollectionsHashSet<?>) set).pset));
	}

	@Override
	public SymbolicSet<T> keepOnly(SymbolicSet<? extends T> set) {
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		Iterator<T> iter = pset.iterator();
		PSet<T> newSet = HashTreePSet.empty();

		while (iter.hasNext()) {
			T t1 = iter.next();
			T t2 = factory.canonic(t1);

			assert t2 != null;
			newSet = newSet.plus(t2);
		}
		pset = newSet;
	}

	@Override
	public Comparator<T> comparator() {
		return null;
	}

}
