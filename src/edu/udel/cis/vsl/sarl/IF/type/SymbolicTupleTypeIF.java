package edu.udel.cis.vsl.sarl.IF.type;

import edu.udel.cis.vsl.sarl.IF.StringObject;

public interface SymbolicTupleTypeIF extends SymbolicTypeIF {

	StringObject name();

	SymbolicTypeSequenceIF sequence();
}
