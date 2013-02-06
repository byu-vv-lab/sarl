package edu.udel.cis.vsl.sarl.IF.type;

import edu.udel.cis.vsl.sarl.IF.object.StringObject;

public interface SymbolicUnionTypeIF extends SymbolicTypeIF {

	SymbolicTypeSequenceIF sequence();

	StringObject name();

}
