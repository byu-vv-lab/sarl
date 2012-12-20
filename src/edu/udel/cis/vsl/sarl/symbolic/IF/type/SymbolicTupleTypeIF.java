package edu.udel.cis.vsl.sarl.symbolic.IF.type;

public interface SymbolicTupleTypeIF extends SymbolicTypeIF {
	int numFields();
	
	String name();

	SymbolicTypeIF fieldType(int index);
}
