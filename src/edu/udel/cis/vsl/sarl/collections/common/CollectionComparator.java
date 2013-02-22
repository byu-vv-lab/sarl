package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection.SymbolicCollectionKind;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSequence;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSet;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

public class CollectionComparator implements Comparator<SymbolicCollection<?>> {

	private Comparator<SymbolicExpression> elementComparator;

	public CollectionComparator() {
	}

	public void setElementComparator(
			Comparator<SymbolicExpression> elementComparator) {
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
	 * Assume sets have the same size. All sorted sets come first, then unsorted
	 * ones.
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	private int compareSets(SymbolicSet<?> s1, SymbolicSet<?> s2) {
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
