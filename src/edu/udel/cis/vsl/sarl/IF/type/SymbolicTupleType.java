package edu.udel.cis.vsl.sarl.IF.type;

import edu.udel.cis.vsl.sarl.IF.object.StringObject;

public interface SymbolicTupleType extends SymbolicType {

	StringObject name();

	SymbolicTypeSequence sequence();
}
