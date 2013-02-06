package edu.udel.cis.vsl.sarl.IF.type;

import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

public interface SymbolicTypeSequenceIF extends Iterable<SymbolicTypeIF>,
		SymbolicObject {

	int numTypes();

	SymbolicTypeIF getType(int index);

}
