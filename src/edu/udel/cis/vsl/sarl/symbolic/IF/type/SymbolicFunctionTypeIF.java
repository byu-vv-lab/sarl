package edu.udel.cis.vsl.sarl.symbolic.IF.type;

public interface SymbolicFunctionTypeIF extends SymbolicTypeIF {

	int numInputs();

	SymbolicTypeIF inputType(int index);

	SymbolicTypeIF outputType();

}
