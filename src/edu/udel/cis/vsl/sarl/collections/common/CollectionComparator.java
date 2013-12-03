/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSet;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection.SymbolicCollectionKind;

public class CollectionComparator implements Comparator<SymbolicCollection<?>> {

	private Comparator<SymbolicExpression> elementComparator;

	public CollectionComparator() {
	}

	/**
	 * Sets the comparator for the CollectionComparator
	 * 
	 * @param s1
	 *            the comparator to be set
	 */
	public void setElementComparator(Comparator<SymbolicExpression> elementComparator) {
		this.elementComparator = elementComparator;
	}

	@Override
	public int compare(SymbolicCollection<?> o1, SymbolicCollection<?> o2) {
		SymbolicCollectionKind kind = o1.collectionKind();
		int result = kind.compareTo(o2.collectionKind());

		if (result != 0)
			return result;
		result = o1.size() - o2.size();
		if (result != 0)
			return result;
		// compare two sequences, sets, compare two maps
		switch (kind) {
		case SEQUENCE:
			return compareSequences((SymbolicSequence<?>) o1,
					(SymbolicSequence<?>) o2);
		case SET:
			return compareSets((SymbolicSet<?>) o1, (SymbolicSet<?>) o2);
		case MAP:
			return compareMaps((SymbolicMap<?, ?>) o1, (SymbolicMap<?, ?>) o2);
		default:
			throw new SARLInternalException("unreachable");
		}
	}

	/**
	 * Compares two sequences. 
	 * 
	 * @param s1
	 *            a symbolic sequence
	 * @param s2
	 *            a symbolic sequence
	 * @return the result if the the elements are not equal else it returns 0 if the sets were equal
	 */
	private int compareSequences(SymbolicSequence<?> s1, SymbolicSequence<?> s2) {
		int size = s1.size();

		for (int i = 0; i < size; i++) {
			int result = elementComparator.compare(s1.get(i), s2.get(i));

			if (result != 0)
				return result;
		}
		return 0;
	}

	/**
	 * Compares two sets. Assumes sets have the same size. All sorted sets come
	 * first, then unsorted ones.
	 * 
	 * @param s1
	 *            a symbolic set
	 * @param s2
	 *            a symbolic set of the same size as <code>s1</code>
	 * @return a negative integer if the first set precedes the second, 0 if the
	 *         two sets are equal, a positive integer if the second set precedes
	 *         the first
	 */
	private int compareSets(SymbolicSet<?> s1, SymbolicSet<?> s2) {
		/*The isSorted method for PcollectionsSortedSymbloic Set is never implemented and always
		returns false so this method will always throw and internal exception until that is implemented
		this method isn't going to do anything since there are no other symbolic sets to create in sarl*/
		
		/*throw new SARLInternalException(
				"Comparison of unsorted sets not efficient");*/
		if (s1.isSorted()) {
			if (s2.isSorted()) {
				Iterator<? extends SymbolicExpression> iter1 = s1.iterator();
				Iterator<? extends SymbolicExpression> iter2 = s2.iterator();

				while (iter1.hasNext()) {
					int result = elementComparator.compare(iter1.next(),
							iter2.next());

					if (result != 0)
						return result;
				}
				return 0;
			} else
				return -1;
		} else {
			if (s2.isSorted())
				return 1;
			else
				throw new SARLInternalException(
						"Comparison of unsorted sets not efficient");
		}
	}

	/**
	 * Compares two Maps.
	 * 
	 * @param s1
	 *            a symbolic map
	 * @param s2
	 *            a symbolic map
	 * @return comparison result if the maps are not equal, 0 if they are equal,
	 * 1 if the first parameter map is sorted and the second parameter map is not sorted,
	 * -1 if the first map is not sorted and the second map is sorted.
	 */
	private <K1 extends SymbolicExpression, V1 extends SymbolicExpression, K2 extends SymbolicExpression, V2 extends SymbolicExpression> int compareMaps(
			SymbolicMap<K1, V1> m1, SymbolicMap<K2, V2> m2) {
		if (m1.isSorted()) {
			if (m2.isSorted()) {
				Iterator<Entry<K1, V1>> iter1 = m1.entries().iterator();
				Iterator<Entry<K2, V2>> iter2 = m2.entries().iterator();
				while (iter1.hasNext()) {
					Entry<? extends SymbolicExpression, ? extends SymbolicExpression> e1 = iter1
							.next();
					Entry<? extends SymbolicExpression, ? extends SymbolicExpression> e2 = iter2
							.next();
					int result = elementComparator.compare(e1.getKey(),
							e2.getKey());

					if (result != 0)
						return result;
					result = elementComparator.compare(e1.getValue(),
							e2.getValue());
					if (result != 0)
						return result;
				}
				return 0;
			} else
				return -1;
		} else {
			if (m2.isSorted())
				return 1;
			else
				throw new SARLInternalException(
						"Comparison of unsorted maps not efficient");
		}

	}
}
