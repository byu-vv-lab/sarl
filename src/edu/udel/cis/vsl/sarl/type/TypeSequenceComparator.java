package edu.udel.cis.vsl.sarl.type;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequenceIF;

public class TypeSequenceComparator implements
		Comparator<SymbolicTypeSequenceIF> {

	private Comparator<SymbolicTypeIF> typeComparator;

	public TypeSequenceComparator() {

	}

	public void setTypeComparator(Comparator<SymbolicTypeIF> c) {
		typeComparator = c;
	}

	@Override
	public int compare(SymbolicTypeSequenceIF o1, SymbolicTypeSequenceIF o2) {
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
