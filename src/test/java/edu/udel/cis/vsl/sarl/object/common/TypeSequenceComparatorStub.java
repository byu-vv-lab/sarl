package edu.udel.cis.vsl.sarl.object.common;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
/**
 * Test stub for TypeSequenceComparator
 * @author justin
 *
 */
public class TypeSequenceComparatorStub implements Comparator<SymbolicTypeSequence> {

	public TypeSequenceComparatorStub() {
	}

	@Override
	public int compare(SymbolicTypeSequence o1, SymbolicTypeSequence o2) {
		String name1 = (o1).toString();
		String name2 = (o2).toString();

		return name1.compareTo(name2);
	}

}
