package edu.udel.cis.vsl.sarl.object.common;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;

/**
 * An implementation of the Symbolic Expression Comparator
 * Used for testing Objects
 * @author jtirrell
 *
 */
public class CollectionComparatorStub implements Comparator<SymbolicCollection<?>> {

	public CollectionComparatorStub() {
	}

	@Override
	public int compare(SymbolicCollection<?> o1, SymbolicCollection<?> o2) {
		String name1 = (o1).toString();
		String name2 = (o2).toString();

		return name1.compareTo(name2);
	}

}
