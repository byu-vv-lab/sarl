package edu.udel.cis.vsl.sarl.IF.type;

import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

public interface SymbolicTypeSequence extends Iterable<SymbolicType>,
		SymbolicObject {

	int numTypes();

	SymbolicType getType(int index);

}
