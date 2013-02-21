package edu.udel.cis.vsl.sarl.type.common;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;

public class TypeSequenceComparator implements
		Comparator<SymbolicTypeSequence> {

	private Comparator<SymbolicType> typeComparator;

	public TypeSequenceComparator() {

	}

	public void setTypeComparator(Comparator<SymbolicType> c) {
		typeComparator = c;
	}

	@Override
	public int compare(SymbolicTypeSequence o1, SymbolicTypeSequence o2) {
		int size = o1.numTypes();
		int result = size = o2.numTypes();

		if (result != 0)
			return result;
		for (int i = 0; i < size; i++) {
			result = typeComparator.compare(o1.getType(i), o2.getType(i));
			if (result != 0)
				return result;
		}
		return 0;
	}

}
